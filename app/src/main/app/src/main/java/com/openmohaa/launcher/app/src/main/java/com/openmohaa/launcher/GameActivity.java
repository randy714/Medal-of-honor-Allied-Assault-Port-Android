package com.openmohaa.launcher;

import android.app.NativeActivity;
import android.os.Bundle;
import android.util.Log;

public class GameActivity extends NativeActivity {

    static {
        System.loadLibrary("openmohaa");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        String gamePath = getIntent().getStringExtra("GAME_PATH");
        String targetFps = getIntent().getStringExtra("TARGET_FPS");

        Log.d("OpenMoHaa", "Memuat Game dari Path: " + gamePath);
        Log.d("OpenMoHaa", "Target FPS: " + targetFps);
        
        nativeSetGameParameters(gamePath, targetFps);
    }

    private native void nativeSetGameParameters(String path, String fps);
}
