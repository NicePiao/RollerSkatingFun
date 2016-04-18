package com.qcw.rooler.ui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.qcw.rooler.R;
import com.qcw.rooler.model.HomeConfig;

/**
 * 首页一个栏目控件
 * Created by qinchaowei on 2016/4/13.
 */
public class VideoGridView extends RelativeLayout {

    private TextView   mTopicTv;
    private GridLayout mGridLayout;

    private HomeConfig.Topic mTopicData;

    public VideoGridView(Context context) {
        super(context);
        initViews();
    }

    public VideoGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    private void initViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.video_grid_layout, this);
        mTopicTv = (TextView) findViewById(R.id.topic_title);
        mGridLayout = (GridLayout) findViewById(R.id.grid_layout);
    }

    /**
     * 设置内容数据
     */
    public void setTopicData(HomeConfig.Topic topicData) {
        mTopicData = topicData;
        refreshView();
    }

    /**
     * 刷新界面
     */
    public void refreshView() {
        mTopicTv.setText(mTopicData.getTitle());
        mGridLayout.removeAllViews();
//        mGridLayout.setColumnCount(3);
//        mGridLayout.setRowCount(
//          mTopicData.getVideo().size() / 3 + mTopicData.getVideo().size() % 3 > 0 ? 1 : 0);
        mGridLayout.setColumnCount(5);
        mGridLayout.setRowCount(5);
        for (int i = 0; i < mTopicData.getVideo().size(); i++) {
            HomeConfig.Video video = mTopicData.getVideo().get(i);
            VideoItemView videoItemView = new VideoItemView(getContext());
            videoItemView.setVideoData(video);

            int row = i / 3;
            int column = i % 3;
            GridLayout.Spec rowSpec = GridLayout.spec(2,2);
            GridLayout.Spec columSpec = GridLayout.spec(2,2);
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams(rowSpec, columSpec);
            mGridLayout.addView(videoItemView, lp);
//               mGridLayout.addView(videoItemView);

        }
    }

    /**
     * 刷新指定的view
     */
    public void refreshVideoItem(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        for (int i = 0; i < mGridLayout.getChildCount(); i++) {
            VideoItemView childView = (VideoItemView) mGridLayout.getChildAt(i);
            if (childView.isMatchUrl(url)) {
                childView.refreshView();
            }
        }
    }

}
