package co.jueyi.geekshop;

import co.jueyi.geekshop.properties.ConfigOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on Nov, 2020 by @author bobo
 */
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableAsync
@EnableConfigurationProperties(ConfigOptions.class)
@RestController
@Slf4j
public class GeekShopApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(GeekShopApplication.class, args);
        log.info("GeekShop Started! Have Fun!");
    }

    @GetMapping
    public String hello() {return "Hello GeekShop!";}
}
