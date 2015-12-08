package net.meluo.newdoc.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class LowBChatItem implements Serializable {

	/**
     *
     */
	private static final long serialVersionUID = 7967835246275954462L;

	public Long id;
	public String fid;
	public String name;
	public String from_head_url;
	public String time;
	public String content;
	public Boolean from_me = false;
	public String img = null;
	public String voice = null;
	public int voice_size = 0;

	public LowBChatItem() {
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

	public JSONObject toJSON() {

		JSONObject o = new JSONObject();
		try {

			if (this.fid != null) {
				o.put("poster", this.fid + "");
			}
			if (this.name != null) {
				o.put("poster_name", this.name);
			}
			if (this.time != null) {
				o.put("post_time", this.time);
			}
			if (this.content != null) {
				o.put("content", this.content);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return o;
	}

	public void fromJSONObject(JSONObject o) {
		try {
			if (o.has("sessionid")) {
				this.id = o.getLong("sessionid");
			}
			if (o.has("poster")) {
				this.fid = o.getString("poster");
			}
			if (o.has("post_name")) {
				this.name = o.getString("post_name");
			}
			if (o.has("post_img")) {
				this.from_head_url = o.getString("post_img");
			}
			if (o.has("post_time")) {
				this.time = o.getString("post_time");
			}
			if (o.has("content")) {
				this.content = o.getString("content");
			}
			if (o.has("img")) {
				this.img = o.getString("img");
			}
			if (o.has("v")) {
				this.voice = o.getString("v");
			}
			if (o.has("vs")) {
				this.voice_size = o.getInt("vs");
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}