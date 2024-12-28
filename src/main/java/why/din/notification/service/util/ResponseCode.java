package why.din.notification.service.util;

import lombok.Getter;

@Getter
public enum ResponseCode {
    SUCCESS("00", "SUCCESS"),
    FAILED("", "FAILED"),
    BACKEND_ERROR("11", "(BACKEND) "),
    NATS_ERROR("41", "NATS ERROR"),
    CONNECTION_ERROR("91", "CONNECTION ERROR TO BACKEND"),
    TIMEOUT_ERROR("92", "TIMEOUT WHILE RETRIEVING DATA FROM BACKEND"),
    GENERAL_ERROR("99", "GENERAL ERROR");

    private final String code;
    private final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
