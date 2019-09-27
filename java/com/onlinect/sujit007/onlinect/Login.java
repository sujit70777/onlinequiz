package com.onlinect.sujit007.onlinect;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

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

public class Login extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    Button loginButton , registerButton;
    EditText emailEt , passwordEt;
    String emailId , password, id;
    AlertDialog.Builder builder;
    CheckBox checkBox;
    ArrayAdapter<String> arrayAdapter;
    String url = "http://172.16.1.113/onlineCt/login.php";
    SharedPreferences sharedPreferences;
    String email , pass ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//
//        if(Build.VERSION.SDK_INT >22) {
//
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
//                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
//                            MY_PERMISSIONS_REQUEST_READ_CONTACTS
//                    );
//                }
//            }
//        }

        sharedPreferences = getSharedPreferences("userData" , MODE_PRIVATE);

        email = sharedPreferences.getString("email" , "");
        pass = sharedPreferences.getString("pass", "");

        emailEt = (EditText) findViewById(R.id.login_userName);
        passwordEt = (EditText) findViewById(R.id.login_password);
        loginButton = (Button) findViewById(R.id.email_sign_in_button);
        registerButton = (Button) findViewById(R.id.email_register_button);
        checkBox = (CheckBox) findViewById(R.id.checkBox);


        builder = new AlertDialog.Builder(Login.this);


        if(!email.equals("") && !pass.equals("")){
            checkBox.setChecked(true);
            emailEt.setText(email);
            passwordEt.setText(pass);
        }


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Login.this , RegistationActivity.class));
                overridePendingTransition(R.anim.slide_in , R.anim.slide_out);
                finish();

            }
        });



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailId = emailEt.getText().toString();
                password = passwordEt.getText().toString();

                if(checkBox.isChecked()){

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email" , emailId);
                    editor.putString("pass" , password);
                    editor.commit();

                }else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email" , "");
                    editor.putString("pass" , "");
                    editor.commit();
                }



                if(emailId.equals("") || password.equals("")){
                    builder.setTitle("Something went wrong....");
                    builder.setMessage("Please fill all the fields.");
                    displayAlert("Input Error");
                }else {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONArray jsonArray = new JSONArray(response);
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String code = jsonObject.getString("code");
                                        String message = jsonObject.getString("message");
                                        if(code.equals("Login success")){
                                            id = jsonObject.getString("id");
                                            //  Toast.makeText(getApplicationContext() , id , Toast.LENGTH_LONG).show();
                                        }

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
                            params.put("email", emailId);
                            params.put("password" ,password);
                            return params;
                        }
                    };

                    MySingletone.getmInstance(Login.this).addTorequestQue(stringRequest);

                }


            }
        });




    }

    public void displayAlert(final String code) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (code.equals("Login failed")) {
                    passwordEt.setText("");
                    emailEt.setText("");
                }

                if (code.equals("Login success")) {

                    Intent intent = new Intent(Login.this, HomeActivity.class);

                    intent.putExtra("id" , id);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in , R.anim.slide_out);
                    finish();

                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
