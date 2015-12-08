package net.meluo.newdoc.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Appointment implements Serializable {
	/**
     *
     */
	private static final long serialVersionUID = 6529280428229051950L;
	public static final String ID = "id";
	public static final String TIMESLOTID = "timeslotid";
	public static final String DATE = "actual_date";
	public static final String STATUS = "status";
	public static final String COMMENT = "doctor_comment";
	public static final String RATING = "doctor_rating";
	public static final String DOC_NAME = "doctor_name";
	public static final String CATALOG_NAME = "catalog_name";
	public static final String ROOM_NAME = "room_name";
	public static final String START_TIME = "start_time";
	public static final String END_TIME = "end_time";

	private long id;
	private String timeslotid;
	private String date;
	private String status;
	private String comment;
	private String rating;
	private String doc_name;
	private String catalog_name;
	private String room_name;
	private String start_time;
	private String end_time;

	public Appointment() {

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
			if (o.has(TIMESLOTID)) {
				this.timeslotid = o.getString(TIMESLOTID);
			}
			if (o.has(DATE)) {
				this.date = o.getString(DATE);
			}
			if (o.has(STATUS)) {
				this.status = o.getString(STATUS);
			}
			if (o.has(COMMENT)) {
				this.comment = o.getString(COMMENT);
			}
			if (o.has(RATING)) {
				this.rating = (o.getString(RATING));
			}
			if (o.has(DOC_NAME)) {
				this.doc_name = o.getString(DOC_NAME);
			}

			if (o.has(CATALOG_NAME)) {
				this.catalog_name = o.getString(CATALOG_NAME);
			}
			if (o.has(ROOM_NAME)) {
				this.room_name = o.getString(ROOM_NAME);
			}
			if (o.has(START_TIME)) {
				this.start_time = o.getString(START_TIME);
			}
			if (o.has(END_TIME)) {
				this.end_time = o.getString(END_TIME);
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

	public String getTimeslotid() {
		return timeslotid;
	}

	public void setTimeslotid(String timeslotid) {
		this.timeslotid = timeslotid;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getDoc_name() {
		return doc_name;
	}

	public void setDoc_name(String doc_name) {
		this.doc_name = doc_name;
	}

	public String getCatalog_name() {
		return catalog_name;
	}

	public void setCatalog_name(String catalog_name) {
		this.catalog_name = catalog_name;
	}

	public String getRoom_name() {
		return room_name;
	}

	public void setRoom_name(String room_name) {
		this.room_name = room_name;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

}
