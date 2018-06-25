package com.wuyson.blurbyrenderscript;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private ImageView mImgOriginal;
    private Button btnBlur;
    private Handler mHandler;
    private RenderScriptUtils mBlurRs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bitmap bitmap = (Bitmap) msg.obj;
                mImgOriginal.setImageBitmap(bitmap);
            }
        };

        mImgOriginal = findViewById(R.id.img_original);
        btnBlur = findViewById(R.id.btn_blur);

//        Bitmap bitmapIV = ((BitmapDrawable) mImgOriginal.getDrawable()).getBitmap();
        final Bitmap bitmap = BitmapUtils.decodeSampleBitmapFromResource(getResources(),R.drawable.app_img_jessica,
                50,50);

        mBlurRs = new RenderScriptUtils(this);


        btnBlur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap blur = mBlurRs.blur(bitmap, 25f);

                        Message message = new Message();
                        message.what = 1;
                        message.obj = bitmap;
                        mHandler.sendMessage(message);
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBlurRs.destory();
    }
}
