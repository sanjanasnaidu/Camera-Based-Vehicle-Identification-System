package com.innovvscript.ocrdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class searchdata extends AppCompatActivity {
    String compare;
    Button send;
    TextView name, vno, vname, vcolor, mobile, email, address, vtype;
    SharedPreferences preferences;
    String str;
    private static String URL_LOGIN = "https://smartcame.000webhostapp.com/vehicle/compare.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchdata);
        Intent intent = getIntent();
        str = intent.getStringExtra("textValue");
        str = str.replaceAll("\\s", "");
         System.out.println("vehicle_num"+str);
        send = findViewById(R.id.sendmssg);
        name = findViewById(R.id.name);
        vno = findViewById(R.id.vnumber);
        vname = findViewById(R.id.vname);
        vcolor = findViewById(R.id.vcolor);
        mobile = findViewById(R.id.mobile);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        vtype=findViewById(R.id.vtype);
        display();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(searchdata.this, challan.class);
                intent.putExtra("name", name.getText().toString());
                intent.putExtra("vtype", vtype.getText().toString());
                intent.putExtra("mobile", mobile.getText().toString());
                startActivity(intent);
            }
        });
    }

    private void display() {
        //compare = this.textValue.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println("Response : "+ response);
                            response = response.replaceAll("connected","");
                            System.out.println("Updated Response : "+ response);
                            Log.d("TAG", "Length :- " + response.length());
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e("msg: ", response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("vehicle");
                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    name.setText(object.getString("owner_name").trim());
                                    vno.setText(object.getString("vehicle_num").trim());
                                    vcolor.setText(object.getString("vehicle_color").trim());
                                    vname.setText(object.getString("vehicle_name").trim());
                                    vtype.setText(object.getString("vehicle_type").trim());
                                    mobile.setText(object.getString("mobile").trim());
                                    email.setText(object.getString("email").trim());
                                    address.setText(object.getString("address").trim());
                                }
                            } else {
                                Toast.makeText(searchdata.this, "Data not found", Toast.LENGTH_SHORT).show();
                                send.setEnabled(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(searchdata.this, "Error!" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(searchdata.this, "Error!"+error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String> params = new HashMap<>();
                params.put("vehicle_num",str);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

}


