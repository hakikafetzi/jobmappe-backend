package dev.kafetzi.jobmappe.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI jobMappeOpenApi() {
        return new OpenAPI().info(new Info()
                .title("JobMappe API")
                .description("""
                        A job-application tracker for the German job market -
                        Job postings, companies, applications and notes in one place, from the first draft to 
                        the final answer.
                        """)
                .version("0.1.0")
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT")));
    }
}