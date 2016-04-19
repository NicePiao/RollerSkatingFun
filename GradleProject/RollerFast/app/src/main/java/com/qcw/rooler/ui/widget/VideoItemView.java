package com.qcw.rooler.ui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.qcw.rooler.R;
import com.qcw.rooler.download.DownloadTool;
import com.qcw.rooler.model.HomeConfig;
import org.videolan.vlc.AudioServiceController;

/**
 * 首页ItemView
 * Created by qinchaowei on 2016/4/13.
 */
public class VideoItemView extends RelativeLayout implements View.OnClickListener{

    private ImageView mIconImage;
    private TextView  mNameTv;
    private ImageView mCorerImage;

    private HomeConfig.Video mVideoData;

    public VideoItemView(Context context) {
        super(context);
        initViews();
    }

    public VideoItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    private void initViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.video_item, this);
        mIconImage = (ImageView) findViewById(R.id.icon);
        mNameTv = (TextView) findViewById(R.id.name);
        mCorerImage = (ImageView) findViewById(R.id.corner);

        setOnClickListener(this);
    }

    /**
     * 设置内容数据
     */
    public void setVideoData(HomeConfig.Video videoData) {
        mVideoData = videoData;
        refreshView();

    }

    /**
     * 刷新界面
     */
    public void refreshView() {
        mIconImage.setImageResource(R.drawable.video_default_pic); // todo qcw
        mNameTv.setText(mVideoData.getName());
        mCorerImage.setVisibility(DownloadTool.getInstance(getContext()).isFileDownloaded(
          mVideoData.getUrl()) ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 是否是指定的videoitem
     */
    public boolean isMatchUrl(String url) {
        return !TextUtils.isEmpty(url) && url.equals(mVideoData.getUrl());
    }

    @Override
    public void onClick(View view) {
        if (!TextUtils.isEmpty(mVideoData.getUrl())) {
            AudioServiceController c = AudioServiceController.getInstance();

            String playUrl = mVideoData.getUrl(); // todo qcw
            c.load(playUrl, false);
        }
    }
}
