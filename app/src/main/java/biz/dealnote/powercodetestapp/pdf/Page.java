package biz.dealnote.powercodetestapp.pdf;

import android.net.Uri;

/**
 * Created by r.kolbasa on 29.12.2017.
 * PowercodeTestApp
 */
public class Page {

    private Uri image;

    private String comment;

    public Uri getImage() {
        return image;
    }

    public Page setImage(Uri image) {
        this.image = image;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public Page setComment(String comment) {
        this.comment = comment;
        return this;
    }
}