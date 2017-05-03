package com.schradersoft.timetofishmobile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.schradersoft.timetofishmobile.com.schradersoft.timetofishmobile.domain.models.User;
import com.schradersoft.timetofishmobile.core.Api;
import com.schradersoft.timetofishmobile.core.IJsonResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class RegisterActivity extends AppCompatActivity implements IJsonResponseHandler {

    EditText _firstName;
    EditText _middleInitial;
    EditText _lastName;
    EditText _username;
    EditText _email;
    EditText _password;
    EditText _confirmPassword;
    ImageView _avatarPreview;
    ImageButton _getPhoto;
    ImageButton _takePhoto;
    Button _registerUser;

    View _form;
    ProgressBar _progress;

    int GET_PHOTO = 1;
    int TAKE_PHOTO = 2;

    Bitmap _avatarBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        _firstName = (EditText) findViewById(R.id.register_form_first_name);
        _middleInitial = (EditText) findViewById(R.id.register_form_middle_initial);
        _lastName = (EditText) findViewById(R.id.register_form_last_name);
        _username = (EditText) findViewById(R.id.register_form_username);
        _email = (EditText) findViewById(R.id.register_form_email);
        _password = (EditText) findViewById(R.id.register_form_password);
        _confirmPassword = (EditText) findViewById(R.id.register_form_confirm_password);
        _avatarPreview = (ImageView) findViewById(R.id.register_form_avatar_preview);
        _getPhoto = (ImageButton) findViewById(R.id.register_form_get_photo_btn);
        _takePhoto = (ImageButton) findViewById(R.id.register_form_take_photo_btn);
        _registerUser = (Button) findViewById(R.id.register_form_register_user_btn);

        _form = findViewById(R.id.registration_form);
        _progress = (ProgressBar) findViewById(R.id.registration_progress);

        _getPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, GET_PHOTO);
            }
        });

        _takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null)
                    startActivityForResult(intent, TAKE_PHOTO);
            }
        });

        _registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttemptRegister();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GET_PHOTO) {
                if (data == null) {
                    //handle error
                    return;
                }

                try {
                    InputStream stream = getApplicationContext().getContentResolver().openInputStream(data.getData());
                    if (stream == null) {
                        //handle error
                        return;
                    }
                    BufferedInputStream buf = new BufferedInputStream(stream);
                    _avatarBitmap = BitmapFactory.decodeStream(buf);
                    _avatarPreview.setBackground(null);
                    _avatarPreview.setImageBitmap(_avatarBitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else if (requestCode == TAKE_PHOTO) {
                Bundle extras = data.getExtras();
                _avatarBitmap = (Bitmap) extras.get("data");
                _avatarPreview.setBackground(null);
                _avatarPreview.setImageBitmap(_avatarBitmap);
            }
        }
    }

    public void AttemptRegister() {
        _firstName.setError(null);
        _lastName.setError(null);
        _username.setError(null);
        _email.setError(null);
        _password.setError(null);
        _confirmPassword.setError(null);

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(_confirmPassword.getText().toString())) {
            _confirmPassword.setError(getString(R.string.error_field_required));
            focusView = _confirmPassword;
            cancel = true;
        } else if (!_confirmPassword.getText().toString().equals(_password.getText().toString())) {
            _confirmPassword.setError(getString(R.string.error_passwords_must_match));
            focusView = _confirmPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(_password.getText().toString())) {
            _password.setError(getString(R.string.error_field_required));
            focusView = _password;
            cancel = true;
        }

        if (TextUtils.isEmpty(_email.getText().toString())) {
            _email.setError(getString(R.string.error_field_required));
            focusView = _email;
            cancel = true;
        }

        if (TextUtils.isEmpty(_username.getText().toString())) {
            _username.setError(getString(R.string.error_field_required));
            focusView = _username;
            cancel = true;
        } else if (IsUsernameTaken(_username.getText().toString())) {
            _username.setError(getString(R.string.error_username_unavailable));
            focusView = _username;
            cancel = true;
        }

        if (TextUtils.isEmpty(_lastName.getText().toString())) {
            _lastName.setError(getString(R.string.error_field_required));
            focusView = _lastName;
            cancel = true;
        }

        if (TextUtils.isEmpty(_firstName.getText().toString())) {
            _firstName.setError(getString(R.string.error_field_required));
            focusView = _firstName;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);

            User requestedUser = BuildUserFromForm();

            try {
                Api.getInstance(this).Post("user", this, requestedUser);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        _form.setVisibility(show ? View.GONE : View.VISIBLE);
        _form.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                _form.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        _progress.setVisibility(show ? View.VISIBLE : View.GONE);
        _progress.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                _progress.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    private boolean IsUsernameTaken(String username) {
        //TODO: Implement me
        return false;
    }

    private User BuildUserFromForm() {
        User user = new User();

        user.set_firstName(_firstName.getText().toString());
        user.set_middleInitial(_middleInitial.getText().toString());
        user.set_lastName(_lastName.getText().toString());
        user.set_username(_username.getText().toString());
        user.set_email(_email.getText().toString());
        user.set_password(_password.getText().toString());
        user.set_avatar(_avatarBitmap);

        return user;
    }

    @Override
    public void HandleResponse(JSONObject response) {
        showProgress(false);

        try {
            String newUserId = response.getString("id");

            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("username", _username.getText().toString());
            intent.putExtra("password", _password.getText().toString());

            startActivity(intent);

            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void HandleException(VolleyError error) {
        showProgress(false);
    }
}
