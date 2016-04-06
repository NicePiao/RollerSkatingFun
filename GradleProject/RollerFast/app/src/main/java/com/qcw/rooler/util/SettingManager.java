package com.qcw.rooler.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import com.qcw.rooler.model.SettingConfig;
import com.qcw.rooler.model.SettingConfigUtil;

/**
 * 设置配置项管理器
 * Created by qinchaowei on 2016/4/6.
 */
public class SettingManager {

    private static SettingManager sSettingManager;

    private Context       mContext;
    private SettingConfig mConfig;

    private SettingManager(Context context) {
        mContext = context;
        mConfig = SettingConfigUtil.getCachedSettingConfig(mContext);
    }

    public static synchronized SettingManager getInstance(Context context) {
        if (sSettingManager == null) {
            sSettingManager = new SettingManager(context);
        }
        return sSettingManager;
    }

    /**
     * 更新配置数据
     */
    public void updateConfig(String json) {
        final SettingConfig config = SettingConfig.createObject(json);
        if (config != null) {
            SettingConfigUtil.saveSettingConfig(mContext, json);
            mConfig = config;
        }
    }

    /**
     * 是否有新版本
     */
    public boolean hasNewVersion() {
        if (mConfig != null && mConfig.update_model != null) {
            String lateastVersion = mConfig.update_model.version;
            String apkPath = mConfig.update_model.apk_url;
            return !TextUtils.isEmpty(apkPath) && versionLarger(lateastVersion,
              getCurVersion(mContext));
        }
        return false;
    }

    /**
     * 获取更新apk路径
     */
    public String getNewApkPath() {
        if (mConfig != null && mConfig.update_model != null) {
            return mConfig.update_model.apk_url;
        }
        return "";
    }

    /**
     * 比较版本号
     *
     * @param latestVersion 最新的版本号
     * @param curVersion    当前版本号
     * @return true:最新版本号大于当前版本号
     */
    public static boolean versionLarger(String latestVersion, String curVersion) {
        if (TextUtils.isEmpty(latestVersion) || TextUtils.isEmpty(curVersion)) {
            return false;
        }

        String[] latest = latestVersion.split("\\.");
        String[] cur = curVersion.split("\\.");

        if (latest.length != cur.length) {
            return false;
        }

        for (int i = 0; i < latest.length; i++) {
            try {
                int latestNum = Integer.parseInt(latest[i]);
                int curNum = Integer.parseInt(cur[i]);
                if (latestNum > curNum) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getCurVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
        }
        return "";
    }

}
