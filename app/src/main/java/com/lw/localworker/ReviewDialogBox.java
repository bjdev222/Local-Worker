package com.lw.localworker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lw.localworker.model.ReviewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReviewDialogBox  extends BottomSheetDialogFragment implements View.OnClickListener {


    Button submit;
    ImageView s1,s2,s3,s4,s5;
    TextInputEditText title,desc;
    String stitle,sdesc,wNumber,uNumber;
    ReviewModel reviewModel;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user ;
    Date date;
    String localTime;


    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;
    View v;
    int rate=0;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState)
    {
         v = inflater.inflate(R.layout.activity_review_dialog_box,
                container, false);

        initUI();


        user= FirebaseAuth.getInstance().getCurrentUser();
        uNumber = user.getPhoneNumber();



         date=new Date();
         localTime=new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss").format(date);


        firebaseDatabase = FirebaseDatabase.getInstance();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        wNumber = prefs.getString("pNumber", ""); // get value of Worker phone num
        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("Feedback/"+wNumber).child(String.valueOf(date));


        reviewModel=new ReviewModel();
        s1.setOnClickListener(this);
        s2.setOnClickListener(this);
        s3.setOnClickListener(this);
        s4.setOnClickListener(this);
        s5.setOnClickListener(this);
        submit.setOnClickListener(this);









       /* Button algo_button = v.findViewById(R.id.algo_button);

        algo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getActivity(),
                        "Algorithm Shared", Toast.LENGTH_SHORT)
                        .show();
                dismiss();
            }
        });*/


        return v;
    }

    private void initUI() {

        submit=v.findViewById(R.id.submit);

        s1=v.findViewById(R.id.s1);
        s2=v.findViewById(R.id.s2);
        s3=v.findViewById(R.id.s3);
        s4=v.findViewById(R.id.s4);
        s5=v.findViewById(R.id.s5);

        title=v.findViewById(R.id.title);
        desc=v.findViewById(R.id.desc);
    }

    @Override
    public void onClick(View v) {
        if(v==s1){
            rate=1;
            s1.setImageResource(R.drawable.fs);
            s2.setImageResource(R.drawable.bs);
            s3.setImageResource(R.drawable.bs);
            s4.setImageResource(R.drawable.bs);
            s5.setImageResource(R.drawable.bs);
        }
        if(v==s2){
            rate=2;
            s1.setImageResource(R.drawable.fs);
            s2.setImageResource(R.drawable.fs);
            s3.setImageResource(R.drawable.bs);
            s4.setImageResource(R.drawable.bs);
            s5.setImageResource(R.drawable.bs);
        }
        if(v==s3){
            rate=3;
            s1.setImageResource(R.drawable.fs);
            s2.setImageResource(R.drawable.fs);
            s3.setImageResource(R.drawable.fs);
            s4.setImageResource(R.drawable.bs);
            s5.setImageResource(R.drawable.bs);
        }
        if(v==s4){
            rate=4;
            s1.setImageResource(R.drawable.fs);
            s2.setImageResource(R.drawable.fs);
            s3.setImageResource(R.drawable.fs);
            s4.setImageResource(R.drawable.fs);
            s5.setImageResource(R.drawable.bs);
        }
        if(v==s5){
            rate=5;
            s1.setImageResource(R.drawable.fs);
            s2.setImageResource(R.drawable.fs);
            s3.setImageResource(R.drawable.fs);
            s4.setImageResource(R.drawable.fs);
            s5.setImageResource(R.drawable.fs);
        }
        if(v==submit){
            sendFeedback();
        }
    }

    private void sendFeedback() {

        stitle=title.getText().toString();
        sdesc=desc.getText().toString();

        if(stitle==null){
            title.setError("Please Fill this details");
        }
        else if(sdesc==null){
            desc.setError("Please Fill this details");
        }
        else if(rate==0){
            Toast.makeText(getContext(), "Please give star!!!", Toast.LENGTH_SHORT).show();
        }
        else {




            reviewModel.setTitle(stitle);
            reviewModel.setDesc(sdesc);
            reviewModel.setRate(String.valueOf(rate));
            reviewModel.setUname(uNumber);
            reviewModel.setWphone(wNumber);
            reviewModel.setDate(localTime);


            // we are use add value event listener method
            // which is called with database reference.
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // inside the method of on Data change we are setting
                    // our object class to our database reference.
                    // data base reference will sends data to firebase.
                    databaseReference.setValue(reviewModel);

                    // after adding this data we are showing toast message.
                    Toast.makeText(getContext(), "data added", Toast.LENGTH_SHORT).show();
                    dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // if the data is not added or it is cancelled then
                    // we are displaying a failure toast message.
                    Toast.makeText(getContext(), "Fail to add data " + error, Toast.LENGTH_SHORT).show();
                }
            });
        }


        }

    }