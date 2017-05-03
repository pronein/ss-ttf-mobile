package com.schradersoft.timetofishmobile.com.schradersoft.timetofishmobile.domain.models;

import android.graphics.Bitmap;

import java.util.Date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @Expose
    @SerializedName("firstName")
    private String _firstName;
    @Expose
    @SerializedName("lastName")
    private String _lastName;
    @Expose
    @SerializedName("middle")
    private String _middleInitial;
    @Expose
    @SerializedName("registrationDate")
    private Date _registrationDate;
    @Expose
    @SerializedName("username")
    private String _username;
    @Expose
    @SerializedName("email")
    private String _email;
    @Expose
    @SerializedName("password")
    private String _password;

    private Bitmap _avatar;

    public String get_firstName() {
        return _firstName;
    }

    public void set_firstName(String _firstName) {
        this._firstName = _firstName;
    }

    public String get_lastName() {
        return _lastName;
    }

    public void set_lastName(String _lastName) {
        this._lastName = _lastName;
    }

    public String get_middleInitial() {
        return _middleInitial;
    }

    public void set_middleInitial(String _middleInitial) {
        this._middleInitial = _middleInitial;
    }

    public Date get_registrationDate() {
        return _registrationDate;
    }

    public void set_registrationDate(Date _registrationDate) {
        this._registrationDate = _registrationDate;
    }

    public String get_username() {
        return _username;
    }

    public void set_username(String _username) {
        this._username = _username;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public void set_password(String password) {_password = password;}
    public String get_password() {return _password;}

    public void set_avatar(Bitmap image) {_avatar = image;}
    public Bitmap get_avatar() {return _avatar;}

    public User(){}
}
