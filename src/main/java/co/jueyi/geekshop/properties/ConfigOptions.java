package co.jueyi.geekshop.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created on Nov, 2020 by @author bobo
 */
@ConfigurationProperties(prefix = "geekshop")
@Data
public class ConfigOptions {
    /**
     * Configuration for authorization.
     */
    private AuthOptions authOptions;
}
