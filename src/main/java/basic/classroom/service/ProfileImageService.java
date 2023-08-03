package basic.classroom.service;

import basic.classroom.domain.Instructor;
import basic.classroom.domain.Lecture;
import basic.classroom.domain.ProfileImage;
import basic.classroom.domain.Student;
import basic.classroom.exception.StoreImageException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProfileImageService {
    public static void initializeMemberProfile(Student student) {
        student.getProfileImage().setImageName(null);
        student.getProfileImage().setDataType(null);
        student.getProfileImage().setImageData(null);
    }

    public static void initializeMemberProfile(Instructor instructor) {
        instructor.getProfileImage().setImageName(null);
        instructor.getProfileImage().setDataType(null);
        instructor.getProfileImage().setImageData(null);
    }

    public static void saveImageFile(MultipartFile imageFile, Student student) throws IOException {
        ProfileImage profileImage = validProfileImage(imageFile);
        student.setProfileImage(profileImage);
    }

    public static void saveImageFile(MultipartFile imageFile, Instructor instructor) throws IOException {
        ProfileImage profileImage = validProfileImage(imageFile);
        instructor.setProfileImage(profileImage);
    }

    public static void saveLectureImageFile(MultipartFile imageFile, Lecture lecture) throws IOException {
        ProfileImage profileImage = validProfileImage(imageFile);
        lecture.setProfileImage(profileImage);
    }

    public static ProfileImage validProfileImage(MultipartFile imageFile) throws IOException {
        ProfileImage profileImage = getProfileImage(imageFile);
        List<String> validImageTypes = getValidImageTypes();
        if (!validImageTypes.contains(imageFile.getContentType())) {
            throw new StoreImageException();
        }

        return profileImage;
    }

    public static ProfileImage getProfileImage(MultipartFile imageFile) throws IOException {
        String imageName = Normalizer.normalize(imageFile.getOriginalFilename(), Normalizer.Form.NFC);
        String contentType = imageFile.getContentType();
        byte[] imageBytes = imageFile.getBytes();

        return new ProfileImage(imageName, contentType, imageBytes);
    }

    public static List<String> getValidImageTypes() {
        List<String> validImageTypes = new ArrayList<>();
        ValidImageType[] values = ValidImageType.values();
        for (ValidImageType value : values) {
            String dataType = "image/" + value.toString().toLowerCase();
            validImageTypes.add(dataType);
        }
        return validImageTypes;
    }
}
