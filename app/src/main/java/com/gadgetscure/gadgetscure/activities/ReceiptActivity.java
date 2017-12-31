package com.gadgetscure.gadgetscure.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.gadgetscure.gadgetscure.R;


public class ReceiptActivity extends AppCompatActivity {
   private String userid,message;
   private TextView UserId,Message;
    private View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.Bookingtoolbar);
        toolbar.setTitle("Booking Summary");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        v= findViewById(R.id.UserId);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        userid= extras.getString("UserId");
        message= extras.getString("Message");
        UserId = (TextView)findViewById(R.id.UserId);
        UserId.setText(userid);
        Message= (TextView) findViewById(R.id.message);
        Message.setText(message);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                Intent i = new Intent(this,MainActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(ReceiptActivity.this,MainActivity.class);

        startActivity(startMain);

    }



}
