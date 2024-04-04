package basic.classroom.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileImage {
    private String imageName;
    private String dataType;
    @Lob
    private byte[] imageData;

    public static ProfileImage fromLecture(Lecture lecture) {
        if (lecture.getProfileImage() == null) {
            return ProfileImage.builder().build();
        }

        return ProfileImage.builder()
                .imageName(lecture.getProfileImage().getImageName())
                .dataType(lecture.getProfileImage().getDataType())
                .imageData(lecture.getProfileImage().getImageData())
                .build();
    }
}
