package com.amazing.android.autopompomme.api;

public class MemberInfo {
    private String nickname;
    private String userUri;

    public MemberInfo(String nickname,String userUri){
        this.nickname = nickname;
        this.userUri = userUri;
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
}
