package com.example.danilskarupin.homework3;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

/**
 * Created by danilskarupin on 30.11.16.
 */

public class LoadService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
            final String url = "http://www.1exotic.ru/images/exotic/wh3.jpg";
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        Log.d(TAG, "DownloadStarted");
                        URL fileURL = new URL(url);
                        Bitmap bmp = BitmapFactory.decodeStream(fileURL.openConnection().getInputStream());
                        File f = new File(getFilesDir().getAbsolutePath() + MainActivity.imagePath);
                        FileOutputStream fout = new FileOutputStream(f);
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, fout);
                        fout.close();
                        sendBroadcast(new Intent(MainActivity.LOADING_ENDED));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return null;
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            return START_STICKY;
        }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private final String TAG = "LoadService";
}
