package com.kmm.laserstars.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kmm.laserstars.R;
import com.kmm.laserstars.databinding.TagItemRvBinding;
import com.kmm.laserstars.models.Tag;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AllTagsAdapter extends RecyclerView.Adapter<AllTagsAdapter.Holder> {

    private final ArrayList<Tag> tags;
    private AdapterItemOnClick<Tag> onTagItemClick;
    private final boolean isDeletable;

    public AllTagsAdapter(ArrayList<Tag> tags, boolean isDeletable) {
        this.tags = tags;
        this.isDeletable = isDeletable;
    }

    @NonNull
    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tag_item_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Holder holder, int position) {
        Tag tag = tags.get(position);
        holder.binding.setTag(tag);
        if (isDeletable)
            holder.binding.delete.setVisibility(View.GONE);
        holder.binding.delete.setOnClickListener(v -> {
            if (onTagItemClick != null) {
                int index = holder.getAdapterPosition();
                Tag _tag = tags.get(index);
                onTagItemClick.onClick(index, _tag.getId(), _tag);
            }
        });

    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public void setOnTagItemClick(AdapterItemOnClick<Tag> onTagItemClick) {
        this.onTagItemClick = onTagItemClick;
    }

    public void removeTag(int pos) {
        this.tags.remove(pos);
        this.notifyItemRemoved(pos);
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TagItemRvBinding binding;

        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = TagItemRvBinding.bind(itemView);
        }
    }
}
