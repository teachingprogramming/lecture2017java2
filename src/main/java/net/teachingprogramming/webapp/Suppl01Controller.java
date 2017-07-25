package net.teachingprogramming.webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/suppl01")
public class Suppl01Controller {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Suppl01Controllerのコンストラクタ
     */
    @Autowired
    public Suppl01Controller(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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

    /**
     * 管理者用トップページ
     */
    @GetMapping("/admin/")
    public String adminIndex() {
        return "suppl01/admin/index";
    }

    /**
     * ユーザの追加（GET、フォームを表示）
     */
    @GetMapping("/admin/add_user")
    public String adminAddUserGet() {
        return "suppl01/admin/add_user";
    }

    /**
     * ユーザの追加（POST、データベースを操作）
     */
    @PostMapping("/admin/add_user")
    public String adminAddUserPost(@RequestParam("username") String username,
                                   @RequestParam("password") String password,
                                   @RequestParam("role") String role) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        jdbcTemplate.update("INSERT INTO users ( username, password , enabled ) VALUES (?, ?, ?)", username, passwordEncoder.encode(password), true);
        jdbcTemplate.update("INSERT INTO authorities ( username, authority) VALUES (?, ?)", username, role);
        return "redirect:/suppl01/admin/";
    }


}
