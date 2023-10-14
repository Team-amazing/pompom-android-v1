package com.amazing.android.autopompomme.ranking;

public class RankingData {
    private String nickName;
    private int score;

    public RankingData() {}

    public RankingData(String nickName, int score) {
        this.nickName = nickName;
        this.score = score;
    }

    public String getNickName() {
        return nickName;
    }

    public int getScore() {
        return score;
    }
}
