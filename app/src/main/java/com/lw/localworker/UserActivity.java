package com.lw.localworker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lw.localworker.adapter.CityAdapter;
import com.lw.localworker.adapter.UserAdapter;
import com.lw.localworker.fragment.HomeFragment;
import com.lw.localworker.model.CityModel;
import com.lw.localworker.model.ReviewModel;
import com.lw.localworker.model.UModel;
import com.lw.localworker.model.WModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class UserActivity extends AppCompatActivity implements LocationListener {

    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;

    RecyclerView rcv, cityRcv;
    EditText searchLocation, searchWork, catSeaarchLoc;

    LinearLayout editTextLL;

    WModel wModel;
    private List<WModel> listData;
    private List<WModel> catListData;
    String currentCity;

    private CityAdapter mAdapter;
    private RecyclerView.LayoutManager cityLayoutManager;
    private List<CityModel> viewItems = new ArrayList<>();

    Integer catFlag = 0;
    String keyword = "";

    UserAdapter adapter;
    LocationManager locationManager;
    ProgressDialog progressDialog;
    public TextView noResult;

    public static TextView txt;

    CardView lwork;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        window.setTitleColor(ContextCompat.getColor(this, R.color.black));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //category on click
        catFlag = getIntent().getIntExtra("flag", 0);
        keyword = getIntent().getStringExtra("keyword");
        Log.d("TAG", "onCreate: cat flag : " + catFlag);
        Log.d("TAG", "onCreate: cat keywoprd : " + keyword);


        initUI();

        if (catFlag == 1 && !keyword.equals(null)) {
            lwork.setVisibility(View.GONE);
        }

        wModel = new WModel();

        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference("WorkersDetails");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        rcv.setLayoutManager(linearLayoutManager);

        listData = new ArrayList<>();
        catListData = new ArrayList<>();

        final DatabaseReference nm = firebaseDatabase.getReference("WorkersDetails");

        addItemsFromJSON();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        cityRcv.setHasFixedSize(true);

        // use a linear layout manager
        cityLayoutManager = new LinearLayoutManager(this);
        cityRcv.setLayoutManager(cityLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new CityAdapter(UserActivity.this, this, viewItems);
        cityRcv.setAdapter(mAdapter);

        // data is retrived but hidden
        nm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {
                        WModel l = npsnapshot.getValue(WModel.class);
                        listData.add(l);


                        if (catFlag == 1 && !keyword.equals(null)) {
                            for (int i = 0; i < listData.size(); i++) {
                                if (listData.get(i).getWDesc().contains(keyword) || listData.get(i).getWWorkName().contains(keyword)) {
                                    catListData.add(listData.get(i));



                                }

                            }
                            adapter = new UserAdapter(catListData, getApplicationContext());
                            rcv.setAdapter(adapter);
                        } else {
                            adapter = new UserAdapter(listData, getApplicationContext());
                            rcv.setAdapter(adapter);
                        }

                    }


                    //for category
                    check();


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //data filter by city
        searchLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString() == "") {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    prefs.edit().putString("Ework", keyword).commit();
                    adapter.getFilter2().filter(s);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s == "") {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    prefs.edit().putString("Ework", keyword).commit();
                    adapter.getFilter2().filter(s);
                }
                cityRcv.setVisibility(View.VISIBLE);
                if (catFlag == 1 && !keyword.equals(null)) {
                    mAdapter.getFilter().filter(s);
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    prefs.edit().putString("Ework", keyword).commit();
                    adapter.getFilter2().filter(s);

                } else {
                    mAdapter.getFilter().filter(s);
                }

                if (count == 0) {
                    cityRcv.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() == "") {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    prefs.edit().putString("Ework", keyword).commit();
                    adapter.getFilter2().filter(s);
                }
            }
        });


      /*  catSeaarchLoc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                prefs.edit().putString("Ework", keyword).commit();
                adapter.getFilter2().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

        //work find by filter
        searchWork.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                rcv.setVisibility(View.VISIBLE);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                prefs.edit().putString("Ecity", searchLocation.getText().toString()).commit();
                adapter.getFilter().filter(s);

                if (count == 0) {
                    rcv.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    //for chrck whether it is search or come from category
    private void check() {

        if (catFlag == 1 && !keyword.equals(null)) {
            rcv.setVisibility(View.VISIBLE);
/*
            adapter.getFilter().filter(keyword);
*/


        }
    }


    private void initUI() {

        rcv = findViewById(R.id.rcv);
        cityRcv = findViewById(R.id.cityRcv);
        searchLocation = findViewById(R.id.searchLocation);
        searchWork = findViewById(R.id.searchWork);
        lwork = findViewById(R.id.lwork);

        editTextLL = findViewById(R.id.ll);
        noResult = findViewById(R.id.noResult);


    }


    private void addItemsFromJSON() {
        try {

            String jsonDataString = readJSONDataFromFile();
            JSONArray jsonArray = new JSONArray(jsonDataString);

            for (int i = 0; i < jsonArray.length(); ++i) {

                JSONObject itemObj = jsonArray.getJSONObject(i);

                String name = itemObj.getString("name");
                String state = itemObj.getString("state");

                CityModel holidays = new CityModel(name, state);
                viewItems.add(holidays);
            }

        } catch (JSONException | IOException e) {
            Log.d("TAG", "addItemsFromJSON: ", e);
        }
    }

    private String readJSONDataFromFile() throws IOException {

        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();

        try {

            String jsonString = null;
            inputStream = getResources().openRawResource(R.raw.cityname);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, "UTF-8"));

            while ((jsonString = bufferedReader.readLine()) != null) {
                builder.append(jsonString);
            }

        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return new String(builder);
    }


    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {

        try {


            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Loading...");
            progressDialog.show();
            progressDialog.setCancelable(false);
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 5, UserActivity.this);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void onLocationChanged(Location location) {
        Toast.makeText(getApplicationContext(), "" + location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_SHORT).show();

        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getSubAdminArea();
            String state = addresses.get(0).getAdminArea();

            Log.d("TAG", "onLocationChanged: " + addresses);
            Log.d("TAG", "City : " + city);
            Log.d("TAG", "State : " + state);
            Toast.makeText(getApplicationContext(), address, Toast.LENGTH_SHORT).show();

            currentCity = city + "," + "  " + state;
            progressDialog.dismiss();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}