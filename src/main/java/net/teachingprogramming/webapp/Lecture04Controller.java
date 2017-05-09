package net.teachingprogramming.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * 第4回で使うコントローラ
 */
@Controller
@RequestMapping("/lecture04")
public class Lecture04Controller {

    /**
     * おみくじ: 名前の入力
     */
    @RequestMapping("/omikuji_input")
    public String omikujiInput() {
        return "lecture04/omikuji_input";
    }

    /**
     * おみくじ: 結果の出力
     */
    @RequestMapping("/omikuji_output")
    public String omikujiOutput(ModelMap modelMap, @RequestParam("name") String name) {
        String result = "吉";
        if (Math.random() < 0.5) {
            result = "大吉";
        }
        modelMap.addAttribute("result", result);
        modelMap.addAttribute("name", name);
        return "lecture04/omikuji_output";
    }

    /**
     * 掲示板: コメントを保存しておくフィールド
     */
    private List<String> commentList = new ArrayList<>();
    // private ArrayList<String> commentList = new ArrayList<String>(); // ListとArrayListの関係がよくわからない人向け

    /**
     * 掲示板: get
     */
    @GetMapping("bbs")
    public String bbsGet(ModelMap modelMap) {
        modelMap.addAttribute("commentList", commentList);
        return "lecture04/bbs";
    }

    /**
     * 掲示板: post
     */
    @PostMapping("bbs")
    public String bbsGet(ModelMap modelMap, @RequestParam("comment") String comment) {
        commentList.add(comment);
        modelMap.addAttribute("commentList", commentList);
        return "lecture04/bbs";
    }

    /**
     * じゃんけん: get
     */
    @GetMapping("janken")
    public String janken() {
        return "lecture04/janken";
    }

    /**
     * じゃんけん: post
     */
    @PostMapping("janken")
    public String janken(ModelMap modelMap, @RequestParam("janken") String janken) {
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append("あなたは");
        if (janken.equals("g")) {
            resultBuilder.append("グー");
        } else if (janken.equals("c")) {
            resultBuilder.append("チョキ");
        } else {
            resultBuilder.append("パー");
        }
        resultBuilder.append("を出して");
        double random = Math.random();
        if (random < 0.333) {
            resultBuilder.append("勝ちました。");
        } else if (random < 0.666) {
            resultBuilder.append("負けました。");
        } else {
            resultBuilder.append("引き分けました。");
        }
        modelMap.addAttribute("result", resultBuilder.toString());
        return "lecture04/janken";
    }

    /**
     * アンケート
     */
    @RequestMapping("questionnaire")
    public String questionnaire(ModelMap modelMap,
                           @RequestParam(value = "java", required = false) boolean java,
                           @RequestParam(value = "kotlin", required = false) boolean kotlin,
                           @RequestParam(value = "scala", required = false) boolean scala) {
        StringBuilder resultBuilder = new StringBuilder();
        if (java) {
            resultBuilder.append("Javaがチェックされました。");
        }
        if (kotlin) {
            resultBuilder.append("Kotlinがチェックされました。");
        }
        if (scala) {
            resultBuilder.append("Scalaがチェックされました。");
        }
        modelMap.addAttribute("result", resultBuilder.toString());
        return "lecture04/questionnaire";
    }

}
