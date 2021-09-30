package com.lw.localworker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lw.localworker.db.AppDatabase;
import com.lw.localworker.db.Save;
import com.lw.localworker.model.WModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {


    String name,work,pnum,desc,wsap,email,address,purl,gurl1,gurl2,gurl3,gurl4;
    TextView wName,wWork,wPNumber,wAddress,rating,wDesc;
    CircleImageView galleryImage1,galleryImage2,galleryImage3,galleryImage4;
    ImageView call,mail,whatsap,save;

    CircleImageView profileImage;

    FirebaseDatabase firebaseDatabase;

    int maxid=0;

    int review=0;
    long totalReview;
    DatabaseReference databaseReference;
    WModel wModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        name=getIntent().getStringExtra("name");
        work=getIntent().getStringExtra("work");
        pnum=getIntent().getStringExtra("mnumber");
        email=getIntent().getStringExtra("email");
        desc=getIntent().getStringExtra("desc");
        wsap=getIntent().getStringExtra("wsap");
        address=getIntent().getStringExtra("address");
        purl=getIntent().getStringExtra("purl");
        gurl1=getIntent().getStringExtra("g1");
        gurl2=getIntent().getStringExtra("g2");
        gurl3=getIntent().getStringExtra("g3");
        gurl4=getIntent().getStringExtra("g4");

         wModel = (WModel) getIntent().getSerializableExtra("Model");


        initUI();

        AppDatabase db=AppDatabase.getDBInstance(getApplicationContext());
        List<Save> list=db.userDao().loadAllByIds(pnum);

        //fill heart if it is in wish book
        if(list.size()!=0) {
            save.setImageResource(R.drawable.fheart);
        }


        setData();


        checkFeedback();

        call.setOnClickListener(this);
        whatsap.setOnClickListener(this);
        mail.setOnClickListener(this);

        rating.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    private void checkFeedback() {

        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference("Feedback/"+pnum);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    maxid= (int) snapshot.getChildrenCount();
                    //  Toast.makeText(Dashboard.this, "children is"+maxid, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String desk = ds.child("rate").getValue(String.class);
                     totalReview=ds.getChildrenCount();
                    Log.d("TAG", "onDataChange: "+totalReview);

                    assert desk != null;
                    review=review + Integer.parseInt(desk);

                    float y=desk.length();

                }
                Log.e("TAG", String.valueOf(review));

                float finalRating=(float)review/maxid;
                Log.d("TAG", "fn: "+finalRating);


                rating.setText(String.valueOf(finalRating)+" Ratings  "+String.valueOf(maxid)+" Reviews");



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        };
        databaseReference.addListenerForSingleValueEvent(valueEventListener);

    }

    private void setData() {
        final ProgressDialog progressDialog
                = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please be patient...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        wName.setText(name);
        wWork.setText(work);
        wDesc.setText(desc);
        wPNumber.setText(pnum);
        wAddress.setText(address);

        Picasso.get().load(purl).into(profileImage);
        Picasso.get().load(gurl1).into(galleryImage1);
        Picasso.get().load(gurl2).into(galleryImage2);
        Picasso.get().load(gurl3).into(galleryImage3);
        Picasso.get().load(gurl4).into(galleryImage4);


        progressDialog.dismiss();
    }

    private void initUI() {
        wName=findViewById(R.id.name);
        wWork=findViewById(R.id.work);
        wDesc=findViewById(R.id.desc);
        wPNumber=findViewById(R.id.pnumber);
        wAddress=findViewById(R.id.address);
        rating=findViewById(R.id.rating);

        profileImage=findViewById(R.id.profileImage);
        galleryImage1=findViewById(R.id.g1);
        galleryImage2=findViewById(R.id.g2);
        galleryImage3=findViewById(R.id.g3);
        galleryImage4=findViewById(R.id.g4);

        call=findViewById(R.id.call);
        mail=findViewById(R.id.mail);
        whatsap=findViewById(R.id.wp);

        save=findViewById(R.id.save);



    }


    @Override
    public void onClick(View v) {
        if(v==call){
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+pnum));
            startActivity(intent);
        }
        if(v==whatsap){
            String url = "https://api.whatsapp.com/send?phone=+91"+wsap;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
        if(v==mail){
            try {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                intent.putExtra(Intent.EXTRA_SUBJECT, "My feedback");
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "There are no email client installed on your device.", Toast.LENGTH_SHORT).show();
            }
        }
        if(v==rating){

            Intent intent=new Intent(getApplicationContext(),ReviewShowActivity.class);
            startActivity(intent);

        }
        if(v==save){

            saveData();


        }
    }

    private void saveData() {
        AppDatabase db=AppDatabase.getDBInstance(getApplicationContext());
        List<Save> list=db.userDao().loadAllByIds(pnum);

        if(list.size()==0) {
            save.setImageResource(R.drawable.fheart);
            Save save=new Save(pnum);
            db.userDao().insertAll(save);
            Toast.makeText(this, "Added success", Toast.LENGTH_SHORT).show();

            Log.d("TAG", "saveData: "+db.userDao().getAll());
        }
        else {
            save.setImageResource(R.drawable.save);

            db.userDao().deleteData(pnum);
            Toast.makeText(this, "Remove From Wishlist Added ", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "Remove : "+db.userDao().getAll());


        }



    }


}