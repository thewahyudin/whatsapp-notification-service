package why.din.notification.service.config;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class OkHttpConfig {
    @Autowired
    private CmbConfig cfg;

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(cfg.getTimeout(), TimeUnit.SECONDS)
                .writeTimeout(cfg.getTimeout(), TimeUnit.SECONDS)
                .readTimeout(cfg.getTimeout(), TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .followRedirects(true)
                .build();
    }
}
