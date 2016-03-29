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

    public static final int FILE_STATE_DOWNLOADING = 0;
    public static final int FILE_STATE_PUASED = 1;
    public static final int FILE_STATE_FINISHED = 2;
    public static final int FILE_STATE_INVALID = 3;


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
     * 删除文件：
     * 1 停止下载线程
     * 2 删除下载数据库Task内容
     * 3 删除下载的文件
     * 4 删除下载文件的Sp数据
     *
     * @param url 下载文件地址
     */
    public void delete(String url) {
        ReportStructure reportStructure = getReportStructure(url);
        if (reportStructure == null) {
            return;
        }
        mDmp.delete(reportStructure.getTaskId(),true);
    }

    public boolean startDownload(String url) {
        int taskId = getTaskId(url);
        if (taskId != -1) {
            try {
                mDmp.startDownload(taskId);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public boolean pauseDownload(String url) {
        int taskId = getTaskId(url);
        if (taskId != -1) {
            try {
                mDmp.pauseDownload(taskId);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * 获取文件的下载状态
     *
     * @param url 文件地址
     * @return FILE_STATE_**
     */
    public int getFileState(String url) {
        if (TextUtils.isEmpty(url)) {
            return FILE_STATE_INVALID;
        }

        ReportStructure structure = getReportStructure(url);
        if (structure == null) {
            createTask(url);
            structure = getReportStructure(url);
            if (structure == null) {
                return FILE_STATE_INVALID;
            }
        }
        switch (structure.getState()) {
            case TaskStates.INIT:
            case TaskStates.READY:
            case TaskStates.PAUSED:
                return FILE_STATE_PUASED;
            case TaskStates.DOWNLOADING:
                return FILE_STATE_DOWNLOADING;
            case TaskStates.DOWNLOAD_FINISHED:
            case TaskStates.END:
                return FILE_STATE_FINISHED;
            default:
                return FILE_STATE_INVALID;
        }
    }

    private int createTask(String url) {
        String name = createNameFromUrl(url);
        if (TextUtils.isEmpty(name)) {
            return -1;
        }
        // 覆盖
        return mDmp.addTask(name, url, true, false);
    }

    private String createNameFromUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }

        String name = Md5.getMD5(url);
        int lastDot = url.lastIndexOf('.');
        if (lastDot != -1) {
            name += url.substring(lastDot, url.length());
        }
        return name;
    }

    private int getTaskId(String url) {
        ReportStructure rs = getReportStructure(url);
        if (rs != null) {
            return rs.getTaskId();
        } else {
            return -1;
        }
    }

    private ReportStructure getReportStructure(String url) {
        String name = createNameFromUrl(url);
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        Task task = mDmp.getTaskFromName(name);
        if (task == null) {
            return null;
        }

        return getReportStructure(task.id);
    }

    private ReportStructure getReportStructure(int taskId) {
        return mDmp.singleDownloadStatus(taskId);
    }

}
