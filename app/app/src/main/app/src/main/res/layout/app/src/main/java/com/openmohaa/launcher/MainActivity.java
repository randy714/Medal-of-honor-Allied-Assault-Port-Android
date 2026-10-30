package com.openmohaa.launcher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;

public class MainActivity extends AppCompatActivity {
    private EditText edtGamePath;
    private Spinner spinnerFps, spinnerRenderer;
    private Button btnLaunch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtGamePath = findViewById(R.id.edtGamePath);
        spinnerFps = findViewById(R.id.spinnerFps);
        spinnerRenderer = findViewById(R.id.spinnerRenderer);
        btnLaunch = findViewById(R.id.btnLaunch);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        edtGamePath.setText(prefs.getString("game_path", "/storage/emulated/0/MOHAA"));

        ArrayAdapter<String> fpsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"30", "60", "90"});
        spinnerFps.setAdapter(fpsAdapter);

        ArrayAdapter<String> rendererAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"OpenGL ES 2.0", "OpenGL ES 3.0"});
        spinnerRenderer.setAdapter(rendererAdapter);

        btnLaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = edtGamePath.getText().toString();
                File pakFile = new File(path + "/main/pak0.pk3");
                if (!pakFile.exists()) {
                    Toast.makeText(MainActivity.this, "Error: File pak0.pk3 tidak ditemukan!", Toast.LENGTH_LONG).show();
                    return;
                }
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("game_path", path);
                editor.apply();

                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("GAME_PATH", path);
                startActivity(intent);
            }
        });
    }
                  }
