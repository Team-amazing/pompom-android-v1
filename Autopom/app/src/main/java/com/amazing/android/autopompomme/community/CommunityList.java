package com.amazing.android.autopompomme.community;

import java.util.List;

public class CommunityList {
    private String postId;
    private String profileName;
    private String profileUri;
    private String date;
    private String title;
    private List<String> postUri;
    private String content;
    private int likeNum;
    private int commentNum;

    public CommunityList() {
    }

    public CommunityList(String postId, String profileName, String profileUri, String date, String title, List<String> postUri, String content) {
        this.postId = postId;
        this.profileName = profileName;
        this.profileUri = profileUri;
        this.date = date;
        this.title = title;
        this.postUri = postUri;
        this.content = content;
    }

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

    public String getProfileUri() {
        return profileUri;
    }

    public void setProfileUri(String profile) {
        this.profileUri = profile;
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
