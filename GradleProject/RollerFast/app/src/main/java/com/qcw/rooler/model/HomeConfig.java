package com.qcw.rooler.model;

import android.text.TextUtils;
import com.google.gson.Gson;

import java.util.List;

/**
 * 主页配置model
 * Created by qinchaowei on 15/11/22.
 */
public class HomeConfig {

    private List<Topic> topic;

    public void setTopic(List<Topic> topic) {
        this.topic = topic;
    }

    public List<Topic> getTopic() {
        return topic;
    }

    public class Topic {

        private String title;
        private List<Video> video;

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setVideo(List<Video> video) {
            this.video = video;
        }

        public List<Video> getVideo() {
            return video;
        }

    }

    public class Video {

        private String name;
        private String url;
        private String img;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getImg() {
            return img;
        }

    }

    /**
     * 从json字符串中获取HomeConfig类
     */
    public static HomeConfig createObject(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        try {
            HomeConfig config = new Gson().fromJson(json.trim(), HomeConfig.class);
            return config;
        } catch (Exception e) {
            return null;
        }
    }

}
