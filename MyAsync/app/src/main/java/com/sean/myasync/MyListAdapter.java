package com.sean.myasync;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2016/4/25.
 */
public class MyListAdapter extends BaseAdapter implements AbsListView.OnScrollListener {

	private static final String TAG = "MyListAdapter";
	List<NewsBean> mlist;
	LayoutInflater mInflater;
	ImageLoader mimageloader;
	private boolean isscroll = false;
	private String[] MURL;

	public MyListAdapter(List<NewsBean> mlist, Context context) {
		this.mlist = mlist;
		mInflater = LayoutInflater.from(context);
		mimageloader = new ImageLoader();
		MURL = new String[mlist.size()];
		for (int i = 0; i < mlist.size(); i++) {
			MURL[i] = mlist.get(i).geticon_url();
		}
	}

	@Override
	public int getCount() {
		return mlist.size();
	}

	@Override
	public Object getItem(int position) {
		return mlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		VeiwHoler vh = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_layout, null);
			vh = new VeiwHoler();
			vh.tv_title = (TextView) convertView.findViewById(R.id.mytitle);
			vh.tv_content = (TextView) convertView.findViewById(R.id.mycontent);
			vh.iv_icon = (ImageView) convertView.findViewById(R.id.myicon);
			convertView.setTag(vh);
		} else {
			vh = (VeiwHoler) convertView.getTag();
		}
		vh.tv_title.setText(mlist.get(position).getTitle());
		vh.tv_content.setText(mlist.get(position).getContent());
		// 加载默认图片
		vh.iv_icon.setImageResource(R.mipmap.ic_launcher);

		// 往每一个imageview中设置一个与之对应的URL，加载时判断一下， 防止图片错位的情况
		vh.iv_icon.setTag(mlist.get(position).geticon_url());
		if (!isscroll) {
			mimageloader.dispaly(vh.iv_icon, mlist.get(position).geticon_url());
		} else {
			mimageloader.cancelAllTask();
		}


		return convertView;
	}

	// listview 的滑动状态监听
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {
			isscroll = false;
		} else {
			isscroll = true;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

	}

	class VeiwHoler {
		TextView tv_title, tv_content;
		ImageView iv_icon;
	}
}
