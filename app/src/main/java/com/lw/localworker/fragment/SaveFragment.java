package com.lw.localworker.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lw.localworker.R;
import com.lw.localworker.UserActivity;
import com.lw.localworker.adapter.CityAdapter;
import com.lw.localworker.adapter.UserAdapter;
import com.lw.localworker.db.AppDatabase;
import com.lw.localworker.db.Save;
import com.lw.localworker.model.CityModel;
import com.lw.localworker.model.WModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SaveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SaveFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View view;

    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;

    RecyclerView rcv,cityRcv;
    EditText searchLocation,searchWork;

    LinearLayout editTextLL;

    WModel wModel;
    private List<WModel>listData;

    String currentCity;

    private CityAdapter mAdapter;
    private RecyclerView.LayoutManager cityLayoutManager;

    private List<CityModel> viewItems = new ArrayList<>();

    Integer catFlag=0;
    String keyword="";
    /*TestAdapter adapter;*/

    UserAdapter adapter;

    LocationManager locationManager;
    ProgressDialog progressDialog;


    List<WModel> newList=new ArrayList<>();


    public SaveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SaveFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SaveFragment newInstance(String param1, String param2) {
        SaveFragment fragment = new SaveFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_save, container, false);

        initUI();

        wModel=new WModel();

        firebaseDatabase = FirebaseDatabase.getInstance();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());

        rcv.setLayoutManager(linearLayoutManager);

        listData=new ArrayList<>();

        AppDatabase db=AppDatabase.getDBInstance(getContext());

        List<Save> list=db.userDao().getAll();
        Log.d("TAG", "onCreateView: list"+list);



        final DatabaseReference nm= firebaseDatabase.getReference("WorkersDetails");


        // data is retrived but hidden
        nm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()){
                        WModel l=npsnapshot.getValue(WModel.class);
                        newList.clear();

                        listData.add(l);
                        for(int i=0;i<listData.size();i++){

                            for(int j=0;j<list.size();j++){
                                if(listData.get(i).getWPNumber().equals(list.get(j).getWPNumber())){


                                    newList.add(listData.get(i));
                                    Log.d("TAG,", "onDataChange: "+listData.get(i));

                                }
                            }

                        }



                    }

                    adapter=new UserAdapter(newList,getContext());
                    rcv.setAdapter(adapter);



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return  view;

    }

    //for chrck whether it is search or come from category


    private void initUI() {

        rcv=view.findViewById(R.id.rcv);

    }


}