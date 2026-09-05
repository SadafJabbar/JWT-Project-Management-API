package project_management__api.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI projectApi(){
        return new OpenAPI().info(new Info().title("Project Management Api")
                .version("1.0")
                .description("Project Api for jwt practice")
                .contact(new Contact().name("Sadaf Jabbar")));
    }
}