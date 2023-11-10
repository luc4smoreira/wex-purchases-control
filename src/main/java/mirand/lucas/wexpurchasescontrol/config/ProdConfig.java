package mirand.lucas.wexpurchasescontrol.config;

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
@Profile("prod")
public class ProdConfig {
    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerCustomizer() {
        return factory -> factory.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error"));
    }

    /***
     * Remove stack trace from error page
     *
     * @return ErrorAttributes
     */
    @Bean
    public ErrorAttributes errorAttributes() {

        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
                //remove stack trace, do not expose error information in production
                options = options.excluding(ErrorAttributeOptions.Include.STACK_TRACE, ErrorAttributeOptions.Include.BINDING_ERRORS);

                return super.getErrorAttributes(webRequest, options);
            }
        };
    }
}
