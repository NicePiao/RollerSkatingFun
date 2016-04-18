package com.qcw.rooler.download;

import android.content.Context;
import com.golshadi.majid.report.listener.DownloadManagerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 下载信息处理中心
 * Created by qinchaowei on 2016/4/13.
 */
public class DownloadInfoCenter implements DownloadManagerListener{

    public static DownloadInfoCenter sInstance;

    private Context mContext;
    private List<SimpleDownloadListener> mListeners = new ArrayList<SimpleDownloadListener>(); // todo qcw 软引用

    private DownloadInfoCenter(Context context) {
        mContext = context;
    }

    public static synchronized DownloadInfoCenter getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DownloadInfoCenter(context);
        }
        return sInstance;
    }

    public void addListener(SimpleDownloadListener listener) {
        mListeners.add(listener);
    }

    public void removeListener(SimpleDownloadListener listener) {
        mListeners.remove(listener);
    }

    @Override
    public void OnDownloadStarted(long taskId) {
        String url = DownloadTool.getInstance(mContext).getUrl((int) taskId);
        for (SimpleDownloadListener listener : mListeners) {
            if (listener.isCare(url)) {
                listener.onStartDownload(url);
            }
        }
    }

    @Override
    public void OnDownloadPaused(long taskId) {
        String url = DownloadTool.getInstance(mContext).getUrl((int) taskId);
        for (SimpleDownloadListener listener : mListeners) {
            if (listener.isCare(url)) {
                listener.onStopDownload(url);
            }
        }
    }

    @Override
    public void onDownloadProcess(long taskId, double percent, long downloadedLength) {
        String url = DownloadTool.getInstance(mContext).getUrl((int) taskId);
        for (SimpleDownloadListener listener : mListeners) {
            if (listener.isCare(url)) {
                listener.onDownlaodProgress(url,percent);
            }
        }
    }

    @Override
    public void OnDownloadFinished(long taskId) {
        String url = DownloadTool.getInstance(mContext).getUrl((int) taskId);
        for (SimpleDownloadListener listener : mListeners) {
            if (listener.isCare(url)) {
                listener.onFinisihDownload(url);
            }
        }
    }

    @Override
    public void OnDownloadRebuildStart(long taskId) {
        // todo qcw
    }

    @Override
    public void OnDownloadRebuildFinished(long taskId) {
        // todo qcw
    }

    @Override
    public void OnDownloadCompleted(long taskId) {
        // todo qcw
    }

    @Override
    public void connectionLost(long taskId) {
        // todo qcw
    }
}
