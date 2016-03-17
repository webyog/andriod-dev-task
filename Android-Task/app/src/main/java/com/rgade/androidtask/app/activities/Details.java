package com.rgade.androidtask.app.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.rgade.androidtask.app.R;
import com.rgade.androidtask.app.core.DataManager;
import com.rgade.androidtask.app.helpers.Utils;
import com.rgade.androidtask.app.models.MessageFull;
import com.rgade.androidtask.app.models.Participant;

public class Details extends AppCompatActivity {
    public static final String KEY_INDEX = "index";
    public static final String KEY_ID = "id";
    private LinearLayout mContainer;
    private TextView mBody;
    private TextView mSubject;
    private ImageView mStar;
    private MessageFull mMessage;
    private TextView mDate;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        String id = i.getStringExtra(KEY_ID);
        pos = i.getIntExtra(KEY_INDEX, -1);
        setContentView(R.layout.activity_details);
        mContainer = (LinearLayout) findViewById(R.id.details_participant_container);
        mBody = (TextView) findViewById(R.id.details_body);
        mSubject = (TextView) findViewById(R.id.details_subject);
        mStar = (ImageView) findViewById(R.id.details_star);
        mDate = (TextView) findViewById(R.id.details_date);
        DataManager.getInstance(getApplicationContext()).fetchMessage(new DataManager.Callback<MessageFull>() {
            @Override
            public void onCall(MessageFull response) {
                mMessage = response;
                setData();
            }
        }, id);

    }

    private void setData() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (mMessage == null || pos == -1) {
            Toast.makeText(getBaseContext(), "Unable to retreive message", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            mMessage.setStarred(DataManager.getInstance(null).getMessages().get(pos).isStarred());
            for (Participant p : mMessage.getParticipants()) {
                View v = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_participant, mContainer, false);
                ((TextView) v.findViewById(R.id.participant_name)).setText(p.getName());
                ((TextView) v.findViewById(R.id.participant_email)).setText(p.getEmail());
                mContainer.addView(v);
            }
            mStar.setVisibility(View.VISIBLE);
            if (mMessage.isStarred()) {
                mStar.setImageResource(android.R.drawable.btn_star_big_on);
            } else {
                mStar.setImageResource(android.R.drawable.btn_star_big_off);
            }
            mStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageView v = (ImageView) view;
                    if (mMessage.isStarred()) {
                        DataManager.getInstance(null).getMessages().get(pos).setStarred(false);
                        v.setImageResource(android.R.drawable.btn_star_big_off);
                    } else {
                        DataManager.getInstance(null).getMessages().get(pos).setStarred(true);
                        v.setImageResource(android.R.drawable.btn_star_big_on);
                    }
                }
            });
            mDate.setText(Utils.getFullDateTime(mMessage.getTs()));
            mSubject.setText(mMessage.getSubject());
            mBody.setText(mMessage.getBody());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            DataManager.getInstance(null).deleteMessage(new DataManager.Callback<Boolean>() {
                @Override
                public void onCall(Boolean response) {
                    if (response) {
                        DataManager.getInstance(null).getMessages().remove(pos);
                        finish();
                    } else {
                        Toast.makeText(getBaseContext(), "Unable to delete message", Toast.LENGTH_SHORT).show();
                    }
                }
            }, mMessage.getId());
            return true;
        } else if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
