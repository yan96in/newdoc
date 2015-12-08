package net.meluo.newdoc.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Room implements Serializable {
	/**
     *
     */
	private static final long serialVersionUID = 6529280428229051950L;
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String LONGITUDE = "longitude";
	public static final String LATITUDE = "latitude";
	public static final String ADDRESS = "address";
	public static final String DETAIL = "detail";
	public static final String PHONE = "phone";
	public static final String CITY_CODE = "city_code";
	public static final String AREA_CODE = "area_code";
	public static final String RATING = "rating";
	public static final String ISBOUND = "isBound";

	private long id;
	private String name = "";
	private Double longitude = 0.0;
	private Double latitude = 0.0;
	private String address = "";
	private String detail = "";
	private String phone = "";
	private int city_code;
	private int area_code;
	private long rating;
	private Boolean isBound = false;

	public Room() {

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
			if (o.has(LONGITUDE)) {
				this.longitude = o.getDouble(LONGITUDE);
			}
			if (o.has(LATITUDE)) {
				this.latitude = o.getDouble(LATITUDE);
			}
			if (o.has(ADDRESS)) {
				this.address = o.getString(ADDRESS);
			}
			if (o.has(DETAIL)) {
				this.detail = o.getString(DETAIL);
			}

			if (o.has(PHONE)) {
				this.phone = o.getString(PHONE);
			}
			if (o.has(CITY_CODE)) {
				this.city_code = o.getInt(CITY_CODE);
			}
			if (o.has(AREA_CODE)) {
				this.area_code = o.getInt(AREA_CODE);
			}
			if (o.has(RATING)) {
				this.rating = o.getLong(RATING);
			}
			if (o.has(ISBOUND)) {
				this.isBound = o.getBoolean(ISBOUND);
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

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getCity_code() {
		return city_code;
	}

	public void setCity_code(int city_code) {
		this.city_code = city_code;
	}

	public int getArea_code() {
		return area_code;
	}

	public void setArea_code(int area_code) {
		this.area_code = area_code;
	}

	public long getRating() {
		return rating;
	}

	public void setRating(long rating) {
		this.rating = rating;
	}

	public Boolean getIsBound() {
		return isBound;
	}

	public void setIsBound(Boolean isBound) {
		this.isBound = isBound;
	}

}
