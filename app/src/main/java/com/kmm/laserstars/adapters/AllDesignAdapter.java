package com.kmm.laserstars.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kmm.laserstars.R;
import com.kmm.laserstars.databinding.DesignItemBinding;
import com.kmm.laserstars.models.Design;
import com.kmm.laserstars.models.User;
import com.kmm.laserstars.services.API;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AllDesignAdapter extends RecyclerView.Adapter<AllDesignAdapter.Holder> {

    private final ArrayList<Design> designs;
    private AdapterItemOnClick<Design> itemOnClickListener;
    private AdapterItemOnClick<Design> itemOnLongClickListener;
    private AdapterItemOnClick<User> onDesignOwnerClickListener;

    public AllDesignAdapter(ArrayList<Design> designs) {
        this.designs = designs;
    }

    @NonNull
    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.design_item, parent, false));
    }

    public void addNewDesign(Design design) {
        designs.add(design);
        this.notifyItemInserted(designs.size());
    }

    public void removeDesign(Design design) {
        int pos = designs.indexOf(design);
        designs.remove(design);
        this.notifyItemRemoved(pos);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Holder holder, int position) {
        Design design = designs.get(position);
        holder.binding.designName.setText(design.getName());
//        holder.binding.designDesc.setText(design.getDesc());
        if (design.getOwner() != null)
            holder.binding.designOwnerName.setText(design.getOwner().getName());
//        holder.binding.designCreatedAt.setText(design.getCreatedAt());
        if (design.getVideoURL() != null) {
            holder.binding.playVideo.setVisibility(View.VISIBLE);
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();


        WindowManager wm = (WindowManager)
                holder.itemView.getContext()
                        .getSystemService(Context.WINDOW_SERVICE);

        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int _height = displayMetrics.heightPixels;

//        double height = getRandomHeight(_height * 0.15, _height * 0.5);
//        ViewGroup.MarginLayoutParams layoutParams =
//                new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) height);
//
//        holder.binding.image.setLayoutParams(layoutParams);

        Glide.with(holder.itemView)
                .asBitmap()
                .load(API.RES_URL + design.getUrl())
                .dontAnimate()
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull @NotNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {
                        int imageHeight = resource.getHeight();
                        int imageWidth = resource.getWidth();

                        int containerWidth = holder.binding.container.getMeasuredWidth();
                        double ratio = (double) (containerWidth / imageWidth);
                        ViewGroup.MarginLayoutParams
                                layoutParams = new ViewGroup.MarginLayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                (int) (imageHeight * ratio));

                        Bitmap dist = Bitmap.createScaledBitmap(resource,
                                ViewGroup.LayoutParams.MATCH_PARENT, (int) (imageHeight * ratio)
                                , false);
                        resource.recycle();
                        holder.binding.image.setLayoutParams(layoutParams);
                        holder.binding.image.setImageBitmap(dist);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
        holder.itemView.setOnClickListener(v -> {
            if (itemOnClickListener != null) {
                itemOnClickListener.onClick(holder.getAdapterPosition(),
                        design.getId(), design
                );
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (itemOnLongClickListener != null) {
                itemOnLongClickListener.onClick(holder.getAdapterPosition(),
                        design.getId(), design
                );
            }
            return true;
        });
        holder.binding.designOwnerName.setOnClickListener(v -> {
            if (onDesignOwnerClickListener != null)
                onDesignOwnerClickListener.onClick(-1, -1, design.getOwner());
        });
    }

    @Override
    public int getItemCount() {
        return designs.size();
    }

    public void setItemOnClickListener(AdapterItemOnClick<Design> itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

    public void setItemOnLongClickListener(AdapterItemOnClick<Design> itemOnLongClickListener) {
        this.itemOnLongClickListener = itemOnLongClickListener;
    }

    public void setOnDesignOwnerClickListener(AdapterItemOnClick<User> onDesignOwnerClickListener) {
        this.onDesignOwnerClickListener = onDesignOwnerClickListener;
    }

    public static class Holder extends RecyclerView.ViewHolder {
        DesignItemBinding binding;

        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = DesignItemBinding.bind(itemView);
        }
    }

    private double getRandomHeight(double min, double max) {
        return min + Math.random() * (max - min);
    }

}
