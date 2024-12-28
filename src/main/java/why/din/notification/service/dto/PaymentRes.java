package why.din.notification.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRes {
    private String res_code;
    private String res_msg;
    private String status;

    public PaymentRes(String res_code, String res_msg, String status) {
        this.res_code = res_code;
        this.res_msg = res_msg;
        this.status = status;
    }
}
