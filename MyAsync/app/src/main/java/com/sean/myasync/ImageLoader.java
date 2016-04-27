package com.sean.myasync;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2016/4/25.
 * 图片加载器，带内存缓存的
 */
public class ImageLoader {

	/**
	 * 在内存中缓存bitmap ，不用每次滚动都下载图片
	 * 1. 初始方法中初始化 lrucache
	 * 2. 每次下载之后，缓存图片
	 * 3. 每次加载图片时下先去cache中找，没找到才下载
	 */
	private LruCache<String, Bitmap> mCaches;
	private Set<BitmapTask> TaskSet;

	public ImageLoader() {

		TaskSet = new HashSet<>();

		// 获取最大可用内存
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		//  设置cache 大小
		int cachesize = maxMemory / 4;
		mCaches = new LruCache<String, Bitmap>(cachesize) {
			// 重写 sizeof 方法
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// 在每次存入缓存的时候调用，必须获得存入对象的大小  ！！！
				return value.getByteCount();
			}
		};
	}

	public void putBitmap2cache(String url, Bitmap bitmap) {
		if (getBitmapfromcache(url) == null) {
			mCaches.put(url, bitmap);
		}
	}

	public Bitmap getBitmapfromcache(String url) {
		return mCaches.get(url);
	}

	// 通过url获取bitmap，并将bitmap设置的到imageview中
	public void dispaly(ImageView imageView, String url) {
		//先从缓存中获取
		Bitmap bitmap = getBitmapfromcache(url);
		if (bitmap == null) {
			// 异步下载图片并加载到imageview
			BitmapTask task = new BitmapTask(imageView, url);
			task.execute(url);
			TaskSet.add(task);
		} else {
			imageView.setImageBitmap(bitmap);
		}
	}

	// 取消所有的下载任务
	public void cancelAllTask() {
		for (BitmapTask task : TaskSet) {
			task.cancel(true);
		}
	}

	class BitmapTask extends AsyncTask<String, Void, Bitmap> {

		private ImageView imageView;
		private String murl;

		public BitmapTask(ImageView imageView, String murl) {
			this.imageView = imageView;
			this.murl = murl;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			InputStream inputStream = null;
			Bitmap bitmap = null;
			try {
				inputStream = new URL(params[0]).openStream();
				bitmap = NetworkUtil.getInstance().getBitmap(inputStream);
				// 每次下载之后，保存到cache中
				if (bitmap != null)
					putBitmap2cache(params[0], bitmap);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			super.onPostExecute(bitmap);
			if (bitmap != null && imageView.getTag() == murl) {
				imageView.setImageBitmap(bitmap);
			}
		}
	}
}
