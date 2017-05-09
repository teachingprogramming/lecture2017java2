package net.teachingprogramming.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

}
