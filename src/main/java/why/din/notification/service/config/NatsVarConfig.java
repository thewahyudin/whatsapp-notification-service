package why.din.notification.service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "nats")
@Getter
@Setter
public class NatsVarConfig {
    private String url;
    private String topic;
}
