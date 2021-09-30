package com.lw.localworker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.lw.localworker.model.UModel;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;


public class UProfileActivity extends AppCompatActivity implements View.OnClickListener {


    EditText uName, uPhoneNumber, uWPNumber, uEmailId;
    Button profileUpdateBtn;

    FirebaseUser user;
    String phoneNumber;
    final String[] url2 = {" "};
    Uri imageUri, mCropImageUri;
    StorageReference storageReference;
    CircleImageView profileImage;
    String profileUrl;
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;
    //for image
    int flag = 0;

    Progress progress;
    ExecutorService executorService;

    ImageView infoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_profile);
        initUI();
        executorService = Executors.newFixedThreadPool(15);

        progress = new Progress();

        user = FirebaseAuth.getInstance().getCurrentUser();
        phoneNumber = user.getPhoneNumber();
        uPhoneNumber.setText(phoneNumber);

        profileImage.setOnClickListener(this);
        profileUpdateBtn.setOnClickListener(this);



        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("UserDetails").child(phoneNumber);
        retriveData();


    }


    private void initUI() {

        uName = findViewById(R.id.uName);
        uPhoneNumber = findViewById(R.id.uPhoneNumber);
        uWPNumber = findViewById(R.id.uWPNumber);
        uEmailId = findViewById(R.id.uEmailId);

        profileImage = findViewById(R.id.profileImage);

        profileUpdateBtn = findViewById(R.id.updateProfileBtn);
    }

    @Override
    public void onClick(View v) {
        if (v == profileImage) {
            onSelectImageClick(v);
            flag = 0;

        }
        if (v == profileUpdateBtn) {
/*
            progress.execute();
*/
            UploadDetail();


        }



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

                if (flag == 0) {
                    profileImage.setImageURI(result.getUri());

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


    private void UploadImage() {
        String url = UUID.randomUUID().toString();

        Date date = new Date();
        String localTime = new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss").format(date);

        final ProgressDialog progressDialog
                = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        FirebaseStorage storage = FirebaseStorage.getInstance();


        StorageReference ref = storage.getReference().child("UProfiles/" + phoneNumber + "/" + phoneNumber + " " + localTime + "_Profile");


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
                                        Toast.makeText(getApplicationContext(), String.valueOf(flag), Toast.LENGTH_SHORT).show();
                                        Log.d("TAG", "onSuccess: Flag is" + String.valueOf(flag));
                                        url2[0] = uri.toString();
                                        if (flag == 0) {
                                            profileUrl = url2[0];
                                        }


                                        Toast.makeText(getApplicationContext(), uri.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });


                                // Image uploaded successfully
                                // Dismiss dialog
                                progressDialog.dismiss();
                                Toast
                                        .makeText(getApplicationContext(),
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
                                .makeText(getApplicationContext(),
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


    private void retriveData() {


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
                if (snapshot.exists()) {
                    UModel wModel = snapshot.getValue(UModel.class);

                    // after getting the value we are setting
                    // our value to our text view in below line.


                    uName.setText(wModel.getuName());

                    uWPNumber.setText(wModel.getuWPNumber());
                    uEmailId.setText(wModel.getuEmailId());


                    profileUrl = (wModel.getuProfileUrl()).replaceAll("^\"|\"$", "");

                  /*  Glide.with(getApplicationContext()).load(profileUrl).into(profileImage);

                    Glide.with(getApplicationContext()).load(galleryUrl1).into(g1);
                    Glide.with(getApplicationContext()).load(galleryUrl2).into(g2);
                    Glide.with(getApplicationContext()).load(galleryUrl3).into(g3);
                    Glide.with(getApplicationContext()).load(galleryUrl4).into(g4);*/

                    Picasso.get().load(profileUrl).into(profileImage);


                    progressDialog.dismiss();


                } else {
                    progressDialog.dismiss();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(getApplicationContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UploadDetail() {

        final UModel wModel = new UModel();

        String suName = uName.getText().toString();
        String suPhoneNumber = uPhoneNumber.getText().toString();
        String suWPNumber = uWPNumber.getText().toString();
        String suEmailId = uEmailId.getText().toString();


        if (suName.isEmpty()) {
            uName.setError("Please Fill this Details");
        } else if (suWPNumber.isEmpty()) {
            uWPNumber.setError("Please Fill this Details");
        } else if (suEmailId.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(suEmailId).matches()) {

            uEmailId.setError("Please Fill this Details Carefully");
        } else if (profileUrl == null) {
            Toast.makeText(this, "Please Upload Profile Image", Toast.LENGTH_LONG).show();
        } else {


            wModel.setuName(suName);
            wModel.setuPhoneNumber(suPhoneNumber);
            wModel.setuWPNumber(suWPNumber);
            wModel.setuEmailId(suEmailId);


            wModel.setuProfileUrl(profileUrl);


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
                    Toast.makeText(getApplicationContext(), "data added", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // if the data is not added or it is cancelled then
                    // we are displaying a failure toast message.
                    Toast.makeText(getApplicationContext(), "Fail to add data " + error, Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    class Progress extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            UploadDetail();
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}