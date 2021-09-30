package com.lw.localworker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lw.localworker.adapter.CityAdapter;
import com.lw.localworker.adapter.CityAdapterP;
import com.lw.localworker.model.CityModel;
import com.lw.localworker.model.WModel;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class WProfileActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText wName,wPhoneNumber,wWPNumber,wEmailId,wAddress,wCity,wPincode,wState,wService,wDesc;
    TextView editPhotoText;
    Button updateProfile,logoutBtn,next;
    FirebaseUser user ;
    String phoneNumber;
    final String[] url2 = {" "};
    Uri imageUri,mCropImageUri;
    StorageReference storageReference;
    CircleImageView profileImage,g1,g2,g3,g4;
    String profileUrl,galleryUrl1,galleryUrl2,galleryUrl3,galleryUrl4;
    FirebaseDatabase firebaseDatabase;

    //search city
    private RecyclerView.LayoutManager layoutManager;
    private CityAdapterP mAdapter;
    private List<CityModel> viewItems = new ArrayList<>();
    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    RecyclerView recycleCity;
    //for image
    int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_w_profile);

        initUI();
        user= FirebaseAuth.getInstance().getCurrentUser();
         phoneNumber = user.getPhoneNumber();
        wPhoneNumber.setText(phoneNumber);

        updateProfile.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
        editPhotoText.setOnClickListener(this);
        next.setOnClickListener(this);

        g1.setOnClickListener(this);
        g2.setOnClickListener(this);
        g3.setOnClickListener(this);
        g4.setOnClickListener(this);


        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("WorkersDetails").child(phoneNumber);
        retriveData();


        addItemsFromJSON();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recycleCity.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recycleCity.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new CityAdapterP(WProfileActivity.this,this, viewItems);
        recycleCity.setAdapter(mAdapter);


        wCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                recycleCity.setVisibility(View.VISIBLE);

                mAdapter.getFilter().filter(s);

                if(count==0)
                {
                    recycleCity.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });






    }

    private void retriveData() {
        // calling add value event listener method
        // for getting the values from database.


        final ProgressDialog progressDialog
                = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // this method is call to get the realtime
                // updates in the data.
                // this method is called when the data is
                // changed in our Firebase console.
                // below line is for getting the data from
                // snapshot of our database.
                if(snapshot.exists()) {
                    WModel wModel = snapshot.getValue(WModel.class);

                    // after getting the value we are setting
                    // our value to our text view in below line.



                    wName.setText(wModel.getWName());

                    wWPNumber.setText(wModel.getWWNumber());
                    wAddress.setText(wModel.getWAddress());
                    wEmailId.setText(wModel.getWEmailId());
                    wAddress.setText(wModel.getWAddress());
                    wCity.setText(wModel.getWCity());
                    wPincode.setText(wModel.getWPincode());
                    wState.setText(wModel.getWState());
                    wService.setText(wModel.getWWorkName());
                    wDesc.setText(wModel.getWDesc());


                    wModel.getWProfileUrl().isEmpty();

                    profileUrl=(wModel.getWProfileUrl()).replaceAll("^\"|\"$", "");
                    galleryUrl1=(wModel.getWGalleryUrl1()).replaceAll("^\"|\"$", "");
                    galleryUrl2=(wModel.getWGalleryUrl2()).replaceAll("^\"|\"$", "");
                    galleryUrl3=(wModel.getWGalleryUrl3()).replaceAll("^\"|\"$", "");
                    galleryUrl4=(wModel.getWGalleryUr4()).replaceAll("^\"|\"$", "");

                  /*  Glide.with(getApplicationContext()).load(profileUrl).into(profileImage);

                    Glide.with(getApplicationContext()).load(galleryUrl1).into(g1);
                    Glide.with(getApplicationContext()).load(galleryUrl2).into(g2);
                    Glide.with(getApplicationContext()).load(galleryUrl3).into(g3);
                    Glide.with(getApplicationContext()).load(galleryUrl4).into(g4);*/

                    Picasso.get().load(profileUrl).into(profileImage);
                    Picasso.get().load(galleryUrl1).into(g1);
                    Picasso.get().load(galleryUrl2).into(g2);
                    Picasso.get().load(galleryUrl3).into(g3);
                    Picasso.get().load(galleryUrl4).into(g4);


                    progressDialog.dismiss();


                }
                else {
                    progressDialog.dismiss();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(WProfileActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initUI() {

        wName=findViewById(R.id.wName);
        wPhoneNumber=findViewById(R.id.wPhoneNumber);
        wWPNumber=findViewById(R.id.wWPNumber);
        wEmailId=findViewById(R.id.wEmailId);
        wAddress=findViewById(R.id.wAddress);
        wCity=findViewById(R.id.wCity);
        wPincode=findViewById(R.id.wPincode);
        wState=findViewById(R.id.wState);
        wService=findViewById(R.id.wService);
        wDesc=findViewById(R.id.wDesc);

        updateProfile=findViewById(R.id.updateProfileBtn);
        logoutBtn=findViewById(R.id.logoutBtn);
        next=findViewById(R.id.next);

        profileImage=findViewById(R.id.profileImage);

        editPhotoText=findViewById(R.id.editPhotoText);

        g1=findViewById(R.id.g1);
        g2=findViewById(R.id.g2);
        g3=findViewById(R.id.g3);
        g4=findViewById(R.id.g4);

        recycleCity=findViewById(R.id.rcv);
    }

    @Override
    public void onClick(View v) {
        if(v==editPhotoText){
            onSelectImageClick(v);
            flag=0;

        }
        if(v==updateProfile){
            UploadDetail();
        }
        if(v==g1){
            flag=1;
            onSelectImageClick(v);

        }

        if(v==g2){
            flag=2;
            onSelectImageClick(v);


        }

        if(v==g3){
            flag=3;
            onSelectImageClick(v);

        }

        if(v==g4){
            flag=4;
            onSelectImageClick(v);


        }
        if(v==logoutBtn){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            prefs.edit().putBoolean("Islogin", false).commit(); // islogin is a boolean value of your login status
            Intent intent=new Intent(getApplicationContext(),WLoginActivity.class);
            startActivity(intent);
        }
        if(v==next){
            Intent intent=new Intent(getApplicationContext(),UserActivity.class);
            startActivity(intent);
        }
    }

    private void UploadDetail() {

        final WModel wModel=new WModel();

        String swName=wName.getText().toString();
        String swPhoneNumber=wPhoneNumber.getText().toString();
        String swWPNumber=wWPNumber.getText().toString();
        String swEmailId=wEmailId.getText().toString();
        String swAddress=wAddress.getText().toString();
        String swCity=wCity.getText().toString().toLowerCase();
        String swPincode=wPincode.getText().toString();
        String swState=wState.getText().toString();
        String swService=wService.getText().toString();
        String swDesc=wDesc.getText().toString();


        if(swName.isEmpty()){
            wName.setError("Please Fill this Details");
        }
        else if(swWPNumber.isEmpty()){
            wWPNumber.setError("Please Fill this Details");
        }
        else if(swEmailId.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(swEmailId).matches()){

            wEmailId.setError("Please Fill this Details Carefully");
        }
        else if(swAddress.isEmpty()){
            wAddress.setError("Please Fill this Details");
        }
        else if(swCity.isEmpty()){
            wCity.setError("Please Fill this Details");
        }
        else if(swPincode.isEmpty()){
            wPincode.setError("Please Fill this Details");
        }
        else if(swState.isEmpty()){
            wState.setError("Please Fill this Details");
        }
        else if(swService.isEmpty()){
            wState.setError("Please Fill this Details");
        }
        else if(swDesc.isEmpty() || swDesc.length()>100){
            wDesc.setError("Please Fill this Details or Description length should have maximum 100 characters");
        }
        else if(profileUrl==null){
            Toast.makeText(this, "Please Upload Profile Image", Toast.LENGTH_LONG).show();
        }
        else if(galleryUrl1==null){
            Toast.makeText(this, "Please Upload Work Gallery Image", Toast.LENGTH_LONG).show();
        }
        else if(galleryUrl2==null){
            Toast.makeText(this, "Please Upload Work Gallery Image", Toast.LENGTH_LONG).show();
        }
        else if(galleryUrl3==null){
            Toast.makeText(this, "Please Upload Work Gallery Image", Toast.LENGTH_LONG).show();
        }
        else if(galleryUrl4==null){
            Toast.makeText(this, "Please Upload Work Gallery Image", Toast.LENGTH_LONG).show();
        }
        else {


            wModel.setWName(swName);
            wModel.setWPNumber(swPhoneNumber);
            wModel.setWWNumber(swWPNumber);
            wModel.setWEmailId(swEmailId);
            wModel.setWAddress(swAddress);
            wModel.setWCity(swCity);
            wModel.setWPincode(swPincode);
            wModel.setWState(swState);
            wModel.setWWorkName(swService);
            wModel.setWDesc(swDesc);
            wModel.setWSearch(swCity.toLowerCase()+" "+swService.toLowerCase());


            wModel.setWProfileUrl(profileUrl);
            wModel.setWGalleryUrl1(galleryUrl1);
            wModel.setWGalleryUrl2(galleryUrl2);
            wModel.setWGalleryUrl3(galleryUrl3);
            wModel.setWGalleryUr4(galleryUrl4);


            // we are use add value event listener method
            // which is called with database reference.
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // inside the method of on Data change we are setting
                    // our object class to our database reference.
                    // data base reference will sends data to firebase.
                    databaseReference.setValue(wModel);

                    // after adding this data we are showing toast message.
                    Toast.makeText(WProfileActivity.this, "data added", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // if the data is not added or it is cancelled then
                    // we are displaying a failure toast message.
                    Toast.makeText(WProfileActivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
                }
            });
        }


    }


    private void UploadImage() {
        String url = UUID.randomUUID().toString();

        Date date=new Date();
        String localTime=new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss").format(date);

        final ProgressDialog progressDialog
                = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        FirebaseStorage storage = FirebaseStorage.getInstance();


        StorageReference ref = storage.getReference().child("WGallery/" + phoneNumber + "/" + phoneNumber + " " + localTime + "_Gallery");



        if(flag==0) {
             ref = storage.getReference().child("WProfiles/"+phoneNumber+"/"+phoneNumber+" "+localTime+"_Profile");
        }

        if(flag==1) {
             ref = storage.getReference().child("WGallery/" + phoneNumber + "/" + phoneNumber + " " + localTime + "_Gallery_1");
        }

        if(flag==2) {
             ref = storage.getReference().child("WGallery/" + phoneNumber + "/" + phoneNumber + " " + localTime + "_Gallery_2");
        }

        if(flag==3) {
             ref = storage.getReference().child("WGallery/" + phoneNumber + "/" + phoneNumber + " " + localTime + "_Gallery_3");
        }

        if(flag==4) {
            ref = storage.getReference().child("WGallery/" + phoneNumber + "/" + phoneNumber + " " + localTime + "_Gallery_4");
        }


        Log.e("Url is", url);

        // adding listeners on upload
        final StorageReference finalRef = ref;
        ref.putFile(imageUri)
                .addOnSuccessListener(
                        new OnSuccessListener<UploadTask.TaskSnapshot>() {


                            // here is Url for photo
                            // Task<Uri> downloadUrl = ref.getDownloadUrl();

                            @Override
                            public void onSuccess(
                                    UploadTask.TaskSnapshot taskSnapshot) {
                                finalRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        //uri will be final url of image
                                        Log.d("onSuccess: uri= ", uri.toString());
                                        Toast.makeText(WProfileActivity.this, String.valueOf(flag), Toast.LENGTH_SHORT).show();
                                        Log.d("TAG", "onSuccess: Flag is"+String.valueOf(flag));
                                        url2[0] = uri.toString();
                                        if(flag==0){
                                            profileUrl=url2[0];
                                        }
                                        if(flag==1){
                                            galleryUrl1=url2[0];
                                        }
                                        if(flag==2){
                                            galleryUrl2=url2[0];
                                        }
                                        if(flag==3){
                                            galleryUrl3=url2[0];
                                        }
                                        if(flag==4){
                                            galleryUrl4=url2[0];
                                        }

                                           Toast.makeText(WProfileActivity.this,uri.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });


                                // Image uploaded successfully
                                // Dismiss dialog
                                progressDialog.dismiss();
                                Toast
                                        .makeText(WProfileActivity.this,
                                                "Image Uploaded!!",
                                                Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }

                )

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        // Error, Image not uploaded
                        progressDialog.dismiss();
                        Toast
                                .makeText(WProfileActivity.this,
                                        "Failed " + e.getMessage(),
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .addOnProgressListener(
                        new OnProgressListener<UploadTask.TaskSnapshot>() {

                            // Progress Listener for loading
                            // percentage on the dialog box
                            @Override
                            public void onProgress(
                                    UploadTask.TaskSnapshot taskSnapshot) {
                                double progress
                                        = (100.0
                                        * taskSnapshot.getBytesTransferred()
                                        / taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage(
                                        "Uploaded "
                                                + (int) progress + "%");
                            }
                        });

    }



    public void onSelectImageClick(View view) {
        CropImage.startPickImageActivity(this);
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of pick image chooser
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                if(flag==0){
                    profileImage.setImageURI(result.getUri());

                }
                if(flag==1){
                    g1.setImageURI(result.getUri());

                }
                if(flag==2){
                    g2.setImageURI(result.getUri());

                }
                if(flag==3){
                    g3.setImageURI(result.getUri());

                }
                if(flag==4){
                    g4.setImageURI(result.getUri());

                }
                UploadImage();


                Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
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
            Log.d("TAG", "addItemsFromJSON: ", e);
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