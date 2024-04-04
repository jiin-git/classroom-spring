package basic.classroom.dto;

import basic.classroom.domain.Lecture;
import basic.classroom.domain.ProfileImage;
import lombok.*;

public class LectureResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LectureDetailsResponse {
        private ProfileImage profileImage;
        private LectureBasicInfo lectureBasicInfo;
        private InstructorInfo instructorInfo;

        public static LectureDetailsResponse fromLecture(Lecture lecture) {
            return LectureDetailsResponse.builder()
                    .lectureBasicInfo(LectureBasicInfo.fromLecture(lecture))
                    .instructorInfo(InstructorInfo.fromLecture(lecture))
                    .profileImage(ProfileImage.fromLecture(lecture)).build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LectureBasicResponse {
        private LectureBasicInfo lectureBasicInfo;
        private ProfileImage profileImage;

        public static LectureBasicResponse fromLecture(Lecture lecture) {
            return LectureBasicResponse.builder()
                    .lectureBasicInfo(LectureBasicInfo.fromLecture(lecture))
                    .profileImage(ProfileImage.fromLecture(lecture))
                    .build();
        }
    }
}
