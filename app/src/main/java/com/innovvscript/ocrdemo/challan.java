package com.innovvscript.ocrdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;

public class challan extends AppCompatActivity {

    private static final String TAG = "Challan";
    private TextView dates,mobile,name,fine,vtype,place,challan,tps;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    Button send;
    int increment=1;
    int backButtonCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challan);

        dates=findViewById(R.id.date);
        mobile=findViewById(R.id.mobile);
        name=findViewById(R.id.name);
        send=findViewById(R.id.sendmssg);
        fine=findViewById(R.id.fine);
        vtype=findViewById(R.id.vtype);
        place=findViewById(R.id.place);
        challan=findViewById(R.id.cno);
        tps=findViewById(R.id.tps);

        final Intent intent = getIntent();
        String str = intent.getStringExtra("name");
        String str1=intent.getStringExtra("mobile");
        String str2=intent.getStringExtra("vtype");
        name.setText(str);
        mobile.setText(str1);
        vtype.setText(str2);
        challan.setText("TPB"+increment);

        final Spinner spinner=(Spinner)findViewById(R.id.violation);
        if(vtype.getText().equals("Four-wheeler"))
        {
            ArrayAdapter<String> adapter= new ArrayAdapter<String>(challan.this,android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.fourwheeler));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(spinner.getSelectedItem().toString().equals("General Offence"))
                    {
                        fine.setText("Rs800");
                    }
                    else if(spinner.getSelectedItem().toString().equals("Violation of Road Rules"))
                    {
                        fine.setText("Rs1,000");
                    }
                    else if(spinner.getSelectedItem().toString().equals("Over-Speeding"))
                    {
                        fine.setText("Rs2,000");
                    }
                    else if(spinner.getSelectedItem().toString().equals("Rash Driving"))
                    {
                        fine.setText("Rs3,000");
                    }
                    else if(spinner.getSelectedItem().toString().equals("Drunk Driving"))
                    {
                        fine.setText("Rs15,000");
                    }
                    else if(spinner.getSelectedItem().toString().equals("Not Wearing Seatbelt"))
                    {
                        fine.setText("Rs1,500");
                    }
                    else if(spinner.getSelectedItem().toString().equals("Blocking Way for Emergency Vehicles"))
                    {
                        fine.setText("Rs7,000");
                    }
                    else if(spinner.getSelectedItem().toString().equals("Overloading of Passengers"))
                    {
                        fine.setText("Rs2,000");
                    }
                    else
                    {
                        //
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });
            spinner.setAdapter(adapter);
        }
        else if(vtype.getText().equals("Two-wheeler"))
        {
            ArrayAdapter<String> adapter= new ArrayAdapter<String>(challan.this,android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.twowheeler));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(spinner.getSelectedItem().toString().equals("General Offence"))
                    {
                        fine.setText("Rs500");
                    }
                    else if(spinner.getSelectedItem().toString().equals("Violation of Road Rules"))
                    {
                        fine.setText("Rs500");
                    }
                    else if(spinner.getSelectedItem().toString().equals("Over-Speeding"))
                    {
                        fine.setText("Rs1,000");
                    }
                    else if(spinner.getSelectedItem().toString().equals("Rash Driving"))
                    {
                        fine.setText("Rs1,500");
                    }
                    else if(spinner.getSelectedItem().toString().equals("Drunk Driving"))
                    {
                        fine.setText("Rs10,000");
                    }
                    else if(spinner.getSelectedItem().toString().equals("Not Wearing Helmet"))
                    {
                        fine.setText("Rs500");
                    }
                    else if(spinner.getSelectedItem().toString().equals("Blocking Way for Emergency Vehicles"))
                    {
                        fine.setText("Rs5,000");
                    }
                    else if(spinner.getSelectedItem().toString().equals("Overloading of Passengers"))
                    {
                        fine.setText("Rs1,000");
                    }
                    else
                    {
                        //
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });
        }
        else {
            Toast.makeText(challan.this, "Error", Toast.LENGTH_SHORT).show();
        }

        dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        challan.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                dates.setText(date);
            }
        };

       send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString()=="" || challan.getText().toString()=="" || dates.getText().toString()=="" || place.getText().toString()=="" ||
                        tps.getText().toString()=="" || vtype.getText().toString()=="" || spinner.getSelectedItem().toString()=="" || fine.getText().toString()=="" || mobile.getText().toString()=="")
                {
                    Toast.makeText(challan.this, "Fill the empty fields...",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    Uri uri = Uri.parse("smsto:" + mobile.getText().toString());

                    Intent smsIntent = new Intent(Intent.ACTION_SENDTO, uri);
                    // add the message at the sms_body extra field
                    smsIntent.putExtra("sms_body", "Dear " + name.getText().toString() + "\n You have been charged " + fine.getText().toString() + ", challan number " + challan.getText().toString() + " for violating the rule " + spinner.getSelectedItem().toString() + "\n on date "
                            + dates.getText() + ", place " + place.getText().toString() + ". Kindly pay the fine to avoid penalities or legal action.\nWith Regards Traffic Police India");
                    try {
                        startActivity(smsIntent);
                    } catch (Exception ex) {
                        Toast.makeText(challan.this, "Your sms has failed...",
                                Toast.LENGTH_LONG).show();
                        ex.printStackTrace();
                    }
                    increment++;
                    challan.setText("TPB" + increment);
                }
            }
        });

    }

    public void onBackPressed() {
        Intent intent = new Intent(this,homepage.class);
        startActivity(intent);
    }
}
