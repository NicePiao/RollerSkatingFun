/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package org.videolan.vlc.gui;

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

import com.actionbarsherlock.app.SherlockFragment;
import com.chaowei.com.rollerfast.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.videolan.vlc.AudioServiceController;
import org.videolan.vlc.http.HttpUtil;
import org.videolan.vlc.model.HomeConfig;

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

        setData(makeTestData());
        return v;
    }

    private List<DownloadRecord> makeTestData() {
        List<DownloadRecord> list = new ArrayList<DownloadRecord>();
        for (int i = 0; i < 10; i++) {
            DownloadRecord record = new DownloadRecord();
            record.title = "北平无战事 " + i;
            list.add(record);
        }
        return list;
    }

    public void setData(List<DownloadRecord> downloadRecord) {
        mDownloadRecords = new ArrayList<DownloadRecord>();
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

            DownloadRecord record = mDownloadRecords.get(i);
            titleTv.setText(record.title);
            return view;
        }
    }


    public class DownloadRecord {
        public String imgUrl;
        public String title;
    }

}
