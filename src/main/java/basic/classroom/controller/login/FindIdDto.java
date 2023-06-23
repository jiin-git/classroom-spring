package basic.classroom.controller.login;

import basic.classroom.domain.MemberStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class FindIdDto {

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email
    private String email;

    @NotNull(message = "회원 상태를 설정해주세요.")
    private MemberStatus memberStatus;

    public FindIdDto(String name, String email, MemberStatus memberStatus) {
        this.name = name;
        this.email = email;
        this.memberStatus = memberStatus;
    }
}
