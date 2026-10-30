package com.openmohaa.launcher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
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
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText edtGamePath;
    private Spinner spinnerFps, spinnerLang, spinnerRenderer;
    private Button btnLaunch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        loadSavedLanguage();
        setContentView(R.layout.activity_main);

        edtGamePath = findViewById(R.id.edtGamePath);
        spinnerFps = findViewById(R.id.spinnerFps);
        spinnerLang = findViewById(R.id.spinnerLang);
        spinnerRenderer = findViewById(R.id.spinnerRenderer);
        btnLaunch = findViewById(R.id.btnLaunch);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        edtGamePath.setText(prefs.getString("game_path", "/storage/emulated/0/MOHAA"));

        setupSpinners();

        btnLaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = edtGamePath.getText().toString();
                
                File pakFile = new File(path + "/main/pak0.pk3");
                if (!pakFile.exists()) {
                    Toast.makeText(MainActivity.this, "Error: File pak0.pk3 tidak ditemukan di folder tersebut!", Toast.LENGTH_LONG).show();
                    return;
                }

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("game_path", path);
                editor.putString("target_fps", spinnerFps.getSelectedItem().toString());
                editor.putString("renderer", spinnerRenderer.getSelectedItem().toString());
                
                String selectedLang = spinnerLang.getSelectedItemPosition() == 0 ? "id" : "en";
                editor.putString("language", selectedLang);
                editor.apply();

                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("GAME_PATH", path);
                intent.putExtra("TARGET_FPS", spinnerFps.getSelectedItem().toString());
                startActivity(intent);
            }
        });
    }

    private void setupSpinners() {
        ArrayAdapter<String> fpsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"30", "60", "90"});
        spinnerFps.setAdapter(fpsAdapter);

        ArrayAdapter<String> langAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"Bahasa Indonesia", "English"});
        spinnerLang.setAdapter(langAdapter);

        ArrayAdapter<String> rendererAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"OpenGL ES 2.0 (Stable)", "OpenGL ES 3.0"});
        spinnerRenderer.setAdapter(rendererAdapter);
    }

    private void loadSavedLanguage() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String langCode = prefs.getString("language", "id");
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Resources res = getResources();
        Configuration config = res.getConfiguration();
        config.setLocale(locale);
        res.updateConfiguration(config, res.getDisplayMetrics());
    }
            }
                                                             
