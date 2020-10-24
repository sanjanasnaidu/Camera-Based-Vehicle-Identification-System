package com.innovvscript.ocrdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.CommonStatusCodes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CameraCapture extends AppCompatActivity implements View.OnClickListener {

    // Use a compound button so either checkbox or switch widgets work.
    private TextView statusMessage;
    private TextView textValue;

    private Toolbar mToolbar;
    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText edtSeach;
    Button search;
    String compare;

    private static final int RC_OCR_CAPTURE = 9003;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_capture);

        statusMessage = (TextView)findViewById(R.id.status_message);
        textValue = (TextView)findViewById(R.id.text_value);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        search=findViewById(R.id.search_text);
        setSupportActionBar(mToolbar);

        findViewById(R.id.read_text).setOnClickListener(this);
        displayStatus();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //search();
                if(textValue.getText().toString()=="")
                {
                    Toast.makeText(CameraCapture.this,"Cannot perform this operation",Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(CameraCapture.this, searchdata.class);
                    intent.putExtra("textValue", textValue.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }

    public void displayStatus()
    {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        statusMessage.setVisibility(View.GONE);
                    }
                });
            }
        };
        thread.start(); //start the thread
        statusMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                handleMenuSearch();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void handleMenuSearch(){
        ActionBar action = getSupportActionBar(); //get the actionbar

        if(isSearchOpened){ //test if the search is open

            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true); //show the title in the action bar

            //hides the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);

            //add the search icon in the action bar
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_open_search));

            isSearchOpened = false;
        } else { //open the search entry

            action.setDisplayShowCustomEnabled(true); //enable it to display a
            // custom view in the action bar.
            action.setCustomView(R.layout.search_bar);//add the custom view
            action.setDisplayShowTitleEnabled(false); //hide the title

            edtSeach = (EditText)action.getCustomView().findViewById(R.id.edtSearch); //the text editor

            //this is a listener to do a search when the user clicks on search button
            edtSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        doSearch();
                        return true;
                    }
                    return false;
                }
            });


            edtSeach.requestFocus();

            //open the keyboard focused in the edtSearch
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edtSeach, InputMethodManager.SHOW_IMPLICIT);


            //add the close icon
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_close_search));

            isSearchOpened = true;
        }
    }

    @Override
    public void onBackPressed() {
        if(isSearchOpened) {
            handleMenuSearch();
            return;
        }
        super.onBackPressed();
    }

    private static boolean isContain(String source, String subItem){
        String pattern = "\\b"+subItem+"\\b";
        Pattern p=Pattern.compile(pattern);
        Matcher m=p.matcher(source);
        return m.find();
    }


    private void doSearch() {

        if(textValue.getText().toString().equals("No Number plate detected"))
        {
            Toast.makeText(getApplicationContext(),"First Detect Number Plate!",Toast.LENGTH_SHORT).show();
            return;
        }

        String result = textValue.getText().toString().toLowerCase();
        String search = edtSeach.getText().toString().toLowerCase();

        if(isContain(result,search))
        {
            int counter=0,pos = 0,counterForWords=0;
            for( int i=0; i < result.indexOf(search); i++) {
                if( result.charAt(i) == '\n' ) {
                    counter++;
                    pos = i;
                }
            }

            for(int i=pos;i<result.indexOf(search);i++) {
                if( result.charAt(i) == ' ' ) {
                    counterForWords++;
                }
            }

            counter++;
            counterForWords++;

            statusMessage.setText("Word is at " + counter + "th Line , " + counterForWords + "th word");
            statusMessage.setTextColor(Color.GREEN);

            String ett =edtSeach.getText().toString();
            String tvt =textValue.getText().toString();

            textValue.setText(tvt);

            int ofe = tvt.indexOf(ett,0);
            Spannable WordtoSpan = new SpannableString( textValue.getText() );

            for(int ofs=0;ofs<tvt.length() && ofe!=-1;ofs=ofe+1)
            {


                ofe = tvt.indexOf(ett,ofs);
                if(ofe == -1)
                    break;
                else
                {

                    WordtoSpan.setSpan(new BackgroundColorSpan(0xFFFFFF00), ofe, ofe+ett.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    textValue.setText(WordtoSpan, TextView.BufferType.SPANNABLE);
                }


            }
        }
        else {
            statusMessage.setText("Word Not Found");
            statusMessage.setTextColor(Color.RED);
        }

        statusMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.read_text) {
            // launch Ocr capture activity.
            Intent intent = new Intent(this, OcrCaptureActivity.class);
            intent.putExtra(OcrCaptureActivity.AutoFocus, true);
            intent.putExtra(OcrCaptureActivity.UseFlash, false);

            startActivityForResult(intent, RC_OCR_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
                    statusMessage.setText(R.string.ocr_success);
                    textValue.setText(text);
                    Log.d(TAG, "Number Plate read: " + text);
                } else {
                    statusMessage.setText(R.string.ocr_failure);
                    Log.d(TAG, "null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.ocr_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
            displayStatus();
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);


        }
    }
}
