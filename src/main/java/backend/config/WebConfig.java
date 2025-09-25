package backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Applica a tutti gli endpoint (es. /api/utenti)
                .allowedOrigins("http://localhost:4200") // Origine del frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Metodi HTTP consentiti
                .allowedHeaders("*") // Permette tutti gli header
                .allowCredentials(true); // Permette l'invio di cookie e credenziali
    }
}