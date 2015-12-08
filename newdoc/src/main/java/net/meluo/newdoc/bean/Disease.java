package net.meluo.newdoc.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Disease implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String detail;
	private String treat;
	private String other;
	private String type;

	public Disease() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getTreat() {
		return treat;
	}

	public void setTreat(String treat) {
		this.treat = treat;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	}

}
