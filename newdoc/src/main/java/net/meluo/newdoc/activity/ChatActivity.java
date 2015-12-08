package net.meluo.newdoc.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bigd.utl.Checking;
import com.bigd.utl.SoundMeter;
import com.dadao.view.TitleBar;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders.Any.M;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.way.util.L;
import com.way.util.PreferenceConstants;
import com.way.util.PreferenceUtils;
import com.way.util.T;
import com.way.util.TimeUtil;

import net.meluo.newdoc.R;
import net.meluo.newdoc.adapter.FaceAdapter;
import net.meluo.newdoc.adapter.FacePageAdeapter;
import net.meluo.newdoc.adapter.LowBChatAdapter;
import net.meluo.newdoc.app.NDApp;
import net.meluo.newdoc.bean.LowBChatItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChatActivity extends Activity implements OnTouchListener,
		OnGestureListener, OnClickListener {
	public static final String INTENT_EXTRA_USERNAME = ChatActivity.class
			.getName() + ".username";// 昵称对应的key
	private ViewPager mFaceViewPager;// 表情选择ViewPager
	private int mCurrentPage = 0;// 当前表情页
	private boolean mIsFaceShow = false;// 是否显示表情
	private ImageButton btnSound;// 发送语音
	private ImageButton btnKeyBoard;// 发送语音
	private Button btnAddSound;// 发送语音
	private Button mSendMsgBtn;// 发送消息button
	private ImageButton mFaceSwitchBtn;// 切换键盘和表情的button
	private ImageButton btnAdd;// 发送附件

	private RelativeLayout rlInputBar, rlInputArea;

	TitleBar mTitleBar;
	private EditText mChatEditText;// 消息输入框
	private LinearLayout mFaceRoot;// 表情父容器

	private GridView mAddRoot;// 表情父容器
	private WindowManager.LayoutParams mWindowNanagerParams;
	private InputMethodManager mInputMethodManager;
	private List<String> mFaceMapKeys;// 表情对应的字符串数组
	private String mWithJabberID = null;// 当前聊天用户的ID
	private String mQuestionID = null;// 当前问题的ID

	View activityRootView = null;

	private ArrayList<LowBChatItem> list = new ArrayList<LowBChatItem>();

	private static final int FLING_MIN_DISTANCE = 50;
	private static final int FLING_MIN_VELOCITY = 0;
	GestureDetector mGestureDetector;
	LowBChatAdapter mAdapter;
	private ContentResolver mContentResolver;

	private static final int PICK_PHOTO = 101;

	private Handler mHandler = new Handler();
	private SoundMeter mSensor;
	private String voiceName;

	private long voiceStartTime = 0l;
	private long voiceEndTime = 0l;

	ImageView mImgVoice;
	int from;

	private PullToRefreshListView mPullListView;
	private ListView mListView;

	public static void actionStart(Context fromContext, String qid, int from) {
		L.d(from + "____");
		Intent chatIntent = new Intent(fromContext, ChatActivity.class);
		chatIntent.putExtra("qid", qid);
		chatIntent.putExtra("from", from);
		fromContext.startActivity(chatIntent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cv_chat);
		// WindowUtl.setTranslucentStatus(this);
		mContentResolver = this.getContentResolver();
		initData();
		initView();
		setLowBChatAdapter();
		initFacePage();
		refresh();
	}

	private void setLowBChatAdapter() {
		mAdapter = new LowBChatAdapter(ChatActivity.this, list);
		mListView.setAdapter(mAdapter);
		// mListView.setSelection(adapter.getCount() - 1);
	}

	private void initData() {
		Intent chatIntent = this.getIntent();
		mQuestionID = chatIntent.getStringExtra("qid");
		from = chatIntent.getIntExtra("from", 0);
		Set<String> keySet = NDApp.getInstance().getFaceMap().keySet();
		mFaceMapKeys = new ArrayList<String>();
		mFaceMapKeys.addAll(keySet);
	}

	@SuppressLint("NewApi")
	private void initView() {

		activityRootView = findViewById(R.id.c_root);

		// 给该layout设置监听，监听其布局发生变化事件
		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {

						// 比较Activity根布局与当前布局的大小
						int heightDiff = activityRootView.getRootView()
								.getHeight() - activityRootView.getHeight();
						if (heightDiff > 100) {
							// 大小超过100时，一般为显示虚拟键盘事件
							mListView.setSelection(mAdapter.getCount() - 1);
							if (mAddRoot.isShown()) {
								mAddRoot.setVisibility(View.GONE);
							}

						} else {
							// 大小小于100时，为不显示虚拟键盘或虚拟键盘隐藏
						}
					}
				});

		mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		mWindowNanagerParams = getWindow().getAttributes();

		mTitleBar = (TitleBar) this.findViewById(R.id.title_bar);
		mTitleBar.setTitleText("在线咨询");

		mPullListView = (PullToRefreshListView) this
				.findViewById(R.id.msg_listView);
		mPullListView.setPullLoadEnabled(false);
		mPullListView.setPullRefreshEnabled(true);
		mPullListView.setScrollLoadEnabled(false);
		mPullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				refresh();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {

			}
		});

		mListView = mPullListView.getRefreshableView();
		mListView.setDivider(null);

		btnAdd = (ImageButton) findViewById(R.id.btn_add);
		btnSound = (ImageButton) findViewById(R.id.btn_sound);
		btnKeyBoard = (ImageButton) findViewById(R.id.btn_kb);

		btnAddSound = (Button) findViewById(R.id.btn_add_sound);
		rlInputBar = (RelativeLayout) this.findViewById(R.id.inputBar);
		rlInputArea = (RelativeLayout) this
				.findViewById(R.id.aio_input_send_container);
		if (from == 1) {
			rlInputBar.setVisibility(View.GONE);
		}

		mAddRoot = (GridView) findViewById(R.id.main_add);
		SimpleAdapter adapter = new SimpleAdapter(this, getData(),
				R.layout.itm_add_btn, new String[] { "name", "img" },
				new int[] { R.id.txt_itm_add, R.id.img_itm_add });

		mAddRoot.setAdapter(adapter);
		mAddRoot.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				// TODO Auto-generated method stub
				L.d("test", id + "__" + position);
				switch (position) {
				case 0:
					// 添加图片
					Intent i = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 调用android的图库
					startActivityForResult(i, PICK_PHOTO);

					if (mAddRoot.isShown()) {
						mAddRoot.setVisibility(View.GONE);
					}

					break;
				case 1:
					// 添加视频

					break;
				}
			}
		});

		mSendMsgBtn = (Button) findViewById(R.id.btn_send);
		mFaceSwitchBtn = (ImageButton) findViewById(R.id.face_switch_btn);
		mChatEditText = (EditText) findViewById(R.id.input);
		mFaceRoot = (LinearLayout) findViewById(R.id.face_ll);
		mFaceViewPager = (ViewPager) findViewById(R.id.face_pager);
		mChatEditText.setOnTouchListener(this);

		mChatEditText.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (mWindowNanagerParams.softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
							|| mIsFaceShow) {
						mFaceRoot.setVisibility(View.GONE);
						mIsFaceShow = false;
						// imm.showSoftInput(msgEt, 0);
						return true;
					}
				}
				return false;
			}
		});
		mChatEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				// if (s.length() > 0) {
				// mSendMsgBtn.setVisibility(View.VISIBLE);
				// btnAdd.setVisibility(View.GONE);
				// } else {
				// mSendMsgBtn.setVisibility(View.GONE);
				// btnAdd.setVisibility(View.VISIBLE);
				// }
			}
		});
		btnKeyBoard.setOnClickListener(this);
		mFaceSwitchBtn.setOnClickListener(this);
		mSendMsgBtn.setOnClickListener(this);
		btnAdd.setOnClickListener(this);
		btnSound.setOnClickListener(this);
		btnAddSound.setOnTouchListener(this);
		btnAddSound
				.setOnGenericMotionListener(new View.OnGenericMotionListener() {

					@Override
					public boolean onGenericMotion(View v, MotionEvent event) {
						// TODO Auto-generated method stub

						return false;
					}
				});

		mImgVoice = (ImageView) this.findViewById(R.id.cc_img_voice);
		mSensor = new SoundMeter();
	}

	private void loadHistery() {
		if (list.isEmpty()) {
			return;
		}
		Ion.with(this,
				PreferenceConstants.BASE_URL + "/base/getQuestionAndAnswer")
				.setMultipartParameter(
						"uid",
						PreferenceUtils.getPrefString(ChatActivity.this, "UID",
								"")).setMultipartParameter("qid", mQuestionID)

				.setMultipartParameter("minid", list.get(0).id + "")

				.asString().setCallback(new FutureCallback<String>() {

					@Override
					public void onCompleted(Exception e1, String data) {
						mPullListView.onPullDownRefreshComplete();
						if (e1 != null) {
							T.showShort(ChatActivity.this, "网络异常");
						}

						try {
							JSONArray array = new JSONArray(data);
							int num = array.length();
							for (int i = num; i > 0; i--) {
								JSONObject o = array.getJSONObject(i - 1);
								LowBChatItem ci = new LowBChatItem();
								ci.fromJSONObject(o);
								if (o.getString("uid").equals(
										PreferenceUtils.getPrefString(
												ChatActivity.this, "UID", ""))) {
									ci.from_me = true;
								}
								list.add(0, ci);
							}
							mAdapter.notifyDataSetChanged();

							// mListView.setSelectionFromTop(num, 100);

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	private void loadMore() {
		long maxid = 999999;

		if (list.isEmpty()) {
			maxid = -1;
		} else {
			maxid = list.get(list.size() - 1).id;

		}

		M m = Ion
				.with(this,
						PreferenceConstants.BASE_URL
								+ "/base/getQuestionAndAnswer")
				.setMultipartParameter(
						"uid",
						PreferenceUtils.getPrefString(ChatActivity.this, "UID",
								"")).setMultipartParameter("qid", mQuestionID);
		if (maxid != -1) {
			m.setMultipartParameter("maxid", maxid + "");
		}
		m.asString().setCallback(new FutureCallback<String>() {

			@Override
			public void onCompleted(Exception e1, String data) {
				mPullListView.onPullDownRefreshComplete();
				if (e1 != null) {
					T.showShort(ChatActivity.this, "网络异常");
				}

				try {
					JSONArray array = new JSONArray(data);
					int num = array.length();

					for (int i = 0; i < num; i++) {
						JSONObject o = array.getJSONObject(i);
						LowBChatItem ci = new LowBChatItem();
						ci.fromJSONObject(o);
						if (o.getString("uid").equals(
								PreferenceUtils.getPrefString(
										ChatActivity.this, "UID", ""))) {
							ci.from_me = true;
						}
						list.add(ci);
					}

					mAdapter.notifyDataSetChanged();
					mListView.setSelection(mAdapter.getCount() - 1);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void refresh() {
		list.clear();
		Ion.with(this,
				PreferenceConstants.BASE_URL + "/1/Consults/" + mQuestionID)
				.asString().setCallback(new FutureCallback<String>() {

					@Override
					public void onCompleted(Exception e1, String data) {
						mPullListView.onPullDownRefreshComplete();
						if (e1 != null) {
							T.showShort(ChatActivity.this, "网络异常");
						}

						try {
							JSONObject result = new JSONObject(data)
									.getJSONObject("data");
							JSONArray array = result.getJSONArray("talks");
							int num = array.length();
							if (list.isEmpty()) {
								for (int i = 0; i < num; i++) {
									JSONObject o = array.getJSONObject(i);
									LowBChatItem ci = new LowBChatItem();
									ci.fromJSONObject(o);
									if (o.getString("poster").equals(
											NDApp.getInstance().user
													.getMobile())) {
										ci.from_me = true;
									}
									list.add(ci);
								}
							} else {
								for (int i = num; i > 0; i--) {
									JSONObject o = array.getJSONObject(i - 1);
									LowBChatItem ci = new LowBChatItem();
									ci.fromJSONObject(o);
									if (o.get("poster").equals(
											NDApp.getInstance().user
													.getMobile())) {
										ci.from_me = true;
									}
									list.add(0, ci);
								}
							}

							mAdapter.notifyDataSetChanged();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	// 主题列表的数据初始化
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("name", "图片");
		map.put("img", R.drawable.ic_add_img);
		list.add(map);

		map = new HashMap<String, Object>();

		map.put("name", "视频");
		map.put("img", R.drawable.ic_add_video);
		list.add(map);

		return list;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			int heightDiff = activityRootView.getRootView().getHeight()
					- activityRootView.getHeight();
			if (heightDiff > 100) {
				// 大小超过100时，一般为显示虚拟键盘事件
				mInputMethodManager.hideSoftInputFromWindow(
						mChatEditText.getWindowToken(), 0);

				return false;
			} else {
				return super.onKeyDown(keyCode, event);
			}

		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.msg_listView:
			mInputMethodManager.hideSoftInputFromWindow(
					mChatEditText.getWindowToken(), 0);

			mFaceRoot.setVisibility(View.GONE);
			mIsFaceShow = false;
			break;
		case R.id.input:
			mInputMethodManager.showSoftInput(mChatEditText, 0);
			mListView.setSelection(mAdapter.getCount() - 1);

			mFaceRoot.setVisibility(View.GONE);
			mIsFaceShow = false;
			break;
		case R.id.btn_add_sound:
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				mImgVoice.setVisibility(View.VISIBLE);

				voiceStartTime = System.currentTimeMillis();
				voiceName = voiceStartTime + ".amr";
				start(voiceName);
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {

					}
				}, 300);

				break;
			}
			if (event.getAction() == MotionEvent.ACTION_MOVE) {

				if (event.getY() < -50) {
					cancell_up();
					// T.showShort(ChatActivity.this, event.getY() + "取消发送。");
					return false;
				}

			}

			if (event.getAction() == MotionEvent.ACTION_UP) {
				if (event.getY() < 0) {
					cancell();
					// T.showShort(ChatActivity.this, event.getY() + "取消发送。");
					return false;
				}

				stop();
				voiceEndTime = System.currentTimeMillis();
				String voicePath = Environment.getExternalStorageDirectory()
						.getAbsolutePath()
						+ File.separator
						+ "_Teacher"
						+ File.separator + "voice" + File.separator + voiceName;

				if (voiceEndTime - voiceStartTime < 1000) {
					// T.showShort(ChatActivity.this, "录音时间过短，取消发送。");
					// cancell();
					cancellShort();
					return false;
				}
				mImgVoice.setVisibility(View.GONE);

				this.sendVoiceMessage(voicePath);
				break;
			}
			break;

		default:
			break;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.face_switch_btn:
			if (!mIsFaceShow) {
				mInputMethodManager.hideSoftInputFromWindow(
						mChatEditText.getWindowToken(), 0);
				try {
					Thread.sleep(80);// 解决此时会黑一下屏幕的问题
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mFaceRoot.setVisibility(View.VISIBLE);
				mIsFaceShow = true;
				mAddRoot.setVisibility(View.GONE);
			} else {
				mFaceRoot.setVisibility(View.GONE);
				mInputMethodManager.showSoftInput(mChatEditText, 0);
				mListView.setSelection(mAdapter.getCount() - 1);

				mIsFaceShow = false;
				mAddRoot.setVisibility(View.GONE);
			}
			break;
		case R.id.btn_send:// 发送消息
			sendMessageIfNotNull();
			break;
		case R.id.btn_sound:

			btnSound.setVisibility(View.INVISIBLE);
			btnKeyBoard.setVisibility(View.VISIBLE);
			btnAddSound.setVisibility(View.VISIBLE);
			rlInputArea.setVisibility(View.INVISIBLE);
			mInputMethodManager.hideSoftInputFromWindow(
					mChatEditText.getWindowToken(), 0);

			break;
		case R.id.btn_kb:
			btnSound.setVisibility(View.VISIBLE);
			btnKeyBoard.setVisibility(View.INVISIBLE);
			btnAddSound.setVisibility(View.INVISIBLE);
			rlInputArea.setVisibility(View.VISIBLE);

			mInputMethodManager.showSoftInput(mChatEditText, 0);

			break;
		case R.id.btn_add:
			if (mAddRoot.isShown()) {
				mAddRoot.setVisibility(View.GONE);
			} else {
				mInputMethodManager.hideSoftInputFromWindow(
						mChatEditText.getWindowToken(), 0);

				mFaceRoot.setVisibility(View.GONE);
				mAddRoot.setVisibility(View.VISIBLE);
			}
			break;
		default:
			break;
		}
	}

	private void sendVoiceMessage(String path) {
		LowBChatItem lbci = new LowBChatItem();
		lbci.time = TimeUtil.getChatTime(System.currentTimeMillis());
		lbci.from_me = true;
		lbci.fid = PreferenceUtils.getPrefString(ChatActivity.this, "UID", "");
		lbci.from_head_url = "";
		lbci.voice = path;
		lbci.voice_size = (int) ((this.voiceEndTime - this.voiceStartTime) / 1000);
		this.list.add(lbci);

		this.mAdapter.notifyDataSetChanged();
		mListView.setSelection(mAdapter.getCount() - 1);

		Ion.with(this, PreferenceConstants.BASE_URL + "/Base/AnswerQuestion")
				.setMultipartParameter(
						"fromUid",
						PreferenceUtils.getPrefString(ChatActivity.this, "UID",
								"")).setMultipartParameter("qid", mQuestionID)
				.setMultipartFile("file", new File(path)).asString()
				.setCallback(new FutureCallback<String>() {

					@Override
					public void onCompleted(Exception arg0, String data) {
						if (Checking.isNullorBlank(data)) {
							T.showShort(ChatActivity.this, "网络错误，请稍后重试");
							return;
						}
						try {
							JSONObject o = new JSONObject(data);
							if (o.getInt("code") == 0) {
								if (mAddRoot.isShown()) {
									mAddRoot.setVisibility(View.GONE);
								}
								loadMore();
							} else {
								T.showShort(ChatActivity.this,
										o.getString("msg"));
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	private void sendImgMessage(String path) {
		LowBChatItem lbci = new LowBChatItem();
		lbci.time = TimeUtil.getChatTime(System.currentTimeMillis());
		lbci.from_me = true;
		lbci.fid = PreferenceUtils.getPrefString(ChatActivity.this, "UID", "");
		lbci.from_head_url = "";
		lbci.img = path;
		this.list.add(lbci);
		this.mAdapter.notifyDataSetChanged();
		mListView.setSelection(mAdapter.getCount() - 1);

		Ion.with(this, PreferenceConstants.BASE_URL + "/Base/AnswerQuestion")
				.setMultipartParameter(
						"fromUid",
						PreferenceUtils.getPrefString(ChatActivity.this, "UID",
								"")).setMultipartParameter("qid", mQuestionID)
				.setMultipartFile("file", new File(path)).asString()
				.setCallback(new FutureCallback<String>() {

					@Override
					public void onCompleted(Exception arg0, String data) {
						if (Checking.isNullorBlank(data)) {
							T.showShort(ChatActivity.this, "网络错误，请稍后重试");
							return;
						}
						try {
							JSONObject o = new JSONObject(data);
							if (o.getInt("code") == 0) {
								if (mAddRoot.isShown()) {
									mAddRoot.setVisibility(View.GONE);
								}
								loadMore();
							} else {
								T.showShort(ChatActivity.this,
										o.getString("msg"));
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	// 发送文字消息
	private void sendMessageIfNotNull() {

		final String value = mChatEditText.getText().toString();
		if (Checking.isNullorBlank(value)) {
			T.showShort(this, "消息内容不能为空。");
			return;
		}

		JSONObject o = new JSONObject();
		try {
			o.put("content", value);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JSONArray a = new JSONArray();
		a.put(o);

		Ion.with(this,
				PreferenceConstants.BASE_URL + "/1/Consults/" + mQuestionID)
				.setMultipartParameter("items", a.toString()).asString()
				.setCallback(new FutureCallback<String>() {

					@Override
					public void onCompleted(Exception arg0, String data) {
						if (Checking.isNullorBlank(data)) {
							T.showShort(ChatActivity.this, "网络错误，请稍后重试");
							return;
						}
						try {
							JSONObject o = new JSONObject(data);
							if (o.getInt("retcode") == 0) {
								mChatEditText.setText(null);
								// addChatMessageToDB(value);
								refresh();
							} else {
								T.showShort(ChatActivity.this,
										o.getString("msg"));
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	private void initFacePage() {
		// TODO Auto-generated method stub
		List<View> lv = new ArrayList<View>();
		for (int i = 0; i < NDApp.NUM_PAGE; ++i) {
			lv.add(getGridView(i));
		}
		FacePageAdeapter fadapter = new FacePageAdeapter(lv);
		if (fadapter != null) {
			mFaceViewPager.setAdapter(fadapter);
			mFaceViewPager.setCurrentItem(mCurrentPage);
			fadapter.notifyDataSetChanged();
			mFaceRoot.setVisibility(View.GONE);
		}

		mFaceViewPager.setCurrentItem(mCurrentPage);
		mFaceViewPager
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int arg0) {
						// TODO Auto-generated method stub
						mCurrentPage = arg0;
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
						// TODO Auto-generated method stub

					}
				});
	}

	private GridView getGridView(int i) {
		// TODO Auto-generated method stub
		GridView gv = new GridView(this);
		gv.setNumColumns(7);
		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));// 屏蔽GridView默认点击效果
		gv.setBackgroundColor(Color.TRANSPARENT);
		gv.setCacheColorHint(Color.TRANSPARENT);
		gv.setHorizontalSpacing(1);
		gv.setVerticalSpacing(1);
		gv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		gv.setGravity(Gravity.CENTER);
		gv.setAdapter(new FaceAdapter(this, i));
		gv.setOnTouchListener(forbidenScroll());
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (arg2 == NDApp.NUM) {// 删除键的位置
					int selection = mChatEditText.getSelectionStart();
					String text = mChatEditText.getText().toString();
					if (selection > 0) {
						String text2 = text.substring(selection - 1);
						if ("]".equals(text2)) {
							int start = text.lastIndexOf("[");
							int end = selection;
							mChatEditText.getText().delete(start, end);
							return;
						}
						mChatEditText.getText()
								.delete(selection - 1, selection);
					}
				} else {
					int count = mCurrentPage * NDApp.NUM + arg2;
					// 注释的部分，在EditText中显示字符串
					// String ori = msgEt.getText().toString();
					// int index = msgEt.getSelectionStart();
					// StringBuilder stringBuilder = new StringBuilder(ori);
					// stringBuilder.insert(index, keys.get(count));
					// msgEt.setText(stringBuilder.toString());
					// msgEt.setSelection(index + keys.get(count).length());

					// 下面这部分，在EditText中显示表情
					Bitmap bitmap = BitmapFactory.decodeResource(
							getResources(), (Integer) NDApp.getInstance()
									.getFaceMap().values().toArray()[count]);
					if (bitmap != null) {
						int rawHeigh = bitmap.getHeight();
						int rawWidth = bitmap.getHeight();
						int newHeight = 30;
						int newWidth = 30;
						// 计算缩放因子
						float heightScale = ((float) newHeight) / rawHeigh;
						float widthScale = ((float) newWidth) / rawWidth;
						// 新建立矩阵
						Matrix matrix = new Matrix();
						matrix.postScale(heightScale, widthScale);
						// 设置图片的旋转角度
						// matrix.postRotate(-30);
						// 设置图片的倾斜
						// matrix.postSkew(0.1f, 0.1f);
						// 将图片大小压缩
						// 压缩后图片的宽和高以及kB大小均会变化
						Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0,
								rawWidth, rawHeigh, matrix, true);
						ImageSpan imageSpan = new ImageSpan(ChatActivity.this,
								newBitmap);
						String emojiStr = mFaceMapKeys.get(count);
						SpannableString spannableString = new SpannableString(
								emojiStr);
						spannableString.setSpan(imageSpan,
								emojiStr.indexOf('['),
								emojiStr.indexOf(']') + 1,
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						mChatEditText.append(spannableString);
					} else {
						String ori = mChatEditText.getText().toString();
						int index = mChatEditText.getSelectionStart();
						StringBuilder stringBuilder = new StringBuilder(ori);
						stringBuilder.insert(index, mFaceMapKeys.get(count));
						mChatEditText.setText(stringBuilder.toString());
						mChatEditText.setSelection(index
								+ mFaceMapKeys.get(count).length());
					}
				}
			}
		});
		return gv;
	}

	private OnTouchListener forbidenScroll() {
		return new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					return true;
				}
				return false;
			}
		};
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			// Fling left
			Toast.makeText(this, "向左手势", Toast.LENGTH_SHORT).show();
		} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			// Fling right
			Toast.makeText(this, "向右手势", Toast.LENGTH_SHORT).show();
		}

		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {

		case PICK_PHOTO:
			if (data != null && data.getData() != null) {
				Uri selectedImage = data.getData();
				String[] filePathColumns = { MediaColumns.DATA };
				Cursor c = this.getContentResolver().query(selectedImage,
						filePathColumns, null, null, null);
				c.moveToFirst();
				int columnIndex = c.getColumnIndex(filePathColumns[0]);
				String picturePath = c.getString(columnIndex);
				c.close();
				L.d("图库", picturePath);

				// 发送图片类型信息

				sendImgMessage(picturePath);
			}
			break;
		default:

		}

	}

	private static final int POLL_INTERVAL = 300;

	private Runnable mSleepTask = new Runnable() {
		@Override
		public void run() {
			stop();
		}
	};
	private Runnable mPollTask = new Runnable() {
		@Override
		public void run() {
			double amp = mSensor.getAmplitude();
			updateDisplay(amp);

			mHandler.postDelayed(mPollTask, POLL_INTERVAL);

		}
	};

	private Runnable mCancellTask = new Runnable() {
		@Override
		public void run() {

			mImgVoice.setVisibility(View.GONE);

		}
	};

	private void start(String name) {
		mSensor.start(name);
		mHandler.postDelayed(mPollTask, POLL_INTERVAL);
	}

	private void stop() {
		mHandler.removeCallbacks(mSleepTask);
		mHandler.removeCallbacks(mPollTask);
		mSensor.stop();
		// volume.setImageResource(R.drawable.amp1);
	}

	private void cancell_up() {
		mHandler.removeCallbacks(mSleepTask);
		mHandler.removeCallbacks(mPollTask);

		mImgVoice.setBackgroundResource(R.drawable.sensor_cancell);

	}

	private void cancell() {
		mHandler.postDelayed(mCancellTask, 300);
	}

	private void cancellShort() {
		mHandler.removeCallbacks(mSleepTask);
		mHandler.removeCallbacks(mPollTask);

		mImgVoice.setBackgroundResource(R.drawable.sensor00);
		mHandler.postDelayed(mCancellTask, 1000);
	}

	private void updateDisplay(double signalEMA) {

		switch ((int) signalEMA) {
		case 0:
			mImgVoice.setBackgroundResource(R.drawable.sensor01);
			break;

		case 1:
			mImgVoice.setBackgroundResource(R.drawable.sensor02);
			break;
		case 2:
			mImgVoice.setBackgroundResource(R.drawable.sensor03);
			break;
		case 3:
			mImgVoice.setBackgroundResource(R.drawable.sensor04);
			break;
		case 4:
			mImgVoice.setBackgroundResource(R.drawable.sensor05);
			break;
		case 5:
			mImgVoice.setBackgroundResource(R.drawable.sensor06);
			break;
		default:
			mImgVoice.setBackgroundResource(R.drawable.sensor06);
			break;
		}
	}

}
