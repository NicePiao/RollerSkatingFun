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
            playUrl = "http://k.youku.com/player/getFlvPath/sid" +
              "/845984423891020ec2c4f_00/st/flv/fileid/03000208005430B01849631468DEFEC61C5678" +
              "-3A78-37BA-1971-21A0D4EEA0E7?K=be28ed427747c40d24129018&hd=0&ts=411&pid" +
              "=65a965fbf632be6f&ymovie=1&r=/3sLngL0Q6CXymAIiF9JUfR5MDecwxp/gSVk" +
              "/o8apWJ3KUkaGrqktKh7cO9ZZoqYN5iGQUM9dNrj6YzDV" +
              "+fl4AXEjOQRZG4SyoYwp9M2ypYU7FiQ8xAa6kQeMVpEmnS" +
              "/ITWAND9KTHbLrZoEDBhLldss1obwkUxeBTTkTuJhwcFdRgMrNoyYVHznmOneDvLM&oip=1019354353" +
              "&sid=845984423891020ec2c4f&token=9755&did=cb0c6d4f6b515f1902273507c75357b0&ev=1" +
              "&ctype=20&ep=%2FZEFPmAvoL%2F8JvURVs9HQ9Ab8BBnY%2BDmubPxPbTOehOJADzH" +
              "%2FL9aKcOrwjSfUf5ykyjoaznvODBCftGPNk9u2SuRPl9RJCSqlcVINqjlbex7qa8j6BgbB51" +
              "%2BH1Qdt7za";
            c.load(playUrl, false);
        }
    }
}
