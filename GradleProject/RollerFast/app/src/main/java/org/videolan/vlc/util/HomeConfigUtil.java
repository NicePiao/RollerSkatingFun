package org.videolan.vlc.util;

import android.content.Context;
import android.text.TextUtils;
import org.videolan.vlc.model.HomeConfig;

import java.io.File;
import java.io.FileReader;

/**
 * 首页配置缓存
 * Created by qinchaowei on 2016/3/31.
 */
public class HomeConfigUtil {

    private static final String FILE_NAME = "home_config.txt";

    /**
     * 存储服务器返回的首页json
     */
    public static void saveHomeConfig(Context context, String json) {
        if (TextUtils.isEmpty(json)) {
            return;
        }

        FileCopyUtils.copyToFile(json, new File(context.getCacheDir() + "/" + FILE_NAME));
    }

    /**
     * 返回上次缓存的首页数据
     */
    public static HomeConfig getCachedHomeConfig(Context context) {
        File cache = new File(context.getCacheDir() + "/" + FILE_NAME);
        try {
            if (cache.exists() && cache.isFile()) { // 本地缓存有数据
                String json = FileCopyUtils.copyToString(new FileReader(cache));
                return HomeConfig.createObject(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
