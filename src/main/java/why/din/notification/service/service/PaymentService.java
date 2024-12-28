package why.din.notification.service.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.nats.client.Connection;
import io.nats.client.Nats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import why.din.notification.service.config.NatsVarConfig;
import why.din.notification.service.dto.PaymentReq;
import why.din.notification.service.dto.PaymentRes;
import why.din.notification.service.exception.NotificationServiceException;
import why.din.notification.service.util.ResponseCode;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Slf4j
public class PaymentService {
    private final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    private Connection natConnection;
    private final NatsVarConfig natsCfg;

    public ResponseEntity<PaymentRes> payment (PaymentReq req) {
        String customerId = req.getCustomer_id();
        String customerPhoneNumber = req.getCustomer_phone_number();
        String productId = req.getProduct_id();
        String paymentAmount = req.getPayment_amount();
        String currency = req.getCurrency();

        String res_code = "";
        String res_msg = "";
        String status = ResponseCode.FAILED.getMessage();
        LocalDateTime startProcess;

        // LogsId
        LocalDateTime startTime = LocalDateTime.now();
        String microseconds = String.format("%06d", startTime.getNano() / 1000);
        String logsId = String.format("%s%s", startTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")), microseconds);

        try {
            String reqStr = gson.toJson(req);
            log.info(String.format("%s - Payment service started. Inc Req: %s", logsId, reqStr));

            Map<String, String> reqMap = gson.fromJson(reqStr, Map.class);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy hh:mm a");
            String paymentDateTime = startTime.format(formatter);
            reqMap.put("payment_dt", paymentDateTime);
            String reqMapStr = gson.toJson(reqMap);

            // Publish to NATS
            startProcess = LocalDateTime.now();
            natConnection = Nats.connect(natsCfg.getUrl());
            natConnection.publish(natsCfg.getTopic(), reqMapStr.getBytes(StandardCharsets.UTF_8));
            log.info(String.format("%s - Data published to NATS. Time elapsed: %d ms",
                    logsId, Duration.between(startProcess, LocalDateTime.now()).toMillis()));

            status = ResponseCode.SUCCESS.getMessage();
            res_code = ResponseCode.SUCCESS.getCode();
            res_msg = ResponseCode.SUCCESS.getMessage();

        } catch (NotificationServiceException e) {
            res_code = e.getErrorCode();
            res_msg = e.getErrorMessage();
            log.error(String.format("%s - NotificationServiceException %s: %s", logsId, e.getOrigin(), e.getDetail()));
        } catch (Exception e) {
            res_code = ResponseCode.GENERAL_ERROR.getCode();
            res_msg = ResponseCode.GENERAL_ERROR.getMessage();
            log.error(String.format("%s - GeneralException: %s", logsId, e.getMessage()));
        }

        PaymentRes response = new PaymentRes(res_code, res_msg, status);

        String resStr = gson.toJson(response);
        log.info(String.format("%s - Payment service finished. Out Res: %s. Time elapsed: %d ms",
                logsId, resStr, Duration.between(startTime, LocalDateTime.now()).toMillis()));

        return ResponseEntity.ok(response);
    }
}
