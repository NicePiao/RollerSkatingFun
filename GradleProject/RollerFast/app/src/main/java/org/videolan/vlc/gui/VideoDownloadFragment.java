/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package org.videolan.vlc.gui;

import android.app.DownloadManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.chaowei.com.rollerfast.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.videolan.vlc.AudioServiceController;
import org.videolan.vlc.http.HttpUtil;
import org.videolan.vlc.model.HomeConfig;
import org.videolan.vlc.util.DownloadFileInfoManager;
import org.videolan.vlc.util.DownloadTool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 下载界面
 * Created by qinchaowei on 2015/11/18.
 */
public class VideoDownloadFragment extends SherlockFragment {

    private ListView mDownloadListView;
    private DownloadAdapter mDownloadAdapter;
    private List<DownloadRecord> mDownloadRecords;

    /* All subclasses of Fragment must include a public empty constructor. */
    public VideoDownloadFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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

        DownloadRecord record = new DownloadRecord();
        record.url = "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1457276357&di=595c147defdc7e23c032f9daa9932791&src=http://pic31.nipic.com/20130713/4826724_225329401184_2.jpg";
        record.title = "图片1";
        list.add(record);

        DownloadRecord record2 = new DownloadRecord();
        record2.url = "http://img2.3lian.com/2014/f5/99/d/3.jpg";
        record2.title = "图片2";
        list.add(record2);

        DownloadRecord record3 = new DownloadRecord();
        record3.url = "http://pic3.nipic.com/20090615/1412106_072833027_2.jpg";
        record3.title = "图片3";
        list.add(record3);

        DownloadRecord record4 = new DownloadRecord();
        record4.url = "http://img01.taopic.com/140926/318765-140926211P235.jpg";
        record4.title = "图片4";
        list.add(record4);

        DownloadRecord record5 = new DownloadRecord();
        record5.url = "http://pic23.nipic.com/20120911/10781601_165603577148_2.jpg";
        record5.title = "图片5";
        list.add(record5);

        return list;
    }

    public void setData(List<DownloadRecord> downloadRecord) {
        mDownloadRecords.clear();
        if (downloadRecord != null) {
            mDownloadRecords.addAll(downloadRecord);
        }
        mDownloadAdapter.notifyDataSetChanged();
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
                view = LayoutInflater.from(getActivity()).inflate(R.layout.video_download_list_item, null);
            }
            ImageView iconImage = (ImageView) view.findViewById(R.id.download_icon);// todo net image
            TextView titleTv = (TextView) view.findViewById(R.id.name);
            TextView progressTv = (TextView) view.findViewById(R.id.progress);
            Button pauseResumeBtn = (Button) view.findViewById(R.id.pause_or_resume);
            Button delBtn = (Button) view.findViewById(R.id.del);

            final DownloadRecord record = mDownloadRecords.get(i);
            titleTv.setText(record.title);

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
                    int fileState = DownloadTool.getInstance(getActivity()).getFileState(record.url);
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


            return view;
        }
    }

    public class DownloadRecord {
        public String url;
        public String imgUrl;
        public String title;
    }

}
