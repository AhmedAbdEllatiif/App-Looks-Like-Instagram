package com.example.ahmedd.firabasetest;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.camera.view.CameraView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.TextureView;
import android.widget.Toast;

public class TestActivity extends AppCompatActivity {


    PreviewConfig config;
    Preview preview;
    TextureView textureView;

    private CameraView cameraView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

       // cameraView = findViewById(R.id.cameraView);
        config = new PreviewConfig.Builder().build();
        preview = new Preview(config);
        textureView = findViewById(R.id.view_finder);

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }else {
            //cameraView.bindToLifecycle(this);
            preview.setOnPreviewOutputUpdateListener(
                    new Preview.OnPreviewOutputUpdateListener() {
                        @Override
                        public void onUpdated(@NonNull Preview.PreviewOutput previewOutput) {
                            // Your code here. For example, use previewOutput.getSurfaceTexture()
                            // and post to a GL renderer.
                            textureView.setSurfaceTexture(previewOutput.getSurfaceTexture());
                            Log.e("onUpdated","Here");
                        };
                    });
            CameraX.bindToLifecycle((LifecycleOwner) this, preview);
        }
/*



        CameraX.bindToLifecycle((LifecycleOwner) this, preview);*/

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                cameraView.bindToLifecycle(this);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }


    private static final int MY_CAMERA_REQUEST_CODE = 100;



    public static boolean hasAllPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
