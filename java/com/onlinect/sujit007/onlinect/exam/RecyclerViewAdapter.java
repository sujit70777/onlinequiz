package com.onlinect.sujit007.onlinect.exam;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.onlinect.sujit007.onlinect.MySingletone;
import com.onlinect.sujit007.onlinect.QuestionsAnswer;
import com.onlinect.sujit007.onlinect.R;
import com.onlinect.sujit007.onlinect.RuleActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Sujit007 on 2/20/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    ArrayList<ExamInfo> questionsArrayList = new ArrayList<>();
    Context context;
    String id , type;
    boolean check;


    public RecyclerViewAdapter(ArrayList<ExamInfo> questionsArrayList, Context context, String id , String type) {
        this.questionsArrayList = questionsArrayList;
        this.context = context;
        this.id = id;
        this.type = type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.l_exam_list_view, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view, context);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText(questionsArrayList.get(position).getExamName());
        holder.dept.setText(questionsArrayList.get(position).getBatchName());
        holder.totalMarks.setText("Total Marks: " + String.valueOf(questionsArrayList.get(position).getTotalQues()));
        holder.totalTime.setText("Total Time: " + String.valueOf(questionsArrayList.get(position).getTotalTime()) + " min");
        Log.d("-----------------------", "onBindViewHolder:---------- ");

    }

    @Override
    public int getItemCount() {
        return questionsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Context context;

        TextView name, dept, totalTime, totalMarks, endTime;

        public MyViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            name = (TextView) itemView.findViewById(R.id.examName);
            dept = (TextView) itemView.findViewById(R.id.Dept);
            totalTime = (TextView) itemView.findViewById(R.id.totalTime);
            totalMarks = (TextView) itemView.findViewById(R.id.totalMarks);
         //   endTime = (TextView) itemView.findViewById(R.id.EndTime);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            ExamInfo examInfo = questionsArrayList.get(position);

            Intent intent = new Intent(this.context, RuleActivity.class);
            Intent intent2 = new Intent(this.context, QuestionsAnswer.class);
            Bundle bundle = new Bundle();

            bundle.putString("id", String.valueOf(examInfo.getId()));
            bundle.putString("time", String.valueOf(examInfo.getTotalTime()));
            bundle.putString("userId", id);
            bundle.putString("type" , type);

            intent.putExtras(bundle);
            intent2.putExtras(bundle);

            if(type.equals("1")){
                checkExam(id, String.valueOf(examInfo.getId()) , intent);
            }else {
                context.startActivity(intent2);
            }





        }

        public void checkExam(final String userId, final String examId  , final Intent intent) {

            String url = "http://172.16.1.113/onlineCt/checkExam.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                if (jsonObject.getString("check").equals("0")) {
                                    context.startActivity(intent);
                                }else {
                                    Toast.makeText(context , "This Exam is Already Given" , Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
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

            MySingletone.getmInstance(context).addTorequestQue(stringRequest);




        }


    }
}
