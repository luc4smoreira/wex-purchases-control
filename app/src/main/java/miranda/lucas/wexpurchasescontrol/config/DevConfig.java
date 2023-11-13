package miranda.lucas.wexpurchasescontrol.config;


import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Configuration
@Profile("dev")
public class DevConfig {
    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerCustomizer() {
        return factory -> factory.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error"));
    }

    /***
     * Define the error page to display the stacktrace
     *
     * @return ErrorAttributes
     */
    @Bean
    public ErrorAttributes errorAttributes() {
        // Retorne com stack trace
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
                options = options.including(ErrorAttributeOptions.Include.STACK_TRACE);
                return super.getErrorAttributes(webRequest, options);
            }
        };
    }
}
