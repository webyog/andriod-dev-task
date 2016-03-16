package com.rgade.androidtask.app.models;


public class MessageFull {
    private String id;
    private String subject;
    private Participant[] participants;
    private String body;
    private boolean isRead;
    private boolean isStarred;
    private long ts;

    public String getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public Participant[] getParticipants() {
        return participants;
    }

    public String getBody() {
        return body;
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
