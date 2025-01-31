package dev.gui.processo_sergipetec.connection;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/*
    Essa classe evita que o erro de CORS aconteça durante a conexão com o front
    Confira o allowedOrigins e veja se está batendo com o endereço de hospedagem do front-end.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://127.0.0.1:5500") // Adicione ou altere o domínio se necessário
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true);
    }
}
