package com.sean.myasync;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2016/4/25.
 * 网络工具类
 */
public class NetworkUtil {

	private static NetworkUtil ourInstance = new NetworkUtil();

	public static NetworkUtil getInstance() {
		return ourInstance;
	}

	private NetworkUtil() {
	}

	/**
	 * @param is 输入流
	 * @return 该输入流读取的数据，以字符串的形式
	 * @throws IOException
	 */
	public String readStream(InputStream is) throws IOException {
		// stringbuffer 性能高
		StringBuffer res = new StringBuffer();
		String line;
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
		while ((line = br.readLine()) != null) {
			res.append(line);
		}
		br.close();
		return res.toString();
	}

	public Bitmap getBitmap(InputStream is) {
		Bitmap bitmap;
		BufferedInputStream bis = new BufferedInputStream(is);
		bitmap = BitmapFactory.decodeStream(bis);
		try {
			bis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}
