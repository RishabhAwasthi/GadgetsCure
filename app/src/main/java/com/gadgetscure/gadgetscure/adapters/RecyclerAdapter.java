package com.gadgetscure.gadgetscure.adapters;

/**
 * Created by Shade on 5/9/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gadgetscure.gadgetscure.R;
import com.gadgetscure.gadgetscure.activities.IssuesActivity;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

private static String issue,price;

    Context context;


    private String[] details = {"Rs.200", "Rs.200", "Rs.300", "Rs.300", "Rs.300", "Rs.300", "Rs.500"
    };

    private int[] images = {R.drawable.mobile,
            R.drawable.tablet,
            R.drawable.laptop,
            R.drawable.desktop,
            R.drawable.network,
            R.drawable.printer,
            R.drawable.family,
            R.drawable.network,
            R.drawable.printer,


    };

    private String[] names = {"Mobiles", "Tablets", "Laptops", "Desktops", "Networking ", "Printers", "Parental Control "
    };

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.itemDetail.setText(details[i]);
        viewHolder.itemImage.setImageResource(images[i]);
        viewHolder.itemName.setText(names[i]);

    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public int currentItem;
        public ImageView itemImage;

        public TextView itemDetail;
        public TextView itemName;

        public ViewHolder(View itemView) {
            super(itemView);


            itemImage = (ImageView) itemView.findViewById(R.id.item_image);

            itemDetail = (TextView) itemView.findViewById(R.id.item_detail);
            itemName = (TextView) itemView.findViewById(R.id.name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    context = v.getContext();
                    Intent i = new Intent(context, IssuesActivity.class);
                    // i.putExtra("Issue",names[position]);
                    Bundle extras = new Bundle();


                    extras.putString("Issue", names[position]);
                    extras.putString("Price", details[position]);
                    issue=names[position];
                    price=details[position];
                    i.putExtras(extras);
                    context.startActivity(i);

                }
            });

        }
    }
    public static String getIssue(){
        return issue;
    }
    public static String getPrice(){
        return price;
    }
}