package com.amazing.android.autopompomme.profile;

import android.net.Uri;

import java.util.List;

public class MyUploadList {
    private String postId;
    private String profileName;
    private Uri profileUri;
    private String date;
    private String title;
    private List<String> postUri;
    private String content;
    private int likeNum;
    private int commentNum;

    public MyUploadList() {}

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public Uri getProfile() {
        return profileUri;
    }

    public void setProfile(Uri profile) {
        this.profileUri = profileUri;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getPostUri() {
        return postUri;
    }

    public void setPostUri(List<String> postUri) {
        this.postUri = postUri;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }
}
