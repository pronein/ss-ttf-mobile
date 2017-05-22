package com.schradersoft.timetofishmobile.domain.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.schradersoft.timetofishmobile.core.Api;
import com.schradersoft.timetofishmobile.core.IJsonResponseHandler;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

/**
 * Created by adamd on 5/3/2017.
 */

public class Photo {
    @Expose
    private String _id;
    @Expose
    @SerializedName("caption")
    private String _caption;
    @Expose
    @SerializedName("date")
    private Date _date;
    @Expose
    @SerializedName("name")
    private String _name;
    @Expose
    @SerializedName("uploadedOn")
    private Date _uploadedOn;
    @Expose
    @SerializedName("uploadedBy")
    private User _uploadedBy;
    @Expose
    @SerializedName("uploaded.latitude")
    private Number _uploadedLatitude;
    @Expose
    @SerializedName("uploaded.logitude")
    private Number _uploadedLongitude;
    @Expose
    @SerializedName("image")
    private Image _image;

    public Photo() {
    }

    public Photo(String id) {
        _id = id;
    }

    public Photo(Image image) {
        _image = image;
    }

    public String getId() {
        return _id;
    }

    public Image getImage() {
        return _image;
    }

    public void setName(String name) {
        _name = name;
    }

    public void setCaption(String caption) {
        _caption = caption;
    }

    public void setDate(Date date) {
        _date = date;
    }

    public void setUploadedOn(Date date) {
        _uploadedOn = date;
    }

    public void setLatitude(Number latitude) {
        _uploadedLatitude = latitude;
    }

    public void setLongitude(Number longitude) {
        _uploadedLongitude = longitude;
    }

    public void setImage(Image image) {
        _image = image;
    }

    public void setUploadedBy(User user) {
        _uploadedBy = user;
    }

    public void setUploadedBy(String id) {
        _uploadedBy = new User(id);
    }

    public class Image {
        @Expose
        @SerializedName("originalName")
        private String _originalName;
        @Expose
        @SerializedName("generatedName")
        private String _generatedName;
        @Expose
        @SerializedName("uri")
        private String _uri;

        private Bitmap _bmp;

        public Image(Bitmap bmp, String original, String generated, String uri) {
            _originalName = original;
            _bmp = bmp;
            _generatedName = generated;
            _uri = uri;
        }

        public Bitmap getBitmap() {
            return _bmp;
        }

        public String getOriginalName() {
            return _originalName;
        }

        public String getGeneratedName() {
            return _generatedName;
        }

        public String getUri() {
            return _uri;
        }
    }

    public static class PhotoDeserializer implements JsonDeserializer<Photo> {
        @Override
        public Photo deserialize(final JsonElement json,
                                 final Type typeOfT,
                                 final JsonDeserializationContext context) throws JsonParseException {

            if (json.isJsonObject()) {
                try {
                    final JsonObject jsonObject = json.getAsJsonObject();
                    final String id = jsonObject.get("_id").getAsString();
                    final String caption = jsonObject.get("caption").getAsString(); //TODO: Comes back as null if not exists
                    final String dateString = jsonObject.get("date").getAsString();
                    final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
                    final Date date = dateFormatter.parse(dateString);
                    final String name = jsonObject.get("name").getAsString();
                    final String uploadedOnString = jsonObject.get("uploadedOn").getAsString();
                    final Date uploadedOn = dateFormatter.parse(uploadedOnString);
                    final JsonObject location = jsonObject.getAsJsonObject("uploadLocation");
                    final Number latitude = location != null ? location.get("latitude").getAsNumber() : null;
                    final Number longitude = location != null ? location.get("longitude").getAsNumber() : null;
                    final Image image = context.deserialize(jsonObject.get("image"), Image.class);
                    final User user = context.deserialize(jsonObject.get("uploadedBy"), User.class);

                    final Photo photo = new Photo(id);
                    photo.setCaption(caption);
                    photo.setDate(date);
                    photo.setName(name);
                    photo.setUploadedOn(uploadedOn);
                    photo.setLatitude(latitude);
                    photo.setLongitude(longitude);
                    photo.setImage(image);
                    photo.setUploadedBy(user);

                    return photo;
                } catch (ParseException e) {
                    e.printStackTrace();
                    return new Photo();
                }
            } else {
                return new Photo(json.getAsString());
            }
        }
    }

    public static class PhotoDownloader extends AsyncTask<Hashtable<String, Bitmap>, Void, Bitmap> {
        private String _url;
        private ImageView _view;
        private Api _api;

        public PhotoDownloader(String url, ImageView view, Context context) {
            _url = url;
            _view = view;
            _api = Api.getInstance(context);
        }

        @Override
        protected Bitmap doInBackground(Hashtable<String, Bitmap>... params) {
            try {
                URL url = new URL(_api.getBaseUri() + _url);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                if (params.length > 0 && params[0] != null)
                    params[0].put(_url, bmp);
                return bmp;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public void onPostExecute(Bitmap result) {
            if (result != null)
                _view.setImageBitmap(result);
        }
    }
}
