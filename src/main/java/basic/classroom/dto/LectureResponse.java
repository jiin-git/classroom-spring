package basic.classroom.dto;

import basic.classroom.domain.Lecture;
import basic.classroom.domain.ProfileImage;
import lombok.*;

public class LectureResponse {

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class InstructorLectureBasicResponse {
        private LectureBasicInfo lectureBasicInfo;
        private ProfileImage profileImage;

        public static InstructorLectureBasicResponse fromLecture(Lecture lecture) {
            return InstructorLectureBasicResponse.builder()
                    .lectureBasicInfo(LectureBasicInfo.fromLecture(lecture))
                    .profileImage(ProfileImage.fromLecture(lecture))
                    .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class InstructorLectureDetailsResponse {
        private ProfileImage profileImage;
        private LectureBasicInfo lectureBasicInfo;
        private InstructorInfo instructorInfo;

        public static InstructorLectureDetailsResponse fromLecture(Lecture lecture) {
            return InstructorLectureDetailsResponse.builder()
                    .lectureBasicInfo(LectureBasicInfo.fromLecture(lecture))
                    .instructorInfo(InstructorInfo.fromLecture(lecture))
                    .profileImage(ProfileImage.fromLecture(lecture)).build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class StudentLectureBasicResponse {
        private Long mapperId;
        private LectureBasicInfo lectureBasicInfo;
        private String instructorName;
        private ProfileImage profileImage;

        public static StudentLectureBasicResponse fromLecture(Lecture lecture, Long mapperId) {
            return StudentLectureBasicResponse.builder()
                    .mapperId(mapperId)
                    .lectureBasicInfo(LectureBasicInfo.fromLecture(lecture))
                    .instructorName(lecture.getInstructor().getMember().getName())
                    .profileImage(ProfileImage.fromLecture(lecture))
                    .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class StudentLectureDetailsResponse {
        private Long mapperId;
        private LectureBasicInfo lectureBasicInfo;
        private InstructorInfo instructorInfo;
        private ProfileImage profileImage;

        public static StudentLectureDetailsResponse fromLecture(Lecture lecture, Long mapperId) {
            return StudentLectureDetailsResponse.builder()
                    .mapperId(mapperId)
                    .lectureBasicInfo(LectureBasicInfo.fromLecture(lecture))
                    .instructorInfo(InstructorInfo.fromLecture(lecture))
                    .profileImage(ProfileImage.fromLecture(lecture)).build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class StudentFindLecturesResponse {
        private Long mapperId;
        private Boolean isApplied;
        private LectureBasicInfo lectureBasicInfo;
        private InstructorInfo instructorInfo;
        private ProfileImage profileImage;

        public static StudentFindLecturesResponse fromLecture(Lecture lecture, Long mapperId) {
            StudentFindLecturesResponseBuilder studentFindLecturesResponseBuilder = StudentFindLecturesResponse.builder()
                    .lectureBasicInfo(LectureBasicInfo.fromLecture(lecture))
                    .instructorInfo(InstructorInfo.fromLecture(lecture))
                    .profileImage(ProfileImage.fromLecture(lecture));

            if (mapperId == null) {
                return studentFindLecturesResponseBuilder.isApplied(false).build();
            }
            return studentFindLecturesResponseBuilder.mapperId(mapperId).isApplied(true).build();
        }
    }

}
