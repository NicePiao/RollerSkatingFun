/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package org.videolan.vlc.gui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import com.chaowei.com.rollerfast.R;
import com.golshadi.majid.report.listener.DownloadManagerListener;
import org.videolan.vlc.util.DownloadTool;

import java.util.ArrayList;
import java.util.List;

/**
 * 下载界面
 * Created by qinchaowei on 2015/11/18.
 */
public class VideoDownloadFragment extends SherlockFragment implements DownloadManagerListener {

    private ListView             mDownloadListView;
    private DownloadAdapter      mDownloadAdapter;
    private List<DownloadRecord> mDownloadRecords;

    /* All subclasses of Fragment must include a public empty constructor. */
    public VideoDownloadFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DownloadTool.getInstance(getActivity()).setDownloadListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
        getSherlockActivity().getSupportActionBar().setTitle(R.string.history);

        View v = inflater.inflate(R.layout.video_download_layout, container, false);
        mDownloadListView = (ListView) v.findViewById(R.id.download_lv);
        mDownloadAdapter = new DownloadAdapter();
        mDownloadRecords = new ArrayList<DownloadRecord>();
        mDownloadListView.setAdapter(mDownloadAdapter);
        setData(makeTestData());
        return v;
    }

    private List<DownloadRecord> makeTestData() {
        List<DownloadRecord> list = new ArrayList<DownloadRecord>();

//        DownloadRecord record = new DownloadRecord();

        DownloadRecord record2 = new DownloadRecord();
        record2.url = "http://img2.3lian.com/2014/f5/99/d/3.jpg";
        record2.title = "图片2";
        list.add(record2);

        DownloadRecord record3 = new DownloadRecord();
        record3.url = "http://pic3.nipic.com/20090615/1412106_072833027_2.jpg";
        record3.title = "图片3";
        list.add(record3);

        DownloadRecord record5 = new DownloadRecord();
        record5.url = "http://pic23.nipic.com/20120911/10781601_165603577148_2.jpg";
        record5.title = "图片5";
        list.add(record5);

        DownloadRecord record6 = new DownloadRecord();
        record6.url = " https://dl.wandoujia.com/files/jupiter/latest/wandoujia-wandoujia_web.apk";
        record6.title = "文件6";
        list.add(record6);

        DownloadRecord record7 = new DownloadRecord();
        record7.url = "http://download.zdworks.com/zdclock/zdclock_latest.apk";
        record7.title = "文件7";
        list.add(record7);

        return list;
    }

    public void setData(List<DownloadRecord> downloadRecord) {
        mDownloadRecords.clear();
        if (downloadRecord != null) {
            mDownloadRecords.addAll(downloadRecord);
        }
        mDownloadAdapter.notifyDataSetChanged();
    }

    private View getViewFromUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }

        for (int i = 0; i < mDownloadRecords.size(); i++) {
            DownloadRecord record = mDownloadRecords.get(i);
            if (url.equals(record.url)) {
                // 获取当前可以看到的item位置
                int visiblePosition = mDownloadListView.getFirstVisiblePosition();
                View view = mDownloadListView.getChildAt(i - visiblePosition);
                return view;
            }
        }

        return null;
    }

    private void updateItemView(long taskId) {
        String url = DownloadTool.getInstance(getActivity()).getUrl((int) taskId);

        for (int i = 0; i < mDownloadRecords.size(); i++) {
            final DownloadRecord record = mDownloadRecords.get(i);
            if (!TextUtils.isEmpty(url) && url.equals(record.url)) {
                // 获取当前可以看到的item位置
                int visiblePosition = mDownloadListView.getFirstVisiblePosition();
                final View view = mDownloadListView.getChildAt(i - visiblePosition);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDownloadAdapter.updateView(view, record);
                    }
                });
                return;
            }
        }
    }


    @Override
    public void OnDownloadStarted(long taskId) {
        updateItemView(taskId);
    }

    @Override
    public void OnDownloadPaused(long taskId) {
        updateItemView(taskId);
    }

    @Override
    public void onDownloadProcess(long taskId, double percent, long downloadedLength) {
        updateItemView(taskId);
    }

    @Override
    public void OnDownloadFinished(long taskId) {
        updateItemView(taskId);
    }

    @Override
    public void OnDownloadRebuildStart(long taskId) {
        updateItemView(taskId);
    }

    @Override
    public void OnDownloadRebuildFinished(long taskId) {
        updateItemView(taskId);
    }

    @Override
    public void OnDownloadCompleted(long taskId) {
        updateItemView(taskId);
    }

    @Override
    public void connectionLost(long taskId) {
        updateItemView(taskId);
    }

    public class DownloadAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDownloadRecords.size();
        }

        @Override
        public Object getItem(int i) {
            return mDownloadRecords.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.video_download_list_item,
                  null);
            }
            updateView(view,mDownloadRecords.get(i));
            return view;
        }

        public void updateView( View view,final DownloadRecord record) {
            if (view == null) {
                return;
            }
            ImageView iconImage =
              (ImageView) view.findViewById(R.id.download_icon);// todo net image
            TextView titleTv = (TextView) view.findViewById(R.id.name);
            TextView progressTv = (TextView) view.findViewById(R.id.progress);
            Button pauseResumeBtn = (Button) view.findViewById(R.id.pause_or_resume);
            Button delBtn = (Button) view.findViewById(R.id.del);

            titleTv.setText(record.title);

            double percent = DownloadTool.getInstance(getActivity()).getFileProgress(record.url);
            titleTv.setText((int) percent + "/100");

            int fileState = DownloadTool.getInstance(getActivity()).getFileState(record.url);
            if (fileState == DownloadTool.FILE_STATE_FINISHED) {
                pauseResumeBtn.setText("已完成");
            } else if (fileState == DownloadTool.FILE_STATE_DOWNLOADING) {
                pauseResumeBtn.setText("下载中...");
            } else if (fileState == DownloadTool.FILE_STATE_PUASED) {
                pauseResumeBtn.setText("暂停中...");
            } else {
                pauseResumeBtn.setText("文件出错");
            }

            pauseResumeBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int fileState =
                      DownloadTool.getInstance(getActivity()).getFileState(record.url);
                    if (fileState == DownloadTool.FILE_STATE_DOWNLOADING) {
                        DownloadTool.getInstance(getActivity()).pauseDownload(record.url);
                    } else if (fileState == DownloadTool.FILE_STATE_PUASED) {
                        DownloadTool.getInstance(getActivity()).startDownload(record.url);
                    }
                    notifyDataSetChanged();
                }
            });

            delBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    DownloadTool.getInstance(getActivity()).delete(record.url);
                    notifyDataSetChanged();
                }
            });
        }
    }

    public class DownloadRecord {
        public String url;
        public String imgUrl;
        public String title;
    }

}
