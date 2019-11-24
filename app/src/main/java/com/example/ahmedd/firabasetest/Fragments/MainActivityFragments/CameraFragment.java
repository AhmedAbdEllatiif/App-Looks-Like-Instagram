package com.example.ahmedd.firabasetest.Fragments.MainActivityFragments;


import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.camera.core.CameraX;

import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.camera.view.CameraView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ahmedd.firabasetest.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CameraFragment extends Fragment {

    private View view;
            private CameraView cameraView;

    private int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    TextureView textureView;

    public CameraFragment() {
        // Required empty public constructor
    }

    PreviewConfig config;
    Preview preview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.fragment_camera, container, false);
      /*  cameraView = view.findViewById(R.id.cameraView);

        cameraView = PreviewConfig.Builder.*/
        textureView = view.findViewById(R.id.view_finder);

        config = new PreviewConfig.Builder().build();
        preview = new Preview(config);


        if (allPermissionsGranted()){
            preview.setOnPreviewOutputUpdateListener(
                    new Preview.OnPreviewOutputUpdateListener() {
                        @Override
                        public void onUpdated(@NonNull Preview.PreviewOutput previewOutput) {
                            textureView.setSurfaceTexture(previewOutput.getSurfaceTexture());
                            Log.e("PreviewOutput","is here");
                        }
                    });
            CameraX.bindToLifecycle((LifecycleOwner) this, preview);
        }




// The use case is bound to an Android Lifecycle with the following code.



        return view;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if(allPermissionsGranted()){
                //startCamera();
                preview.setOnPreviewOutputUpdateListener(
                        new Preview.OnPreviewOutputUpdateListener() {
                            @Override
                            public void onUpdated(@NonNull Preview.PreviewOutput previewOutput) {
                                textureView.setSurfaceTexture(previewOutput.getSurfaceTexture());
                                Log.e("PreviewOutput","is here");
                            }
                        });
                CameraX.bindToLifecycle((LifecycleOwner) this, preview);
            } else{
                Toast.makeText(getContext(), "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private boolean allPermissionsGranted(){

        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }
}
