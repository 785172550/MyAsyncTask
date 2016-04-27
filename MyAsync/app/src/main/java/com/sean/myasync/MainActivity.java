package com.sean.myasync;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个程序我要实现的是用异步下载慕课网提供的信息，并用优化的listview来显示
 * URL:http://www.imooc.com/api/teacher?type=4&num=30
 * 1. 创建每个item 显示数据的bean对象
 * 2. 创建listview的item
 * 3. 创建网络asynctask
 * 4. 在内存中用lrucache 缓存图片  优化  ---->  imageloader
 * 5. listview 滚动时对画面的流畅度要求高，listview在滚动时要重绘UI，异步加载数据时也要重绘UI
 * 所以当两次重绘发生在同一时刻，就会使画面卡顿（特别是很复杂的listview）
 * 解决方法让listview滚动时，不加载，滑动停止时才加载   -----> MyListAdapter
 */
public class MainActivity extends AppCompatActivity {


	private static final String TAG = "MainActivity";
	private ListView mlistview;
	String urlpath = "http://www.imooc.com/api/teacher?type=4&num=30";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mlistview = (ListView) findViewById(R.id.mlistview);
		// async 任务执行 获取json 数据 并转化为
		new MyTask().execute(urlpath);

	}

	class MyTask extends AsyncTask<String, Void, List<NewsBean>> {

		@Override
		protected List<NewsBean> doInBackground(String... params) {
			return getJsonData(params[0]);
		}

		// 执行完成任务之后 获取数据更新UI
		@Override
		protected void onPostExecute(List<NewsBean> newsBeans) {
			super.onPostExecute(newsBeans);
			mlistview.setAdapter(new MyListAdapter(newsBeans, MainActivity.this));
		}
	}


	/**
	 * 将json 格式的字符串转化为 NewsBean 的list
	 *
	 * @param url
	 * @return
	 */
	public List<NewsBean> getJsonData(String url) {

		List<NewsBean> newsBeanList = new ArrayList<>();
		try {
			String data = NetworkUtil.getInstance().readStream(new URL(url).openStream());
			//Log.d(TAG, "getJsonData: " + data);
			JSONObject jsonObject = new JSONObject(data);
			JSONArray jsonArray = jsonObject.getJSONArray("data");

			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObject = jsonArray.getJSONObject(i);
				NewsBean newsBean = new NewsBean(
						jsonObject.getString("name"),
						jsonObject.getString("description"),
						jsonObject.getString("picSmall"));
//				newsBean.setTitle(jsonObject.getString("name"));
//				newsBean.setContent(jsonObject.getString("description"));
//				newsBean.seticon_url(jsonObject.getString("picSmall"));
				newsBeanList.add(newsBean);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return newsBeanList;
	}
}
