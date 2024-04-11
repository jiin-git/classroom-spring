package basic.classroom.dto;

import basic.classroom.domain.MemberStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

public class FindPassword {

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class FindPasswordRequest {
        @NotBlank(message = "아이디를 입력해주세요.")
        private String loginId;
        @Email @NotBlank(message = "이메일을 입력해주세요.")
        private String email;
        @NotNull(message = "회원 상태를 설정해주세요.")
        private MemberStatus memberStatus;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class FindPasswordResponse {
        private int status;
        private String password;
    }
}
