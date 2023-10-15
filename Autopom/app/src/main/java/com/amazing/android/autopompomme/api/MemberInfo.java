package com.amazing.android.autopompomme.api;

public class MemberInfo {
    private String nickname;
    private String userUri;
    private int score;

    public MemberInfo() {}

    public MemberInfo(String nickname,String userUri,int score){
        this.nickname = nickname;
        this.userUri = userUri;
        this.score = score;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public String getUserUri() {
        return userUri;
    }

    public void setUserUri(String userUri) {
        this.userUri = userUri;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
