package com.amazing.android.autopompomme.community.detail;

public class Comment {
    private String userId;
    private String comment;
    private String timestamp;
    private String profileUri;

    public Comment(String userId, String comment, String timestamp, String profileUri) {
        this.userId = userId;
        this.comment = comment;
        this.timestamp = timestamp;
        this.profileUri = profileUri;
    }

    private Comment() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getProfileUri() {
        return profileUri;
    }

    public void setProfileUri(String profileUri) {
        this.profileUri = profileUri;
    }
}