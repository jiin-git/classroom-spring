package basic.classroom.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMyPageRequest {
    @Email @NotBlank
    private String email;
    private MultipartFile imageFile;
}

