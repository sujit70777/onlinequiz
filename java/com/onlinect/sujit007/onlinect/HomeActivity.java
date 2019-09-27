package com.onlinect.sujit007.onlinect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.onlinect.sujit007.onlinect.exam.ExamActivity;

public class HomeActivity extends AppCompatActivity {
    Button startExam , avalableAns;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        startExam = (Button) findViewById(R.id.Exambtn);
        avalableAns = (Button) findViewById(R.id.AvalableQuesbtn);
        id = getIntent().getStringExtra("id");
      //  Toast.makeText(getApplicationContext(), id, Toast.LENGTH_LONG).show();

        startExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ExamActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id" , id);
                bundle.putString("type" , "1");
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

 avalableAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ExamActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id" , id);
                bundle.putString("type" , "2");
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });



    }

}
