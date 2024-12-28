package why.din.notification.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import why.din.notification.service.dto.PaymentReq;
import why.din.notification.service.dto.PaymentRes;
import why.din.notification.service.service.NotifierService;
import why.din.notification.service.service.PaymentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class Controller {
    private final PaymentService paymentService;
    private final NotifierService notifierService;

    @PostMapping("payment")
    public ResponseEntity<PaymentRes> payment (@RequestBody PaymentReq paymentReq){
        return paymentService.payment(paymentReq);
    }

    @GetMapping("notification")
    public String notification(){
        notifierService.subscribeService();
        return "OK";
    }


}
