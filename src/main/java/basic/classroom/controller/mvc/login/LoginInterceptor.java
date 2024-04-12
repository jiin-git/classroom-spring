//package basic.classroom.controller.mvc.login;
//
//import basic.classroom.controller.SessionConst;
//import basic.classroom.domain.MemberStatus;
//import basic.classroom.exception.AuthorizationNotAvailableException;
//import basic.classroom.exception.ErrorCode;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//@Slf4j
//public class LoginInterceptor implements HandlerInterceptor {
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String requestUrl = request.getRequestURI();
//        log.info("Request URI = {}", requestUrl);
//
//        HttpSession session = request.getSession(false);
//        // 로그인 x
//        if (session == null || session.getAttribute(SessionConst.LOGIN_ID) == null) {
//            response.sendRedirect("/");
//            return false;
//        }
//        // 로그인 o
//        // 서로 계정 권한이 다른 사용자가 접속을 시도하는 경우
//        else {
//            MemberStatus memberStatus = (MemberStatus) session.getAttribute(SessionConst.MEMBER_STATUS);
//            String memberStatusToString = memberStatus.toString().toLowerCase();
//            log.info("memberStatus = {}", memberStatusToString);
//
//            if (requestUrl.indexOf(memberStatusToString) == -1) {
//                log.info("memberStatus is not match URI.");
//                throw new AuthorizationNotAvailableException(ErrorCode.INVALID_MEMBER_STATUS);
//            }
//        }
//
//        return true;
//    }
//}
