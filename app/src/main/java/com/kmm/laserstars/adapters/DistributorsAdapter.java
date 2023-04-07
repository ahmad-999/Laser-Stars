package com.kmm.laserstars.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kmm.laserstars.R;
import com.kmm.laserstars.databinding.DistributorItemBinding;
import com.kmm.laserstars.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DistributorsAdapter extends RecyclerView.Adapter<DistributorsAdapter.Holder> {
    private final ArrayList<User> distributors;
    private AdapterItemOnClick<User> onItemClickListener;

    public void setOnItemLongClickListener(AdapterItemOnClick<User> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    private AdapterItemOnClick<User> onItemLongClickListener;

    public DistributorsAdapter(ArrayList<User> distributors) {
        this.distributors = distributors;
    }


    @NonNull
    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.distributor_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Holder holder, int position) {
        User user = distributors.get(position);
        holder.binding.distName.setText(user.getName());
        holder.binding.distDesc.setText(user.getPhone());
        Glide.with(holder.itemView)
                .load(com.kmm.laserstars.services.API.RES_URL + user.getAvatar())
                .into(holder.binding.avatar);
        holder.binding.distCreatedAt.setText(user.getCreatedAt());
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(holder.getAdapterPosition(),
                        user.getId(), user);
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onClick(holder.getAdapterPosition(),
                        user.getId(), user);
            }
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return distributors.size();
    }

    public void setOnItemClickListener(AdapterItemOnClick<User> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void removeDis(int pos) {
        this.distributors.remove(pos);
        this.notifyItemRemoved(pos);
    }

    public static class Holder extends RecyclerView.ViewHolder {
        DistributorItemBinding binding;

        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = DistributorItemBinding.bind(itemView);
        }
    }
}
