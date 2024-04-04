package basic.classroom.dto;

import basic.classroom.domain.MemberStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

public class FindIds {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FindIdsRequest {
        @NotBlank(message = "이름을 입력해주세요.")
        private String name;
        @Email @NotBlank(message = "이메일을 입력해주세요.")
        private String email;
        @NotNull(message = "회원 상태를 설정해주세요.")
        private MemberStatus memberStatus;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FindIdsResponse {
        private int status;
        private List<String> loginIds;
    }
}
