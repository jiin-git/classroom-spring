package basic.classroom.domain;

import basic.classroom.dto.CreateMember.CreateMemberRequest;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Builder
@Getter @Setter
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

    public static Member fromMember(Member member) {
        return Member.builder()
                .name(member.getName())
                .loginId(member.getLoginId())
                .password(member.getPassword())
                .email(member.getEmail())
                .build();
    }
    public static Member fromCreateMemberRequest(CreateMemberRequest createMemberRequest) {
        return Member.builder()
                .name(createMemberRequest.getName())
                .loginId(createMemberRequest.getLoginId())
                .password(createMemberRequest.getPassword())
                .email(createMemberRequest.getEmail())
                .build();
    }
    public Member(CreateMemberRequest createMemberRequest) {
        this.name = createMemberRequest.getName();
        this.loginId = createMemberRequest.getLoginId();
        this.password = createMemberRequest.getPassword();
        this.email = createMemberRequest.getEmail();
    }
}
