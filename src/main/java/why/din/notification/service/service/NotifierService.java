package why.din.notification.service.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.nats.client.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import why.din.notification.service.config.NatsVarConfig;
import why.din.notification.service.exception.NotificationServiceException;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@Service
public class NotifierService {
    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    private Connection natsConnection;
    private final NatsVarConfig natsCfg;
    private final CallMeBotService callMeBotService;

    public NotifierService(NatsVarConfig natsCfg, CallMeBotService callMeBotService) {
        this.natsCfg = natsCfg;
        this.callMeBotService = callMeBotService;
    }

    public void subscribeService() {
        // LogsId
        LocalDateTime startTime = LocalDateTime.now();
        String microseconds = String.format("%06d", startTime.getNano() / 1000);
        String logsId = String.format("%s%s", startTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")), microseconds);

        try {
            MessageHandler handler = new MessageHandlerImpl(callMeBotService);

            natsConnection = Nats.connect(natsCfg.getUrl());
            Dispatcher dispatcher = natsConnection.createDispatcher(handler);
            dispatcher.subscribe(natsCfg.getTopic());
            System.out.println(String.format("Listening for messages on %s ...", natsCfg.getTopic()));

        } catch (Exception e) {
            log.error(String.format("%s - GeneralException: %s", logsId, e.getMessage()));
        }
    }

    public void close() throws Exception {
        natsConnection.close();
    }

    public static class MessageHandlerImpl implements MessageHandler {
        private final CallMeBotService callMeBotService;

        // Constructor injection for CallMeBotService
        public MessageHandlerImpl(CallMeBotService callMeBotService) {
            this.callMeBotService = callMeBotService;
        }

        @Override
        public void onMessage(Message msg) {
            // LogsId
            LocalDateTime startTime = LocalDateTime.now();
            String microseconds = String.format("%06d", startTime.getNano() / 1000);
            String logsId = String.format("%s%s", startTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")), microseconds);

            try {
                log.info(String.format("%s - Notifier service started.", logsId));

                // Process the message
                String message = new String(msg.getData());
                Map<String, Object> msgMap = gson.fromJson(message, Map.class);
                String custId = msgMap.get("customer_id").toString();
                String custPhoneNum = msgMap.get("customer_phone_number").toString();
                String productId = msgMap.get("product_id").toString();
                String amount = msgMap.get("payment_amount").toString();
                String currency = msgMap.get("currency").toString();
                String paymentDT = msgMap.get("payment_dt").toString();

                DecimalFormat decFormat = new DecimalFormat("#,###");
                String amountF = decFormat.format(Integer.parseInt(amount));

                String teks = String.format("""
                    Hello! You have just made a payment for product *%s* worth *%s %s* at _%s_.
                    
                    If you did not make this transaction please contact our call center at *+62 122333* immediately.""",
                        productId, currency, amountF, paymentDT);

                LocalDateTime startProcess = LocalDateTime.now();
                String response = callMeBotService.sendNotification(custPhoneNum, teks);
                log.info(String.format("%s - Notification sent for %s. Time elapsed: %d ms. Response: %s",
                        logsId, custId, Duration.between(startProcess, LocalDateTime.now()).toMillis(), "\"..."+response.substring(response.length()-50)+"\""));

            } catch (NotificationServiceException e) {
                log.error(String.format("%s - NotificationServiceException %s: %s", logsId, e.getOrigin(), e.getDetail()));
            } catch (Exception e) {
                log.error(String.format("%s - GeneralException: %s", logsId, e.getMessage()));
            }

            log.info(String.format("%s - Notifier service finished. Time elapsed: %d ms",
                    logsId, Duration.between(startTime, LocalDateTime.now()).toMillis()));
        }
    }
}
