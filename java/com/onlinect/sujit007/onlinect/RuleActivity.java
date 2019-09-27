package com.onlinect.sujit007.onlinect;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RuleActivity extends AppCompatActivity {

    String string, url, id ,time , userId;
    Button agree, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule);
        string = "";
        url = "http://172.16.1.113/onlineCt/questions.php";
        Bundle bundle = getIntent().getExtras();


        id =  bundle.get("id").toString();
        time =  bundle.get("time").toString();
        userId =  bundle.get("userId").toString();
     //   Toast.makeText(getApplicationContext(), id +" "+time, Toast.LENGTH_LONG).show();

        new BackData().execute();

        agree = (Button) findViewById(R.id.agree_rule);
        cancel = (Button) findViewById(R.id.cancel_rule);

        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(RuleActivity.this, TakingExamActivity.class);
                Bundle bundle1 = new Bundle();

                bundle1.putString("questions" , string);
                bundle1.putString("time" , time);
                bundle1.putString("userId" , userId);
                bundle1.putString("examId" , id);
                intent.putExtras(bundle1);
                if (string.equals("[]")) {
                    Toast.makeText(getApplicationContext(), "Questions is not loaded", Toast.LENGTH_LONG).show();

                } else {
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    finish();

                }

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });


    }

    class BackData extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            string = response;

                            //     Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //  Toast.makeText(getApplicationContext(), error+"", Toast.LENGTH_LONG).show();

                        }
                    }

            ) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id", id);

                    return params;
                }
            };

            MySingletone.getmInstance(RuleActivity.this).addTorequestQue(stringRequest);

            return null;
        }
    }
}
