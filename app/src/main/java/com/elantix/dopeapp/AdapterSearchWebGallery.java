package com.elantix.dopeapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by oleh on 3/28/16.
 */
public class AdapterSearchWebGallery extends RecyclerView.Adapter<AdapterSearchWebGallery.GalleryViewHolder>{

    private LayoutInflater inflater;
    private Context context;

    int[] pictures = {
            R.drawable.i0,
            R.drawable.i1,
            R.drawable.i2,
            R.drawable.i3,
            R.drawable.i4,
            R.drawable.i5,
            R.drawable.i6,
            R.drawable.i7,
            R.drawable.i8
    };

    public AdapterSearchWebGallery(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_gallery_row, parent, false);
        GalleryViewHolder holder = new GalleryViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        Glide.with(context).load(pictures[position]).into(holder.picture);
    }

    @Override
    public int getItemCount() {
        return pictures.length;
    }

    class GalleryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView picture;

        public GalleryViewHolder(View itemView) {
            super(itemView);
            picture = (ImageView) itemView.findViewById(R.id.search_web_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == itemView.getId()){
                Log.w("Log", "Item " + getAdapterPosition());
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", getAdapterPosition());
                ((SearchWebActivity)context).setResult(Activity.RESULT_OK, returnIntent);
                ((SearchWebActivity) context).finish();
            }
        }
    }
}
