package why.din.notification.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentReq {
    private String customer_id;
    private String customer_phone_number;
    private String product_id;
    private String payment_amount;
    private String currency;
}
