package com.example.datastoragemanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public final static int DEFAULT = 0;
    private final static String SHARED_PREFERENCES_FILENAME = "my-preferences-file";
    private final static String UPDATE_INFO_KEY ="update-info";
    private final static String EMPTY_STRING = "";

    private EditText etInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);
    }
    //if we use this, it will always jump to the below method
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnOk){
            Log.i("btnOk", "btnOk");
        }
        storeUpdateInfoIntoPreferences();

    }

    private void storeUpdateInfoIntoPreferences() {
        String info = etInfo.getText().toString();

        if (updateInfoIsValid(info)) {
            String simplifiedInfo = info.trim();
            SharedPreferences sPrefs = getSharedPreferences(SHARED_PREFERENCES_FILENAME, MODE_PRIVATE);
            SharedPreferences.Editor sPrefsEditor = sPrefs.edit();
            sPrefsEditor.putString(UPDATE_INFO_KEY, simplifiedInfo);

            etInfo.setText(EMPTY_STRING);
            Toast.makeText(this, "String '" + simplifiedInfo + "' is saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Data not valid", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean updateInfoIsValid(String info){
        return !info.trim().isEmpty();
    }

        //modify layout so not relLayout but consLayout
}
