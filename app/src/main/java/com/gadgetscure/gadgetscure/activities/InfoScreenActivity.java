package com.gadgetscure.gadgetscure.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.gadgetscure.gadgetscure.R;
import com.gadgetscure.gadgetscure.adapters.RecyclerAdapter;
import com.gadgetscure.gadgetscure.data.DbContract;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class InfoScreenActivity extends AppCompatActivity{
    public String  cost,description,Date,Time,ampm;
    public TextView Description;
    private static String device_issue, problem;
    private static  String cur_date;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
        private final Random rand = new Random();
    private int t=0,d=0;

    private  int dd,mm,yy;
    private EditText mPickDate,name,address,phone;
    private EditText time;
   private int mYear;
    private int mMonth;
   private int mDay;
    private int hr,min;
    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID=1;
    String Name;
   static String Phone;
    static String Address;

    String username = MainActivity.getMyString();
    private static long ref_no;
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

        setContentView(R.layout.activity_info_screen);

        if(!internetConnectionAvailable(7000)){

            RelativeLayout noConnect =(RelativeLayout)findViewById(R.id.emptyview);
            RelativeLayout yesConnect =(RelativeLayout)findViewById(R.id.lin);
            yesConnect.setVisibility(View.INVISIBLE);
            noConnect.setVisibility(View.VISIBLE);
            Button tryAgain=(Button)findViewById(R.id.try_again);
            tryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(InfoScreenActivity.this,MainActivity.class);
                    startActivity(i);
                }
            });


        }

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.infotoolbar);
        toolbar.setTitle("Booking Info");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InfoScreenActivity.this, IssuesActivity.class);
                // i.putExtra("Issue",names[position]);
                Bundle extras = new Bundle();


                extras.putString("Issue", RecyclerAdapter.getIssue() );
                extras.putString("Price",RecyclerAdapter.getPrice() );
                i.putExtras(extras);
                startActivity(i);

            }
        });

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mDatabaseReference = mFirebaseDatabase.getReference().child("messages");





        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        device_issue = extras.getString("Issue");


        problem = extras.getString("Problem");
        cost = extras.getString("Price");
        if(extras.getString("Description")!=null)
        description = extras.getString("Description");

        TextView deviceissue = (TextView) findViewById(R.id.problem);
        TextView problem_type = (TextView) findViewById(R.id.problemtype);
        final TextView price = (TextView) findViewById(R.id.price);
        deviceissue.setText(device_issue);
        problem_type.setText(problem);
        price.setText(cost);


        mPickDate = (EditText) findViewById(R.id.mPickDate);//button for showing date picker dialog
        mPickDate.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) { showDialog(DATE_DIALOG_ID); }
        });

        // get the current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
       dd=mDay;
        mm=mMonth;
        yy=mYear;
        cur_date=String.valueOf(mDay)+"/"+String.valueOf(mMonth+1)+"/"+String.valueOf(mYear) ;


        // display the current date
        updateDisplay();

         time = (EditText) findViewById(R.id.time);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // showTimePickerDialog(view);
                showDialog(TIME_DIALOG_ID);
            }
        });
        Calendar initialTime = Calendar.getInstance();

        hr = initialTime.get(Calendar.HOUR_OF_DAY);
        min= initialTime.get(Calendar.MINUTE);
        updateDisplayTime();


        ImageView add = (ImageView) findViewById(R.id.add);
      /*  add.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {

                                       buildDialog(InfoScreenActivity.this).show();

                                   }
                               }


        );
        */
      add.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              final Dialog delte_dialog = new Dialog(InfoScreenActivity.this);
              delte_dialog.setContentView(R.layout.custom_dialog);
            final  EditText input=(EditText) delte_dialog.findViewById(R.id.des);

              delte_dialog.show();

              Button cancel = (Button) delte_dialog.findViewById(R.id.no);
              cancel.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      delte_dialog.dismiss();
                  }
              });

              Button proceed = (Button) delte_dialog.findViewById(R.id.add);
              proceed.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {

                      description=input.getText().toString();
                      Description= (TextView) findViewById(R.id.description);
                      Description.setText(description);



                      delte_dialog.dismiss();
                  }
              });
          }
      });


        name = (EditText) findViewById(R.id.user_name);
        address = (EditText) findViewById(R.id.address);
        phone = (EditText) findViewById(R.id.phone_num);
        if(!TextUtils.isEmpty(Phone))
        phone.setText(Phone);
        if(!TextUtils.isEmpty(Address))
        address.setText(Address);
        final     SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Phone=sp.getString("Phone","");
        Address=sp.getString("Address","");


        name.setText(username);
        address.setText(Address);
        phone.setText(Phone);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setFocusableInTouchMode(true);
            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                address.setFocusableInTouchMode(true);
            }
        });


        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone.setFocusableInTouchMode(true);
            }
        });









        Button msg = (Button) findViewById(R.id.msg);


        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!internetConnectionAvailable(7000)){

                    RelativeLayout noConnect =(RelativeLayout)findViewById(R.id.emptyview);
                    RelativeLayout yesConnect =(RelativeLayout)findViewById(R.id.lin);
                    yesConnect.setVisibility(View.INVISIBLE);
                    noConnect.setVisibility(View.VISIBLE);
                    Button tryAgain=(Button)findViewById(R.id.try_again);
                    tryAgain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i=new Intent(InfoScreenActivity.this,MainActivity.class);
                            startActivity(i);
                        }
                    });



                }
                else {


                    int flag = 0;

                    Name = name.getText().toString();

                    Address = address.getText().toString();
                    Phone = phone.getText().toString();

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("Phone", Phone);
                    editor.putString("Address",Address);
                    editor.commit();

                    Date = mPickDate.getText().toString();
                    Time = time.getText().toString();
                    long x = 10011100011000l;
                    long y = 10001001000101l;
                    ref_no = x + ((long) (rand.nextDouble() * (y - x)));


                    String message = "Ref : " + ref_no + ",  Name : " + Name +
                            ",  Address : " + Address +
                            ",  Phone number : " + Phone +
                            ",  Pickup Date : " + Date +
                            ",  Pickup Time : " + Time +
                            ",  Device/Problem : " + device_issue +
                            ",  Problem : " + problem +
                            ",  Inspection Charges : " + cost +
                            ",  Description : " + description;

                    if (TextUtils.isEmpty(Name) || TextUtils.isEmpty(Address) || TextUtils.isEmpty(Phone) || TextUtils.isEmpty(Date) || TextUtils.isEmpty(Time)) {

                        Snackbar.make(v, "                !!   Please Fill All The Fields   !! ",
                                Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        flag = 1;
                    } else if (!TextUtils.isDigitsOnly(Phone) || (Phone.length() != 10)) {
                        Snackbar.make(v, "                !!   Please Enter a Valid Number   !! ",
                                Snackbar.LENGTH_LONG).setAction("Action", null).show();


                        flag = 1;
                    } else if (!Name.matches("^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$")) {
                        flag = 1;
                        Snackbar.make(v, "                !!   Please Enter a Valid Name   !! ",
                                Snackbar.LENGTH_LONG).setAction("Action", null).show();


                    }

                    if (d == 1) {

                        Snackbar.make(v, "                !!   Please Enter a Valid Date  !! ",
                                Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                    if (t == 1) {

                        Snackbar.make(v, "                !!   Please Enter a Valid Time  !! ",
                                Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }


                    if (flag == 0 && t == 0 && d == 0) {
                        Toast.makeText(InfoScreenActivity.this, "!!   Your appointment has been booked   !!", Toast.LENGTH_SHORT).show();


                        mDatabaseReference.push().setValue(message);

                        message = "Name : " + Name +
                                "\nAddress : " + Address +
                                "\nPhone number : " + Phone +
                                "\nPickup Date : " + Date +
                                "\nPickup Time : " + Time +
                                "\nDevice/Problem : " + device_issue +
                                "\nProblem : " + problem +
                                "\nInspection Charges : " + cost;

                        saveReceipt();


                        Intent i = new Intent(InfoScreenActivity.this, ReceiptActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("UserId", String.valueOf(ref_no));
                        extras.putString("Message", message);
                        i.putExtras(extras);
                        startActivity(i);


                    }
                }


            }
        });



    }
    private void saveReceipt(){
        ContentValues values = new ContentValues();
        values.put(DbContract.DbEntry.COLUMN_REF, ref_no);
        values.put(DbContract.DbEntry.COLUMN_DEVICE, device_issue);
        values.put(DbContract.DbEntry.COLUMN_ISSUE, problem);
        values.put(DbContract.DbEntry.COLUMN_DATE, cur_date);
                    Uri newUri = getContentResolver().insert(DbContract.DbEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, "Error while saving",
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, "Recipt saved",
                        Toast.LENGTH_SHORT).show();
            }

    }


    @Override
    protected Dialog onCreateDialog(int id) {
        if(id== DATE_DIALOG_ID)
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
      else if(id==TIME_DIALOG_ID) {
            return new TimePickerDialog(this, mTimeSetListener, hr, min,false);
        }

        return null;
    }



    //update month day year
    private void updateDisplay() {
        if(mYear>yy)
        {
            if(mYear==yy+1) {
                d = 0;
                mPickDate.setText(//this is the edit text where you want to show the selected date
                        new StringBuilder()
                                // Month is 0 based so add 1
                                .append(mDay).append("-")
                                .append(mMonth + 1).append("-")
                                .append(mYear).append(""));
            }
            else {
                d = 1;
                mPickDate.setText("Enter a valid Date");
                Toast.makeText(this, "Unfortunately we don't have a time machine !!", Toast.LENGTH_SHORT).show();

            }

        }
        else {
            if (mDay < dd || mMonth < mm || mYear < yy) {
                d = 1;
                mPickDate.setText("Enter a valid Date");
                Toast.makeText(this, "Unfortunately we don't have a time machine !!", Toast.LENGTH_SHORT).show();
            } else {
                d = 0;
                mPickDate.setText(//this is the edit text where you want to show the selected date
                        new StringBuilder()
                                // Month is 0 based so add 1
                                .append(mDay).append("-")
                                .append(mMonth + 1).append("-")
                                .append(mYear).append(""));
            }
        }

    }


    private void updateDisplayTime() {
        String newtime;
        int flag=0;
        if(hr< 12) {
            ampm = "AM";
        } else {
            ampm = "PM";
        }
        if(min<10)
            newtime= "0"+String.valueOf(min);
        else
            newtime=String.valueOf(min);
        if(hr<9)
            flag=1;
        if(hr>=19) {
            if ((hr + min) > 19)
                flag = 1;
            else
                flag=0;
        }
        if(flag==1) {
            t=1;
            time.setText("Enter between 9 AM to 7 PM");
        }
        else {
            t=0;
            time.setText(//this is the edit text where you want to show the selected date
                    new StringBuilder()
                            // Month is 0 based so add 1
                            .append(hr).append(":")
                            .append(newtime).append(" ")
                            .append(ampm));
        }
    }
        // the call back received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };

    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int i, int i1) {
                    hr=i;
                    min=i1;
                    updateDisplayTime();
                }

            };


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
        Intent i = new Intent(InfoScreenActivity.this, IssuesActivity.class);
        // i.putExtra("Issue",names[position]);
        Bundle extras = new Bundle();


        extras.putString("Issue",RecyclerAdapter.getIssue() );
        extras.putString("Price",RecyclerAdapter.getPrice() );
        i.putExtras(extras);
        startActivity(i);

    }




}

