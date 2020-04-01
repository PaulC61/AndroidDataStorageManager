package com.example.datastoragemanager;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int DEFAULT = 0;
    private static final String SHARED_PREFERENCES_FILENAME = "my-preferences-file";
    private static final String UPDATE_INFO_KEY ="update-info";
    private static final String CUSTOM_DIRECTORY = "Paul/my-music";
    private static final String EMPTY_STRING = "";
    private static final String INTERNAL_STORAGE_FILENAME = "my-internal-file";
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 101;

    private EditText etInfo;
    private TextView tvUpdateInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnInternalStorage = findViewById(R.id.btnInternalStorage);
        btnInternalStorage.setOnClickListener(this);

        final Button btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);

        final Button btnCurrentFiles = findViewById(R.id.btnGetFiles);
        btnCurrentFiles.setOnClickListener(this);

        final Button btnExternalStorage = findViewById(R.id.btnExternalStorage);
        btnExternalStorage.setOnClickListener(this);

        etInfo = findViewById(R.id.edtTextUpdate);

        tvUpdateInfo = findViewById(R.id.textViewHello);
    }


    @Override
    protected void onResume(){
        super.onResume();

        SharedPreferences sPrefs = getSharedPreferences(SHARED_PREFERENCES_FILENAME, MODE_PRIVATE);
        String storedInfo = sPrefs.getString(UPDATE_INFO_KEY, getString(R.string.default_text));
        tvUpdateInfo.setText(storedInfo);
    }
    //if we use this, it will always jump to the below method
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnOk:
                Log.i("btnOk", "btnOk");
                storeUpdateInfoIntoPreferences();
                break;
            case R.id.btnInternalStorage:
                storeUpdateInfoIntoInternalStorage();
                break;
            case R.id.btnGetFiles:
                displayCurrentInternalFiles();
                break;
            case R.id.btnExternalStorage:
                checkExternalStorageWritePermission();
                break;
            default:
                Log.w("switch","No 'case' matched");

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                storeUpdateInfoIntoExternalStorage();
            } else {
                Log.w("requestPermission", "External storage not granted");
                Toast.makeText(this, "External storage usage not granted",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkExternalStorageWritePermission(){
        final boolean isPermissionGranted= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if(isPermissionGranted){
            storeUpdateInfoIntoExternalStorage();
        }else{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }
    }

    private void storeUpdateInfoIntoExternalStorage() {

        boolean isAvailable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

        if(isAvailable){
            createDirectoryInDownloadsPublicFolder(CUSTOM_DIRECTORY);
        }else{
            Toast.makeText(this,"Location is not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void createDirectoryInDownloadsPublicFolder(String folderPath) {
        final File personalFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), folderPath);

        if(!personalFolder.mkdirs()){
            Toast.makeText(this,"Something Went Wrong",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,":)",Toast.LENGTH_SHORT).show();
        }
    }

    private void displayCurrentInternalFiles() {
        StringBuilder stringBuilder = new StringBuilder();
        String[] currentFiles = fileList();
        for (String file : currentFiles) {
            stringBuilder.append(file).append("\n");
        }
        Toast.makeText(this, "", Toast.LENGTH_LONG).show();
    }

    private void storeUpdateInfoIntoInternalStorage() {
       String info = etInfo.getText().toString() + "\t";

       if (updateInfoIsValid(info)) {
           try {
               FileOutputStream fileOutputStream = openFileOutput(INTERNAL_STORAGE_FILENAME, MODE_APPEND);
               fileOutputStream.write(info.getBytes());
               fileOutputStream.close();
               Toast.makeText(this, "Data saved into file", Toast.LENGTH_SHORT).show();
           } catch (FileNotFoundException e) {
               e.printStackTrace();
               Log.w("try-catch", "FileNotFoundException");
           } catch (IOException e) {
               e.printStackTrace();
               Log.e("try-catch", "IOException");
           } finally {
               etInfo.setText(EMPTY_STRING);
           }
       }else{
           Toast.makeText(this,"Data not valid", Toast.LENGTH_SHORT).show();
       }
    }

    private void storeUpdateInfoIntoPreferences() {
        String info = etInfo.getText().toString();

        if (updateInfoIsValid(info)) {
            String simplifiedInfo = info.trim();
            SharedPreferences sPrefs = getSharedPreferences(SHARED_PREFERENCES_FILENAME, MODE_PRIVATE);
            SharedPreferences.Editor sPrefsEditor = sPrefs.edit();
            sPrefsEditor.putString(UPDATE_INFO_KEY, simplifiedInfo);
            sPrefsEditor.apply();
            
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
