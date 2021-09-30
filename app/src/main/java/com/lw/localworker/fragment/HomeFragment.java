package com.lw.localworker.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lw.localworker.CategoryActivity;
import com.lw.localworker.MainActivity;
import com.lw.localworker.R;
import com.lw.localworker.UserActivity;
import com.lw.localworker.adapter.CategoryAdapter;
import com.lw.localworker.adapter.SliderAdapter;
import com.lw.localworker.adapter.UserAdapter;
import com.lw.localworker.model.CategoryModel;
import com.lw.localworker.model.SliderItem;
import com.lw.localworker.model.WModel;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;


public class HomeFragment extends Fragment implements View.OnClickListener  {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    TextView search;
    View view;

    SliderView sliderView;

    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;

    ArrayList<SliderItem> coursesArrayList;

    RecyclerView catRecyclerview;

    CategoryAdapter categoryAdapter;
    private List<CategoryModel>listData;
    LocationManager locationManager;

    TextView seeMore;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2)  {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        initUI();

/*
getCurrentLocation();
*/

        coursesArrayList = new ArrayList<SliderItem>();

        firebaseDatabase = FirebaseDatabase.getInstance();

        listData=new ArrayList<>();

        getSliderInfo();

        getCategoryInfo();

        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),2);
        catRecyclerview.setLayoutManager(gridLayoutManager);

        search.setOnClickListener(this);

        seeMore.setOnClickListener(this);

        return view;
    }


    private void getCategoryInfo() {
        final ProgressDialog progressDialog
                = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please be patient...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        final DatabaseReference nm= firebaseDatabase.getReference("Category");

        nm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()){
                        CategoryModel l=npsnapshot.getValue(CategoryModel.class);
                        listData.add(l);
                    }
                    catRecyclerview.setAdapter(new CategoryAdapter(listData,getContext(),1));
                    progressDialog.dismiss();



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();

            }
        });
    }

    private void getSliderInfo() {


        final DatabaseReference sliderref = firebaseDatabase.getReference("Slider");


        sliderref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                coursesArrayList.add(snapshot.getValue(SliderItem.class));
                sliderView.setSliderAdapter(new SliderAdapter(getContext(),  coursesArrayList));
                sliderView.startAutoCycle();


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                coursesArrayList.remove(snapshot.getValue(String.class));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initUI() {
        search=view.findViewById(R.id.search);
         sliderView = view.findViewById(R.id.imageSlider);
         catRecyclerview=view.findViewById(R.id.cat_recycle);
         seeMore=view.findViewById(R.id.seeMore);


    }



    @Override
    public void onClick(View v) {
        if(v==search){
            Intent intent=new Intent(getContext(), UserActivity.class);
            startActivity(intent);
        }
        if(v==seeMore){
            Intent intent=new Intent(getContext(), CategoryActivity.class);
            startActivity(intent);

        }

    }



    @Override
    public void onStart() {
        super.onStart();

    }
}