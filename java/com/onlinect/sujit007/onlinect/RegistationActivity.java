package com.onlinect.sujit007.onlinect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistationActivity extends AppCompatActivity {

    Spinner deptSpinner;

    String[] deptList = {"CSE", "EEE", "LAW", "BBA", "ENG", "PHY"};
    String userName, studentId, batchNumber, departmentId, mobileNumber, emailId, password, confrmPassword;
    EditText userNameEt, studentIdEt, batchNumberEt, mobileNumberEt, emailIdEt, passwordEt, confirmPasswordEt;
    AlertDialog.Builder builder;
    ArrayAdapter<String> arrayAdapter;
    String url = "http://172.16.1.113/onlineCt/register.php";
    Button cancel, register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registation);


        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        final String imei = tm.getDeviceId();
      //  Toast.makeText(getApplicationContext() , imei , Toast.LENGTH_LONG).show();

        deptSpinner = (Spinner) findViewById(R.id.department_spinner);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, deptList);
        deptSpinner.setAdapter(arrayAdapter);

        deptSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(getApplicationContext(), " "+position , Toast.LENGTH_LONG).show();
                departmentId = String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cancel = (Button) findViewById(R.id.cancel_registration);
        register = (Button) findViewById(R.id.agree_registration);
        builder = new AlertDialog.Builder(RegistationActivity.this);

        userNameEt = (EditText) findViewById(R.id.register_user_name);
        studentIdEt = (EditText) findViewById(R.id.register_student_id);
        batchNumberEt = (EditText) findViewById(R.id.register_student_batch_number);
        mobileNumberEt = (EditText) findViewById(R.id.register_mobile);
        emailIdEt = (EditText) findViewById(R.id.register_email);
        passwordEt = (EditText) findViewById(R.id.register_password);
        confirmPasswordEt = (EditText) findViewById(R.id.register_confirm_password);



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistationActivity.this, Login.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = userNameEt.getText().toString();
                studentId = studentIdEt.getText().toString();
                batchNumber = batchNumberEt.getText().toString();
                mobileNumber = mobileNumberEt.getText().toString();
                emailId = emailIdEt.getText().toString();
                password = passwordEt.getText().toString();
                confrmPassword = confirmPasswordEt.getText().toString();

                if (userName.equals("") || studentId.equals("") || batchNumber.equals("") || mobileNumber.equals("") || emailId.equals("") || password.equals("") || confrmPassword.equals("")) {
                    builder.setTitle("Something went wrong....");
                    builder.setMessage("Please fill all the fields.");
                    displayAlert("Input Error");

                } else {
                    if (emailId.contains("@") != true) {
                        builder.setTitle("Something went wrong....");
                        builder.setMessage("Insert valid email...");
                        displayAlert("Email Error");
                    } else {
                        if (!(password.equals(confrmPassword))) {

                            builder.setTitle("Something went wrong....");
                            builder.setMessage("Your password are not matching ");
                            displayAlert("Input Error");

                        } else {


                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            try {
                                                JSONArray jsonArray = new JSONArray(response);
                                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                                String code = jsonObject.getString("code");
                                                String message = jsonObject.getString("message");
                                                Log.d("oooooooooooooooo", "onResponse: ");
                                                builder.setTitle(code);
                                                builder.setMessage(message);
                                                displayAlert(code);


                                               // Toast.makeText(getApplicationContext() , code +" "+ message , Toast.LENGTH_LONG).show();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            //    Toast.makeText(getApplicationContext() , response , Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    },

                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError volleyError) {

                                        }
                                    }

                            ) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {

                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("name", userName);
                                    params.put("studentId", studentId);
                                    params.put("batchNumber", batchNumber);
                                    params.put("departmentName", departmentId+1);
                                    params.put("mobile", mobileNumber);
                                    params.put("email", emailId);
                                    params.put("password", password);
                                    params.put("imei" ,imei);


                                    return params;
                                }
                            };

                            MySingletone.getmInstance(RegistationActivity.this).addTorequestQue(stringRequest);


                        }


                    }


                }
            }
        });


    }

    public void displayAlert(final String code) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (code.equals("Input Error")) {
                    passwordEt.setText("");
                    confirmPasswordEt.setText("");
                }
                if (code.equals("Email Error")) {
                    passwordEt.setText("");
                    confirmPasswordEt.setText("");
                    emailIdEt.setText("");
                }
                if (code.equals("Registration Failed !!")) {
                    userNameEt.setText("");
                    studentIdEt.setText("");
                    batchNumberEt.setText("");
                    mobileNumberEt.setText("");
                    emailIdEt.setText("");
                    passwordEt.setText("");
                    confirmPasswordEt.setText("");

                }
                if (code.equals("Registration Success..")) {
                    startActivity(new Intent(RegistationActivity.this, Login.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();

                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}
