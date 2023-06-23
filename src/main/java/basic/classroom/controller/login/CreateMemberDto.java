package basic.classroom.controller.login;

import basic.classroom.domain.MemberStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class CreateMemberDto {

    @Size(min = 2, max = 20, message = "아이디는 2 ~ 20자리 사이로 입력해주세요.")
    private String loginId;

    @Size(min = 2, max = 15, message = "비밀번호는 2 ~ 15자리 사이로 입력해주세요.")
    private String loginPw;

    @Size(min = 2, max = 10, message = "이름은 2 ~ 10자리 사이로 입력해주세요.")
    private String name;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email
    private String email;

    @NotNull(message = "회원 상태를 설정해주세요.")
    private MemberStatus memberStatus;

    public CreateMemberDto(String loginId, String loginPw, String name, String email, MemberStatus memberStatus) {
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.name = name;
        this.email = email;
        this.memberStatus = memberStatus;
    }
}
