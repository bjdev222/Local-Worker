package com.lw.localworker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lw.localworker.adapter.CategoryAdapter;
import com.lw.localworker.model.CategoryModel;
import com.lw.localworker.model.SliderItem;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {


    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;

    ArrayList<SliderItem> coursesArrayList;

    RecyclerView catRecyclerview;

    CategoryAdapter categoryAdapter;
    private List<CategoryModel> listData;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
        window.setTitleColor(ContextCompat.getColor(this,R.color.black));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initUI();

        firebaseDatabase = FirebaseDatabase.getInstance();

        listData=new ArrayList<>();
        getCategoryInfo();


        GridLayoutManager gridLayoutManager=new GridLayoutManager(getApplicationContext(),2);
        catRecyclerview.setLayoutManager(gridLayoutManager);
    }

    private void initUI() {
        catRecyclerview=findViewById(R.id.cat_recycle);
    }


    private void getCategoryInfo() {



        final DatabaseReference nm= firebaseDatabase.getReference("Category");

        nm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()){
                        CategoryModel l=npsnapshot.getValue(CategoryModel.class);
                        listData.add(l);
                    }
                    catRecyclerview.setAdapter(new CategoryAdapter(listData,getApplicationContext(),0));



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}