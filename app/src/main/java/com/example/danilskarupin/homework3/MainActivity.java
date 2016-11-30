package com.example.danilskarupin.homework3;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    static final String LOADING_ENDED = "LOADSERVICE_IMAGELOADED";

    static final String imagePath = "/image.png";

    ImageView imgView;
    TextView textView;
    BroadcastReceiver receiver, actReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgView = (ImageView) findViewById(R.id.image);
        textView = (TextView) findViewById(R.id.text);

        actReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                context.startService(new Intent(context, LoadService.class));
            }
        };

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                showImage();
            }
        };

        registerReceiver(actReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        registerReceiver(receiver, new IntentFilter(LOADING_ENDED));
    }

    public void showImage() {
        File f = new File(getFilesDir().getAbsolutePath() + imagePath);
        if (f.exists()) {
            imgView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            imgView.setImageBitmap(BitmapFactory.decodeFile(f.getPath()));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(actReceiver);
        unregisterReceiver(receiver);
    }

}
