package org.videolan.vlc.http;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

/**
 * Created by qinchaowei on 15/11/22.
 */
public class HttpUtil {

        private static String HOST="http://101.200.238.202:5000";
//    private static String HOST="http://www.piuker.com:5000";
    private static String HONE_URL= HOST+"/home/";


    public static void httpGetHomeConfig(Callback callback) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(HONE_URL)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(callback);
    }


}
