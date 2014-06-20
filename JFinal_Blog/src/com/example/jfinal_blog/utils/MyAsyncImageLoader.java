package com.example.jfinal_blog.utils;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

/**
 * 
 * 
 * 项目名称：Blog 类名称：MyAsyncImageLoader 类描述： 创建人：Administrator 创建时间：2014-6-20
 * 下午4:46:53 修改人：Administrator 修改时间：2014-6-20 下午4:46:53 修改备注：
 * 
 * @version
 * 
 */
public class MyAsyncImageLoader {
	/**
	 * 缓存集合
	 */
	private HashMap<String, SoftReference<Bitmap>> cacheMap;
	private ExecutorService pool = Executors.newFixedThreadPool(5);

	public MyAsyncImageLoader() {
		cacheMap = new HashMap<String, SoftReference<Bitmap>>();
	}

	/**
	 * 根据url,size加载bitmap loadBitmap
	 * 
	 * @param
	 * @return
	 * @Exception
	 * @since
	 */
	public Bitmap loadBitmap(final String url, int reqSize) {
		if (cacheMap.containsKey(url)) {
			SoftReference<Bitmap> reference = cacheMap.get(url);
			Bitmap bitmap = null;
			if (reference != null) {
				bitmap = reference.get();
			}
			if (bitmap != null && !bitmap.isRecycled()) {
				return bitmap;
			}
		}
		final Handler handler = new Handler() {
			public void handleMessage(android.os.Message msg) {

			};
		};
		if (pool.isTerminated()) {
			pool = Executors.newFixedThreadPool(5);
		}
		pool.submit(new Runnable() {

			@Override
			public void run() {
				Bitmap bitmap = null;
				if (isSDcardExist()) {
					if (url != null && url.trim().length() > 0) {
						// 从网络下载数据
					}
				} else {

				}
				if (bitmap != null && !bitmap.isRecycled()) {
					cacheMap.put(url, new SoftReference<Bitmap>(bitmap));
				}
				Message message = handler.obtainMessage(0, bitmap);
				handler.sendMessage(message);
			}
		});

		return null;
	}

	public static boolean isSDcardExist() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}
}
