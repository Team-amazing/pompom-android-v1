package com.amazing.android.autopompomme.profile;

public class MyPamList {
    private String plantBirth;
    private String plantImgUri;
    private String plantNickName;
    private String plantSpecies;
    private String uid;

    public MyPamList() {}
    public String getPlantBirth() {
        return plantBirth;
    }

    public String getPlantImgUri() {
        return plantImgUri;
    }

    public String getPlantNickName() {
        return plantNickName;
    }

    public String getPlantSpecies() {
        return plantSpecies;
    }

    public void setPlantBirth(String plantBirth) {
        this.plantBirth = plantBirth;
    }

    public void setPlantImgUri(String plantImgUri) {
        this.plantImgUri = plantImgUri;
    }

    public void setPlantNickName(String plantNickName) {
        this.plantNickName = plantNickName;
    }

    public void setPlantSpecies(String plantSpecies) {
        this.plantSpecies = plantSpecies;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
