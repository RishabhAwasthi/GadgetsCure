package com.gadgetscure.gadgetscure.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.gadgetscure.gadgetscure.R;
import com.gadgetscure.gadgetscure.adapters.RecyclerAdapter;
import com.gadgetscure.gadgetscure.data.ImageSaver;
import com.gadgetscure.gadgetscure.fragments.MyAccountFragment;
import com.gadgetscure.gadgetscure.fragments.SwipeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity{

    Toolbar toolbar;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;


    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    private static final int RC_SIGN_IN=1;
    private static final int RC_PHOTO_PICKER = 2;

    private static String mUsername,memail;

    private TextView nav_user,nav_mail,nav_picker;
    private ImageView nav_dp;
    static final int NUM_ITEMS = 3;
    ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    ViewPager viewPager;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);



        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("Gadgets Cure");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();



        NavigationView nv = (NavigationView) findViewById(R.id.nav);
        View hView = nv.getHeaderView(0);
        nav_user = (TextView) hView.findViewById(R.id.profile_name);
        nav_mail = (TextView) hView.findViewById(R.id.email);
        nav_picker = (TextView) hView.findViewById(R.id.picker);


            nav_dp = (ImageView) hView.findViewById(R.id.dp);

            Bitmap bitmap = new ImageSaver(MainActivity.this).
                    setFileName("myImage.png").
                    setDirectoryName("images").
                    load();
        if(bitmap!=null)
            nav_dp.setImageBitmap(bitmap);









        nav_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);

            }
        });


        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.sign_out)
                    AuthUI.getInstance().signOut(MainActivity.this);
                else if (id == R.id.contact) {
                    Toast.makeText(MainActivity.this, mUsername + " It's Under Construction ", Toast.LENGTH_SHORT).show();

                } else if (id == R.id.rate)
                    Toast.makeText(MainActivity.this, mUsername + " It's Under Construction ", Toast.LENGTH_SHORT).show();
                else if (id == R.id.history) {
                    Intent i = new Intent(MainActivity.this, OrderActivity.class);
                    startActivity(i);
                }else if(id==R.id.home)
                {
                    Intent i = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(i);
                }
                else if(id==R.id.callbook){
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:9550822772"));
                    startActivity(intent);

                }
                else if(id==R.id.how){
                    Intent i = new Intent(MainActivity.this, ScreenSlidePagerActivity.class);
                    startActivity(i);
                }
                else if(id==R.id.acc)
                {
                    MyAccountFragment myAccountFragment = new MyAccountFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.acframe, myAccountFragment).commit();

                }


                drawerLayout.closeDrawers();

                return true;
            }
        });


        imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        if (viewPager != null) {
            viewPager.setAdapter(imageFragmentPagerAdapter);
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);



        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    mUsername = user.getDisplayName();
                    memail = user.getEmail();
                    nav_user.setText(mUsername);
                    nav_mail.setText(memail);


                } else {
                    // User is signed out
                    mUsername = "Anonymous";
                    memail = "abc@xyz.com";
                    nav_user.setText(mUsername);
                    nav_mail.setText(memail);


                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder().setIsSmartLockEnabled(false).setProviders(
                                    AuthUI.EMAIL_PROVIDER, AuthUI.GOOGLE_PROVIDER).setTheme(R.style.LoginTheme)
                                    .setLogo(R.mipmap.bg_login).build(), RC_SIGN_IN);
                }


            }
        };

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);




    }


    public static String getMyString(){
        return mUsername;
    }
    public static String getEmail(){
        return memail;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, " your are Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }}
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK)
        {

            InputStream stream;
            try {
                Toast.makeText(MainActivity.this, "Image saved", Toast.LENGTH_SHORT).show();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                stream = getContentResolver().openInputStream(data.getData());
                Bitmap realImage = BitmapFactory.decodeStream(stream);
                new ImageSaver(MainActivity.this).
                        setFileName("myImage.png").
                        setDirectoryName("images").
                        save(realImage);
               // saveToInternalStorage(realImage);

                nav_dp.setImageBitmap(realImage);


            }
            catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }

    }

    @Override
    protected void onResume() {
        super.onResume();


        mFirebaseAuth.addAuthStateListener(mAuthStateListener);

    }

    public static class ImageFragmentPagerAdapter extends FragmentPagerAdapter {
        public ImageFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            SwipeFragment fragment = new SwipeFragment();
            return SwipeFragment.newInstance(position);
        }
    }









    @Override
    protected void onPause() {



        super.onPause();

        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);


    }




}
