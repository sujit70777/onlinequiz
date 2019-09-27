package com.onlinect.sujit007.onlinect;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.onlinect.sujit007.onlinect.RunningExam.Questions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sujit007 on 3/31/2017.
 */

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder> {

    ArrayList<Questions> questionses;
    Context context;
    String userId, examId, type;
    String url = "http://172.16.1.113/onlineCt/givingExam.php";


    public QuestionsAdapter(ArrayList<Questions> questionses, Context context, String userId, String examId, String type) {
        this.questionses = questionses;
        this.context = context;
        this.userId = userId;
        this.examId = examId;
        this.type = type;
    }

    @Override
    public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_row, parent, false);
        QuestionViewHolder viewHolder = new QuestionViewHolder(view, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final QuestionViewHolder holder, final int position) {

        holder.itemView.setActivated(holder.getAdapterPosition() == position);
        holder.question.setText(position + 1 + ": " + questionses.get(position).getQues());
        holder.optionA.setText(questionses.get(position).getOptA());
        holder.optionB.setText(questionses.get(position).getOptB());
        holder.optionC.setText(questionses.get(position).getOptC());
        holder.optionD.setText(questionses.get(position).getOptD());
        holder.ans.setText("Answer: " + questionses.get(position).getAns());
        final int status, ans, quesId;







        quesId = questionses.get(position).getSl();
        holder.options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            //    Toast.makeText(context, "" + questionses.get(position).getType(), Toast.LENGTH_SHORT).show();

                switch (checkedId) {
                    case R.id.radioButton1:
                        if ((holder.optionA.isChecked())) {
                            //   Toast.makeText(context , userId+" "+questionses.get(position).getSl()+" A"+holder.optionA.getText().toString() , Toast.LENGTH_SHORT).show();

                            if (holder.optionA.getText().toString().equals(questionses.get(position).getAns())) {
                                sendData(userId, questionses.get(position).getSl(), holder.optionA.getText().toString(), 1);
                            } else {
                                sendData(userId, questionses.get(position).getSl(), holder.optionA.getText().toString(), 0);

                            }

                            questionses.get(position).setType(1);

                        }
                        break;
                    case R.id.radioButton2:
                        if ((holder.optionB.isChecked())) {
                            if (holder.optionB.getText().toString().equals(questionses.get(position).getAns())) {
                                sendData(userId, questionses.get(position).getSl(), holder.optionB.getText().toString(), 1);
                            } else {
                                sendData(userId, questionses.get(position).getSl(), holder.optionB.getText().toString(), 0);

                            }
                            questionses.get(position).setType(2);


                        }
                        break;
                    case R.id.radioButton3:
                        if ((holder.optionC.isChecked())) {

                            if (holder.optionC.getText().toString().equals(questionses.get(position).getAns())) {
                                sendData(userId, questionses.get(position).getSl(), holder.optionC.getText().toString(), 1);
                            } else {
                                sendData(userId, questionses.get(position).getSl(), holder.optionC.getText().toString(), 0);

                            }
                            questionses.get(position).setType(3);

                        }
                        break;
                    case R.id.radioButton4:
                        if ((holder.optionD.isChecked())) {
                            if (holder.optionD.getText().toString().equals(questionses.get(position).getAns())) {
                                sendData(userId, questionses.get(position).getSl(), holder.optionD.getText().toString(), 1);
                            } else {
                                sendData(userId, questionses.get(position).getSl(), holder.optionD.getText().toString(), 0);

                            }

                            questionses.get(position).setType(4);

                        }
                        break;
                    default:
                        break;
                }


            }
        });

//        if (questionses.get(position).getType() == 4) {
//            holder.optionA.setChecked(false);
//            holder.optionB.setChecked(false);
//            holder.optionC.setChecked(false);
//            holder.optionD.setChecked(true);
//            holder.options.clearCheck();
//        } else if (questionses.get(position).getType() == 1) {
//            holder.optionA.setChecked(true);
//            holder.optionB.setChecked(false);
//            holder.optionC.setChecked(false);
//            holder.optionD.setChecked(false);
//            holder.options.clearCheck();
//        } else if (questionses.get(position).getType() == 2) {
//            holder.optionA.setChecked(false);
//            holder.optionB.setChecked(true);
//            holder.optionC.setChecked(false);
//            holder.optionD.setChecked(false);
//            holder.options.clearCheck();
//        } else if (questionses.get(position).getType() == 3) {
//            holder.optionA.setChecked(false);
//            holder.optionB.setChecked(false);
//            holder.optionC.setChecked(true);
//            holder.optionD.setChecked(false);
//            holder.options.clearCheck();
//        } else if (questionses.get(position).getType() == 0) {
//            holder.optionA.setChecked(false);
//            holder.optionB.setChecked(false);
//            holder.optionC.setChecked(false);
//            holder.optionD.setChecked(false);
//            holder.options.clearCheck();
//        }
//

    }

    @Override
    public int getItemCount() {
        return questionses.size();
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder {
        Context context;
        RadioGroup options;
        RadioButton optionA, optionB, optionC, optionD;
        TextView question, ans;

        public QuestionViewHolder(View itemView, Context context) {
            super(itemView);
            question = (TextView) itemView.findViewById(R.id.question);
            options = (RadioGroup) itemView.findViewById(R.id.Options);
            optionA = (RadioButton) itemView.findViewById(R.id.radioButton1);
            optionB = (RadioButton) itemView.findViewById(R.id.radioButton2);
            optionC = (RadioButton) itemView.findViewById(R.id.radioButton3);
            optionD = (RadioButton) itemView.findViewById(R.id.radioButton4);
            ans = (TextView) itemView.findViewById(R.id.answer);

            if (type.equals("1")) {
                options.setVisibility(View.GONE);
            } else {
                ans.setVisibility(View.GONE);
            }

        }
    }


    public void sendData(final String userId, final int quesId, final String status, final int ans) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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
                params.put("quesId", String.valueOf(quesId));
                params.put("status", status);
                params.put("ans", String.valueOf(ans));
                params.put("examId", examId);

                return params;
            }
        };

        MySingletone.getmInstance(context).addTorequestQue(stringRequest);


    }
}
