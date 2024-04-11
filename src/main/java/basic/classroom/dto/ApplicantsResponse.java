package basic.classroom.dto;

import basic.classroom.domain.Student;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ApplicantsResponse {
    private Long id;
    private String name;

    public static ApplicantsResponse fromStudent(Student applicant) {
        return ApplicantsResponse.builder()
                .id(applicant.getId())
                .name(applicant.getMember().getName()).build();
    }
}