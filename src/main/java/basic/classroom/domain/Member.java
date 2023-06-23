package basic.classroom.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Embeddable
@Getter @Setter
public class Member {
    private String name;
    private String loginId;
    private String password;
    private String email;

//    @Enumerated(EnumType.STRING)
//    private MemberStatus memberStatus;

//    private List<Mail> mailbox;

    protected Member() {
    }

    public Member(String name, String loginId, String password, String email) {
        this.name = name;
        this.loginId = loginId;
        this.password = password;
        this.email = email;
    }
}
