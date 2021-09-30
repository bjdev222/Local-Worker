package com.lw.localworker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class WLoginActivity extends AppCompatActivity {

    // variable for FirebaseAuth class
    private FirebaseAuth mAuth;


    EditText t2, t1;
    Button b2, b3;
    String phonenumber;
    String otpid;

    // variable for our text input
    // field for phone and OTP.
    private EditText edtPhone, edtOTP;

    TextView resend, resendInfo;

    RelativeLayout otpLL;

    // buttons for generating OTP and verifying OTP
    private Button verifyOTPBtn, generateOTPBtn;


    boolean Islogin;
    // string for storing our verification ID
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_w_login);
        // below line is for getting instance
        // of our FirebaseAuth.
        mAuth = FirebaseAuth.getInstance();

        // initializing variables for button and Edittext.
        edtPhone = findViewById(R.id.idEdtPhoneNumber);
        edtOTP = findViewById(R.id.idEdtOtp);
        verifyOTPBtn = findViewById(R.id.idBtnVerify);
        generateOTPBtn = findViewById(R.id.idBtnGetOtp);

        otpLL = findViewById(R.id.otpll);
        resend = findViewById(R.id.resend);
        resendInfo = findViewById(R.id.resendInfo);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Islogin = prefs.getBoolean("Islogin", false); // get value of last login status


        if (Islogin) {
            Intent i = new Intent(WLoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();

        }

        generateOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonenumber = edtPhone.getText().toString();
                initiateotp();
                otpLL.setVisibility(View.VISIBLE);

                resendTime();


            }
        });


        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonenumber = edtPhone.getText().toString();
                initiateotp();
                Toast.makeText(WLoginActivity.this, "Resend", Toast.LENGTH_SHORT).show();
                resend.setVisibility(View.GONE);
                resendInfo.setVisibility(View.VISIBLE);
                resendTime();

            }
        });


        verifyOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtOTP.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(), "Blank Field can not be processed", Toast.LENGTH_LONG).show();
                else if (edtOTP.getText().toString().length() != 6)
                    Toast.makeText(getApplicationContext(), "INvalid OTP", Toast.LENGTH_LONG).show();
                else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid, edtOTP.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }

            }
        });


    }

    private void resendTime() {
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                //here you can have your logic to set text to edittext
                resendInfo.setText("Resend after " + millisUntilFinished / 1000 + " Seconds");

            }

            public void onFinish() {
                resend.setVisibility(View.VISIBLE);
                resendInfo.setVisibility(View.GONE);
            }

        }.start();


    }

    private void initiateotp() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phonenumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        otpid = s;
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });        // OnVerificationStateChangedCallbacks

    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Signin Code success", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(WLoginActivity.this, MainActivity.class);
                            startActivity(i);
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            prefs.edit().putBoolean("Islogin", true).commit(); // islogin is a boolean value of your login status
                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(), "Signin Code Error", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    @Override
    public void onBackPressed() {
        /*super.onBackPressed();*/
    }
}