package com.onlinect.sujit007.onlinect;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.onlinect.sujit007.onlinect.RunningExam.Questions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class TakingExamActivity extends AppCompatActivity {

    String string, userId, examId;
    String totalMarks , totalAns;
    String t;
    int position;
    JSONArray jsonArray;
    ArrayList<Questions> questionses = new ArrayList<>();
    ArrayList<String> strings = new ArrayList<>();
    RadioGroup options;
    RadioButton optionA, optionB, optionC, optionD;
    TextView question, timerTv;
    Button previous, submit, next;
    int[] arr, rlist;
    int time, time2;
    CountDownTimer timer, timer2;
    ProgressBar bar;
    RecyclerView recyclerView;
    RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder> adapter;
    RecyclerView.LayoutManager layoutManager;

    boolean pause;
    Handler handler;
    boolean end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taking_exam);

        //  string = getIntent().getStringExtra("questions");

        Bundle bundle = getIntent().getExtras();

        string = bundle.getString("questions");
        t = bundle.getString("time");
        userId = bundle.getString("userId");
        examId = bundle.getString("examId");
        //   Toast.makeText(getApplicationContext(), examId, Toast.LENGTH_LONG).show();

        time = Integer.parseInt(t);
        time2 = time;
        position = 0;
        question = (TextView) findViewById(R.id.question);
        timerTv = (TextView) findViewById(R.id.timer);
        options = (RadioGroup) findViewById(R.id.Options);
        optionA = (RadioButton) findViewById(R.id.radioButton1);
        optionB = (RadioButton) findViewById(R.id.radioButton2);
        optionC = (RadioButton) findViewById(R.id.radioButton3);
        optionD = (RadioButton) findViewById(R.id.radioButton4);
        bar = (ProgressBar) findViewById(R.id.progressBar);

        recyclerView = (RecyclerView) findViewById(R.id.questionsList);
        layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemViewCacheSize(2048);
        recyclerView.hasFixedSize();
        questionses = new ArrayList<>();
        handler = new Handler();

//-----------------CONVERTING JSON DATA INTO ARRAYLIST ------------------------
        try {
            jsonArray = new JSONArray(string);
            int count = 0;

            arr = new int[jsonArray.length()];
            while (count < jsonArray.length()) {

                JSONObject jsonObject = jsonArray.getJSONObject(count);
                strings.add(jsonObject.getString("opt_a"));
                strings.add(jsonObject.getString("opt_b"));
                strings.add(jsonObject.getString("opt_c"));
                strings.add(jsonObject.getString("opt_d"));
                Collections.shuffle(strings);
                Collections.shuffle(strings);
                Questions questions = new Questions(
                        jsonObject.getInt("Sl"),
                        jsonObject.getString("question"),
                        strings.get(0),
                        strings.get(1),
                        strings.get(2),
                        strings.get(3),

                        jsonObject.getString("ans"),
                        0
                );
                strings.clear();
                questionses.add(questions);
                count++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //---------------------------------------------------------------------------------
        Collections.shuffle(questionses);
        Collections.shuffle(questionses);


        adapter = new QuestionsAdapter(questionses, TakingExamActivity.this, userId, examId , "1 ");
        recyclerView.setAdapter(adapter);


        //-------------------PROGRESS BAR -----------------

        bar.setMax(60 * time2);

        timer = new CountDownTimer(60 * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timerTv.setText(time + ":" + millisUntilFinished / 1000);
                // bar.setProgress((int)millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                if (time > 0) {
                    timer.start();
                    time--;
                    if (time == 0) {
                        timerTv.setTextColor(0xffff4444);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            timerTv.setBackground(getDrawable(R.drawable.timerr));
                        }

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Time Up", Toast.LENGTH_LONG).show();
                    examEnd();
                }

            }
        };

        time--;

        timer.start();


        timer2 = new CountDownTimer(60 * time2 * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                bar.setProgress((int) millisUntilFinished / 1000);
            }


            @Override
            public void onFinish() {

            }
        };


        timer2.start();




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.exam_manu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            examEnd();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void examEnd() {

        timer.cancel();
        timer2.cancel();


        Bundle bundle = new Bundle();
        bundle.putString("userId" , userId);
        bundle.putString("examId" , examId);

        Intent intent = new Intent(TakingExamActivity.this , ResultActivity.class);

        intent.putExtras(bundle);



        startActivity(intent);
        overridePendingTransition(R.anim.slide_in , R.anim.slide_out);
        end = true;
        finish();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!end) {
            end = true;
            examEnd();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!end) {
            pause = true;
            Toast toast = new Toast(this);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toastCustom));
            toast.setView(view);
            toast.show();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (pause && !end) {
                        examEnd();
                    }

                }
            }, 10000);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        pause = false;

    }
}
