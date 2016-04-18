/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.qcw.rooler.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragment;
import com.qcw.rooler.R;
import com.qcw.rooler.download.DownloadInfoCenter;
import com.qcw.rooler.download.SimpleDownloadListener;
import com.qcw.rooler.http.HttpUtil;
import com.qcw.rooler.model.HomeConfig;
import com.qcw.rooler.model.HomeConfigUtil;
import com.qcw.rooler.ui.widget.VideoGridView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 轮滑首页
 * Created by qinchaowei on 2015/11/18.
 */
public class VideoHomeFragment extends SherlockFragment implements View.OnClickListener {

    private LinearLayout mContainerView;
    private View         mEmptyContainer;
    private View         mRefreshBtn;
    private List<VideoGridView> mVideoGridList = new ArrayList<VideoGridView>();

    private SimpleDownloadListener mSimpleDownloadListener;

    /* All subclasses of Fragment must include a public empty constructor. */
    public VideoHomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSimpleDownloadListener = new SimpleDownloadListener() {

            @Override
            public void onFinisihDownload(String url) {
                super.onFinisihDownload(url);
                refreshItem(url);
            }

            @Override
            public void onDeleteDownload(String url) {
                super.onDeleteDownload(url);
                refreshItem(url);
            }
        };
        DownloadInfoCenter.getInstance(getActivity()).addListener(mSimpleDownloadListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
        getSherlockActivity().getSupportActionBar().setTitle(R.string.home);

        View v = inflater.inflate(R.layout.video_home_layout, container, false);
        mContainerView = (LinearLayout) v.findViewById(R.id.content);
        mEmptyContainer = v.findViewById(R.id.empty_container);
        mRefreshBtn = v.findViewById(R.id.load_data);

        mRefreshBtn.setOnClickListener(this);

        HomeConfig cachedConfig = HomeConfigUtil.getCachedHomeConfig(getActivity());
        if (cachedConfig == null) {
            mEmptyContainer.setVisibility(View.VISIBLE);
        } else {
            mEmptyContainer.setVisibility(View.GONE);
            setData(cachedConfig);
        }
        loadDataFromNet();
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mSimpleDownloadListener.removeAll();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DownloadInfoCenter.getInstance(getActivity()).removeListener(mSimpleDownloadListener);
    }

    private void setData(HomeConfig homeConfig) {
        if (homeConfig == null || homeConfig.getTopic() == null || homeConfig.getTopic().isEmpty
          ()) {
            return;
        }

        mContainerView.removeAllViews();
        mVideoGridList.clear();
        for (HomeConfig.Topic topicData : homeConfig.getTopic()) {
            VideoGridView videoGridView = new VideoGridView(getActivity());
            videoGridView.setTopicData(topicData);
            mVideoGridList.add(videoGridView);
            mContainerView.addView(videoGridView);

            for (HomeConfig.Video video : topicData.getVideo()) {
                mSimpleDownloadListener.addCareUrl(video.getUrl());
            }
        }
    }

    private void loadDataFromNet() {
        HttpUtil.httpGetHomeConfig(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("qcw", "onFailure");
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "加载数据失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String json = response.body().string();
                final HomeConfig config = HomeConfig.createObject(json);
                Log.d("qcw", "onResponse " + json);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (config != null) {
                            HomeConfigUtil.saveHomeConfig(getActivity(), json);
                            setData(config);
                            Toast.makeText(getActivity(), "加载数据成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "加载数据失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void refreshItem(String url) {
        for (VideoGridView videoGridView : mVideoGridList) {
            videoGridView.refreshVideoItem(url);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.load_data:
                loadDataFromNet();
                break;
            default:
                break;
        }
    }
}
