package net.meluo.newdoc.bean;

import com.bigd.utl.Checking;

import org.json.JSONException;
import org.json.JSONObject;

public class UserBean {
    public static final int D_S_NOT_CHECKED = 0;
    public static final int D_S_CHECKING = 1;
    public static final int D_S_CHECKED = 2;

    private int doctor_status = 0;
    private String name = "";
    private String real_name = "";
    private int sex = 0;
    private String picture_url = "";
    private String weinxin = "";
    private String mobile = "";
    private boolean is_real = false;
    private String citizenid = "";
    private String insuranceid = "";

    public JSONObject toJSONObject() {
        JSONObject o = new JSONObject();

        try {
            if (!Checking.isNullorBlank(this.name)) {
                o.put("name", this.name);
            }

            // o.put("sex", this.sex + "");

            if (!Checking.isNullorBlank(this.picture_url)) {
                o.put("picture_url", this.picture_url);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return o;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getCitizenid() {
        return citizenid;
    }

    public void setCitizenid(String citizenid) {
        this.citizenid = citizenid;
    }

    public String getInsuranceid() {
        return insuranceid;
    }

    public void setInsuranceid(String insuranceid) {
        this.insuranceid = insuranceid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public String getWeinxin() {
        return weinxin;
    }

    public void setWeinxin(String weinxin) {
        this.weinxin = weinxin;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean isIs_real() {
        return is_real;
    }

    public void setIs_real(boolean is_real) {
        this.is_real = is_real;
    }
    public int getDoctor_status() {
        return doctor_status;
    }

    public void setDoctor_status(int doctor_status) {
        this.doctor_status = doctor_status;
    }

}
