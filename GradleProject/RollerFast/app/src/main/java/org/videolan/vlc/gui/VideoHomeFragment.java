/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package org.videolan.vlc.gui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;
import com.chaowei.com.rollerfast.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.videolan.vlc.AudioServiceController;
import org.videolan.vlc.http.HttpUtil;
import org.videolan.vlc.model.HomeConfig;

import java.io.IOException;
import java.util.logging.Handler;

/**
 * Created by qinchaowei on 2015/11/18.
 */
public class VideoHomeFragment extends SherlockFragment {

    private LinearLayout mContainerView;

    /* All subclasses of Fragment must include a public empty constructor. */
    public VideoHomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HttpUtil.httpGetHomeConfig(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.d("qcw", response.body().toString());
                final HomeConfig config = HomeConfig.createObject(response.body().string());
                if (config != null) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setData(config);
                        }
                    });
                    Log.d("qcw", "ok");
                } else {
                    Log.d("qcw", "fail");
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getSherlockActivity().getSupportActionBar().setTitle(R.string.history);

        View v = inflater.inflate(R.layout.video_home_layout, container, false);
        mContainerView = (LinearLayout) v.findViewById(R.id.content);
        return v;
    }

    public void setData(HomeConfig homeConfig) {
        if (homeConfig == null || homeConfig.getTopic() == null || homeConfig.getTopic().isEmpty()) {
            return;
        }

        for (HomeConfig.Topic topicData : homeConfig.getTopic()) {
            View topicView = LayoutInflater.from(getActivity()).inflate(R.layout.video_grid_layout, null);

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
                            if (view.getTag() != null && !TextUtils.isEmpty((String)view.getTag())) {
                                AudioServiceController c = AudioServiceController.getInstance();
                                String s = (String)view.getTag();

                              /* Use the audio player by default. If a video track is
                               * detected, then it will automatically switch to the video
                               * player. This allows us to support more types of streams
                               * (for example, RTSP and TS streaming) where ES can be
                               * dynamically adapted rather than a simple scan.
                               */
                                c.load(s, false);
                            } else{
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


}
