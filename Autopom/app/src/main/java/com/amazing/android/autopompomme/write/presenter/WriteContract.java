package com.amazing.android.autopompomme.write.presenter;

import android.net.Uri;

import java.util.List;

public interface WriteContract {
    interface View{

    }

    interface Presenter{
        void write(String profileName, Uri profileUri, String date,String title, String detail, List<Uri> imgUris);
    }
}
