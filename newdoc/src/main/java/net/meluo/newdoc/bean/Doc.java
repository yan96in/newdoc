package net.meluo.newdoc.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Doc implements Serializable {
	/**
     *
     */
	private static final long serialVersionUID = 6529280428229051950L;
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String HEAD_URL = "picture_url";
	public static final String DEPARTMENT = "catalog";
	public static final String SPECIALTY = "goodat";
	public static final String TITLE = "title";
	public static final String EDUCATION = "education";
	public static final String RESEARCH = "research";
	public static final String EXPERIENCE = "work_experience";
	public static final String TIME = "time";
	public static final String IS_FOCUS = "isFocus";

	private long id;
	private String name = "";
	private String title = "";
	private String education = "";
	private String research = "";
	private String experience = "";
	private String head = "";
	private ArrayList<Catalog> department = new ArrayList<Catalog>();
	private String specialty = "";
	private String time = "";
	private boolean isFocus = false;

	public Doc() {

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
			if (o.has(HEAD_URL)) {
				this.head = o.getString(HEAD_URL);
			}
			if (o.has(TITLE)) {
				this.title = o.getString(TITLE);
			}
			if (o.has(EDUCATION)) {
				this.education = o.getString(EDUCATION);
			}
			if (o.has(RESEARCH)) {
				this.setResearch(o.getString(RESEARCH));
			}
			if (o.has(EXPERIENCE)) {
				this.experience = o.getString(EXPERIENCE);
			}
			if (o.has(DEPARTMENT)) {
				JSONArray a = o.getJSONArray(DEPARTMENT);
				int num = a.length();
				for (int i = 0; i < num; i++) {
					Catalog c = new Catalog();
					c.fromJSONObject(a.getJSONObject(i));
					department.add(c);
				}
			}
			if (o.has(SPECIALTY)) {
				this.specialty = o.getString(SPECIALTY);
			}
			if (o.has(TIME)) {
				this.time = o.getString(TIME);
			}
			if (o.has(IS_FOCUS)) {
				this.setFocus(true);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String getDepartment() {

		String catalog = "";

		int num = department.size();
		if (num > 0) {
			for (int i = 0; i < num; i++) {
				Catalog c = department.get(i);
				catalog += c.getName() + ",";
			}
			catalog = catalog.substring(0, catalog.length() - 1);
		}

		return catalog;
	}

	public void setDepartment(ArrayList<Catalog> department) {
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

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getResearch() {
		return research;
	}

	public void setResearch(String research) {
		this.research = research;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public boolean isFocus() {
		return isFocus;
	}

	public void setFocus(boolean isFocus) {
		this.isFocus = isFocus;
	}

}
