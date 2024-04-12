//package basic.classroom.controller.mvc.members.v1;
//
//import basic.classroom.controller.SessionConst;
//import basic.classroom.domain.Instructor;
//import basic.classroom.domain.Student;
//import basic.classroom.dto.UpdateMemberDto;
//import basic.classroom.dto.UpdatePasswordRequest;
//import basic.classroom.service.datajpa.MemberJpaService;
//import jakarta.servlet.http.HttpSession;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.ModelAttribute;
//
//@Slf4j
////@Controller
//@RequiredArgsConstructor
//public class MyPageController {
//    private final MemberJpaService memberService;
//
////    @GetMapping("/instructor/mypage")
//    public String instructorMyPage(HttpSession session, Model model) {
//        Instructor instructor = findInstructor(session);
//        model.addAttribute("instructor", instructor);
//        return "member/instructor/myPage";
//    }
//
////    @GetMapping("/student/mypage")
//    public String studentMyPage(HttpSession session, Model model) {
//        Student student = findStudent(session);
//        model.addAttribute("student", student);
//        return "member/student/myPage";
//    }
//
////    @GetMapping("/instructor/update/mypage")
//    public String updateInstructorMyPageForm(HttpSession session, Model model) {
//        Instructor instructor = findInstructor(session);
//        UpdateMemberDto updateMemberDto = new UpdateMemberDto(instructor);
//
//        model.addAttribute("instructor", updateMemberDto);
//        if (instructor.getProfileImage() != null) {
//            model.addAttribute("profileImage", instructor.getProfileImage());
//        }
//        return "member/instructor/updateMyPage";
//    }
//
////    @GetMapping("/student/update/mypage")
//    public String updateStudentMyPageForm(HttpSession session, Model model) {
//        Student student = findStudent(session);
//        UpdateMemberDto updateMemberDto = new UpdateMemberDto(student);
//
//        model.addAttribute("student", updateMemberDto);
//        if (student.getProfileImage() != null) {
//            model.addAttribute("profileImage", student.getProfileImage());
//        }
//        return "member/student/updateMyPage";
//    }
//
////    @PostMapping("/instructor/update/mypage")
//    public String UpdateInstructorMyPage(@Validated @ModelAttribute("instructor") UpdateMemberDto updateParam, BindingResult bindingResult, HttpSession session) {
//        Instructor instructor = findInstructor(session);
//
//        // 검증 로직
//        if (bindingResult.hasErrors()) {
//            return "member/instructor/updateMyPage";
//        }
//
//        // 성공 로직
//        memberService.update(instructor, updateParam);
//        return "redirect:/instructor/mypage";
//    }
//
////    @PostMapping("/student/update/mypage")
//    public String UpdateStudentMyPage(@Validated @ModelAttribute("student") UpdateMemberDto updateParam, BindingResult bindingResult, HttpSession session) {
//        Student student = findStudent(session);
//
//        // 검증 로직
//        if (bindingResult.hasErrors()) {
//            return "member/student/updateMyPage";
//        }
//
//        // 성공 로직
//        memberService.update(student, updateParam);
//        return "redirect:/student/mypage";
//    }
//
////    @GetMapping("/instructor/update/pw")
//    public String UpdateInstructorPwForm(Model model) {
//        model.addAttribute("pwForm", new UpdatePasswordRequest());
//        return "member/instructor/updatePw";
//    }
//
////    @GetMapping("/student/update/pw")
//    public String updateStudentPwForm(Model model) {
//        model.addAttribute("pwForm", new UpdatePasswordRequest());
//        return "member/student/updatePw";
//    }
//
////    @PostMapping("/instructor/update/pw")
//    public String updateInstructorPw(@Validated @ModelAttribute("pwForm") UpdatePasswordRequest updateParam, BindingResult bindingResult, HttpSession session) {
//        Instructor instructor = findInstructor(session);
//
//        // 검증 로직
//        if (bindingResult.hasErrors()) {
//            return "member/instructor/updatePw";
//        }
//
//        if (!updateParam.getPassword().equals(updateParam.getCheckPassword())) {
//            bindingResult.reject("notMatchPassword", "비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
//            return "member/instructor/updatePw";
//        }
//
//        // 성공 로직
//        memberService.updatePassword(instructor, updateParam.getPassword());
//        return "redirect:/instructor/mypage";
//    }
//
////    @PostMapping("/student/update/pw")
//    public String updateStudentPw(@Validated @ModelAttribute("pwForm") UpdatePasswordRequest updateParam, BindingResult bindingResult, HttpSession session) {
//        Student student = findStudent(session);
//
//        // 검증 로직
//        if (bindingResult.hasErrors()) {
//            return "member/student/updatePw";
//        }
//        if (!updateParam.getPassword().equals(updateParam.getCheckPassword())) {
//            bindingResult.reject("notMatchPassword", "비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
//            return "member/student/updatePw";
//        }
//
//        // 성공 로직
//        memberService.updatePassword(student, updateParam.getPassword());
//        return "redirect:/student/mypage";
//    }
//
//    private Student findStudent(HttpSession session) {
//        Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_ID);
//        Student student = memberService.findStudent(memberId);
//        return student;
//    }
//    private Instructor findInstructor(HttpSession session) {
//        Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_ID);
//        Instructor instructor = memberService.findInstructor(memberId);
//        return instructor;
//    }
//}
