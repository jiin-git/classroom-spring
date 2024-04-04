package basic.classroom.domain;

import basic.classroom.dto.AddLectureRequest;
import basic.classroom.dto.ApplicantsResponse;
import basic.classroom.dto.UpdateLecture.UpdateLectureDto;
import basic.classroom.exception.ApplyLectureException;
import basic.classroom.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Lecture {
    @Id @GeneratedValue
    private Long id;
    private String name;

    @Embedded
    private ProfileImage profileImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    @JsonIgnore
    private Instructor instructor;

    private int personnel;
    private int remainingPersonnel;

    @Enumerated(EnumType.STRING)
    private LectureStatus lectureStatus;

    // key : student id -> mapper id(mapper를 통해서만 학생 엔티티를 접근할 수 있다.)
    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL)
    private Map<Long, LectureStudentMapper> appliedStudents;

    public void addStudent(LectureStudentMapper mapper) {
        if (remainingPersonnel <= 0 || lectureStatus != LectureStatus.OPEN) {
            throw new ApplyLectureException(ErrorCode.FAILED_APPLY_LECTURE);
        }

        remainingPersonnel = remainingPersonnel - 1;
        if (remainingPersonnel == 0) {
            lectureStatus = LectureStatus.FULL;
        }

        Long mapperId = mapper.getId();
        appliedStudents.put(mapperId, mapper);
        mapper.addLecture(this);
    }
    public void removeStudent(Long mapperId) {
        appliedStudents.remove(mapperId);
        remainingPersonnel = remainingPersonnel + 1;

        if (lectureStatus == LectureStatus.FULL) {
            lectureStatus = LectureStatus.OPEN;
        }
    }
    public List<Long> getAppliedStudentsIds() {
        List<Long> appliedStudentsIds = new ArrayList<>();
        appliedStudents.forEach((key, mapper) -> appliedStudentsIds.add(mapper.getStudentId()));
        return appliedStudentsIds;
    }
    public List<ApplicantsResponse> getApplicantsResponse() {
        List<ApplicantsResponse> applicants = new ArrayList<>();
        appliedStudents.forEach((key, mapper) ->
                applicants.add(ApplicantsResponse.fromStudent(mapper.getStudent())));
        return applicants;
    }

    public void addInstructor(Instructor instructor) {
        this.instructor = instructor;
    }
    public Long getInstructorId() {
        return this.instructor.getId();
    }

    public static Lecture createLecture(String name, Instructor instructor, int personnel, LectureStatus lectureStatus) {
        return Lecture.builder()
                .name(name)
                .instructor(instructor)
                .personnel(personnel)
                .remainingPersonnel(personnel)
                .lectureStatus(lectureStatus)
                .appliedStudents(new HashMap<>())
                .build();
    }
    public static Lecture createLecture(Instructor instructor, AddLectureRequest addLectureRequest) {
        return Lecture.builder()
                .name(addLectureRequest.getName())
                .instructor(instructor)
                .personnel(addLectureRequest.getPersonnel())
                .remainingPersonnel(addLectureRequest.getPersonnel())
                .lectureStatus(addLectureRequest.getLectureStatus())
                .appliedStudents(new HashMap<>())
                .build();
    }
    public static Lecture createLecture(Instructor instructor, AddLectureRequest addLectureRequest, ProfileImage profileImage) {
        LectureBuilder lectureBuilder = Lecture.builder()
                .name(addLectureRequest.getName())
                .instructor(instructor)
                .personnel(addLectureRequest.getPersonnel())
                .remainingPersonnel(addLectureRequest.getPersonnel())
                .lectureStatus(addLectureRequest.getLectureStatus())
                .appliedStudents(new HashMap<>());

        if (profileImage == null) {
            return lectureBuilder.build();
        }
        return lectureBuilder.profileImage(profileImage).build();
    }
    public void updateLecture(UpdateLectureDto updateLectureDto) {
        this.personnel = updateLectureDto.getPersonnel();
        this.remainingPersonnel = updateLectureDto.getRemainingPersonnel();
        this.lectureStatus = updateLectureDto.getLectureStatus();
        if (updateLectureDto.getProfileImage() != null) {
            this.profileImage = updateLectureDto.getProfileImage();
        }
    }

    public void updateLecture(ProfileImage profileImage) {
        if (profileImage != null) {
            this.profileImage = profileImage;
        }
    }

    public List<Student> findAppliedStudents() {
        List<Student> students = new ArrayList<>();
        appliedStudents.forEach((k, m) -> students.add(m.getStudent()));

        return students;
    }
}
