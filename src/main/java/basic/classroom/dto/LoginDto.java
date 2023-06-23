package basic.classroom.dto;

import basic.classroom.domain.MemberStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter @Setter
@NoArgsConstructor
public class LoginDto {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String loginPw;

    @NotNull(message = "회원 상태를 설정해주세요.")
    private MemberStatus memberStatus;

    public LoginDto(String loginId, String loginPw, MemberStatus memberStatus) {
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.memberStatus = memberStatus;
    }
}
