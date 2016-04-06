package com.qcw.rooler.http;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

/**
 * 网络接口
 * Created by qinchaowei on 15/11/22.
 */
public class HttpUtil {

    private static String API_HOST        = "http://www.piuker.com:5000";
    private static String HOME_CONFIG_URL = API_HOST + "/home/";
    private static String SETTING_CONFIG_URL = API_HOST + "/setting/";


    /**
     * 服务器拉取首页配置
     */
    public static void httpGetHomeConfig(Callback callback) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(HOME_CONFIG_URL).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(callback);
    }

    /**
     * 服务器拉取设置配置
     */
    public static void httpGetSettingConfig(Callback callback) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(SETTING_CONFIG_URL).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(callback);
    }



}
