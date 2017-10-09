package com.pudding.tangentninety.app;

import android.os.Environment;

import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;

import java.io.File;

/**
 * Created by Error on 2017/6/22 0022.
 */

public class Constants {
    //首页侧边栏
    public static final int TYPE_HOME = 101;
    public static final int TYPE_SECTION= 102;
    public static final int TYPE_COLLECTION = 103;
    public static final int TYPE_HISTORY= 104;
    public static final int TYPE_SETTING = 105;

    //缓存时间
    public static final int NET_CACHE_TIME=0;
    public static final int NO_NET_CACHE_TIME=60 * 60 * 24 * 28;

    //缓存目录
    public static final String PATH_DATA = App.getInstance().getCacheDir().getAbsolutePath() + File.separator ;
    public static final String PATH_NET_CACHE = PATH_DATA + "data"+ File.separator+"NetCache";
    public static final String PATH_GLIDE_CACHE=PATH_DATA+ InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR;

    //图片存储目录
    public static final String PATH_SDCARD=Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Pudding" + File.separator+ "TangentNinety"+ File.separator;
    public static final String PATH_IAMGE = PATH_SDCARD+ "image"+ File.separator;
    public static final String PARH_LOG= PATH_SDCARD+ "log"+ File.separator;

    //SharePreferences的key
    public static final String SP_NIGHT_MODE = "night_mode";
    public static final String SP_NO_IMAGE = "no_image";
    public static final String SP_AUTO_CACHE = "auto_cache";
    public static final String SP_BIG_FONT= "big_font";

}
