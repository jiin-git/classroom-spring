package basic.classroom.domain;

import basic.classroom.dto.CreateMemberDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@Getter @Setter
public class Member {
    private String name;
    private String loginId;
    private String password;
    private String email;

//    @Enumerated(EnumType.STRING)
//    private MemberStatus memberStatus;

//    private List<Mail> mailbox;

    public Member(CreateMemberDto createMemberDto) {
        this.name = createMemberDto.getName();
        this.loginId = createMemberDto.getLoginId();
        this.password = createMemberDto.getLoginPw();
        this.email = createMemberDto.getEmail();
    }

    public Member(String name, String loginId, String password, String email) {
        this.name = name;
        this.loginId = loginId;
        this.password = password;
        this.email = email;
    }
}
