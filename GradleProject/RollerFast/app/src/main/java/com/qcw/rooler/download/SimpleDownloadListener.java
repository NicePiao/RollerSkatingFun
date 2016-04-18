package com.qcw.rooler.download;

import java.util.ArrayList;
import java.util.List;

/**
 * 下载监听器
 * Created by qinchaowei on 2016/4/13.
 */
public class SimpleDownloadListener {

    public List<String> mCareList = new ArrayList<String>();

    /**
     * 增加关心的下载
     */
    public void addCareUrl(String... urls) {
        for (String url : urls) {
            if (!mCareList.contains(url)) {
                mCareList.add(url);
            }
        }
    }

    /**
     * 移除关心的下载
     */
    public void removeCareUrl(String... urls) {
        for (String url : urls) {
            if (mCareList.contains(url)) {
                mCareList.remove(url);
            }
        }
    }

    /**
     * 清除所有
     */
    public void removeAll(){
        mCareList.clear();
    }

    /**
     * 获取关心的下载
     */
    public List<String> getCareList(){
        return mCareList;
    }

    /**
     * 是否关心
     */
    public boolean isCare(String url){
        return mCareList.contains(url);
    }

    // -----回调函数-----------
    public void onNewTaskBegin(String url) {
    }

    public void onStartDownload(String url) {
    }

    public void onDownlaodProgress(String url, double progress) {
    }

    public void onStopDownload(String url) {
    }

    public void onFinisihDownload(String url) {
    }

    public void onDeleteDownload(String url) {
    }
    // -----回调函数-----------

}
