package backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    private final String dataRootPath = "/app/data/";


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // GESTIONE IMMAGINI
        registry
                .addResourceHandler("/images/**")

                .addResourceLocations("file:" + dataRootPath + "images/");

        // GESTIONE DOCUMENTI
        registry
                .addResourceHandler("/documents/**")

                .addResourceLocations("file:" + dataRootPath + "documents/");
    }
}