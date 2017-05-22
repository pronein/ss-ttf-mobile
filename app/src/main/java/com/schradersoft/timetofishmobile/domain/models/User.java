package com.schradersoft.timetofishmobile.domain.models;

import android.graphics.Bitmap;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @Expose
    private String _id;
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

    @Expose
    @SerializedName("avatar")
    private Photo _avatar;

    private Bitmap _avatarImage;

    public String getFirstName() {
        return _firstName;
    }

    public void setFirstName(String firstName) {
        _firstName = firstName;
    }

    public String getLastName() {
        return _lastName;
    }

    public void setLastName(String lastName) {
        _lastName = lastName;
    }

    public String getMiddleInitial() {
        return _middleInitial;
    }

    public void setMiddleInitial(String middleInitial) {
        _middleInitial = middleInitial;
    }

    public Date getRegistrationDate() {
        return _registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        _registrationDate = registrationDate;
    }

    public String getUsername() {
        return _username;
    }

    public void setUsername(String username) {
        _username = username;
    }

    public String getEmail() {
        return _email;
    }

    public void setEmail(String email) {
        _email = email;
    }

    public void setPassword(String password) {
        _password = password;
    }

    public String getPassword() {
        return _password;
    }

    public void setAvatar(Photo avatar) {
        _avatar = avatar;
    }

    public Photo getAvatar() {
        return _avatar;
    }

    public void setAvatarImage(Bitmap avatarImage) {
        _avatarImage = avatarImage;
    }

    public Bitmap getAvatarImage() {
        return _avatarImage;
    }

    public void setId(String id) {_id = id;}
    public User() {
    }

    public User(String id) {
        _id = id;
    }

    public static class UserDeserializer implements JsonDeserializer<User> {
        @Override
        public User deserialize(final JsonElement json,
                                final Type typeOfT,
                                final JsonDeserializationContext context) throws JsonParseException {

            if (json.isJsonObject()) {
                try {
                    final JsonObject jsonObject = json.getAsJsonObject();
                    final String id = jsonObject.get("_id").getAsString();
                    final String firstName = jsonObject.get("firstName").getAsString();
                    final String lastName = jsonObject.get("lastName").getAsString();
                    final String username = jsonObject.get("username").getAsString();
                    final Photo avatar = context.deserialize(jsonObject.get("avatar"), Photo.class);
                    final String email = jsonObject.get("email").getAsString();
                    final String registrationDateString = jsonObject.get("registrationDate").getAsString();
                    final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
                    final Date registrationDate = dateFormatter.parse(registrationDateString);

                    final User user = new User(id);

                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setUsername(username);
                    user.setAvatar(avatar);
                    user.setEmail(email);
                    user.setRegistrationDate(registrationDate);

                    return user;
                } catch (ParseException e) {
                    e.printStackTrace();
                    return new User();
                }
            } else {
                return new User(json.getAsString());
            }
        }
    }
}
