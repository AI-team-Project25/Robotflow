package mbc.aiseat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KWJController {

    @GetMapping("/KWJ")
    public String test() {
        return "KWJ/test";
    }

}
