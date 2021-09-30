package com.lw.localworker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class FeedbackShowActivity extends AppCompatActivity {

    String title,uname,v,desc,date;

    TextView rtitle,rdesc,rdate;
    ImageView s1,s2,s3,s4,s5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_show);


        title=getIntent().getStringExtra("title");
        uname=getIntent().getStringExtra("uname");
        desc=getIntent().getStringExtra("desc");
        date=getIntent().getStringExtra("date");
        v=getIntent().getStringExtra("rate");

        initUI();

        setStar();
        setData();

    }

    private void setData() {
        rtitle.setText(title.trim());
        rdesc.setText(desc.trim());
        rdesc.setText(desc.trim());
        rdate.setText(date.trim());
    }

    private void initUI() {

        rtitle=findViewById(R.id.title);
        rdesc=findViewById(R.id.desc);
        s1=findViewById(R.id.s1);
        s2=findViewById(R.id.s2);
        s3=findViewById(R.id.s3);
        s4=findViewById(R.id.s4);
        s5=findViewById(R.id.s5);
        rdate=findViewById(R.id.date);

    }

    private void setStar() {

        if(v.equals("1")){
          s1.setImageResource(R.drawable.fs);
            s2.setImageResource(R.drawable.bs);
            s3.setImageResource(R.drawable.bs);
            s4.setImageResource(R.drawable.bs);
            s5.setImageResource(R.drawable.bs);
        }
        if(v.equals("2")){
            s1.setImageResource(R.drawable.fs);
            s2.setImageResource(R.drawable.fs);
            s3.setImageResource(R.drawable.bs);
            s4.setImageResource(R.drawable.bs);
            s5.setImageResource(R.drawable.bs);
        }
        if(v.equals("3")){

           s1.setImageResource(R.drawable.fs);
           s2.setImageResource(R.drawable.fs);
            s3.setImageResource(R.drawable.fs);
           s4.setImageResource(R.drawable.bs);
            s5.setImageResource(R.drawable.bs);
        }
        if(v.equals("4")){

            s1.setImageResource(R.drawable.fs);
          s2.setImageResource(R.drawable.fs);
            s3.setImageResource(R.drawable.fs);
           s4.setImageResource(R.drawable.fs);
           s5.setImageResource(R.drawable.bs);
        }
        if(v.equals("5")){

           s1.setImageResource(R.drawable.fs);
          s2.setImageResource(R.drawable.fs);
          s3.setImageResource(R.drawable.fs);
            s4.setImageResource(R.drawable.fs);
          s5.setImageResource(R.drawable.fs);
        }
    }
}