package net.teachingprogramming.webapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 第11回: ノート（メモ）を表すクラス
 */
public class Note {
    /** ID */
    public int id;
    /** ノートの名前 */
    public String title;
    /** ノートの本文 */
    public String body;
    /** 編集日時 */
    public Date date;

    /**
     * フィールドbodyをStringのListとして返す。
     *
     * 処理は次の1行で置き換えることができる。教科書P.204
     * return Arrays.asList(body.split("\n"));
     */
    public List<String> getBodyAsList() {
        List<String> lineList = new ArrayList<>();
        String[] stringArray = body.split("\n");
        for (String line : stringArray) {
            lineList.add(line);
        }
        return lineList;
    }

}
