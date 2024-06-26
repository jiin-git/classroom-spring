package basic.classroom.dto;

import basic.classroom.domain.MemberStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

public class CreateMember {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateMemberRequest {
        @Size(min = 2, max = 20, message = "아이디는 2 ~ 20자리 사이로 입력해주세요.")
        private String loginId;
        @Size(min = 2, max = 15, message = "비밀번호는 2 ~ 15자리 사이로 입력해주세요.")
        private String password;
        @Size(min = 2, max = 10, message = "이름은 2 ~ 10자리 사이로 입력해주세요.")
        private String name;
        @Email @NotBlank(message = "이메일을 입력해주세요.")
        private String email;
        @NotNull(message = "회원 상태를 설정해주세요.")
        private MemberStatus memberStatus;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class CreateMemberResponse {
        private String loginId;
        private String name;
        private String email;
        private MemberStatus memberStatus;

        public static CreateMemberResponse fromRequestDto(CreateMemberRequest createMemberRequest) {
            return CreateMemberResponse.builder()
                    .loginId(createMemberRequest.getLoginId())
                    .name(createMemberRequest.getName())
                    .email(createMemberRequest.getEmail())
                    .memberStatus(createMemberRequest.getMemberStatus()).build();
        }
    }
}
