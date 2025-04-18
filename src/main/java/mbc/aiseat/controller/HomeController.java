package mbc.aiseat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";

    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }// localhost 접속시 index.html 반환
}
