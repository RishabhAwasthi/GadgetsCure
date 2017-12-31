package com.gadgetscure.gadgetscure.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gadgetscure.gadgetscure.R;
import com.gadgetscure.gadgetscure.activities.InfoScreenActivity;
import com.gadgetscure.gadgetscure.activities.MainActivity;
import com.gadgetscure.gadgetscure.activities.OrderActivity;
import com.gadgetscure.gadgetscure.data.ImageSaver;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccountFragment extends Fragment {
    private TextView pro_name,mail;
    private EditText phone,address;
    private static String musername= MainActivity.getMyString();
    private static String memail=MainActivity.getEmail();
    private static String mphone= InfoScreenActivity.getPhone();
    private static String maddress= InfoScreenActivity.getAddress();
    private static final int RC_PHOTO_PICKER = 2;
    private  ImageView propic;




    public MyAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_my_account, container, false);
        pro_name=(TextView)v.findViewById(R.id.acname);
        pro_name.setText(musername);
        mail=(TextView)v.findViewById(R.id.gmail);
        mail.setText(memail);
        phone=(EditText)v.findViewById(R.id.acphone);
        address=(EditText)v.findViewById(R.id.acadd);
        phone.setText(mphone);
        address.setText(maddress);
        mphone=phone.getText().toString();
        maddress=address.getText().toString();
        propic=(ImageView)v.findViewById(R.id.dp);
        Bitmap bitmap = new ImageSaver(getContext()).
                setFileName("myImage.png").
                setDirectoryName("images").
                load();
        if(bitmap!=null)
            propic.setImageBitmap(bitmap);
        TextView showbook=(TextView)v.findViewById(R.id.showbook);
        showbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), OrderActivity.class);
                startActivity(i);
            }
        });
TextView pick=(TextView)v.findViewById(R.id.picker);
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);

            }
        });




        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK)
        {

            InputStream stream;
            try {
                Toast.makeText(getContext(), "Image saved", Toast.LENGTH_SHORT).show();
                stream = getContext().getContentResolver().openInputStream(data.getData());
                Bitmap realImage = BitmapFactory.decodeStream(stream);
                new ImageSaver(getContext()).
                        setFileName("myImage.png").
                        setDirectoryName("images").
                        save(realImage);
                // saveToInternalStorage(realImage);

                propic.setImageBitmap(realImage);
                Toast.makeText(getContext(), "Profile Pic Updated", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getContext(), MainActivity.class);
                startActivity(i);






            }
            catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }

    }




    public static String getPhone(){
    return mphone;
}
    public static String getAddress(){
        return maddress;
    }

}
