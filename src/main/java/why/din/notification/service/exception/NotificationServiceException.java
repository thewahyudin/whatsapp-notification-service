package why.din.notification.service.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationServiceException extends RuntimeException {
  private String errorCode;
  private String errorMessage;
  private String detail;
  private String origin;
  private String carriedData;

  public NotificationServiceException(String errorCode, String errorMessage, String detail, String origin, String carriedData) {
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
    this.detail = detail;
    this.origin = origin;
    this.carriedData = carriedData;
  }

  public NotificationServiceException(String message, String errorCode, String errorMessage, String detail, String origin, String carriedData) {
    super(message);
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
    this.detail = detail;
    this.origin = origin;
    this.carriedData = carriedData;
  }

  public NotificationServiceException(String message, Throwable cause, String errorCode, String errorMessage, String detail, String origin, String carriedData) {
    super(message, cause);
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
    this.detail = detail;
    this.origin = origin;
    this.carriedData = carriedData;
  }

  public NotificationServiceException(Throwable cause, String errorCode, String errorMessage, String detail, String origin, String carriedData) {
    super(cause);
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
    this.detail = detail;
    this.origin = origin;
    this.carriedData = carriedData;
  }

  public NotificationServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String errorCode, String errorMessage, String detail, String origin, String carriedData) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
    this.detail = detail;
    this.origin = origin;
    this.carriedData = carriedData;
  }

  public NotificationServiceException(String message) {
    super(message);
  }
}
