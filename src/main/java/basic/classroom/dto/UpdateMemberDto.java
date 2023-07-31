package basic.classroom.dto;


import basic.classroom.domain.Instructor;
import basic.classroom.domain.Student;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
@NoArgsConstructor
public class UpdateMemberDto {

    private String name;
    private String loginId;

    private String password;
    private String checkPassword;

    @Email(message = "이메일을 입력하세요.")
    @NotBlank(message = "이메일을 입력하세요.")
    private String email;

    private MultipartFile imageFile;

    public UpdateMemberDto(Student student) {
        this.name = student.getMember().getName();
        this.loginId =  student.getMember().getLoginId();
        this.password = student.getMember().getPassword();
        this.email = student.getMember().getEmail();
    }

    public UpdateMemberDto(Instructor instructor) {
        this.name = instructor.getMember().getName();
        this.loginId =  instructor.getMember().getLoginId();
        this.password = instructor.getMember().getPassword();
        this.email = instructor.getMember().getEmail();
    }



//    public UpdateMemberDto(String email) {
//        this.email = email;
//    }
}
