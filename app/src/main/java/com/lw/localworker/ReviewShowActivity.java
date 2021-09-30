package com.lw.localworker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lw.localworker.adapter.FeedbackAdapter;
import com.lw.localworker.model.ReviewModel;

public class ReviewShowActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;



    FeedbackAdapter feedbackAdapter;
    FloatingActionButton reviewBtn;
    RecyclerView rcv;

    String wNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_show);

        initUI();
        reviewBtn.setOnClickListener(this);

        firebaseDatabase = FirebaseDatabase.getInstance();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        wNumber = prefs.getString("pNumber", ""); // get value of Worker phone num

        databaseReference = firebaseDatabase.getReference("Feedback/"+wNumber);


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);

        rcv.setLayoutManager(linearLayoutManager);

        FirebaseRecyclerOptions<ReviewModel> options
                = new FirebaseRecyclerOptions.Builder<ReviewModel>()
                .setQuery(databaseReference, ReviewModel.class)
                .build();

        feedbackAdapter = new FeedbackAdapter(options,this);
        // Connecting Adapter class with the Recycler view*/
        rcv.setAdapter(feedbackAdapter);
    }

    private void initUI() {
        reviewBtn=findViewById(R.id.reviewBtn);
        rcv=findViewById(R.id.rcv);

    }

    @Override
    public void onClick(View v) {
        if(v==reviewBtn){

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            Boolean Islogin = prefs.getBoolean("Islogin", false); // get value of last login status


            if (Islogin) {
                ReviewDialogBox bottomSheet = new ReviewDialogBox();
                bottomSheet.show(getSupportFragmentManager(),"ModalBottomSheet");

            }
            else {
                Toast.makeText(this, "Please login to your account", Toast.LENGTH_SHORT).show();
            }


        }
    }


    @Override protected void onStart()
    {
        super.onStart();
        feedbackAdapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stoping of the activity
    @Override protected void onStop()
    {
        super.onStop();
        feedbackAdapter.stopListening();
    }
}