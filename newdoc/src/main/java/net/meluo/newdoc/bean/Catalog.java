package net.meluo.newdoc.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Catalog implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6529280428229051950L;
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String HEAD_URL = "head";
    public static final String DEPARTMENT = "department";
    public static final String SPECIALTY = "specialty";
    public static final String TIME = "time";

    private int id;
    private String name;
    private String head;
    private String department;
    private String specialty;
    private String time;

    public Catalog() {

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
                this.id = o.getInt(ID);
            }
            if (o.has(NAME)) {
                this.name = o.getString(NAME);
            }
            if (o.has(HEAD_URL)) {
                this.head = o.getString(HEAD_URL);
            }
            if (o.has(DEPARTMENT)) {
                this.head = o.getString(DEPARTMENT);
            }
            if (o.has(SPECIALTY)) {
                this.head = o.getString(SPECIALTY);
            }
            if (o.has(TIME)) {
                this.head = o.getString(TIME);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

}
