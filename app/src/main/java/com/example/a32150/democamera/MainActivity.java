package com.example.a32150.democamera;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    Camera mCamera;
    CameraView mCameraView;
    FrameLayout mFrameLayout;
    LinearLayout mCameraLayout;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        mContext = this;

        mFrameLayout=(FrameLayout)getLayoutInflater().inflate(R.layout.activity_main, null);
        mCameraLayout = mFrameLayout.findViewById(R.id.cameralayout);
        mCameraView = new CameraView(mContext);
        mCameraLayout.addView(mCameraView);

        setContentView(mFrameLayout);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mCameraView != null) {
            mCameraView.stopView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCamera = mCameraView.getmCamera();
        if(mCamera == null) {
            mCameraView.createCamera();
        }
    }

    public void onTakePhoto(View v)   {
        mCamera = mCameraView.getmCamera();
        mCamera.takePicture(camShutterCallback,camRawDataCallback,camJpegCallback);
    }

    public void onShowPhoto(View v)   {
        Intent it = new Intent(Intent.ACTION_VIEW);
        File file = new File("/sdcard/picture.jpg");
        it.setDataAndType(Uri.fromFile(file), "image/*");
        startActivity(it);
    }

    private Camera.ShutterCallback camShutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            System.out.println("onShutter()");
            // 通知使用者已完成拍照,例如發出一個模擬快門的聲音。
        }
    };

    private Camera.PictureCallback camRawDataCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            System.out.println("camRawDataCallback");
            // 接收原始的影像資料。
        }
    };

    private Camera.PictureCallback camJpegCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            System.out.println("camJpegCallback");
            // 接收壓縮成jpeg格式的影像資料。
            FileOutputStream outStream = null;
            try {
                //date = getDate();
                outStream = new FileOutputStream("/sdcard/picture.jpg");
                outStream.write(data);
                outStream.close();
            } catch (IOException e) {
                Toast.makeText(MainActivity.this, "影像檔儲存錯誤！", Toast.LENGTH_SHORT).show();
            }

            if (mCamera != null)
                mCamera.startPreview();
        }
    };
}
