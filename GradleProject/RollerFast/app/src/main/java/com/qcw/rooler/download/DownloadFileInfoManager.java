package com.qcw.rooler.download;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 下载文件信息
 * Created by qinchaowei on 16/3/6.
 */
public class DownloadFileInfoManager {

    private static DownloadFileInfoManager mDfi;
    private Context mContext;
    private SharedPreferences mSp;

    private DownloadFileInfoManager(Context context) {
        mContext = context;
        mSp = mContext.getSharedPreferences("DownloadFileInfo", Context.MODE_PRIVATE);
    }

    public static synchronized DownloadFileInfoManager getInstance(Context context) {
        if (mDfi == null) {
            mDfi = new DownloadFileInfoManager(context);
        }
        return mDfi;
    }

    /**
     * 纪录一条下载数据
     */
    public void addInfo(String url, String title, String imageUrl) {
        mSp.edit().putString(url, makeJsonObjString(url, title, imageUrl)).commit();
    }

    /**
     * 删除一条纪录
     *
     * @param url
     */
    public void delInfo(String url) {
        mSp.edit().remove(url).commit();
    }

    /**
     * 获取一条下载数据
     */
    public DownloadFileInfo getInfo(String url) {
        String jsonObjStr = mSp.getString(url, "");
        return getInfoFromJson(jsonObjStr);
    }

    /**
     * 获取所有的下载数据
     */
    public List<DownloadFileInfo> getAllInfo() {
        Map<String, ?> all = mSp.getAll();
        List<DownloadFileInfo> allInfo = new ArrayList<DownloadFileInfo>();
        for (Object object : all.values()) {
            DownloadFileInfo info = getInfoFromJson((String) object);
            if (info != null) {
                allInfo.add(info);
            }
        }
        return allInfo;
    }

    private String makeJsonObjString(String url, String title, String imageUrl) {
        JSONStringer jsonStringer = new JSONStringer();
        try {
            return jsonStringer.object().key("url").value(url).key("title").value(title).key("imageUrl").value(imageUrl).endObject().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private DownloadFileInfo getInfoFromJson(String jsonStr) {
        if (!TextUtils.isEmpty(jsonStr)) {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                DownloadFileInfo fileInfo = new DownloadFileInfo();
                fileInfo.url = jsonObject.optString("url");
                fileInfo.title = jsonObject.optString("title");
                fileInfo.imageUrl = jsonObject.optString("imageUrl");
                return fileInfo;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 下载数据结构
     */
    public class DownloadFileInfo {
        public String url;
        public String title;
        public String imageUrl;
    }


}
