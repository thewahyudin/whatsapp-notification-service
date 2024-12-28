package why.din.notification.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;
import why.din.notification.service.config.CmbConfig;
import why.din.notification.service.exception.NotificationServiceException;
import why.din.notification.service.util.ResponseCode;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Service
@Slf4j
public class CallMeBotService {
    private final OkHttpClient client;
    private final CmbConfig cfg;

    public String sendNotification (String custPhoneNumber, String message) throws NotificationServiceException {
        try {
            String messageEnc = URLEncoder.encode(message, StandardCharsets.UTF_8);
            String url = String.format("%s/whatsapp.php?phone=%s&text=%s&apikey=%s", cfg.getUrl(), custPhoneNumber, messageEnc, cfg.getApiKey());
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();

        } catch (ConnectException e) {
            throw new NotificationServiceException(ResponseCode.CONNECTION_ERROR.getCode(), ResponseCode.CONNECTION_ERROR.getMessage(), e.getMessage(), this.getClass().getSimpleName() + " " + Thread.currentThread().getStackTrace()[1].getMethodName(), "");
        } catch (SocketTimeoutException e) {
            throw new NotificationServiceException(ResponseCode.TIMEOUT_ERROR.getCode(), ResponseCode.TIMEOUT_ERROR.getMessage(), e.getMessage(), this.getClass().getSimpleName() + " " + Thread.currentThread().getStackTrace()[1].getMethodName(), "");
        } catch (Exception e) {
            throw new NotificationServiceException(ResponseCode.GENERAL_ERROR.getCode(), ResponseCode.GENERAL_ERROR.getMessage(), e.getMessage(), this.getClass().getSimpleName() + " " + Thread.currentThread().getStackTrace()[1].getMethodName(), "");
        }
    }
}
