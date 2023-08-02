package basic.classroom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "이미지 저장에 실패했습니다.")
public class StoreImageException extends RuntimeException {
    public StoreImageException() {
        super();
    }

    public StoreImageException(String message) {
        super(message);
    }

    public StoreImageException(String message, Throwable cause) {
        super(message, cause);
    }

    public StoreImageException(Throwable cause) {
        super(cause);
    }

    protected StoreImageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
