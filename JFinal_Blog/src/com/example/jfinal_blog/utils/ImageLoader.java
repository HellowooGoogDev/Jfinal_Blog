package com.example.jfinal_blog.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.support.v4.util.LruCache;

/**
 * 
 * 类描述： 图片缓存技术类，用于缓存下载好的图片，当程序内存达到设定值将最近最少使用的删除 
 * 创建者： rain 
 * 项目名称： Blog 
 * 创建时间：2013年12月7日 下午3:41:22 版本号： v1.0
 */
public class ImageLoader {
    private static LruCache<String, Bitmap> mMemoryCache;
    private static ImageLoader instance;

    private ImageLoader() {
	int maxMemory = (int) Runtime.getRuntime().maxMemory();
	int cacheSize = maxMemory / 8;
	mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
	    @Override
	    protected int sizeOf(String key, Bitmap value) {
		// TODO Auto-generated method stub
		return value.getByteCount();
	    }
	};
    }

    public static ImageLoader getInstance() {
	if (instance == null) {
	    instance = new ImageLoader();
	}
	return instance;
    }

    /**
     * 
    * 方法描述 : 按照key值添加到lrucache缓存中去
    * 创建者：rain 
    * 项目名称： Blog
    * 类名： ImageLoader.java
    * 版本： v1.0
    * 创建时间： 2013年12月7日 下午3:51:18
    * @param key
    * @param bitMap void
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitMap) {
	if (getBitmapFromMemoryCache(key) == null) {
	    addBitmapToMemoryCache(key, bitMap);
	}
    }

    /**
    * 方法描述 : 根据key从lrucache中获取bitmap
    * 创建者：rain 
    * 项目名称： Blog
    * 类名： ImageLoader.java
    * 版本： v1.0
    * 创建时间： 2013年12月7日 下午3:53:11
    * @param key
    * @return Bitmap
     */
    public Bitmap getBitmapFromMemoryCache(String key) {
	return mMemoryCache.get(key);
    }

    /**
     * 
    * 方法描述 : 根据给定的reqWidth从文件中创建bitmap
    * 创建者：rain 
    * 项目名称： Blog
    * 类名： ImageLoader.java
    * 版本： v1.0
    * 创建时间： 2013年12月7日 下午4:02:01
    * @param pathName
    * @param reqWidth
    * @return Bitmap
     */
    public static Bitmap decodeSampledBitmapFromResource(String pathName,
	    int reqWidth) {
	//第一次解析设置inJustDecodeBounds=true获取图片大小
	final BitmapFactory.Options options = new BitmapFactory.Options();
	options.inJustDecodeBounds = true;
	BitmapFactory.decodeFile(pathName, options);
	//根据reqWidth获取inSampleSize
	options.inSampleSize = calculateInSampleSize(options, reqWidth);
	return BitmapFactory.decodeFile(pathName, options);
    }

    /**
    * 方法描述 : 根据给定的reqWidth获取解析图片inSampleSize
    * 创建者：rain 
    * 项目名称： Blog
    * 类名： ImageLoader.java
    * 版本： v1.0
    * 创建时间： 2013年12月7日 下午4:09:30
    * @param options
    * @param reqWidth
    * @return int
     */
    private static int calculateInSampleSize(Options options, int reqWidth) {
	//源图片的宽度
	final int width = options.outWidth;
	int inSampleSize = 1;
	if (width > reqWidth) {
	    // 获取源图片与要求的图片缩放的比例
	    final int widthRatio = Math.round((float) width / (float) reqWidth);
	    inSampleSize = widthRatio;
	}
	return inSampleSize;
    }
}
