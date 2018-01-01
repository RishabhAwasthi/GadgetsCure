package com.gadgetscure.gadgetscure.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.gadgetscure.gadgetscure.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SwipeFragment extends Fragment {


    public SwipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View swipeView = inflater.inflate(R.layout.swipe_fragment, container, false);
        ImageView imageView = (ImageView) swipeView.findViewById(R.id.imageView);
        Bundle bundle = getArguments();
        int position = bundle.getInt("position");
       if(position==0){
        imageView.setImageResource(R.drawable.refer);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Under Construction refer",Toast.LENGTH_SHORT).show();
            }
        });}
        if(position==1)
            imageView.setImageResource(R.drawable.premium);
        if(position==2)
            imageView.setImageResource(R.drawable.discount);
        return swipeView;
    }

   public static SwipeFragment newInstance(int position) {
        SwipeFragment swipeFragment = new SwipeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        swipeFragment.setArguments(bundle);
        return swipeFragment;
    }
}
