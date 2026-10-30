package com.openmohaa.launcher;
import android.app.NativeActivity;
import android.os.Bundle;
public class GameActivity extends NativeActivity {
    static { System.loadLibrary("openmohaa"); }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}

