package com.example.a32150.democamera;

import android.content.Context;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

/**
 * Created by 32150 on 2017/12/1.
 */

public class CameraView extends SurfaceView implements SurfaceHolder.Callback   {

    private Camera mCamera;
    private SurfaceHolder mSurfHolder;
    private Context mContext;

    public CameraView(Context context) {
        super(context);
        mSurfHolder = getHolder();
        mSurfHolder.addCallback(CameraView.this);
        mContext=context;
    }

    public void createCamera()  {
        if(mCamera!=null)   {
            return;
        } else    {
            mCamera = Camera.open();
        }
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Camera getmCamera() {
        return mCamera;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if(mCamera==null)   {
            mCamera = Camera.open();
        }
        refreshCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int height, int width) {
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        stopView();
    }

    public void stopView()   {
        if(mCamera!=null)   {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public void refreshCamera() {

        if (mSurfHolder.getSurface() == null || mCamera == null)
            return;

        try {
            mCamera.setPreviewDisplay(mSurfHolder);

            Camera.CameraInfo camInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(0, camInfo);


            int rotation = ((AppCompatActivity) mContext).getWindowManager().getDefaultDisplay().getRotation();
            System.out.println("rotation = " + rotation); //直立正著拿的時候rotation=0，逆時針轉90度rotation=1，依此類推
            int degrees = 0;
            switch (rotation) {
                case Surface.ROTATION_0:
                    degrees = 0;
                    break;
                case Surface.ROTATION_90:
                    degrees = 90;
                    break;
                case Surface.ROTATION_180:
                    degrees = 180;
                    break;
                case Surface.ROTATION_270:
                    degrees = 270;
                    break;
            }

            int result = (camInfo.orientation - degrees + 360) % 360;
            System.out.println(
                    "result = " + result + " , orientation = " + camInfo.orientation + " , degrees = " + degrees);
            mCamera.setDisplayOrientation(result);
            Camera.Parameters camParas = mCamera.getParameters();
            if (camParas.getFocusMode().equals(Camera.Parameters.FOCUS_MODE_AUTO)
                    || camParas.getFocusMode().equals(Camera.Parameters.FOCUS_MODE_MACRO)) {
                mCamera.autoFocus(onCamAutoFocus);
            } else
                Toast.makeText(getContext(), "照相機不支援自動對焦！", Toast.LENGTH_SHORT).show();

            mCamera.startPreview();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Toast.makeText(getContext(), "照相機啟始錯誤！", Toast.LENGTH_LONG).show();
        }
    }

    Camera.AutoFocusCallback onCamAutoFocus = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean b, Camera camera) {

        }
    };

}
