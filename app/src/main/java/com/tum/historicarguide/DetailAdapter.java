package com.tum.historicarguide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Mati on 03.04.2017.
 */

public class DetailAdapter extends ArrayAdapter<DetailModel> {

    Context mContext;
    int mLayoutResourceId;
    DetailModel[] mData = null;

    public DetailAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull DetailModel[] data) {
        super(context, resource, data);

        this.mContext = context;
        this.mLayoutResourceId = resource;
        this.mData = data;
    }

    @Nullable
    @Override
    public DetailModel getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;
        DetailHolder holder = null;

        // If we don't have currently a view to reuse
        if (row == null) {
            // Create a new view
            LayoutInflater inflater = LayoutInflater.from(mContext);
            row = inflater.inflate(mLayoutResourceId, parent, false);

            holder = new DetailHolder();

            holder.keyView = (TextView) row.findViewById(R.id.detailKeyTextView);
            holder.valueView = (TextView) row.findViewById(R.id.detailValueTextView);

            row.setTag(holder);
        } else {
            // Otherwise use existing one
            holder = (DetailHolder) row.getTag();
        }
        // Getting the data from the data array
        String key = mData[position].getKey();
        String value = mData[position].getValue();

        // Setting the view to reflect the data we need to display
        holder.keyView.setText(key);
        holder.valueView.setText(value);

        return row;
    }

    private static class DetailHolder {
        TextView keyView;
        TextView valueView;
    }
}
