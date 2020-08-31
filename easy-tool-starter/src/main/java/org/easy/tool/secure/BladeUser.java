
package org.easy.tool.secure;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import org.easy.tool.util.BeanUtil;
import org.easy.tool.util.JsonUtil;
import springfox.documentation.annotations.ApiIgnore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ApiIgnore
@NoArgsConstructor
public class BladeUser implements Serializable {

	private static final long serialVersionUID = 4125096758372084309L;

	public String getClientId() {
		return client_id;
	}

	public void setClientId(String clientId) {
		this.client_id = clientId;
	}

	public String getTenantCode() {
		return tenantCode;
	}

	public void setTenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
	}

	public Long getUserId() {
		return Long.parseLong(openid);
	}

	public void setUserId(Long userId) {
		this.openid = String.valueOf(userId);
	}

	public String getUserName() {
		return user_name;
	}

	public void setUserName(String userName) {
		this.user_name = userName;
	}

	public String getNickName() {
		return nick_name;
	}

	public void setNickName(String nickName) {
		this.nick_name = nickName;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getRoleId() {
		return role_id;
	}

	public void setRoleId(String roleId) {
		this.role_id = roleId;
	}

	public String getRoleName() {
		return role_name;
	}

	public void setRoleName(String roleName) {
		this.role_name = roleName;
	}

	public String getAvatar() {
		return avatar_url;
	}

	public void setAvatar(String avatar) {
		this.avatar_url = avatar;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

    public Set<String> getPermissions() { return permissions; }

    public void setPermissions(Set<String> permissions) { this.permissions = permissions; }

	public String getPath() { return path; }

	public void setPath(String path) { this.path = path; }

	@JsonIgnore private String clientId;
	@JsonIgnore private String tenantCode;
	@JsonIgnore private Long userId;
	@JsonIgnore private String userName;
	@JsonIgnore private String nickName;
	private String account;
	@JsonIgnore private String roleId;
	@JsonIgnore private String roleName;
	@JsonIgnore private String avatar;
	private Integer type;
    private Set<String> permissions;
	private String path;


	public BladeUser(final String clientId, final String tenantCode, final Long userId, final String userName, final String nickName, final String account, final String roleId, final String roleName, final String avatar, final Integer type,Set<String> authorities,String path) {
		this.client_id = clientId;
		this.tenantCode = tenantCode;
		this.openid = String.valueOf(userId);
		this.user_name = userName;
		this.nick_name = nickName;
		this.account = account;
		this.role_id = roleId;
		this.role_name = roleName;
		this.avatar_url = avatar;
		this.type = type;
		this.permissions=authorities;
		this.path=path;
	}


	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getRole_id() {
		return role_id;
	}

	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	public String getAvatar_url() {
		return avatar_url;
	}

	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}

	public Integer getResult_code() {
		return result_code;
	}

	public void setResult_code(Integer result_code) {
		this.result_code = result_code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAuthorization_code() {
		return authorization_code;
	}

	public void setAuthorization_code(String authorization_code) {
		this.authorization_code = authorization_code;
	}

	private String client_id;
	private String openid;
	private String user_name;
	private String nick_name;
	private String role_id;
	private String role_name;
	private String avatar_url;
	private Integer result_code=0;
	private String message;
	private String authorization_code;

    public static  void main(String[] arg){

        BladeUser bladeUser=new BladeUser("clientId","tenactCode",100L,"userName","nickName","account","roleId","roleName","/xxx/avater.jpg",1,null,null);
        System.err.println(JsonUtil.toJson(bladeUser));
        System.err.println(JsonUtil.parse(JsonUtil.toJson(bladeUser),Map.class));

        Map<String, Object> info = new HashMap<>(16);
        info=BeanUtil.toMap(bladeUser);
        System.err.println(info);

        System.err.println(BeanUtil.toBean(info,BladeUser.class));

    }

}
