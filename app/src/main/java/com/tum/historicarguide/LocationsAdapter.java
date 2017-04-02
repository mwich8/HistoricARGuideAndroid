package com.tum.historicarguide;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Mati on 02.04.2017.
 */

public class LocationsAdapter extends ArrayAdapter<Location> {

    Context mContext;
    int mLayoutResourceId;
    RoundImage roundImage;
    Location mData[] = null;

    public LocationsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull Location[] data) {
        super(context, resource, data);

        this.mContext = context;
        this.mLayoutResourceId = resource;
        this.mData = data;
    }

    @Nullable
    @Override
    public Location getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;
        LocationHolder holder = null;

        // If we don't have currently a view to reuse
        if (row == null) {
            // Create a new view
            LayoutInflater inflater = LayoutInflater.from(mContext);
            row = inflater.inflate(mLayoutResourceId, parent, false);

            holder = new LocationHolder();

            holder.nameView = (TextView) row.findViewById(R.id.locationNameView);
            holder.detailView = (TextView) row.findViewById(R.id.locationDetailView);
            holder.imageView = (ImageView) row.findViewById(R.id.rowImageView);

            row.setTag(holder);
        } else {
            // Otherwise use existing one
            holder = (LocationHolder) row.getTag();
        }
        // Getting the data from the data array
        Location location = mData[position];

        // Setting the view to reflect the data we need to display
        holder.nameView.setText(location.getName());
        String detailViewString = "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude();
        holder.detailView.setText(detailViewString);

        // Getting the image
        // load image
        try {
            // get input stream
            String imageName = location.getName() + ".jpg";
            InputStream ims = mContext.getResources().getAssets().open(imageName);
            Bitmap bitmap = BitmapFactory.decodeStream(ims);
            roundImage = new RoundImage(bitmap);
            // set rounded image to ImageView
            holder.imageView.setImageDrawable(roundImage);
        }
        catch(IOException ex) {
            return row;
        }
        return row;
    }

    private static class LocationHolder {
        TextView nameView;
        TextView detailView;
        ImageView imageView;
    }
}
