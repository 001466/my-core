package org.easy.cloud.config;

import com.deocean.common.constant.TokenConstant;
import com.deocean.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@Configuration
@EnableWebMvc
@Slf4j
public class AccessInterceptor extends org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(pathInterceptor()).addPathPatterns("/**");
		super.addInterceptors(registry);
	}


	@Bean
	public HandlerInterceptor pathInterceptor() {
		return new HandlerInterceptor() {

			@Override
			public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
				InputStream input =  request.getInputStream();

				StringBuffer stringBuffer = new StringBuffer();
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
					String line = "";
					while ((line = reader.readLine()) != null) {
						stringBuffer.append(line);
					}
				}
				StringBuilder sb=new StringBuilder(request.getRequestURI().toString());
				sb.append(" ;user-agent:").append(request.getHeader("User-Agent"));
				sb.append(" ;authorization:").append(request.getHeader(TokenConstant.AUTHORIZATION));
				sb.append(" ;hostId:").append(request.getHeader("hostId"));
				sb.append(" ;hostMac:").append(request.getHeader("hostMac"));
				sb.append(" ;params:").append(JsonUtil.toJson(request.getParameterMap()));
				sb.append(" ;body:").append(stringBuffer.toString());
				System.err.println(sb.toString());
				log.info(sb.toString());
				return true;
			}

			@Override
			public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
			}

			@Override
			public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
			}
		};
	}
	
	

	

}
