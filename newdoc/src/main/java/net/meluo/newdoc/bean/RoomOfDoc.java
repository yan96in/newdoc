package net.meluo.newdoc.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class RoomOfDoc implements Serializable {

    private static final long serialVersionUID = -6371314018929026559L;
    public static final int STATUS_TITLE = -1; // 日期不在范围内
    public static final int STATUS_OUT = 0; // 日期不在范围内
    public static final int STATUS_IN = 1; // 日期在范围内但不可选
    public static final int STATUS_ON = 2; // 日期可选

    private final String ID = "id";
    private final String NAME = "room_name";
    private final String STATUS = "status";
    private final String ADDRESS = "room_address";
    private final String SLOTS = "slots";

    private long id;
    private String name;
    private String address;
    private ArrayList<Slote> slots = new ArrayList<Slote>();
    private int status;

    public RoomOfDoc() {

    }

    public RoomOfDoc(long id, String name, int status) {
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
            if (o.has(ADDRESS)) {
                this.address = o.getString(ADDRESS);
            }
            if (o.has(STATUS)) {
                this.setStatus(o.getInt(STATUS));
            }

            if (o.has(SLOTS)) {
                JSONArray a = o.getJSONArray(SLOTS);
                int num = a.length();
                for (int i = 0; i < num; i++) {
                    Slote s = new Slote();
                    s.fromJSONObject(a.getJSONObject(i));
                    slots.add(s);
                }
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<Slote> getSlots() {
        return slots;
    }

    public void setSlots(ArrayList<Slote> slots) {
        this.slots = slots;
    }

    public static class Slote {

        private final String DATE = "actual_date";
        private final String ID = "id";
        private final String TIME_SCOPE = "timescope";
        private final String TS = "ts";

        public Slote() {

        }

        private String date;
        private long id;
        private String timescope;
        private String ts;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getTimescope() {
            return timescope;
        }

        public void setTimescope(String timescope) {
            this.timescope = timescope;
        }

        public String getTs() {
            return ts;
        }

        public void setTs(String ts) {
            this.ts = ts;
        }

        public void fromJSONObject(JSONObject o) {
            try {
                if (o.has(ID)) {
                    this.id = o.getLong(ID);
                }
                if (o.has(DATE)) {
                    this.date = o.getString(DATE);
                }
                if (o.has(TIME_SCOPE)) {
                    this.timescope = o.getString(TIME_SCOPE);
                }
                if (o.has(TS)) {
                    this.ts = (o.getString(TS));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
