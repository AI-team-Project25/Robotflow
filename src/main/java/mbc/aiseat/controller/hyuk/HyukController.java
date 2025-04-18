package mbc.aiseat.controller.hyuk;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
@Log4j2
public class HyukController {

    @GetMapping("/hyuk_index")
    public String hyukIndex(Model model, Principal principal) {

        if (principal != null) {
            String username = principal.getName(); // 로그인한 사용자 이름
            model.addAttribute("username", username); // 뷰에 넘김
        }
        return "hyuk/hyuk_index";

    }

    // 클래스 경로가 /hyuk/classRoom/class1.html 형식으로 올 거니까
    @GetMapping("/classRoom/class{num}.html")
    public String classPage(@PathVariable String num) {
        return "hyuk/classRoom/class" + num;
    }




}
