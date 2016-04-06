package com.qcw.rooler.model;

import android.text.TextUtils;
import com.google.gson.Gson;

/**
 * 版本升级结构
 * Created by qinchaowei on 2016/4/6.
 */
public class SettingConfig {
    public UpdateModel update_model;

    public static class UpdateModel {
        public String version; // 版本号
        public String apk_url; // apk下载地址
    }


    /**
     * 从json字符串中获取HomeConfig类
     */
    public static SettingConfig createObject(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        try {
            SettingConfig config = new Gson().fromJson(json.trim(), SettingConfig.class);
            return config;
        } catch (Exception e) {
            return null;
        }
    }
}
