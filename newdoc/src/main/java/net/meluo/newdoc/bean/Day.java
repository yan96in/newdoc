package net.meluo.newdoc.bean;

import net.meluo.newdoc.bean.RoomOfDoc.Slote;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Day implements Serializable {

    private static final long serialVersionUID = -6371314018929026559L;
    public static final int STATUS_TITLE = -1; // 日期不在范围内
    public static final int STATUS_OUT = 0; // 日期不在范围内
    public static final int STATUS_IN = 1; // 日期在范围内但不可选
    public static final int STATUS_ON = 2; // 日期可选

    private final String ID = "id";
    private final String NAME = "name";
    private final String STATUS = "status";

    private long id;
    private String name;
    private int status;
    private String date;
    private ArrayList<Slote> slots = new ArrayList<Slote>();

    public Day() {

    }

    public Day(long id, String name, int status) {
        this.id = id;
        this.name = name;
        this.status = status;
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
            if (o.has(STATUS)) {
                this.setStatus(o.getInt(STATUS));
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

	public ArrayList<Slote> getSlots() {
		return slots;
	}

	public void setSlots(ArrayList<Slote> slots) {
		this.slots = slots;
	}

}
