package net.meluo.newdoc.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class PositionNode implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -6371314018929026559L;
    /**
     *
     */

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String SUB = "sub";

    private long id;
    private String name;
    private String sub;

    public PositionNode() {

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
                this.id = o.getLong(ID);
            }
            if (o.has(NAME)) {
                this.name = o.getString(NAME);
            }
            if (o.has(SUB)) {
                this.sub = o.getString(SUB);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

}
