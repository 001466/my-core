
package org.easy.secure.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.easy.secure.BladeUser;
import org.easy.secure.exception.SecureException;
import org.easy.secure.constant.SecureConstant;
import org.easy.secure.constant.TokenConstant;
import org.easy.tool.util.BeanUtil;
import org.easy.tool.util.Charsets;
import org.easy.tool.util.StringPool;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.*;

/**
 * Secure工具类
 *
 * @author Chill
 */
@Slf4j
public class SecureRawUtil  {
	public final static String BLADE_USER_REQUEST_ATTR = "_BLADE_USER_REQUEST_ATTR_";

	public static String BASE64_SECURITY = Base64.getEncoder().encodeToString(TokenConstant.SIGN_KEY.getBytes(Charsets.UTF_8));

	public static BladeUser getUser(Claims claims) {
		if (claims == null) {
			return null;
		}
		return BeanUtil.toBean(claims,BladeUser.class);
	}


	public static BladeUser getUser(HttpServletRequest request) {
		// 优先从 request 中获取
		BladeUser bladeUser = null;

		bladeUser=(BladeUser) request.getAttribute(BLADE_USER_REQUEST_ATTR);
		if(bladeUser!=null){
			return bladeUser;
		}

		String authorization = request.getHeader(TokenConstant.AUTHORIZATION);
		if(StringUtils.isEmpty(authorization)){
			return null;
		}

		bladeUser=requestUserinfo(authorization);
		if(bladeUser!=null){
			request.setAttribute(BLADE_USER_REQUEST_ATTR, bladeUser);
			return bladeUser;
		}

		bladeUser= getUser(getClaims(authorization));
		if(bladeUser!=null){
			request.setAttribute(BLADE_USER_REQUEST_ATTR, bladeUser);
		}
		return bladeUser;
	}

	public static BladeUser getUser(org.springframework.web.server.ServerWebExchange exchange) {
		ServerHttpRequest request = exchange.getRequest();
		// 优先从 request 中获取
		BladeUser bladeUser = null;

		bladeUser=(BladeUser) exchange.getAttribute(BLADE_USER_REQUEST_ATTR);
		if(bladeUser!=null){
			return bladeUser;
		}

		List<String> authorizations=request.getHeaders().get(TokenConstant.AUTHORIZATION);
		if(authorizations==null || authorizations.size()==0){
			return null;
		}
		String authorization = authorizations.get(0);
		if(StringUtils.isEmpty(authorization)){
			return null;
		}


		bladeUser=requestUserinfo(authorization);
		if(bladeUser!=null){
			exchange.getAttributes().put(BLADE_USER_REQUEST_ATTR, bladeUser);
			return bladeUser;
		}

		bladeUser= getUser(getClaims(authorization));
		if(bladeUser!=null){
			exchange.getAttributes().put(BLADE_USER_REQUEST_ATTR, bladeUser);
		}
		return bladeUser;
	}



	public static BladeUser getUserByAuthorization(String authorization)  {

		Claims claims = getClaimsByAuthorization(authorization);

		if (claims == null) {
			claims =getClaimsByBladeAuth(authorization);
		}

		if(claims==null){
			return null;
		}

		return getUser(claims);

	}



	public static Claims getClaims(String authorization) {

//		Claims claims=null;
//		//Bladex-X 的auth
//		String bladeAuth = request.getHeader(SecureRawUtil.HEADER);
//		claims=getClaimsByBladeAuth(bladeAuth);
//
//		if(claims!=null){
//			return claims;
//		}

		//后来加入的 auth
		if(StringUtils.isEmpty(authorization)){
		    return null;
        }
		return getClaimsByAuthorization(authorization);

	}

	public static Claims getClaimsByAuthorization(String authorization) {
		return SecureRawUtil.parseJWT(authorization);
	}

	public static Claims getClaimsByBladeAuth(String bladeAuth) {

		if ((bladeAuth != null) && (bladeAuth.length() > TokenConstant.AUTH_LENGTH)) {
			String headStr = bladeAuth.substring(0, 6).toLowerCase();
			if (headStr.compareTo(TokenConstant.BEARER) == 0) {
				bladeAuth = bladeAuth.substring(7);
				return SecureRawUtil.parseJWT(bladeAuth);
			}
		}

		return null;
	}



