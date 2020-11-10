package co.jueyi.geekshop.config;

import co.jueyi.geekshop.ApiClient;
import co.jueyi.geekshop.MockDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Configuration
public class TestConfig {
    public static final long ASYNC_TIMEOUT = 5000;

    @Bean
    public ApiClient apiClient() {
        return new ApiClient();
    }

    @Bean
    public MockDataService mockDataService() {
        return new MockDataService();
    }

    @Bean
    public RestTemplateCustomizer restTemplateCustomizer() {
        return restTemplate -> {
            restTemplate.setRequestFactory(clientHttpRequestFactory());
        };
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        // 延长超时用于调试
        clientHttpRequestFactory.setConnectTimeout(100000);
        clientHttpRequestFactory.setReadTimeout(100000);
        clientHttpRequestFactory.setBufferRequestBody(false);
        return clientHttpRequestFactory;
    }
}
