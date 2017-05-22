package com.schradersoft.timetofishmobile.core.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import com.android.volley.ExecutorDelivery;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.NoCache;
import com.android.volley.toolbox.RequestFuture;
import com.schradersoft.timetofishmobile.core.MultipartRequest;
import com.schradersoft.timetofishmobile.domain.models.Photo;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Adam Schrader on 5/3/2017.
 */

public class UploadPhotoTask extends ApiTask<Photo.Image> {

    private Bitmap _image;

    public UploadPhotoTask(Context context, Bitmap image) {
        super(context);

        _image = image;
    }

    @Override
    protected Photo.Image doInBackground(Void... params) {
        Looper.prepare();

        MultipartRequest req = doWork(_future);
        RequestQueue queue = new RequestQueue(new NoCache(), new BasicNetwork(new HurlStack()),4, new ExecutorDelivery(new Handler(Looper.myLooper())));
        queue.start();
        queue.add(req);

        try {
            return _future.get(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        return null;
    }

    public MultipartRequest doWork(RequestFuture<Photo.Image> future) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        _image.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] payload = byteArrayOutputStream.toByteArray();

        //return _api.PostMultipart("photo/upload", payload, future, future);
        return null;
/*
        try {
            return _future.get(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }*/

        //return null;
    }

/*
    public void onResponse(NetworkResponse response) {
        String photoId = "";

        try {
            String data = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            if (data.length() > 0) {
                JsonParser parser = new JsonParser();
                JsonObject objOther = parser.parse(data).getAsJsonObject();

                photoId = objOther.get("newName").getAsString();
            }
        } catch (UnsupportedEncodingException ex) {
            //TODO: Handle exception here
            ex.printStackTrace();
        } finally {
            setResponse(photoId);
            setTaskComplete();
        }
    }
*/
}
