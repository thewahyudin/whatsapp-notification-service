package why.din.notification.service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cmb")
@Getter
@Setter
public class CmbConfig {
    private String url;
    private String apiKey;
    private long timeout;
}
