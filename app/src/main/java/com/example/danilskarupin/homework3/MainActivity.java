package com.example.danilskarupin.homework3;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    static final String LOADING_ENDED = "LOADSERVICE_IMAGELOADED";

    static final String imageName = "/image.png";

    ImageView imgView;
    TextView textView;
    BroadcastReceiver receiver, actReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgView = (ImageView) findViewById(R.id.image);
        textView = (TextView) findViewById(R.id.text);

        showImage();

        actReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "Download Requested");
                context.startService(new Intent(context, LoadService.class));
            }
        };

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                showImage();
            }
        };

        registerReceiver(actReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        registerReceiver(receiver, new IntentFilter(LOADING_ENDED));
    }

    public void showImage() {
        if (LoadService.lastLoadWasGood) {
            File f = new File(getFilesDir().getAbsolutePath() + imageName);
            if (f.exists()) {
                imgView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
                imgView.setImageBitmap(BitmapFactory.decodeFile(f.getPath()));
            }
        } else {
            imgView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText(R.string.load_went_wrong);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(actReceiver);
        unregisterReceiver(receiver);
    }

    private final String TAG = "MainActivity";
}
