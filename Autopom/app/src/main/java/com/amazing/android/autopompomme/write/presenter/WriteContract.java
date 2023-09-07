package com.amazing.android.autopompomme.write.presenter;

public interface WriteContract {
    interface View{

    }

    interface Presenter{
        void write(String title, String detail);
    }
}
