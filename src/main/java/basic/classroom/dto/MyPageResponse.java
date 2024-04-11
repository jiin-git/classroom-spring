package basic.classroom.dto;

import basic.classroom.domain.Instructor;
import basic.classroom.domain.ProfileImage;
import basic.classroom.domain.Student;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MyPageResponse {
    private String name;
    private String loginId;
    private String email;
    private ProfileImage profileImage;

    public static MyPageResponse fromStudent(Student student) {
        MyPageResponseBuilder studentMyPageResponseBuilder = MyPageResponse.builder()
                .name(student.getMember().getName())
                .loginId(student.getMember().getLoginId())
                .email(student.getMember().getEmail());

        if (student.getProfileImage() == null) {
            return studentMyPageResponseBuilder.build();
        }

        return studentMyPageResponseBuilder.profileImage(student.getProfileImage()).build();
    }
    public static MyPageResponse fromInstructor(Instructor instructor) {
        MyPageResponseBuilder instructorMyPageResponseBuilder = MyPageResponse.builder()
                .name(instructor.getMember().getName())
                .loginId(instructor.getMember().getLoginId())
                .email(instructor.getMember().getEmail());

        if (instructor.getProfileImage() == null) {
            return instructorMyPageResponseBuilder.build();
        }

        return instructorMyPageResponseBuilder.profileImage(instructor.getProfileImage()).build();
    }
}
