package basic.classroom.dto;

import basic.classroom.domain.LectureStatus;
import basic.classroom.domain.ProfileImage;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

public class UpdateLecture {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateLectureRequest {
        @Range(min = 0, max = 100, message = "정원을 0 ~ 100명 사이로 입력해주세요.")
        private int personnel;
        @NotNull(message = "강의 상태를 설정해주세요.")
        private LectureStatus lectureStatus;
        private MultipartFile imageFile;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateLectureDto {
        private int personnel;
        private int remainingPersonnel;
        private LectureStatus lectureStatus;
        private ProfileImage profileImage;

        public static UpdateLectureDto fromRequest(UpdateLectureRequest updateLectureRequest, int remainingPersonnel, ProfileImage profileImage) {
            UpdateLectureDtoBuilder updateLectureDtoBuilder = UpdateLectureDto.builder()
                    .personnel(updateLectureRequest.getPersonnel())
                    .lectureStatus(updateLectureRequest.getLectureStatus())
                    .remainingPersonnel(remainingPersonnel);

            if (profileImage == null) {
                    return updateLectureDtoBuilder.build();
            }

            return updateLectureDtoBuilder.profileImage(profileImage).build();
        }
    }
}
