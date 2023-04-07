package com.kmm.laserstars.adapters;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kmm.laserstars.R;
import com.kmm.laserstars.databinding.FilterItemBinding;
import com.kmm.laserstars.databinding.SearchFilterLayoutBinding;
import com.kmm.laserstars.models.Tag;
import com.kmm.laserstars.models.TagGenre;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FilterItemAdapter extends RecyclerView.Adapter<FilterItemAdapter.Holder> {
    private final ArrayList<Tag> tags;

    public FilterItemAdapter(ArrayList<Tag> tags) {

        this.tags = new ArrayList<>();
//        this.tags.add(new Tag(-1, "الكل", -1, false));
        this.tags.addAll(tags);
        for (Tag tag : tags) {
            if (!tag.isChecked()) return;
        }
        this.tags.get(0).setChecked(true);
    }


    @NonNull
    @NotNull
    @Override
    public FilterItemAdapter.Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new FilterItemAdapter.Holder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FilterItemAdapter.Holder holder, int position) {
        holder.binding.title.setText(tags.get(position).getName());
        int uncheckedId = holder.itemView.getContext().
                getResources().getColor(R.color.light_blue_A200);
        int checkedId = holder.itemView.getContext().
                getResources().getColor(R.color.light_blue_900);
        int white = holder.itemView.getContext().
                getResources().getColor(R.color.white);
        int black = holder.itemView.getContext().
                getResources().getColor(R.color.black);


        Tag tag = tags.get(holder.getAdapterPosition());
        holder.binding.getRoot()
                .setCardBackgroundColor(
                        tag.isChecked() ? checkedId : uncheckedId
                );
        holder.binding.title
                .setTextColor(
                        tag.isChecked() ? white : black
                );

        holder.itemView.setOnClickListener(v -> {
            Tag _tag = tags.get(holder.getAdapterPosition());
           /* if (_tag.getId() == -1) {
                boolean state = _tag.isChecked();
                for (Tag __tag : tags) {
                    __tag.setChecked(!state);
                }
//                _tag.setChecked(!_tag.isChecked());
                this.notifyDataSetChanged();
                return;
            }*/
            holder.binding.getRoot()
                    .setCardBackgroundColor(
                            _tag.isChecked() ? uncheckedId : checkedId
                    );
            holder.binding.title
                    .setTextColor(
                            tag.isChecked() ? black : white
                    );

            _tag.setChecked(!_tag.isChecked());
            // check if there is selected tag before this
            // unselect if found.
            for (Tag __tag : tags) {
                if (!__tag.equals(_tag) && __tag.isChecked()) {
                    // this tag has already been selected
                    // so unselect this tag
                    // and update the UI
                    int index = tags.indexOf(__tag);
                    __tag.setChecked(false);
                    notifyItemChanged(index);
                }
            }
         /*   for (Tag __tag : tags) {
                if (!__tag.isChecked() && __tag.getId() != -1) {
                    this.tags.get(0).setChecked(false);
                    notifyItemChanged(0);
                    return;
                }
            }

            this.tags.get(0).setChecked(true);
            notifyItemChanged(0);*/
        });
    }


    @Override
    public int getItemCount() {
        return tags.size();
    }


    public static class Holder extends RecyclerView.ViewHolder {
        FilterItemBinding binding;

        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = FilterItemBinding.bind(itemView);
        }
    }
}
