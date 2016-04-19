package com.qcw.rooler.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.qcw.rooler.download.DownloadInfoCenter;
import com.qcw.rooler.download.DownloadTool;
import com.qcw.rooler.download.SimpleDownloadListener;

/**
 * 下载状态控件，只关心单个Url
 * Created by qinchaowei on 2016/4/19.
 */
public class DownloadView extends TextView implements View.OnClickListener {

    private SimpleDownloadListener mSimpleDonwloadListener;

    public DownloadView(Context context) {
        super(context);
        setText("no url");
        setOnClickListener(this);
    }

    public DownloadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setText("no url");
        setOnClickListener(this);
    }

    /**
     * 初始化控件
     *
     * @param url
     */
    public void init(String url) {
        if (mSimpleDonwloadListener == null) {
            mSimpleDonwloadListener = new SimpleDownloadListener() {

                @Override
                public void onFinisihDownload(final String url) {
                    super.onFinisihDownload(url);
                    ((Activity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshText(url);
                        }
                    });
                }

                @Override
                public void onDownlaodProgress(final String url, final double progress) {
                    super.onDownlaodProgress(url, progress);
                    ((Activity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshProgress(url, progress);
                        }
                    });
                }
            };
            DownloadInfoCenter.getInstance(getContext()).addListener(mSimpleDonwloadListener);
        } else {
            mSimpleDonwloadListener.removeAll();
        }
        mSimpleDonwloadListener.addCareUrl(url);
        refreshText(url);
    }

    /**
     * 结束控件
     */
    public void deinit() {
        DownloadInfoCenter.getInstance(getContext()).removeListener(mSimpleDonwloadListener);
        mSimpleDonwloadListener.removeAll();
        mSimpleDonwloadListener = null;
    }

    private void refreshText(String url) {
        int fileState = DownloadTool.getInstance(getContext()).getFileState(url);
        if (fileState == DownloadTool.FILE_STATE_FINISHED) {
            setText("已下载");
        } else if (fileState == DownloadTool.FILE_STATE_DOWNLOADING) {
            setText("下载中...");
        } else if (fileState == DownloadTool.FILE_STATE_PUASED) {
            setText("暂停中...");
        } else {
            setText("文件出错");
        }
    }

    private void refreshProgress(String url, double progress) {
        setText(String.valueOf(progress));
    }

    @Override
    public void onClick(View view) {
        if (mSimpleDonwloadListener == null || mSimpleDonwloadListener.getCareList() == null ||
          mSimpleDonwloadListener.getCareList().size() == 0) {
            return;
        }

        String url = mSimpleDonwloadListener.getCareList().get(0);

        DownloadTool downloadTool = DownloadTool.getInstance(getContext());
        int fileState = downloadTool.getFileState(url);
        switch (fileState) {
            case DownloadTool.FILE_STATE_DOWNLOADING:
                DownloadTool.getInstance(getContext()).pauseDownload(url);
                Toast.makeText(getContext(), "暂停下载", Toast.LENGTH_SHORT).show();
                break;
            case DownloadTool.FILE_STATE_PUASED:
                DownloadTool.getInstance(getContext()).startDownload(url);
                Toast.makeText(getContext(), "开始下载", Toast.LENGTH_SHORT).show();
                break;
            case DownloadTool.FILE_STATE_FINISHED:
                Toast.makeText(getContext(), "已经下载完成", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getContext(), "文件不合法", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
