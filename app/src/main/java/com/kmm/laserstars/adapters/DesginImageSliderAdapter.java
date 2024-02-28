package com.kmm.laserstars.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kmm.laserstars.R;
import com.kmm.laserstars.models.Design;
import com.kmm.laserstars.models.Tag;
import com.kmm.laserstars.services.API;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class DesginImageSliderAdapter extends
        SliderViewAdapter<DesginImageSliderAdapter.SliderAdapterVH> {

    private final ArrayList<Design> mSliderItems;

    public DesginImageSliderAdapter(ArrayList<Design> mSliderItems) {
        this.mSliderItems = mSliderItems;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_slider_layout_item, parent, false);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        Design sliderItem = mSliderItems.get(position);

        viewHolder.textViewDescription.setText(sliderItem.getName());
        viewHolder.designDesc.setText(sliderItem.getDesc());
        AllTagsAdapter tagsAdapter = new AllTagsAdapter(sliderItem.getTags(),true);
        viewHolder.designTags.setAdapter(tagsAdapter);
        Glide.with(viewHolder.itemView)
                .load(API.RES_URL + sliderItem.getUrl())
                .fitCenter()
                .into(viewHolder.imageViewBackground);

    }

    private String createTags(ArrayList<Tag> tags) {
        StringBuilder builder = new StringBuilder();
        for (Tag tag : tags)
            builder.append("#").append(tag.getName()).append(" ");
        return builder.toString();
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }


    static class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
//        ImageView imageGifContainer;
        TextView textViewDescription;
        TextView designDesc;
        RecyclerView designTags;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
//            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            textViewDescription = itemView.findViewById(R.id.tv_image_slider);
            designDesc = itemView.findViewById(R.id.tv_image_slider_desc);
            designTags = itemView.findViewById(R.id.tags);
            this.itemView = itemView;
        }
    }
}
