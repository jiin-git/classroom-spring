package basic.classroom.domain;

import jakarta.persistence.*;


//@Entity
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

//    BLob(Binary type 저장)
    @Lob
    private byte[] data;
}
