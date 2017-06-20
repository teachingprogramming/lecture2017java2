package net.teachingprogramming.webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 第10回で使うコントローラ
 */
@Controller
@RequestMapping("/lecture10")
public class Lecture10Controller {

    private final JdbcTemplate jdbcTemplate;

    /**
     * コンストラクタ
     */
    @Autowired
    public Lecture10Controller(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 掲示板: get
     */
    @GetMapping("/bbs")
    public String bbsGet(ModelMap modelMap) {
        List<BbsComment> commentList = new ArrayList<>(); // 表示するコメントのリスト
        List<Map<String, Object>> dataList = jdbcTemplate.queryForList("SELECT * FROM bbs_comment"); // データベースから取り出す。
        for (Map<String, Object> data : dataList) {
            BbsComment comment = new BbsComment();
            comment.body = (String)data.get("body");
            comment.name = (String)data.get("name");
            comment.date = (Date)data.get("date");
            commentList.add(comment);
        }
        modelMap.addAttribute("commentList", commentList);
        return "lecture05/bbs";
    }

    /**
     * 掲示板: post
     */
    @PostMapping("/bbs")
    public String bbsGet(@RequestParam("body") String body, @RequestParam("name") String name) {
        Date date = new Date();
        jdbcTemplate.update("INSERT INTO bbs_comment ( body , name, date ) VALUES (?, ?, ?)", body, name, date);
        return "redirect:/lecture10/bbs"; // http://localhost:18080/lecture10/bbsにリダイレクト（GET）
    }

}
