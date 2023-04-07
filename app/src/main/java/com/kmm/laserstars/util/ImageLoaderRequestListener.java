package com.kmm.laserstars.util;


import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;


public class ImageLoaderRequestListener<RR> implements RequestListener<RR> {
    private final ProgressBar progressBar;
    private final View[] views;

    public ImageLoaderRequestListener(ProgressBar progressBar, View... views) {
        this.progressBar = progressBar;
        this.views = views;
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e,
                                Object model, Target<RR> target,
                                boolean isFirstResource) {
        progressBar.setVisibility(View.GONE);
        if (views != null && views.length > 0) {
            if (views[0] instanceof ImageView) {
                views[0].setVisibility(View.VISIBLE);

            }
        }
        return true;
    }

    @Override
    public boolean onResourceReady(RR resource,
                                   Object model, Target<RR> target,
                                   DataSource dataSource,
                                   boolean isFirstResource) {
        progressBar.setVisibility(View.GONE);
        for (View view : views)
            view.setVisibility(View.VISIBLE);
        return false;
    }

}
