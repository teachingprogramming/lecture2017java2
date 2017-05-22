package net.teachingprogramming.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * 第5回で使うコントローラ
 */
@Controller
@RequestMapping("/lecture05")
public class Lecture05Controller {

    /**
     * 掲示板: コメントを保存しておくフィールド
     */
    private List<BbsComment> commentList = new ArrayList<>();

    /**
     * 掲示板: get
     */
    @GetMapping("/bbs")
    public String bbsGet(ModelMap modelMap) {
        modelMap.addAttribute("commentList", commentList);
        return "lecture05/bbs";
    }

    /**
     * 掲示板: post
     */
    @PostMapping("/bbs")
    public String bbsGet(ModelMap modelMap, @RequestParam("body") String body, @RequestParam("name") String name) {
        BbsComment comment = new BbsComment();
        comment.body = body;
        comment.name = name;
        comment.date = new Date();
        commentList.add(comment);
        modelMap.addAttribute("commentList", commentList);
        return "lecture05/bbs";
    }

}
