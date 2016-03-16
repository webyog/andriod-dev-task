package com.rgade.androidtask.app.models;


public class Message {
    private String id;
    private String subject;
    private String[] participants;
    private String preview;
    private boolean isRead;
    private boolean isStarred;
    private long ts;

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
}
