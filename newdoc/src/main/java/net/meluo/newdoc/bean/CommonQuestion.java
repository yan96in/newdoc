package net.meluo.newdoc.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class CommonQuestion implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6529280428229051950L;

    public static final String NAME = "name";
    public static final String HEAD_URL = "picture_url";
    public static final String CONTENT = "doctor_comment";
    public static final String TIME = "actual_date";
    public static final String START = "doctor_rating";

    private String name;
    private String head;
    private int start;
    private String content;
    private String time;

    public CommonQuestion() {

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

            if (o.has(NAME)) {
                this.name = o.getString(NAME);
            }
            if (o.has(HEAD_URL)) {
                this.head = o.getString(HEAD_URL);
            }
            if (o.has(CONTENT)) {
                this.content = o.getString(CONTENT);
            }

            if (o.has(START)) {
                this.start = o.getInt(START);
            }

            if (o.has(TIME)) {
                this.time = o.getString(TIME);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
