package com.onlinect.sujit007.onlinect.exam;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.onlinect.sujit007.onlinect.MySingletone;
import com.onlinect.sujit007.onlinect.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExamActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ExamInfo> arrayList;
    String id , type;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();

        id = bundle.getString("id");
        type = bundle.getString("type");
        if(type.equals("1")){
            url = "http://172.16.1.113/onlineCt/exam.php";
        }else {
            url = "http://172.16.1.113/onlineCt/exam2.php";
        }


        //   Toast.makeText(getApplicationContext() , id , Toast.LENGTH_SHORT).show();

        recyclerView = (RecyclerView) findViewById(R.id.examList);
        layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();
        arrayList = new ArrayList<>();
        adapter = new RecyclerViewAdapter(arrayList, ExamActivity.this , id , type);
        recyclerView.setAdapter(adapter);
//        ExamInfo examInfo = new ExamInfo(1, "CC", "CSE", 20, 20);
  //      ExamInfo examInfo2 = new ExamInfo(1, "CC", "CSE", 20, 20);
//        arrayList.add(examInfo);
//        arrayList.add(examInfo2);
//        adapter.notifyItemInserted(arrayList.size());

        new BackGroundTask().execute();
        adapter.notifyItemInserted(arrayList.size());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public class BackGroundTask extends AsyncTask {

        Context context;
        JSONArray jsonArray;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            context = ExamActivity.this;
        }

        @Override
        protected ArrayList<ExamInfo> doInBackground(Object[] params) {

            StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            int count = 0;
                            //    Toast.makeText(getApplicationContext() , response+"" , Toast.LENGTH_LONG).show();

                            try {
                                JSONArray jsonArray = new JSONArray(response);

                                while (count < response.length()) {
                                    JSONObject j = jsonArray.getJSONObject(count);
                                    ExamInfo examInfom = new ExamInfo(
                                            j.getInt("id"),
                                            j.getString("examName"),
                                            j.getString("Batch"),
                                            j.getInt("total_ques"),
                                            j.getInt("total_time")
                                    );
                                    Log.d("-----------------------", "Json:---------- ");
                                    arrayList.add(examInfom);
                                    count++;
                                    adapter.notifyItemInserted(arrayList.size());
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("-----------------------", "Json ERROR:---------- ");
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("-----------------------", "Json ERROR:---------- " + error);
                            new BackGroundTask().execute();

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

            MySingletone.getmInstance(ExamActivity.this).addTorequestQue(jsonArrayRequest);
            Log.d("-----------------------", "Json SING:---------- ");

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            adapter.notifyItemInserted(arrayList.size());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }
}
