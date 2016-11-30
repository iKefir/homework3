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
import java.util.Random;

/**
 * Created by danilskarupin on 30.11.16.
 */

public class LoadService extends Service {

    Boolean isLoading = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (isLoading) return START_STICKY;
        isLoading = true;
        final String urls[] = {
                "http://www.1exotic.ru/images/exotic/wh3.jpg",
                "http://img0.liveinternet.ru/images/attach/c/8/125/779/125779280_priroda_1.jpg",
                "http://img1.liveinternet.ru/images/attach/c/4/81/244/81244613_1_krasivaya_zima.jpg",
                "http://www.climbing.ru/media/pic_full/0/2227.jpg"
        };
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Log.d(TAG, "Download Started");
                    URL fileURL = new URL(urls[new Random(System.currentTimeMillis()).nextInt(4)]);
                    Bitmap bmp = BitmapFactory.decodeStream(fileURL.openConnection().getInputStream());
                    File f = new File(getFilesDir().getAbsolutePath() + MainActivity.imagePath);
                    FileOutputStream fout = new FileOutputStream(f);
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, fout);
                    fout.close();
                    Log.d(TAG, "Download Ended");
                    sendBroadcast(new Intent(MainActivity.LOADING_ENDED));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isLoading = false;
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
