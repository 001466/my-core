
package easy.core.swagger2;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * swagger配置
 *
 * @author Chill
 */
@Configuration
@EnableSwagger2
@Profile({"dev","dev2","dev3","dev4", "test","loc"})
@EnableConfigurationProperties(easy.core.swagger2.SwaggerProperties.class)
@Slf4j
public class SwaggerAutoConfiguration {



//方法一：正常序列化Json.class
//	@Bean
//	public GsonHttpMessageConverter gsonHttpMessageConverter() {
//		GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
//		converter.setGson(new GsonBuilder().registerTypeAdapter(Json.class, new SpringfoxJsonToGsonAdapter()).create());
//		return converter;
//	}

//方法二：正常序列化Json.class
//	@Autowired
//	private RequestMappingHandlerAdapter handlerAdapter;
//
//	@PostConstruct
//	public void initEditableValidation() {
//		ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer) handlerAdapter.getWebBindingInitializer();
//		if (initializer.getConversionService() != null) {
//			System.err.println("RegisterFormatters Jsone2StringConverter");
//			GenericConversionService genericConversionService = (GenericConversionService) initializer.getConversionService();
//			genericConversionService.addConverter(Json.class, String.class, new Jsone2StringConverter());
// 		}
//	}


	@Autowired
	SwaggerProperties swaggerProperties;


	private ApiInfo apiInfo() {
		Contact contact = new Contact(swaggerProperties.getContact().getName(), swaggerProperties.getContact().getUrl(), swaggerProperties.getContact().getEmail());
		return new ApiInfoBuilder()
				.title(swaggerProperties.getTitle())
				.description(swaggerProperties.getDescription())
				.contact(contact)
				.version(swaggerProperties.getVersion())
				.build();
	}

	@Bean
	public Docket customDocket() {

		if(swaggerProperties.getBasePackages()==null || swaggerProperties.getBasePackages().size()==0){
			throw new  RuntimeException("swagger.baasePackages properties is not find(没有找到swagger.baasePackages配置，请配置swagger需要扫描的包) : swaggerProperties.getBasePackages() ="+swaggerProperties.getBasePackages());
		}
		for(String bp:swaggerProperties.getBasePackages()) {
			System.err.println("Swagger packages -> {}"+ bp);
		}

		return new Docket(DocumentationType.SWAGGER_2)
				// 详细定制
				.apiInfo(apiInfo()).enable(true)
				.select()
				// 指定当前包路径，这里就添加了两个包，注意方法变成了basePackage，中间加上成员变量splitor
				.apis(basePackage(swaggerProperties.getBasePackages()))
				// 扫描所有 .apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build();
	}





	private static Predicate<RequestHandler> basePackage(final List<String> basePackage) {
		return input -> declaringClass(input).transform(handlerPackage(basePackage)).or(true);
	}

	private static Function<Class<?>, Boolean> handlerPackage(final List<String> basePackage)     {
		return input -> {
 			for (String strPackage : basePackage) {
				boolean isMatch = input.getPackage().getName().startsWith(strPackage);
				if (isMatch) {
					return true;
				}
			}
			return false;
		};
	}

	private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
		return Optional.fromNullable(input.declaringClass());
	}

}