	/**
	 * 解析jsonWebToken
	 *
	 * @param originToken jsonWebToken
	 * @return Claims
	 */
	public static Claims parseJWT(String originToken) {

		if(originToken==null){
			return null;
		}

		String[] arr=originToken.split(" ");
		String jsonWebToken=null;
		if(arr.length==2){
			jsonWebToken=arr[1];
		}else{
			jsonWebToken=arr[0];
		}

		try {
			return Jwts.parser()
				.setSigningKey(Base64.getDecoder().decode(BASE64_SECURITY))
				.parseClaimsJws(jsonWebToken).getBody();
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 创建令牌
	 *
	 * @param user     user
	 * @param audience audience
	 * @param issuer   issuer
	 * @param isExpire isExpire
	 * @return jwt
	 */
	public static String createJWT(Map<String, String> user, String audience, String issuer, boolean isExpire,HttpServletRequest request) {

		String[] tokens = extractAndDecodeHeader(request);
		assert tokens.length == 2;
		String clientId = tokens[0];
		String clientSecret = tokens[1];

		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		//生成签名密钥
		byte[] apiKeySecretBytes = Base64.getDecoder().decode(BASE64_SECURITY);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		//添加构成JWT的类
		JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JsonWebToken")
			.setIssuer(issuer)
			.setAudience(audience)
			.signWith(signatureAlgorithm, signingKey);

		//设置JWT参数
		user.forEach(builder::claim);

		//设置应用id
		builder.claim(TokenConstant.CLIENT_ID, clientId);

		//添加Token过期时间
		if (isExpire) {
			long expMillis = nowMillis + getExpire();
			Date exp = new Date(expMillis);
			builder.setExpiration(exp).setNotBefore(now);
		}

		//生成JWT
		return builder.compact();
	}

	/**
	 * 获取过期时间(次日凌晨3点)
	 *
	 * @return expire
	 */
	public static long getExpire() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 1);
		cal.set(Calendar.HOUR_OF_DAY, 3);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis() - System.currentTimeMillis();
	}

	/**
	 * 获取过期时间的秒数(次日凌晨3点)
	 *
	 * @return expire
	 */
	public static int getExpireSeconds() {
		return (int) (getExpire() / 1000);
	}

	/**
	 * 客户端信息解码
	 */

	@SneakyThrows
	public static String[] extractAndDecodeHeader(HttpServletRequest request) {
		String header = Objects.requireNonNull(request).getHeader(SecureConstant.BASIC_HEADER_KEY);
		return extractAndDecodeHeader(header);
	}
	@SneakyThrows
	public static String[] extractAndDecodeHeader(String header) {
		// 获取请求头客户端信息
		if (header == null || !header.startsWith(SecureConstant.BASIC_HEADER_PREFIX)) {
			throw new SecureException("No client information in request header");
		}
		byte[] base64Token = header.substring(6).getBytes(Charsets.UTF_8_NAME);

		byte[] decoded;
		try {
			decoded = Base64.getDecoder().decode(base64Token);
		} catch (IllegalArgumentException var7) {
			throw new RuntimeException("Failed to decode basic authentication token");
		}

		String token = new String(decoded, Charsets.UTF_8_NAME);
		int index = token.indexOf(StringPool.COLON);
		if (index == -1) {
			throw new RuntimeException("Invalid basic authentication token");
		} else {
			return new String[]{token.substring(0, index), token.substring(index + 1)};
		}
	}

	/**
	 * 获取请求头中的客户端id
	 */
	public static String getClientIdFromHeader(HttpServletRequest request) {
		String[] tokens = extractAndDecodeHeader(request);
		assert tokens.length == 2;
		return tokens[0];
	}



	protected static RestTemplate staticUserInfoRestTemplate;


	public SecureRawUtil(RestTemplate restTemplate){
        staticUserInfoRestTemplate=restTemplate;
    }

	public static BladeUser requestUserinfo(String authorization){
		try {


			if(StringUtils.isEmpty(authorization)){
			    return null;
            }

			String[] arr=authorization.split(" ");
			String token=null;
			if(arr.length==2){
				token=arr[1];
			}else{
				token=arr[0];
			}

			String checkTokenUrl=TokenConstant.CHECK_TOKEN_URL+"?token="+token;
			ResponseEntity<BladeUser> responseEntity= staticUserInfoRestTemplate.getForEntity(checkTokenUrl,BladeUser.class);
			if(responseEntity.getStatusCodeValue()==200){
				return responseEntity.getBody();
			}else{
				return null;
			}

		}catch (Exception e){
			log.error(e.getMessage(),e);
			return null;
		}
	}
}
