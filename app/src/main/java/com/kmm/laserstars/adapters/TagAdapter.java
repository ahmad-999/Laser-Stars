package com.kmm.laserstars.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kmm.laserstars.R;
import com.kmm.laserstars.databinding.AddItemBinding;
import com.kmm.laserstars.databinding.TagItemBinding;
import com.kmm.laserstars.models.Tag;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.Holder> {

    private final ArrayList<Tag> tags;
    private AdapterItemOnClick<Tag> onAddTagClick,onTagCheckListener;

    public TagAdapter(ArrayList<Tag> tags, boolean isAdmin) {
        this.tags = tags;
//        if (isAdmin)
//            this.tags.add(new Tag(-1, "Add"));
    }

    @NonNull
    @NotNull
    @Override
    public TagAdapter.Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.tag_item;
//        if (viewType == -1) {
//            layoutId = R.layout.add_item;
//        }
        return new TagAdapter.Holder(LayoutInflater.from(parent.getContext()).
                inflate(layoutId, parent, false), viewType);
    }

    @Override
    public int getItemViewType(int position) {
//        if (tags.get(position).getId() == -1) return -1;
        return super.getItemViewType(position);
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull TagAdapter.Holder holder, int position) {
        Tag tag = tags.get(position);
        if (tag.getId() != -1) {
            holder.binding.tagCheck.setChecked(tag.isChecked());
            holder.binding.tagCheck.setText(tag.getName());
            holder.binding.tagCheck.setOnCheckedChangeListener((compoundButton, b) -> {
                int index = holder.getAdapterPosition();
                Tag _tag = tags.get(index);
                _tag.setChecked(b);
                if (onTagCheckListener!=null)
                    onTagCheckListener.onClick(index, _tag.getId(),_tag);

            });
        } else {
            holder.addItemBinding.add.setOnClickListener(v -> {
                int index = holder.getAdapterPosition();
                Tag _tag = tags.get(index);
                if (onAddTagClick != null)
                    onAddTagClick.onClick(index, _tag.getId(), _tag);
            });
        }
    }

    public int[] getCheckedIds() {
        ArrayList<Integer> ids = new ArrayList<>();
        for (Tag tag : tags) {
            if (tag.isChecked())
                ids.add(tag.getId());
        }
        int[] returnedIds = new int[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            returnedIds[i] = ids.get(i);
        }
        return returnedIds;
    }


    @Override
    public int getItemCount() {
        return tags.size();
    }

    public void setTagsChecks(ArrayList<Tag> desginTags) {
        for (int i = 0; i < tags.size(); i++) {
            Tag tag = tags.get(i);
            for (int j = 0; j < desginTags.size(); j++) {
                if (tag.compareTo(desginTags.get(j)) == 0) {
                    tag.setChecked(true);
                    notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    public void setOnAddTagClick(AdapterItemOnClick<Tag> onAddTagClick) {
        this.onAddTagClick = onAddTagClick;
    }

    public void setOnTagCheckListener(AdapterItemOnClick<Tag> onTagCheckListener) {
        this.onTagCheckListener = onTagCheckListener;
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TagItemBinding binding;
        AddItemBinding addItemBinding;

        public Holder(@NonNull @NotNull View itemView, int type) {
            super(itemView);
            if (type != -1)
                binding = TagItemBinding.bind(itemView);
//            else addItemBinding = AddItemBinding.bind(itemView);
        }
    }
}
