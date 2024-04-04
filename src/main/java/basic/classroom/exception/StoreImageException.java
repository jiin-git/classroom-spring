package basic.classroom.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "이미지 저장에 실패했습니다.")
public class StoreImageException extends RuntimeException {
    private ErrorCode errorCode;

    public StoreImageException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
