/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.qcw.rooler.ui;

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
import com.qcw.rooler.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.videolan.vlc.AudioServiceController;
import com.qcw.rooler.http.HttpUtil;
import com.qcw.rooler.model.HomeConfig;
import com.qcw.rooler.model.HomeConfigUtil;

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

                                s = "http://main.gslb.ku6.com/s1/f6Fx1o2pMcoC0VycnuwDlg../1459674429193/298549354216c0e0d4c8d0ad4ee16f48/1459827321131/v523/17/41/5006339a755974e5a9a1d654323e59ab-f4v-h264-aac-1067-32-251287.0-34727414-1459669083390-8ed4ec47f88a0d715caaeaa8edbadef9-1-00-00-00.f4v";
                                s = "http://k.youku.com/player/getFlvPath/sid" +
                                  "/845984423891020ec2c4f_00/st/flv/fileid/03000208005430B01849631468DEFEC61C5678-3A78-37BA-1971-21A0D4EEA0E7?K=be28ed427747c40d24129018&hd=0&ts=411&pid=65a965fbf632be6f&ymovie=1&r=/3sLngL0Q6CXymAIiF9JUfR5MDecwxp/gSVk/o8apWJ3KUkaGrqktKh7cO9ZZoqYN5iGQUM9dNrj6YzDV+fl4AXEjOQRZG4SyoYwp9M2ypYU7FiQ8xAa6kQeMVpEmnS/ITWAND9KTHbLrZoEDBhLldss1obwkUxeBTTkTuJhwcFdRgMrNoyYVHznmOneDvLM&oip=1019354353&sid=845984423891020ec2c4f&token=9755&did=cb0c6d4f6b515f1902273507c75357b0&ev=1&ctype=20&ep=%2FZEFPmAvoL%2F8JvURVs9HQ9Ab8BBnY%2BDmubPxPbTOehOJADzH%2FL9aKcOrwjSfUf5ykyjoaznvODBCftGPNk9u2SuRPl9RJCSqlcVINqjlbex7qa8j6BgbB51%2BH1Qdt7za";
                                s = "file:///android_asset/test.mp4";

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
