package net.meluo.newdoc.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class FAQ implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6529280428229051950L;

    public static final String ID = "id";
    public static final String QUESTION = "question";
    public static final String ANSWER_URL = "answer_url";
    public static final String ANSWER = "answer";

    private String id;
    private String question;
    private String answer_url;
    private String answer;

    public FAQ() {

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

            if (o.has(ID)) {
                this.id = o.getString(ID);
            }
            if (o.has(QUESTION)) {
                this.question = o.getString(QUESTION);
            }
            if (o.has(ANSWER)) {
                this.answer = o.getString(ANSWER);
            }
            if (o.has(ANSWER_URL)) {
                this.answer_url = o.getString(ANSWER_URL);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer_url() {
        return answer_url;
    }

    public void setAnswer_url(String answer_url) {
        this.answer_url = answer_url;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
