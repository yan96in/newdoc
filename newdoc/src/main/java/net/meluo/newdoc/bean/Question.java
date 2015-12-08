package net.meluo.newdoc.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6529280428229051950L;

    public static final String ID = "id";
    public static final String CLIENT_ID = "clientid";
    public static final String DOC_ID = "doctorid";
    public static final String CATALOG_ID = "catalogid";
    public static final String SEX = "sex";
    public static final String AGE = "age";
    public static final String STATUS = "status";
    public static final String START = "start_date";
    public static final String CLOSE = "close_date";
    public static final String CONTENT = "content";

    private long id;
    private String clientid;
    private String doctorid;
    private int catalogid;
    private int sex;
    private int age;
    private int status;
    private String start_date;
    private String close_date;
    private String content;

    private ArrayList<String> imgs = new ArrayList<String>();

    public ArrayList<String> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<String> imgs) {
        this.imgs = imgs;
    }

    public Question() {

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

    public JSONObject toJson() {
        JSONObject o = new JSONObject();
        try {
            o.put("sex", this.sex + "");
            o.put("age", this.age + "");
            o.put("catalogid", this.catalogid + "");
            o.put("content", this.content + "");

            int num = this.imgs.size();
            if (num >= 0) {
                JSONArray a = new JSONArray();
                for (int i = 0; i < num; i++) {
                    a.put(imgs.get(i));
                }
                o.put("imgs", a);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return o;
    }

    public void fromJSONObject(JSONObject o) {
        try {

            if (o.has(ID)) {
                this.id = o.getLong("id");
            }
            if (o.has(CLIENT_ID)) {
                this.clientid = o.getString(CLIENT_ID);
            }
            if (o.has(DOC_ID)) {
                this.doctorid = o.getString(DOC_ID);
            }
            if (o.has(CATALOG_ID)) {
                this.catalogid = o.getInt(CATALOG_ID);
            }
            if (o.has(SEX)) {
                this.sex = o.getInt(SEX);
            }
            if (o.has(AGE)) {
                this.age = o.getInt(AGE);
            }
            if (o.has(STATUS)) {
                this.status = o.getInt(STATUS);
            }
            if (o.has(START)) {
                this.start_date = o.getString(START);
            }
            if (o.has(CLOSE)) {
                this.close_date = o.getString(CLOSE);
            }
            if (o.has(CONTENT)) {
                this.content = o.getString(CONTENT);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getDoctorid() {
        return doctorid;
    }

    public void setDoctorid(String doctorid) {
        this.doctorid = doctorid;
    }

    public int getCatalogid() {
        return catalogid;
    }

    public void setCatalogid(int catalogid) {
        this.catalogid = catalogid;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getClose_date() {
        return close_date;
    }

    public void setClose_date(String close_date) {
        this.close_date = close_date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
