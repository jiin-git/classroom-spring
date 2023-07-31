package basic.classroom.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Embeddable
@Getter @Setter
@NoArgsConstructor
public class ProfileImage {

    private String imageName;
    private String dataType;

//    BLob(Binary type 저장)
    @Lob
    private byte[] imageData;

    public ProfileImage(String imageName, String dataType, byte[] imageData) {
        this.imageName = imageName;
        this.dataType = dataType;
        this.imageData = imageData;
    }
}
