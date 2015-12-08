package net.meluo.newdoc.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class History implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6529280428229051950L;

    public static final String TITLE = "title";
    public static final String DATA = "actual_date";
    public static final String CONTENT = "detail";

    private String title;
    private String date;
    private String content;

    public History() {

    }

    public void fromJSONString(String json) {

        JSONObject o;
        try {
            o = new JSONObject(json);
            fromJSONObject(o);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void fromJSONObject(JSONObject o) {
        try {

            if (o.has(TITLE)) {
                this.title = o.getString(TITLE);
            }
            if (o.has(DATA)) {
                this.date = o.getString(DATA);
            }
            if (o.has(CONTENT)) {
                this.content = o.getString(CONTENT);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
