package com.rgade.androidtask.app.activities;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.rgade.androidtask.app.R;
import com.rgade.androidtask.app.adapters.MessageAdapter;
import com.rgade.androidtask.app.core.DataManager;
import com.rgade.androidtask.app.models.Message;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private DataManager mDataManager;
    private RecyclerView mRecyclerView;
    private MessageAdapter mAdapter;
    private SwipeRefreshLayout mRefresher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDataManager = DataManager.getInstance(getApplicationContext());
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRefresher = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mRefresher.setOnRefreshListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MessageAdapter(getBaseContext());
        mRecyclerView.setAdapter(mAdapter);
        ItemTouchHelper.SimpleCallback callback = new MessageSwipeCallback(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);
        pullData();
    }

    private void pullData() {
        mRefresher.setRefreshing(true);
        mDataManager.fetchMessages(new DataManager.Callback<List<Message>>() {
            @Override
            public void onCall(List<Message> response) {
                mRefresher.setRefreshing(false);
                if (response == null) {
                    Toast.makeText(getBaseContext(), "Unable to fetch messages", Toast.LENGTH_SHORT).show();
                } else {
                    mAdapter.updateData(response);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.updateData(DataManager.getInstance(null).getMessages());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        pullData();
    }

    private class MessageSwipeCallback extends ItemTouchHelper.SimpleCallback {
        private MessageAdapter mAdapter;

        public MessageSwipeCallback(MessageAdapter adapter) {
            super(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.LEFT);
            mAdapter = adapter;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            mAdapter.delete(viewHolder.getAdapterPosition());
        }

    }
}
