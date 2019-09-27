package com.onlinect.sujit007.onlinect;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.onlinect.sujit007.onlinect.RunningExam.Questions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuestionsAnswer extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder> adapter;
    RecyclerView.LayoutManager layoutManager;
    JSONArray jsonArray;
    ArrayList<Questions> questionses;
    String id, examId, type, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_ans);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("userId");
        examId = bundle.getString("id");
        type = bundle.getString("type");
        url = "http://172.16.1.113 /onlineCt/questions.php";
        recyclerView = (RecyclerView) findViewById(R.id.ansList);
        layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();
        questionses = new ArrayList<>();
        new BackData().execute();

        adapter = new QuestionsAdapter(questionses, QuestionsAnswer.this, id, examId , type);
        recyclerView.setAdapter(adapter);
    }

    class BackData extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                jsonArray = new JSONArray(response);
                                int count = 0;

                                while (count < jsonArray.length()) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(count);

                                    Questions questions = new Questions(
                                            jsonObject.getInt("Sl"),
                                            jsonObject.getString("question"),
                                            jsonObject.getString("opt_a"),
                                            jsonObject.getString("opt_b"),
                                            jsonObject.getString("opt_c"),
                                            jsonObject.getString("opt_d"),

                                            jsonObject.getString("ans"),
                                            0
                                    );
                                    questionses.add(questions);
                                    count++;
                                    adapter.notifyItemChanged(questionses.size());
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //  Toast.makeText(getApplicationContext(), error+"", Toast.LENGTH_LONG).show();
                            new BackData().execute();
                        }
                    }

            ) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id", examId);

                    return params;
                }
            };

            MySingletone.getmInstance(QuestionsAnswer.this).addTorequestQue(stringRequest);

            return null;
        }
    }
}
