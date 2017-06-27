package net.teachingprogramming.webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 第11回のコントローラ
 * オンラインノート（メモ帳）
 */
@Controller
@RequestMapping("/lecture11")
public class Lecture11Controller {

    private final JdbcTemplate jdbcTemplate;

    /**
     * コンストラクタ
     */
    @Autowired
    public Lecture11Controller(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        // noteテーブルが存在していない場合に作成する
        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS note (\n" +
                "  id INT AUTO_INCREMENT PRIMARY KEY,\n" +
                "  title TEXT,\n" +
                "  body TEXT,\n" +
                "  date TIMESTAMP\n" +
                ")"
        );
    }

    /**
     * トップページ
     */
    @GetMapping("/")
    public String index(ModelMap modelMap) {
        List<Note> noteList = new ArrayList<>(); // 表示するノートのリスト
        List<Map<String, Object>> dataList = jdbcTemplate.queryForList("SELECT * FROM note ORDER BY date DESC"); // 新着順でデータベースから取り出す。
        for (Map<String, Object> data : dataList) {
            Note note = new Note();
            note.id = (int)data.get("id");
            note.title = (String)data.get("title");
            note.body = (String)data.get("body");
            note.date = (Date)data.get("date");
            noteList.add(note);
        }
        modelMap.addAttribute("noteList", noteList);
        return "lecture11/index";
    }

    /**
     * 新規作成(GET)
     */
    @GetMapping("/add")
    public String addGet() {
        return "lecture11/add";
    }

    /**
     * 新規作成(POST)
     */
    @PostMapping("/add")
    public String addPost(@RequestParam("title") String title, @RequestParam("body") String body) {
        Date date = new Date();
        jdbcTemplate.update("INSERT INTO note ( title, body , date ) VALUES (?, ?, ?)", title, body, date);
        return "redirect:/lecture11/"; // トップページにリダイレクト
    }

    /**
     * 編集(GET)
     */
    @GetMapping("/edit")
    public String editGet(@RequestParam("id") int id, ModelMap modelMap) {
        List<Map<String, Object>> dataList = jdbcTemplate.queryForList("SELECT * FROM note WHERE id = ?", id); // IDが一致するものを取得
        Map<String, Object> firstData = dataList.get(0);
        Note note = new Note();
        note.id = (int)firstData.get("id");
        note.title = (String)firstData.get("title");
        note.body = (String)firstData.get("body");
        note.date = (Date)firstData.get("date");
        modelMap.addAttribute("note", note);
        return "lecture11/edit";
    }

    /**
     * 編集(POST)
     */
    @PostMapping("/edit")
    public String editPost(@RequestParam("id") int id, @RequestParam("title") String title, @RequestParam("body") String body) {
        Date date = new Date();
        jdbcTemplate.update("UPDATE note set title = ?, body = ?, date = ? WHERE id = ?", title, body, date, id);
        return "redirect:/lecture11/"; // トップページにリダイレクト
    }

    /**
     * 削除(GET)
     */
    @GetMapping("/delete")
    public String deleteGet(@RequestParam("id") int id) {
        jdbcTemplate.update("DELETE FROM note WHERE id = ?", id);
        return "redirect:/lecture11/";
    }

    /**
     * 表示(GET)
     */
    @GetMapping("/view/{id}")
    public String viewGet(@PathVariable("id") int id, ModelMap modelMap) {
        List<Map<String, Object>> dataList = jdbcTemplate.queryForList("SELECT * FROM note WHERE id = ?", id); // IDが一致するものを取得
        Map<String, Object> firstData = dataList.get(0);
        Note note = new Note();
        note.id = (int)firstData.get("id");
        note.title = (String)firstData.get("title");
        note.body = (String)firstData.get("body");
        note.date = (Date)firstData.get("date");
        // メソッドの最初からここまではeditGetと共通化（メソッド）できる
        modelMap.addAttribute("note", note);
        return "lecture11/view";
    }

}
