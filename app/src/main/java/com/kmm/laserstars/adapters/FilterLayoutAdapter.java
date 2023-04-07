package com.kmm.laserstars.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.kmm.laserstars.R;
import com.kmm.laserstars.databinding.FilterGenreLayoutItemBinding;
import com.kmm.laserstars.databinding.SearchFilterLayoutBinding;
import com.kmm.laserstars.models.Tag;
import com.kmm.laserstars.models.TagGenre;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FilterLayoutAdapter extends RecyclerView.Adapter<FilterLayoutAdapter.Holder> {
    private final ArrayList<TagGenre> genres;

    public FilterLayoutAdapter(ArrayList<TagGenre> genres) {
        this.genres = new ArrayList<>(genres);
    }


    @NonNull
    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_genre_layout_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Holder holder, int position) {
        TagGenre genre = genres.get(position);
        holder.binding.genreTitle.setText(genre.getName());
        FilterItemAdapter itemAdapter = new FilterItemAdapter(genre.getTags());
        holder.binding.genres.setAdapter(itemAdapter);

    }

    public void selectIds(int[] ids) {
        for (int i = 0; i < genres.size(); i++) {
            ArrayList<Tag> tags = genres.get(i).getTags();
            for (int j = 0; j < tags.size(); j++) {
                Tag tag = tags.get(j);
                for (int id : ids) {
                    if (tag.getId() == id)
                        tag.setChecked(true);
                }
            }
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    public int[] getSelectedIds() {
        ArrayList<Integer> arrayIds = new ArrayList<>();
        for (TagGenre genre : genres)
            for (Tag tag : genre.getTags())
                if (tag.isChecked() && tag.getId() != -1)
                    arrayIds.add(tag.getId());
        int[] ids = new int[arrayIds.size()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = arrayIds.get(i);
        }
        return ids;
    }

    public String[] getSelectedNames() {
        ArrayList<String> arrayIds = new ArrayList<>();
        for (TagGenre genre : genres)
            for (Tag tag : genre.getTags())
                if (tag.isChecked())
                    arrayIds.add(tag.getName());
        String[] ids = new String[arrayIds.size()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = arrayIds.get(i);
        }
        return ids;
    }

    public void clean() {
        int size = this.genres.size();
        this.genres.clear();
        this.notifyItemRangeRemoved(0, size);
    }


    public static class Holder extends RecyclerView.ViewHolder {
        FilterGenreLayoutItemBinding binding;

        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = FilterGenreLayoutItemBinding.bind(itemView);
        }
    }
}
