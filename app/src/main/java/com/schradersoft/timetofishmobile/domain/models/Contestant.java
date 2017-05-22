package com.schradersoft.timetofishmobile.domain.models;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Adam Schrader on 5/7/2017.
 */

public class Contestant {

    private Bitmap _image;

    @Expose
    @SerializedName("username")
    private String _username;
    @Expose
    @SerializedName("score")
    private Number _score;
    @Expose
    @SerializedName("avatarUri")
    private String _avatarUri;

    public Bitmap getProfilePicImage() {return _image;}
    public String getUsername() {return _username;}
    public String getScoreText() {return _score.toString() + " point" + (!_score.equals(1) ? "s" : "");}
    public String getAvatarUri() {return _avatarUri;}

    public void setBitmap(Bitmap bmp) {_image = bmp;}
}
