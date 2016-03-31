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
import android.widget.LinearLayout;
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
import org.videolan.vlc.util.HomeConfigUtil;

import java.io.IOException;

/**
 * 轮滑首页
 * Created by qinchaowei on 2015/11/18.
 */
public class VideoHomeFragment extends SherlockFragment implements View.OnClickListener {

    private LinearLayout mContainerView;
    private View         mEmptyContainer;
    private View         mRefreshBtn;

    /* All subclasses of Fragment must include a public empty constructor. */
    public VideoHomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    private void setData(HomeConfig homeConfig) {
        if (homeConfig == null || homeConfig.getTopic() == null || homeConfig.getTopic().isEmpty
          ()) {
            return;
        }

        mContainerView.removeAllViews();
        for (HomeConfig.Topic topicData : homeConfig.getTopic()) {
            View topicView =
              LayoutInflater.from(getActivity()).inflate(R.layout.video_grid_layout, null);

            TextView titleTextView = (TextView) topicView.findViewById(R.id.topic_title);
            titleTextView.setText(topicData.getTitle());

            LinearLayout line1 = (LinearLayout) topicView.findViewById(R.id.line1);
            LinearLayout line2 = (LinearLayout) topicView.findViewById(R.id.line2);
            for (int i = 0; i < 3; i++) {
                View videoView = line1.getChildAt(i);
                TextView nameTv = (TextView) videoView.findViewById(R.id.name);

                if (topicData.getVideo().size() > i) {
                    HomeConfig.Video videoData = topicData.getVideo().get(i);
                    nameTv.setText(videoData.getName());

                    videoView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // todo play
                            if (view.getTag() != null && !TextUtils.isEmpty(
                              (String) view.getTag())) {
                                AudioServiceController c = AudioServiceController.getInstance();
                                String s = (String) view.getTag();

                              /* Use the audio player by default. If a video track is
                               * detected, then it will automatically switch to the video
                               * player. This allows us to support more types of streams
                               * (for example, RTSP and TS streaming) where ES can be
                               * dynamically adapted rather than a simple scan.
                               */
                                c.load(s, false);
                            } else {
                            }

                        }
                    });
                    videoView.setTag(videoData.getUrl());
                    videoView.setVisibility(View.VISIBLE);
                } else {
                    videoView.setVisibility(View.GONE);
                }
            }

            for (int i = 0; i < 3; i++) {
                View videoView = line2.getChildAt(i);
                TextView nameTv = (TextView) videoView.findViewById(R.id.name);

                if (topicData.getVideo().size() > i + 3) {
                    HomeConfig.Video videoData = topicData.getVideo().get(i + 3);
                    nameTv.setText(videoData.getName());
                    videoView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // todo play
                        }
                    });
                    videoView.setVisibility(View.VISIBLE);
                } else {
                    videoView.setVisibility(View.GONE);
                }
            }

            mContainerView.addView(topicView);
        }
    }

    private void loadDataFromNet() {
        HttpUtil.httpGetHomeConfig(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("qcw", "onFailure");
                Toast.makeText(getActivity(), "加载数据失败", Toast.LENGTH_SHORT).show();
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
                        } else {
                            Toast.makeText(getActivity(), "加载数据失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
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
