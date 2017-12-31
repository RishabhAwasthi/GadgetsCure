package com.gadgetscure.gadgetscure.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.firebase.ui.auth.AuthUI;
import com.gadgetscure.gadgetscure.adapters.DataAdapter;
import com.gadgetscure.gadgetscure.R;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class IssuesActivity extends AppCompatActivity {
    Toolbar toolbar;
    String issue, price;
    Context context;
    private ArrayList<String> issues;
    private boolean internetConnectionAvailable(int timeOut) {
        InetAddress inetAddress = null;
        try {
            Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(new Callable<InetAddress>() {
                @Override
                public InetAddress call() {
                    try {
                        return InetAddress.getByName("google.com");
                    } catch (UnknownHostException e) {
                        return null;
                    }
                }
            });
            inetAddress = future.get(timeOut, TimeUnit.MILLISECONDS);
            future.cancel(true);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        } catch (TimeoutException e) {
        }
        return inetAddress!=null && !inetAddress.equals("");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issuescreen);

        if(!internetConnectionAvailable(7000)){

            RelativeLayout noConnect =(RelativeLayout)findViewById(R.id.emptyview);
            RelativeLayout yesConnect =(RelativeLayout)findViewById(R.id.lin);
            yesConnect.setVisibility(View.INVISIBLE);
            noConnect.setVisibility(View.VISIBLE);
            Button tryAgain=(Button)findViewById(R.id.try_again);

            tryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent i=new Intent(IssuesActivity.this,MainActivity.class);

                    startActivity(i);
                }
            });


        }

        Intent i = getIntent();
        Bundle b = i.getExtras();
        issue = b.getString("Issue");
        price = b.getString("Price");

        issue = issue.substring(0, issue.length() - 1);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Choose your " + issue + " Problem");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initViews();
    }

    private void initViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        issues = new ArrayList<>();
        issues.add("Not powering ON");
        issues.add("Not Charging");
        issues.add("Display Damage");
        issues.add("Slow Processing");
        issues.add("Battery problem");
        issues.add("Over Heating");
        issues.add("Speaker Issue");
        issues.add("Body Damage");
        issues.add("Camera Issue");
        issues.add("Keys/Buttons Not Working");
        issues.add("No display/Blank display");
        issues.add("Other/None of the Above");
        RecyclerView.Adapter adapter = new DataAdapter(issues);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);
                    context = rv.getContext();


                    Intent i = new Intent(context, InfoScreenActivity.class);
                    Bundle extras = new Bundle();


                    extras.putString("Issue", issue + " Problem");
                    extras.putString("Problem", issues.get(position));
                    extras.putString("Price", price);
                    i.putExtras(extras);
                    context.startActivity(i);

                    // Toast.makeText(getApplicationContext(), issues.get(position), Toast.LENGTH_SHORT).show();
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
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
}
