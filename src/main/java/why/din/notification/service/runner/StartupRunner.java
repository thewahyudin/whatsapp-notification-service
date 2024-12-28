package why.din.notification.service.runner;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import why.din.notification.service.config.SvcConfig;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class StartupRunner {

    private final OkHttpClient client;
    private final SvcConfig svcCfg;

    @Bean
    public CommandLineRunner runAfterStart() {
        return args -> {
            String url = svcCfg.getRunnerUrl();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    System.out.println("Request to /api/notification successful. Response: " + response.body().string());
                } else {
                    System.out.println("Request to /api/notification failed. Response code: " + response.code());
                }
            } catch (IOException e) {
                System.out.println("Error making request: " + e.getMessage());
            }
        };
    }
}