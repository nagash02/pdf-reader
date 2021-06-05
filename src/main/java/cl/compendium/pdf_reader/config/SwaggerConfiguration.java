package cl.compendium.pdf_reader.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.google.common.base.Predicates;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
	@Bean
    public Docket loginApi(ReloadableResourceBundleMessageSource messageSource) {

        return new Docket(DocumentationType.SWAGGER_2)   
        	.groupName("compendium")
        	.apiInfo(apiInfo(messageSource))      
        	.forCodeGeneration(true)
        	.select()
        	.apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework")))
        	.build()
        	.enableUrlTemplating(false)
        	.useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo(ReloadableResourceBundleMessageSource messageSource) {
    	return new ApiInfoBuilder().title("compendium pdf reader")
				.version("1.0.0")
				.description("prueba").build();
    } 
}
