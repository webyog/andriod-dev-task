package com.rgade.androidtask.app.models;


import com.rgade.androidtask.app.helpers.Utils;

public class Message {
    private String id;
    private String subject;
    private String[] participants;
    private String preview;
    private boolean isRead;
    private boolean isStarred;
    private long ts;
    private String dateString;
    private String participantsString;

    public void setStarred(boolean starred) {
        isStarred = starred;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String[] getParticipants() {
        return participants;
    }

    public String getPreview() {
        return preview;
    }

    public boolean isRead() {
        return isRead;
    }

    public boolean isStarred() {
        return isStarred;
    }

    public long getTs() {
        return ts;
    }

    public String getDateString() {
        return dateString;
    }

    public String getParticipantsString() {
        return participantsString;
    }

    public void process() {
        dateString = Utils.getDate(ts);
        preview = preview.replace("\n", " ");
        StringBuilder sb = new StringBuilder();
        sb.append(participants[0]);
        int i = 1;
        while (i < participants.length) {
            sb.append(", ").append(participants[i]);
            i++;
        }
        participantsString = sb.toString();
    }
}
