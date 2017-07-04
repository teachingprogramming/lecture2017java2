package net.teachingprogramming.webapp;

import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * 第12回のコントローラ
 * オンラインフォトアルバム
 */
@Controller
@RequestMapping("/lecture12")
public class Lecture12Controller {

    private final JdbcTemplate jdbcTemplate;

    private String staticRootPath;

    /**
     * コンストラクタ
     */
    @Autowired
    public Lecture12Controller(JdbcTemplate jdbcTemplate, ResourceHttpRequestHandler resourceHttpRequestHandler) {
        this.jdbcTemplate = jdbcTemplate;

        // noteテーブルが存在していない場合に作成する
        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS photo (\n" +
                        "  id INT AUTO_INCREMENT PRIMARY KEY,\n" +
                        "  title TEXT,\n" +
                        "  filename TEXT,\n" +
                        "  date TIMESTAMP\n" +
                        ")"
        );

        // staticのpathを取得し、フィールドに格納する
        for (Resource resource : resourceHttpRequestHandler.getLocations()) {
            if (resource.getDescription().equals("ServletContext resource [/]")) {
                try {
                    this.staticRootPath = resource.getFile().getAbsolutePath();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

    }

    /**
     * トップページ
     */
    @GetMapping("/")
    public String index(ModelMap modelMap) {
        List<Photo> photoList = new ArrayList<>(); // 表示する写真のリスト
        List<Map<String, Object>> dataList = jdbcTemplate.queryForList("SELECT * FROM photo ORDER BY date DESC"); // 新着順でデータベースから取り出す。
        for (Map<String, Object> data : dataList) {
            Photo photo = new Photo();
            photo.id = (int)data.get("id");
            photo.title = (String)data.get("title");
            photo.filename = (String)data.get("filename");
            photo.date = (Date)data.get("date");
            photoList.add(photo);
        }
        modelMap.addAttribute("photoList", photoList);
        return "lecture12/index";
    }

    /**
     * 新規作成(GET)
     */
    @GetMapping("/add")
    public String addGet() {
        return "lecture12/add";
    }

    /**
     * 新規作成(POST)
     */
    @PostMapping("/add")
    public String addPost(@RequestParam("title") String title,
                          @RequestParam("file") MultipartFile file,
                          @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        // アップロードされたファイルをstatic/lecture12/original/に保存
        File originalFile = new File(staticRootPath +"/lecture12/original/"+file.getOriginalFilename());
        try {
            file.transferTo(originalFile);
        } catch (IOException e) {
            e.printStackTrace();
            return "lecture12/error";
        }
        // サムネイルを作成して、static/lecture12/original/thumbnailに保存
        try {
            BufferedImage originalImage = ImageIO.read(originalFile);
            BufferedImage thumbnailImage = Scalr.resize(originalImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, 100);
            File thumbnailFile = new File(staticRootPath +"/lecture12/thumbnail/"+file.getOriginalFilename());
            ImageWriter imageWriter = ImageIO.getImageWritersBySuffix(FilenameUtils.getExtension(file.getOriginalFilename().toLowerCase())).next();
            ImageOutputStream ios = ImageIO.createImageOutputStream(new FileOutputStream(thumbnailFile));
            imageWriter.setOutput(ios);
            imageWriter.write(thumbnailImage);
            ios.flush();
            ios.close();
            imageWriter.dispose();
        } catch (IOException e) {
            e.printStackTrace();
            return "lecture12/error";
        }
        // データベースに保存
        jdbcTemplate.update("INSERT INTO photo ( title, filename , date ) VALUES (?, ?, ?)", title, file.getOriginalFilename(), date);
        return "redirect:/lecture12/"; // トップページにリダイレクト
    }

    /**
     * 削除(GET)
     */
    @GetMapping("/delete")
    public String deleteGet(@RequestParam("id") int id) {
        // ファイルを削除
        Photo photo = findPhotoById(id);
        File originalFile = new File(staticRootPath +"/lecture12/original/"+photo.filename);
        originalFile.delete();
        File thumbnailFile = new File(staticRootPath +"/lecture12/thumbnail/"+photo.filename);
        thumbnailFile.delete();
        // データベースから削除
        jdbcTemplate.update("DELETE FROM photo WHERE id = ?", id);
        return "redirect:/lecture12/";
    }

    /**
     * 表示(GET)
     */
    @GetMapping("/view/{id}")
    public String viewGet(@PathVariable("id") int id, ModelMap modelMap) {
        Photo photo = findPhotoById(id);
        modelMap.addAttribute("photo", photo);
        return "lecture12/view";
    }

    private Photo findPhotoById(int id) {
        List<Map<String, Object>> dataList = jdbcTemplate.queryForList("SELECT * FROM photo WHERE id = ?", id); // IDが一致するものを取得
        Map<String, Object> firstData = dataList.get(0);
        Photo photo = new Photo();
        photo.id = (int)firstData.get("id");
        photo.title = (String)firstData.get("title");
        photo.filename = (String)firstData.get("filename");
        photo.date = (Date)firstData.get("date");
        return photo;
    }

}
