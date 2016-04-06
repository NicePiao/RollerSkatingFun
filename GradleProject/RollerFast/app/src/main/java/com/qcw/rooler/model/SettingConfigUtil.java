package com.qcw.rooler.model;

import android.content.Context;
import android.text.TextUtils;
import com.qcw.rooler.util.FileCopyUtils;

import java.io.File;
import java.io.FileReader;

/**
 * 服务器设置缓存
 * Created by qinchaowei on 2016/3/31.
 */
public class SettingConfigUtil {

    private static final String FILE_NAME = "setting_config.txt";

    /**
     * 存储服务器返回的设置json
     */
    public static void saveSettingConfig(Context context, String json) {
        if (TextUtils.isEmpty(json)) {
            return;
        }

        FileCopyUtils.copyToFile(json, new File(context.getCacheDir() + "/" + FILE_NAME));
    }

    /**
     * 返回上次缓存的设置数据
     */
    public static SettingConfig getCachedSettingConfig(Context context) {
        File cache = new File(context.getCacheDir() + "/" + FILE_NAME);
        try {
            if (cache.exists() && cache.isFile()) { // 本地缓存有数据
                String json = FileCopyUtils.copyToString(new FileReader(cache));
                return SettingConfig.createObject(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
