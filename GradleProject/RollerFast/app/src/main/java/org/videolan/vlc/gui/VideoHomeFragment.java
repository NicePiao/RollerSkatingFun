/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package org.videolan.vlc.gui;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableLayout;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;
import com.chaowei.com.rollerfast.R;
import org.videolan.android.ui.SherlockGridFragment;
import org.videolan.libvlc.LibVLC;
import org.videolan.vlc.AudioServiceController;

/**
 * Created by qinchaowei on 2015/11/18.
 */
public class VideoHomeFragment extends SherlockFragment {

    private TableLayout mContentTable;


    /* All subclasses of Fragment must include a public empty constructor. */
    public VideoHomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
        getSherlockActivity().getSupportActionBar().setTitle(R.string.history);

        View v = inflater.inflate(R.layout.video_home_layout, container, false);
        mContentTable = (TableLayout) v.findViewById(android.R.id.content_table);
        registerForContextMenu(listView);
        return v;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
      ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.history_view, menu);
    }

    @Override
    public void onListItemClick(ListView l, View v, int p, long id) {
        playListIndex(p);
    }

    private void playListIndex(int position) {
        AudioServiceController audioController = AudioServiceController.getInstance();

        LibVLC.getExistingInstance().setMediaList();
        audioController.playIndex(position);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (!getUserVisibleHint()) {
            return super.onContextItemSelected(item);
        }

        AdapterView.AdapterContextMenuInfo info =
          (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (info == null) // info can be null
        {
            return super.onContextItemSelected(item);
        }
        int id = item.getItemId();

        if (id == R.id.history_view_play) {
            playListIndex(info.position);
            return true;
        } else if (id == R.id.history_view_delete) {
            LibVLC.getExistingInstance().getPrimaryMediaList().remove(info.position);
            mHistoryAdapter.refresh();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    public void refresh() {
        Log.d(TAG, "Refreshing view!");
        if (mHistoryAdapter != null) {
            mHistoryAdapter.refresh();
        }
    }


}
