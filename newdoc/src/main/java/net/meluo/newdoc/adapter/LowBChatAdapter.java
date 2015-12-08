package net.meluo.newdoc.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.bigd.utl.ImgUtl;
import com.bigd.utl.SizeUtl;
import com.koushikdutta.ion.Ion;
import com.way.util.XMPPHelper;

import net.meluo.newdoc.R;
import net.meluo.newdoc.app.NDApp;
import net.meluo.newdoc.bean.LowBChatItem;

import java.util.ArrayList;

public class LowBChatAdapter extends BaseAdapter {

	// 数据源
	ArrayList<LowBChatItem> list;
	private Activity context;
	Typeface typeFace;
	private final LayoutInflater mInflater;

	private final int ITME_TYPE_ME = 0;
	private final int ITME_TYPE_FRIEND = 1;
	private final int ITME_TYPE_ME_IMG = 2;
	private final int ITME_TYPE_FRIEND_IMG = 3;
	private final int ITME_TYPE_ME_VOICE = 4;
	private final int ITME_TYPE_FRIEND_VOICE = 5;

	private MediaPlayer mMediaPlayer = new MediaPlayer();

	// 构造函数
	public LowBChatAdapter(Activity context, ArrayList<LowBChatItem> list) {
		this.context = context;
		this.list = list;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		LowBChatItem chatItem = list.get(position);
		if (chatItem.from_me) {
			if (chatItem.img != null) {
				return ITME_TYPE_ME_IMG;
			}
			if (chatItem.voice != null) {
				return ITME_TYPE_ME_VOICE;
			}
			return ITME_TYPE_ME;
		} else {
			return ITME_TYPE_FRIEND;
		}
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 6;
	}

	@SuppressLint({ "InflateParams" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = new ViewHolder();

		final LowBChatItem chatItem = list.get(position);
		if (convertView == null) {
			switch (this.getItemViewType(position)) {
			case ITME_TYPE_ME:
				convertView = mInflater.inflate(R.layout.itm_chat_me, parent,
						false);
				viewHolder.content = (TextView) convertView
						.findViewById(R.id.im_item_content);
				viewHolder.time = (TextView) convertView
						.findViewById(R.id.im_item_time);
				viewHolder.avatar = (ImageView) convertView
						.findViewById(R.id.im_item_head);
				convertView.setTag(viewHolder);

				break;

			case ITME_TYPE_FRIEND:
				convertView = mInflater.inflate(R.layout.itm_chat_friend,
						parent, false);
				viewHolder.content = (TextView) convertView
						.findViewById(R.id.im_item_content);
				viewHolder.time = (TextView) convertView
						.findViewById(R.id.im_item_time);
				viewHolder.avatar = (ImageView) convertView
						.findViewById(R.id.im_item_head);
				convertView.setTag(viewHolder);

				break;

			case ITME_TYPE_ME_IMG:

				convertView = mInflater.inflate(R.layout.itm_chat_me_img,
						parent, false);
				viewHolder.img = (ImageView) convertView
						.findViewById(R.id.im_item_content_img);
				viewHolder.time = (TextView) convertView
						.findViewById(R.id.im_item_time);
				viewHolder.avatar = (ImageView) convertView
						.findViewById(R.id.im_item_head);
				convertView.setTag(viewHolder);

				break;
			case ITME_TYPE_ME_VOICE:

				convertView = mInflater.inflate(R.layout.itm_chat_me_voice,
						parent, false);
				viewHolder.content_wrap = (RelativeLayout) convertView
						.findViewById(R.id.content_wrap);
				viewHolder.voice = (ImageView) convertView
						.findViewById(R.id.im_item_content_voice);
				viewHolder.voice_size = (TextView) convertView
						.findViewById(R.id.im_item_voice_size);
				viewHolder.time = (TextView) convertView
						.findViewById(R.id.im_item_time);
				viewHolder.avatar = (ImageView) convertView
						.findViewById(R.id.im_item_head);
				convertView.setTag(viewHolder);

				break;

			}
		} else {

			switch (this.getItemViewType(position)) {
			case ITME_TYPE_ME:
				viewHolder = (ViewHolder) convertView.getTag();
				break;
			case ITME_TYPE_FRIEND:
				viewHolder = (ViewHolder) convertView.getTag();
				break;
			case ITME_TYPE_ME_IMG:
				viewHolder = (ViewHolder) convertView.getTag();
				break;
			case ITME_TYPE_ME_VOICE:
				viewHolder = (ViewHolder) convertView.getTag();
				break;
			}
		}

		String imgHeadUrl = ""; // TODO 添加头像显示
		if (chatItem.from_me) {
			imgHeadUrl = NDApp.getInstance().user.getPicture_url();
		}
		Ion.with(context).load(imgHeadUrl).withBitmap()
				.error(R.drawable.ic_tem_head).intoImageView(viewHolder.avatar);
		switch (this.getItemViewType(position)) {

		case ITME_TYPE_ME:
		case ITME_TYPE_FRIEND:
			viewHolder.content.setText(XMPPHelper
					.convertNormalStringToSpannableString(context,
							chatItem.content, false));
			break;
		case ITME_TYPE_ME_IMG:

			DisplayMetrics metric = new DisplayMetrics();
			context.getWindowManager().getDefaultDisplay().getMetrics(metric);
			int screenW = metric.widthPixels;
			int imgW = screenW - SizeUtl.dip2px(context, 130);
			viewHolder.img.setImageBitmap(ImgUtl.getImageThumbnail2(
					chatItem.img, imgW));

			break;
		case ITME_TYPE_ME_VOICE:
			LayoutParams para;
			para = (LayoutParams) viewHolder.content_wrap.getLayoutParams();
			para.width = SizeUtl.dip2px(context,
					40 + (chatItem.voice_size * 10));
			if (chatItem.voice_size > 20) {
				para.width = SizeUtl.dip2px(context, 240);
			}
			viewHolder.content_wrap.setLayoutParams(para);
			viewHolder.voice_size.setText(chatItem.voice_size + "'");
			viewHolder.voice.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					playMusic(chatItem.voice, v);
				}
			});
			break;
		}
		viewHolder.time.setText(chatItem.time);
		return convertView;
	}

	private void playMusic(String name, View v) {

		((ImageView) v).setImageResource(R.drawable.voice_right);

		final AnimationDrawable animationDrawable = (AnimationDrawable) ((ImageView) v)
				.getDrawable();
		animationDrawable.start();
		try {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(name);
			mMediaPlayer.prepare();
			mMediaPlayer.start();
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					animationDrawable.stop();
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static class ViewHolder {
		TextView content;
		TextView time;
		RelativeLayout content_wrap;
		ImageView avatar;
		ImageView img;
		ImageView voice;
		TextView voice_size;
	}
}
