package net.teachingprogramming.webapp;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/suppl01")
public class Suppl01Controller {

    /**
     * トップページ
     */
    @GetMapping("/")
    public String index() {
        return "suppl01/index";
    }

    /**
     * ログインフォームを表示する
     */
    @GetMapping("/login")
    public String login() {
        return "suppl01/login";
    }

    /**
     * ログイン後に遷移するページ
     */
    @GetMapping("/secret/")
    public String secretIndex() {
        return "suppl01/secret/index";
    }

    /**
     * ログイン中のユーザの情報を表示するページ
     */
    @GetMapping("/secret/info")
    public String secretInfo(@AuthenticationPrincipal UserDetails userDetails, ModelMap modelMap) {
        modelMap.addAttribute("username", userDetails.getUsername());
        return "suppl01/secret/info";
    }

}
