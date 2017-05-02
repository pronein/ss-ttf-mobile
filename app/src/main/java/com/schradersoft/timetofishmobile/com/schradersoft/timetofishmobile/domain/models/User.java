package com.schradersoft.timetofishmobile.com.schradersoft.timetofishmobile.domain.models;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("firstName")
    private String _firstName;
    @SerializedName("lastName")
    private String _lastName;
    @SerializedName("middle")
    private String _middleInitial;
    @SerializedName("registrationDate")
    private Date _registrationDate;
    @SerializedName("username")
    private String _username;
    @SerializedName("email")
    private String _email;

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

    public User(){}
}
