package com.example.abhinav.studentfirebase;

import android.net.Uri;

/**
 * Created by ABHINAV on 23-10-2016.
 */

public class Images {
    Uri url;

    public Images(Uri url) {
        this.url = url;
    }

    public Uri getUrl() {
        return url;
    }

    public void setUrl(Uri url) {
        this.url = url;
    }

    public Images() {

    }
}
