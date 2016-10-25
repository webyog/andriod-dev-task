package com.rgade.androidtask.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.rgade.androidtask.app.R;
import com.rgade.androidtask.app.activities.Details;
import com.rgade.androidtask.app.core.DataManager;
import com.rgade.androidtask.app.models.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> mList;
    private Context mContext;

    public MessageAdapter(Context ctx) {
        mContext = ctx;
        mList = new ArrayList<Message>();
    }

    public void updateData(List<Message> list) {
        if (list != null) {
            this.mList.clear();
            this.mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, null);
        MessageViewHolder gvh = new MessageViewHolder(view);
        return gvh;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        holder.bindHolder(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public Message delete(final int pos) {
        Message message=mList.get(pos);
        mList.remove(pos);
        DataManager.getInstance(null).getMessages().remove(pos);
        notifyItemRemoved(pos);
        return message;
    }

    public boolean add(Message message, int pos){
        if(pos>mList.size())
            return false;
        mList.add(pos,message);
        DataManager.getInstance(null).getMessages().add(pos,message);
        notifyItemInserted(pos);
        return true;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private ImageView star;
        private Message mMessage;
        private TextView subject;
        private TextView participants;
        private TextView time;
        private TextView preview;


        public MessageViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.item);
            star = (ImageView) itemView.findViewById(R.id.item_star);
            subject = (TextView) itemView.findViewById(R.id.item_subject);
            participants = (TextView) itemView.findViewById(R.id.item_participants);
            time = (TextView) itemView.findViewById(R.id.item_time);
            preview = (TextView) itemView.findViewById(R.id.item_preview);
            view.setOnClickListener(new ViewClickListener());
            star.setOnClickListener(new StarClickListener());
        }

        public void bindHolder(final int pos) {
            mMessage = mList.get(pos);
            subject.setText(mMessage.getSubject());
            shortlist(mMessage.isStarred(), star);
            setRead(mMessage.isRead(), view);
            time.setText(mMessage.getDateString());
            participants.setText(mMessage.getParticipantsString());
            preview.setText(mMessage.getPreview());
        }

        public void shortlist(boolean shortlist, ImageView view) {
            int pos=getAdapterPosition();
            if (shortlist) {
                mList.get(pos).setStarred(true);
                DataManager.getInstance(null).getMessages().get(pos).setStarred(true);
                view.setImageResource(android.R.drawable.btn_star_big_on);
            } else {
                mList.get(pos).setStarred(false);
                DataManager.getInstance(null).getMessages().get(pos).setStarred(false);
                view.setImageResource(android.R.drawable.btn_star_big_off);
            }
            //TODO put on request Queue
        }

        public void setRead(boolean isRead, View view) {
            int pos=getAdapterPosition();
            if (isRead) {
                view.setSelected(true);
                mList.get(pos).setRead(true);
                DataManager.getInstance(null).getMessages().get(pos).setRead(true);
            } else {
                view.setSelected(false);
            }
            //TODO put on requestQueue
        }

        private class StarClickListener implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                ImageView view = (ImageView) v;
                shortlist(!mMessage.isStarred(), view);
            }
        }

        private class ViewClickListener implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                setRead(true, v);
                Intent i = new Intent(v.getContext(), Details.class);
                i.putExtra(Details.KEY_INDEX, getAdapterPosition());
                i.putExtra(Details.KEY_ID,mList.get(getAdapterPosition()).getId());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(i);
            }
        }

    }

}
