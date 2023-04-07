package com.kmm.laserstars.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kmm.laserstars.R;
import com.kmm.laserstars.databinding.TagGenreItemRvBinding;
import com.kmm.laserstars.models.Tag;
import com.kmm.laserstars.models.TagGenre;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GenreTagAdapter extends RecyclerView.Adapter<GenreTagAdapter.Holder> {

    private final ArrayList<TagGenre> genres;
    private AdapterItemOnClickWithMoreParams<Tag> onTagItemClick;
    private AdapterItemOnClick<TagGenre> onTagGenreLongClick;

    public ArrayList<TagGenre> getGenreTags() {
        return this.genres;
    }

    public GenreTagAdapter(ArrayList<TagGenre> genres) {
        this.genres = genres;
    }

    @NonNull
    @NotNull
    @Override
    public GenreTagAdapter.Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new GenreTagAdapter.Holder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tag_genre_item_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull GenreTagAdapter.Holder holder, int position) {
        TagGenre genre = genres.get(position);
        holder.binding.genreName.setText(genre.getName());
        holder.itemView.setOnLongClickListener(view -> {
            if (onTagGenreLongClick != null)
                onTagGenreLongClick.onClick(position, genre.getId(), genre);
            return false;
        });
        if (genre.getTags() == null || genre.getTags().size() == 0) return;
        AllTagsAdapter tagsAdapter = new AllTagsAdapter(genre.getTags(),false);
        holder.binding.tags.setAdapter(tagsAdapter);
        tagsAdapter.setOnTagItemClick((pos, id, object) -> {
            if (onTagItemClick != null)
                onTagItemClick.onClick(object, id, position, pos);
        });

    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    public void setOnTagItemClick(AdapterItemOnClickWithMoreParams<Tag> onTagItemClick) {
        this.onTagItemClick = onTagItemClick;
    }

    public void removeTag(int fatherPos, int childPos) {
        this.genres.get(fatherPos).getTags().remove(childPos);
        this.notifyItemChanged(fatherPos);
    }

    public void addTagGenre(TagGenre genre) {
        this.genres.add(genre);
        this.notifyItemInserted(this.genres.size());
    }

    public void setOnTagGenreLongClick(AdapterItemOnClick<TagGenre> onTagGenreLongClick) {
        this.onTagGenreLongClick = onTagGenreLongClick;
    }

    public void removeGenre(int pos) {
        this.genres.remove(pos);
        this.notifyItemRemoved(pos);
    }

    public void addTag(Tag tag) {
        for (int i = 0; i < genres.size(); i++) {
            TagGenre genre = genres.get(i);
            if (genre.getId() == tag.getGenreId()) {
                genres.get(i).getTags().add(tag);
                this.notifyItemChanged(i);
                break;
            }
        }
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TagGenreItemRvBinding binding;

        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = TagGenreItemRvBinding.bind(itemView);
        }
    }
}
