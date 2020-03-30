package com.example.datastoragemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
    }
    //modify layout so not relLayout but consLayout
}
