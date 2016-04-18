/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.qcw.rooler.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragment;
import com.qcw.rooler.R;
import com.qcw.rooler.download.DownloadTool;
import com.qcw.rooler.http.HttpUtil;
import com.qcw.rooler.util.SettingManager;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * 下载界面
 * Created by qinchaowei on 2015/11/18.
 */
public class VideoSettingFragment extends SherlockFragment implements View.OnClickListener {

    private ViewGroup mUpdateLayout;
    private TextView  mProgressTv;

    /* All subclasses of Fragment must include a public empty constructor. */
    public VideoSettingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadDataFromNet();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
        getSherlockActivity().getSupportActionBar().setTitle(R.string.setting);

        View v = inflater.inflate(R.layout.video_setting_layout, container, false);
        mUpdateLayout = (ViewGroup) v.findViewById(R.id.setting_update);
        mUpdateLayout.setOnClickListener(this);
        mProgressTv = (TextView) mUpdateLayout.findViewById(R.id.download_progress);

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_update:
                if (SettingManager.getInstance(getActivity()).hasNewVersion()) {
                    // 现在安装
                    final String apkPath =
                      SettingManager.getInstance(getActivity()).getNewApkPath();
//                    final String apkPath = "http://download.zdworks.com/zdclock/zdclock_latest.apk";
                    final DownloadTool downloadTool = DownloadTool.getInstance(getActivity());
                    downloadTool.delete(apkPath);
                    downloadTool.getFileState(apkPath);
                    downloadTool.startDownload(apkPath);
//                    downloadTool.setDownloadListener(new DownloadManagerListener() {
//
//
//                        private void updateState() {
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    double percent = downloadTool.getFileProgress(apkPath);
//                                    mProgressTv.setText((int) percent + "/100");
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void OnDownloadStarted(long taskId) {
//                            updateState();
//                        }
//
//                        @Override
//                        public void OnDownloadPaused(long taskId) {
//                            updateState();
//                        }
//
//                        @Override
//                        public void onDownloadProcess(long taskId, double percent,
//                          long downloadedLength) {
//                            updateState();
//                        }
//
//                        @Override
//                        public void OnDownloadFinished(long taskId) {
//                            updateState();
//                        }
//
//                        @Override
//                        public void OnDownloadRebuildStart(long taskId) {
//                            updateState();
//                        }
//
//                        @Override
//                        public void OnDownloadRebuildFinished(long taskId) {
//                            updateState();
//                        }
//
//                        @Override
//                        public void OnDownloadCompleted(long taskId) {
//                            updateState();
//                        }
//
//                        @Override
//                        public void connectionLost(long taskId) {
//                            updateState();
//                        }
//                    });
                } else {
                    Toast.makeText(getActivity(), "已经是最新版本", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void loadDataFromNet() {
        HttpUtil.httpGetSettingConfig(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(Response response) throws IOException {
                SettingManager.getInstance(getActivity()).updateConfig(response.body().string());
            }
        });
    }

}
