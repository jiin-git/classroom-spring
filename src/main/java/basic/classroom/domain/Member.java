package basic.classroom.domain;

import basic.classroom.dto.CreateMember.CreateMemberRequest;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    private String name;
    private String loginId;
    private String password;
    private String email;

    public void updatePassword(String password) {
        this.password = password;
    }
    public void updateEmail(String email) {
        this.email = email;
    }

    public static Member fromCreateMemberRequest(CreateMemberRequest createMemberRequest) {
        return Member.builder()
                .name(createMemberRequest.getName())
                .loginId(createMemberRequest.getLoginId())
                .password(createMemberRequest.getPassword())
                .email(createMemberRequest.getEmail())
                .build();
    }
}
