package com.amazing.android.autopompomme.write.presenter;

import android.net.Uri;

public interface WriteContract {
    interface View{

    }

    interface Presenter{
        void write(String title, String detail, Uri uri);
    }
}
