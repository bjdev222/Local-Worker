package com.lw.localworker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.lw.localworker.adapter.CityAdapter;
import com.lw.localworker.model.CityModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TestAcivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<CityModel> viewItems = new ArrayList<>();

    private CityAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private static final String TAG = "MainActivity";

EditText searchWork;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_acivity);
        mRecyclerView = (RecyclerView) findViewById(R.id.rcv);
        searchWork=findViewById(R.id.searchLocation);

        addItemsFromJSON();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
/*
        mAdapter = new CityAdapter(TestAcivity.this,this, viewItems);
*/
        mRecyclerView.setAdapter(mAdapter);


        searchWork.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mRecyclerView.setVisibility(View.VISIBLE);

                mAdapter.getFilter().filter(s);

                if(count==0)
                {
                    mRecyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void addItemsFromJSON() {
        try {

            String jsonDataString = readJSONDataFromFile();
            JSONArray jsonArray = new JSONArray(jsonDataString);

            for (int i=0; i<jsonArray.length(); ++i) {

                JSONObject itemObj = jsonArray.getJSONObject(i);

                String name = itemObj.getString("name");
                String state = itemObj.getString("state");

                CityModel holidays = new CityModel(name, state);
                viewItems.add(holidays);
            }

        } catch (JSONException | IOException e) {
            Log.d(TAG, "addItemsFromJSON: ", e);
        }
    }

    private String readJSONDataFromFile() throws IOException{

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
}