package com.kmm.laserstars.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kmm.laserstars.R;
import com.kmm.laserstars.databinding.TagsAddDesignItemBinding;
import com.kmm.laserstars.models.Tag;
import com.kmm.laserstars.models.TagGenre;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AddDesignGenreAdapter extends RecyclerView.Adapter<AddDesignGenreAdapter.Holder> {
    private final ArrayList<TagGenre> genres;

    public AddDesignGenreAdapter(ArrayList<TagGenre> genres) {
        this.genres = genres;
    }

    @NonNull
    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Holder holder = new Holder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.tags_add_design_item, parent, false)
        );
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Holder holder, int position) {
        TagGenre genre = genres.get(position);
        String genreName = genre.getName();
        ArrayList<Tag> tags = genre.getTags();
        String[] list = new String[tags.size() + 1];
        list[0] = "بلا";
        int selected = 0;
        for (int i = 1; i < list.length; i++) {
            list[i] = tags.get(i - 1).getName();
            if (tags.get(i - 1).isChecked())
                selected = i;
        }
        holder.binding.tagGenreName.setText(genreName);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(holder.itemView.getContext(),
                android.R.layout.simple_list_item_1, list);
        holder.binding.tagsList.setAdapter(adapter);

        holder.binding.tagsList.setSelection(selected);
        holder.binding.tagsList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                for (Tag tag : tags)
                    tag.setChecked(false);
                if (i != 0)
                    tags.get(i - 1).setChecked(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    public int[] getCheckedId() {
        ArrayList<Integer> arrayIds = new ArrayList<>();
        for (TagGenre genre : genres)
            for (Tag tag : genre.getTags())
                if (tag.isChecked())
                    arrayIds.add(tag.getId());
        int[] ids = new int[arrayIds.size()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = arrayIds.get(i);
        }
        return ids;
    }

    public void setTagsChecks(ArrayList<Tag> desginTags) {
        for (int i = 0; i < genres.size(); i++) {
            ArrayList<Tag> tags = genres.get(i).getTags();
            for (int j = 0; j < tags.size(); j++) {
                Tag tag = tags.get(j);
                for (int k = 0; k < desginTags.size(); k++) {
                    if (tag.equals(desginTags.get(k)))
                        tag.setChecked(true);
                }
            }
        }
        this.notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TagsAddDesignItemBinding binding;

        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = TagsAddDesignItemBinding.bind(itemView);
        }
    }
}
