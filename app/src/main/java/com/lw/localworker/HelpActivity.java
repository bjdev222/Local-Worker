package com.lw.localworker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HelpActivity extends AppCompatActivity implements View.OnClickListener {

    Button help;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        initUI();
        help.setOnClickListener(this);
    }

    private void initUI() {
        help=findViewById(R.id.qMail);
    }

    @Override
    public void onClick(View v) {
        if(v==help){
            sendMail();

        }
    }

    private void sendMail() {

        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:localworkers222@gmail.com")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"example.yahoo.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "App feedback");
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email client installed on your device.", Toast.LENGTH_SHORT).show();
        }
    }


}