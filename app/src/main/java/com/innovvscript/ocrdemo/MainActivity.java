package com.innovvscript.ocrdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.midi.MidiDeviceService;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;
import java.util.List;

public class MainActivity extends BaseActivity {

    Button mButton;
    Button button_cam;
    TextView textValue;
    SharedPreferences pref;
    DatabaseHelper db;
    Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        db=new DatabaseHelper(this);

        button_cam = findViewById(R.id.button_cam);
        mButton = findViewById(R.id.button_text);
        textValue = findViewById(R.id.text);
        search=findViewById(R.id.button_search);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myBitmap != null) {
                    runTextRecognition();
                } else {
                    showToast("Choose an image");
                }
            }
        });
        button_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(getPickImageChooserIntent(), 200);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textValue.getText().toString()==""){
                    showToast("Cannot perform this operation");
                }
                else {
                    Intent intent = new Intent(MainActivity.this, searchdata.class);
                    intent.putExtra("textValue", textValue.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }



    private void runTextRecognition() {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(myBitmap);
        FirebaseVisionTextDetector detector = FirebaseVision.getInstance()
                .getVisionTextDetector();
        mButton.setEnabled(true);
        detector.detectInImage(image)
                .addOnSuccessListener(
                        new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText texts) {
                                processTextRecognitionResult(texts);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                Toast.makeText(MainActivity.this,"Upload only image",Toast.LENGTH_LONG).show();
                                mButton.setEnabled(false);
                                e.printStackTrace();
                            }
                        });
    }

    private void processTextRecognitionResult(FirebaseVisionText texts) {
        StringBuilder t = new StringBuilder();
        List<FirebaseVisionText.Block> blocks = texts.getBlocks();
        if (blocks.size() == 0) {
            showToast("Data not found");
            return;
        }

        for (int i = 0; i < blocks.size(); i++) {
            t.append(" ").append(blocks.get(i).getText());
        }
        String info = "\n" + t.toString();
        pref.edit().putString("text", t.toString()).apply();
        textValue.setText(info);
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}