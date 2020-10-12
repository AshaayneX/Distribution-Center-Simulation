package WolfPack.SimulatorService;

import java.io.IOException;
import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication

public class SimulatorServiceApplication {

	@Configuration
	@EnableSwagger2
	public class SpringFoxConfig {                                    
		@Bean
		public Docket api() { 	
			return new Docket(DocumentationType.SWAGGER_2)  
			.select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
            .paths(PathSelectors.any())
			.build()
            .apiInfo(apiInfo());                                        
		}
	}
	
    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo("Simulation Service", "The key service that processes the Order information from order generation service then allocate it to a worker.", "API TOS", "", new Contact("", "", ""), "", "", Collections.emptyList());
        return apiInfo;
	}

	public static void main(String[] args) throws IOException {

		SpringApplication.run(SimulatorServiceApplication.class, args);
		Registration.postRegistration();
	}
   ;
}
