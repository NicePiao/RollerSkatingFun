package org.videolan.vlc.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.golshadi.majid.core.DownloadManagerPro;
import com.golshadi.majid.core.enums.TaskStates;
import com.golshadi.majid.database.elements.Task;
import com.golshadi.majid.report.ReportStructure;
import com.golshadi.majid.report.listener.DownloadManagerListener;

/**
 * Created by qinchaowei on 16/3/6.
 */
public class DownloadTool {

    public static final int CREATE_DOWNLOAD_TASK_DOWNLOADED = -2;
    public static final int CREATE_DOWNLOAD_TASK_NEW_BUT_FAILED = -3;
    public static final int CREATE_DOWNLOAD_TASK_PARAM_INVALID = -4;


    private static DownloadTool mDownloadTool;

    private DownloadManagerPro mDmp;
    private Context mContext;

    private DownloadTool(Context context) {
        mContext = context;
        mDmp = new DownloadManagerPro(context);
        mDmp.init("RoolerApp", 10, new DownloadManagerListener() {
            @Override
            public void OnDownloadStarted(long taskId) {
                Log.d("qcw", "OnDownloadStarted " + taskId);
            }

            @Override
            public void OnDownloadPaused(long taskId) {
                Log.d("qcw", "OnDownloadPaused " + taskId);
            }

            @Override
            public void onDownloadProcess(long taskId, double percent, long downloadedLength) {
                Log.d("qcw", "onDownloadProcess " + taskId);
            }

            @Override
            public void OnDownloadFinished(long taskId) {
                Log.d("qcw", "OnDownloadFinished " + taskId);
            }

            @Override
            public void OnDownloadRebuildStart(long taskId) {
                Log.d("qcw", "OnDownloadRebuildStart " + taskId);
            }

            @Override
            public void OnDownloadRebuildFinished(long taskId) {
                Log.d("qcw", "OnDownloadRebuildFinished " + taskId);
            }

            @Override
            public void OnDownloadCompleted(long taskId) {
                Log.d("qcw", "OnDownloadCompleted " + taskId);
            }

            @Override
            public void connectionLost(long taskId) {
                Log.d("qcw", "connectionLost " + taskId);
            }
        });
    }

    public static synchronized DownloadTool getInstance(Context context) {
        if (mDownloadTool == null) {
            mDownloadTool = new DownloadTool(context);
        }
        return mDownloadTool;
    }

    /**
     * 开始一个下载任务
     *
     * @param url      任务网络地址
     * @param title    任务的title，可为空
     * @param imageUrl 任务的图片地址，可为空
     * @return >0启动下载中，并且返回task id；<0有各种标识
     */
    public int startDownload(String url, String title, String imageUrl) {
        if (TextUtils.isEmpty(url)) {
            return CREATE_DOWNLOAD_TASK_PARAM_INVALID;
        }

        ReportStructure rs = getReportStructure(url);
        if (rs == null) {
            String name = Md5.getMD5(url);
            if (TextUtils.isEmpty(name)) {
                return CREATE_DOWNLOAD_TASK_PARAM_INVALID;
            }

            int taskId = mDmp.addTask(name, url, false, false);
            try {
                mDmp.startDownload(taskId);
                DownloadFileInfoManager.getInstance(mContext).addInfo(url, title, imageUrl);
                return taskId;
            } catch (Exception e) {
                return CREATE_DOWNLOAD_TASK_NEW_BUT_FAILED;
            }

        } else {
            if (rs.getState() == TaskStates.DOWNLOAD_FINISHED) {
                return CREATE_DOWNLOAD_TASK_DOWNLOADED;
            } else if (rs.getState() == TaskStates.DOWNLOADING || rs.getState() == TaskStates.END) {
                return rs.getTaskId();
            } else {
                try {
                    mDmp.startDownload(rs.getTaskId());
                    return rs.getTaskId();
                } catch (Exception e) {
                    return rs.getTaskId();// todo qcw
                }
            }

        }


    }

    /**
     * 下载中的任务状态翻转；
     *
     * @param taskId
     */
    public boolean togglePauseResume(int taskId) {
        try {
            ReportStructure rs = getReportStructure(taskId);
            if (rs.getState() == TaskStates.DOWNLOADING) {
                mDmp.pauseDownload(taskId);
                return true;
            } else if (rs.getState() == TaskStates.PAUSED || rs.getState() == TaskStates.INIT) {
                mDmp.startDownload(taskId);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getTaskId(String url) {
        ReportStructure rs = getReportStructure(url);
        if (rs != null) {
            return rs.getTaskId();
        } else {
            return -1;
        }
    }

    private ReportStructure getReportStructure(String url) {
        String name = Md5.getMD5(url);
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        Task task = mDmp.getTaskFromName(name);
        if (task == null) {
            return null;
        }

        ReportStructure rs = mDmp.singleDownloadStatus(task.id);
        return rs;
    }

    private ReportStructure getReportStructure(int taskId) {
        ReportStructure rs = mDmp.singleDownloadStatus(taskId);
        return rs;
    }

}
