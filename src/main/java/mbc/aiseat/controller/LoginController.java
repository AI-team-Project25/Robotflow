package mbc.aiseat.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mbc.aiseat.config.CustomUserDetails;
import mbc.aiseat.dto.*;
import mbc.aiseat.entity.Member;
import mbc.aiseat.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Log4j2
public class LoginController {

    private final MemberService memberService;

    @GetMapping(value = "/signup") // 회원가입 페이지
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "/signup";
    }

    @PostMapping(value = "/signup") // 회원가입 처리
    public String memberForm(@Valid MemberFormDto memberFormdto,
                             BindingResult bindingResult, Model model) throws Exception
    {
        if(bindingResult.hasErrors()) {
            return "/signup";
        }

        try{
            memberService.saveMember(memberFormdto);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            log.info(e.getMessage());
            return "signup";
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            // 로그인된 상태면 홈으로 리다이렉트
            return "redirect:/";
        }

        return "/login"; // 로그인 페이지로 이동
    }

    @GetMapping(value = "/login/error") // 로그인 실패 시 처리
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해 주세요");
        return "/login";
    }

    @GetMapping("/check-email")
    @ResponseBody
    public boolean checkEmailDuplicate(@RequestParam String email) {
        return !memberService.isemailTaken(email); // 사용 중이면 true, 사용 가능하면 false
    }

    @GetMapping("/check-phone")
    @ResponseBody
    public boolean checkPhoneDuplicate(@RequestParam String phone) {
        return !memberService.isphoneTaken(phone); // 사용 중이면 true, 사용 가능하면 false
    }

    @GetMapping("/edit")
    public String editForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        Member member = memberService.findByEmail(email);

        MemberEditDto memberEditDto = new MemberEditDto(member);

        // 연동 여부 설정
        String provider = member.getProvider();
        boolean isLinked = provider != null && !provider.isBlank();

        model.addAttribute("memberEditDto", memberEditDto);
        model.addAttribute("isLinked", isLinked); // 소셜 연동 여부 플래그 추가
        model.addAttribute("provider", provider);

        return "/edit";
    }

    @PostMapping("/edit")
    public String editMember(@Valid MemberEditDto memberEditDto,
                             BindingResult bindingResult,
                             Model model) {

        if (bindingResult.hasErrors()) {
            return "/edit";
        }

        try {
            memberService.updateMember(memberEditDto);

            // 업데이트된 사용자 정보를 다시 불러오기
            Member updatedMember = memberService.findByEmail(memberEditDto.getEmail()); // 이메일이나 ID로 수정된 사용자 정보 찾기
            UserDetails updatedUserDetails = new CustomUserDetails(updatedMember); // 커스텀 UserDetails

            // 새로운 인증 객체 생성
            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                    updatedUserDetails,
                    updatedUserDetails.getPassword(),
                    updatedUserDetails.getAuthorities()
            );

            // 새로 갱신된 인증 정보를 SecurityContext에 설정
            SecurityContextHolder.getContext().setAuthentication(newAuth);

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/edit";
        }

        return "redirect:/"; // 수정 완료 후 리다이렉트
    }

    @PostMapping("/delete")
    public String deleteMember(@AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
        memberService.deleteMember(userDetails.getUsername()); // 사용자 이름 또는 ID를 통해 삭제
        return "redirect:/logout";
    }

    // 연동 해제 버튼 클릭 시 호출되는 메소드
    @PostMapping("/unlink")
    public String unlink(@RequestParam String provider, @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        String email = userDetails.getUsername();
        memberService.unlinkSocialAccount(email, provider);

        return "redirect:/edit";
    }

    @GetMapping("/oauth-success.html")
    public String oauthSuccess(HttpServletRequest request, Model model) {
        String redirectUrl = (String) request.getSession().getAttribute("redirectAfterOAuth");
        if (redirectUrl == null) {
            redirectUrl = "/";
        }
        model.addAttribute("redirectUrl", redirectUrl);
        request.getSession().removeAttribute("redirectAfterOAuth"); // 사용 후 정리
        return "/oauth-success"; // templates/oauth-success.html
    }

    @GetMapping("/findId")
    public String findIdForm(Model model) {
        model.addAttribute("memberFindDto", new MemberFindDto()); // 새로운 MemberFindDto 객체 추가
        return "/findId"; // findId.html을 렌더링
    }

    @PostMapping("/findId")
    public String findIdSearch(@Valid MemberFindDto memberFindDto,
                               BindingResult bindingResult, Model model) {

        // 폼의 유효성 검사
        if (bindingResult.hasErrors()) {
            return "/findId"; // 유효성 검사 실패시 페이지 유지
        }

        // 이메일 찾기
        String email = memberService.findEmailByNameAndPhone(memberFindDto.getName(), memberFindDto.getPhone());

        if (email != null) {
            model.addAttribute("email", email);
        } else {
            model.addAttribute("error", "일치하는 회원이 없습니다.");
        }

        return "/findIdResult"; // 결과를 보여줄 페이지
    }

    @GetMapping("/findPassword")
    public String findPasswordForm(Model model) {
        model.addAttribute("passwordFindDto", new PasswordFindDto());
        return "/findPassword";
    }

    @PostMapping("/findPassword")
    public String findPassword(@Valid PasswordFindDto passwordFindDto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "/findPassword";
        }

        // 이메일을 redirect 파라미터로 전달
        redirectAttributes.addAttribute("email", passwordFindDto.getEmail());
        return "redirect:/resetPassword";
    }

    @GetMapping("/resetPassword")
    public String resetPasswordForm(@RequestParam(required = false) String email, Model model) {
        PasswordResetDto dto = new PasswordResetDto();
        dto.setEmail(email); // 전달된 이메일을 세팅
        model.addAttribute("passwordResetDto", dto);
        return "/resetPassword";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@Valid PasswordResetDto passwordResetDto,
                                BindingResult bindingResult,
                                Model model) {

        log.info("passwordResetDto: " + passwordResetDto.toString());

        log.info("유효성 검사 직전");

        // 기본 유효성 검사
        if (bindingResult.hasErrors()) {
            return "/resetPassword";
        }

        log.info("일치 여부 확인 직전");

        // 비밀번호 일치 여부 확인
        if (!passwordResetDto.isPasswordMatched()) {
            model.addAttribute("passwordMismatch", true);
            return "/resetPassword";
        }

        log.info("비밀번호재설정처리 직전");

        // 실제 비밀번호 재설정 처리
        try {
            log.info("try 안");
            log.info("이메일: " + passwordResetDto.getEmail());
            log.info("패스워드: " + passwordResetDto.getNewPassword());
            memberService.updatePassword(passwordResetDto.getEmail(), passwordResetDto.getNewPassword());
        } catch (IllegalArgumentException e) {
            log.info("catch 안");
            model.addAttribute("passwordResetError", e.getMessage());
            return "resetPassword"; // 이메일이 없을 경우 오류 메시지 표시
        }

        return "redirect:/login"; // 성공적으로 변경 후 로그인 페이지로
    }








}
