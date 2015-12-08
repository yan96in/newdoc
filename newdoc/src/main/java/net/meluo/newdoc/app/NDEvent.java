package net.meluo.newdoc.app;

public class NDEvent {

	public static final int E_ND_LOGIN = 0;
	public static final int E_WX_LOGIN = 1;
	public static final int E_LOGOUT = 2;
	public static final int WX_LOADING = 3;
	public static final int E_INFO_REFRESH = 4;
	public static final int E_SEX_REFRESH = 5;
	public static final int E_NAME_REFRESH = 6;
	public static final int E_REAL_NAME_CHECK = 7;
	public static final int E_HEAD_REFRESH = 8;
	public static final int E_DOC_FOCUS_TRUE = 9;
	public static final int E_DOC_FOCUS_FALSE = 10;
	public static final int E_DAY_CHANGE = 11;
	public static final int E_Body_POINT_CHANGE = 12;
	public static final int E_GET_INFO = 13;
	public static final int E_CLEAR_INFO = 14;
	public static final int E_REFRESH_ROOM_BOUND = 15;

	private int mEvent;
	private Object mData;

	public NDEvent(int event) {
		// TODO Auto-generated constructor stub
		mEvent = event;
	}

	public NDEvent(int event, Object o) {
		// TODO Auto-generated constructor stub
		mEvent = event;
		mData = o;
	}

	public int getEvent() {
		return mEvent;
	}

	public Object getData() {
		return mData;
	}
}