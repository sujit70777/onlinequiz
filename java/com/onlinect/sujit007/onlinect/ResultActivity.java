package com.onlinect.sujit007.onlinect;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class ResultActivity extends AppCompatActivity {

    String marks, ans, userId, examId , name;
    String url = "http://172.16.1.113/onlineCt/result.php";
    Button result;
    TextView totalMarks , totalAns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();

        userId = bundle.getString("userId");
        examId = bundle.getString("examId");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

      //  Toast.makeText(getApplicationContext(), "Total Marks = " + userId + " Total Answered = " + examId, Toast.LENGTH_LONG).show();

        new backdata().execute();

        result = (Button) findViewById(R.id.result);
        totalAns = (TextView) findViewById(R.id.totalAns);
        totalMarks = (TextView) findViewById(R.id.totalCorrectAns);

        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalAns.setText("Total Answered :"+ans+" questions.");
                totalMarks.setText("Total Marks: "+marks);

            }
        });

        result.setVisibility(View.GONE);

    }

    public class backdata extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                name = jsonObject.getString("name");
                                marks = jsonObject.getString("totalMarks");
                                ans = jsonObject.getString("totalAns");

                                totalAns.setText("Congratulation\n"+name+"\nYour\nTotal Answered :"+ans+" questions.");
                                totalMarks.setText("Total Marks: "+marks);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            new backdata().execute();
                        }
                    }

            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userId", userId);
                    params.put("examId", examId);

                    return params;
                }
            };
            MySingletone.getmInstance(ResultActivity.this).addTorequestQue(stringRequest);

            return null;
        }
    }
}
