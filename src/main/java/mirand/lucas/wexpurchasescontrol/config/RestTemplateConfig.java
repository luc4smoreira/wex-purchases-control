package mirand.lucas.wexpurchasescontrol.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    @Value("${connections.external.connect.timeout_seconds}")
    private int connectTimeoutSeconds;

    @Value("${connections.external.read.timeout_seconds}")
    private int readTimeoutSeconds;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        return builder
                .setConnectTimeout(Duration.ofSeconds(connectTimeoutSeconds))
                .setReadTimeout(Duration.ofSeconds(readTimeoutSeconds))
                .build();
    }
}
