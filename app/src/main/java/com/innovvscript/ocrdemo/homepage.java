package com.innovvscript.ocrdemo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class homepage extends AppCompatActivity {

    int backButtonCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
    }

    public void camera(View view){
        final Intent i = new Intent(this,CameraCapture.class);
        startActivity(i);
    }

    public void search(View view){
        final Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }

    public void onBackPressed() {
        {
            if (backButtonCount >= 1) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
                backButtonCount++;
            }
        }
    }
}
