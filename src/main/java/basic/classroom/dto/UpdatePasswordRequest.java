package basic.classroom.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordRequest {
    @Size(min = 2, max = 15, message = "비밀번호는 2~15자리 이내로 입력하세요.")
    private String password;
    @NotBlank(message = "비밀번호를 다시 입력해주세요.")
    private String checkPassword;
}
