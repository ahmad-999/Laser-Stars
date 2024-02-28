package com.kmm.laserstars.ui.search;

import androidx.activity.OnBackPressedCallback;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kmm.laserstars.R;
import com.kmm.laserstars.activities.MainActivity;
import com.kmm.laserstars.activities.PlayVideoActivity;
import com.kmm.laserstars.adapters.AllDesignAdapter;
import com.kmm.laserstars.adapters.FilterLayoutAdapter;
import com.kmm.laserstars.adapters.TagAdapter;
import com.kmm.laserstars.databinding.SearchFilterLayoutBinding;
import com.kmm.laserstars.databinding.SearchFragmentBinding;
import com.kmm.laserstars.models.Design;
import com.kmm.laserstars.models.GetData;
import com.kmm.laserstars.models.Tag;
import com.kmm.laserstars.models.TagGenre;
import com.kmm.laserstars.models.UserType;
import com.kmm.laserstars.util.Constant;
import com.kmm.laserstars.util.LoginStateChecker;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SearchByTagsFragment extends Fragment {

    private SearchViewModel mViewModel;
    private SearchFragmentBinding binding;
    private int[] selectedIds;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        mViewModel.refreshTagGenres(getViewLifecycleOwner());
        requireActivity().getOnBackPressedDispatcher().addCallback(
                new OnBackPressedCallback(false) {
                    @Override
                    public void handleOnBackPressed() {
                        ((MainActivity) requireActivity())
                                .binding.toolbar.setNavigationIcon(null);
                        NavController controller = NavHostFragment.
                                findNavController(SearchByTagsFragment.this);
                        controller.navigateUp();
                    }
                });
        mViewModel.getAllTagGenres().observe(getViewLifecycleOwner(), this::onAllTagsArrive);
        binding = SearchFragmentBinding.inflate(inflater, container, false);
        selectedIds = requireArguments().getIntArray(Constant.SELECTED_IDS);
        String[] selectedNames = requireArguments().getStringArray(Constant.SELECTED_NAMES);
        for (String name : selectedNames) {
            String tags = createTagNames(name, true);
            binding.selectedTags.setText(tags);

        }
        mViewModel.getDesignsByTags(selectedIds)
                .observe(getViewLifecycleOwner(), this::onDesingsArrive);

        return binding.getRoot();
    }

    private void onAllTagsArrive(GetData<ArrayList<TagGenre>> data) {
        binding.filterButton.setVisibility(View.VISIBLE);

    }

    private void onDesingsArrive(GetData<ArrayList<Design>> data) {
        binding.designsPb.setVisibility(View.GONE);
        binding.designs.setVisibility(View.VISIBLE);

        binding.noDesigns.setVisibility(View.GONE);
        if (data == null || data.getData() == null) {
            Toast.makeText(requireContext(), "خطأ في الشبكة", Toast.LENGTH_SHORT).show();
            return;
        }
        if (data.getCode() > 200) {

            Toast.makeText(requireContext(), data.getMsg(), Toast.LENGTH_SHORT).show();
            return;
        }
        if (data.getData().size() == 0) {
            binding.designs.setVisibility(View.GONE);
            binding.noDesigns.setVisibility(View.VISIBLE);
        }
        AllDesignAdapter adapter = new AllDesignAdapter(data.getData());
        adapter.setItemOnClickListener((pos, id, object) -> {
            if (object.getVideoURL() != null) {

                Intent intent = new Intent(requireActivity(), PlayVideoActivity.class);
                intent.putExtra(Constant.VIDEO_URL, object.getVideoURL());
                startActivity(intent);

            } else {
                String imageUrl = object.getUrl();
                NavController controller = NavHostFragment.findNavController(this);
                Bundle bundle = new Bundle();
                bundle.putString(Constant.IMAGE_URL, imageUrl);
                bundle.putSerializable(Constant.DESIGNS, data.getData());
                controller.navigate(R.id.action_searchByTagsFragment_to_designViewFragment, bundle);
            }

        });
        adapter.setOnDesignOwnerClickListener((pos, id, distributor) -> {
            NavController controller = NavHostFragment.findNavController(this);
            String name = distributor.getName();
            Bundle bundle = new Bundle();
            bundle.putString("name", name);
            bundle.putSerializable("distributor", distributor);
            controller.navigate(R.id.action_searchByTagsFragment_to_distributorDesignFragment, bundle);

        });


        binding.designs.setAdapter(adapter);
    }

//    private void onTagsArrive(GetData<ArrayList<Tag>> data) {
//
//        if (data == null) {
//            Toast.makeText(requireContext(), "Network Error.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        adapter = new TagAdapter(data.getData(), false);
//        adapter.setOnTagCheckListener((pos, id, object) -> {
//
//            binding.selectedTags.setText(createTagNames(object.getName(), object.isChecked()));
//        });
//        binding.tagsRv.setAdapter(adapter);
//
//    }

    private String createTagNames(String name, boolean checked) {
        // selected tags : #door | #wind | #new door => [selected tags : #door,#wind,#new door]
        //selected tags : [selected tags : ]
        String oldValue = binding.selectedTags.getText().toString();
        String[] tags = oldValue.split(" \\| ");
        StringBuilder stringBuilder = new StringBuilder();
        if (tags.length > 0) {
            String firstTag = tags[0];
            firstTag = firstTag.replace(Constant.SELECTED_DESIGNS_TITLE, "");
            tags[0] = firstTag;
            //[#door,#wind,#new door]
        }
        if (tags.length == 1) {
            stringBuilder.append(Constant.SELECTED_DESIGNS_TITLE);
            if (tags[0].contains("#")) {
                if (checked)
                    stringBuilder
                            .append(tags[0])
                            .append(" | ")
                            .append("#").append(name);
            } else stringBuilder.append("#").append(name);
            return stringBuilder.toString();
        }
        stringBuilder.append(Constant.SELECTED_DESIGNS_TITLE);
        if (checked) {
            for (String tag : tags) {

                stringBuilder.append(tag).append(" | ");
                //  | name
            }
            stringBuilder.append("#").append(name);
        } else {

            for (int i = 0; i < tags.length; i++) {
                String tag = tags[i];
                if (tag.replace("#", "").equals(name))
                    continue;
                stringBuilder.append(tag);
                if (tags[tags.length - 1].replace("#", "").equals(name)) {
                    if (i < tags.length - 2)
                        stringBuilder.append(" | ");
                } else if (i < tags.length - 1)
                    stringBuilder.append(" | ");
            }
        }
        return stringBuilder.toString();
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SearchFilterLayoutBinding filterBinding
                = SearchFilterLayoutBinding.inflate(getLayoutInflater());
        BottomSheetDialog filterDialog = new BottomSheetDialog(requireActivity());
        filterDialog.setContentView(filterBinding.getRoot());
        binding.filterButton.setOnClickListener(v -> {
            filterDialog.show();
            GetData<ArrayList<TagGenre>> data = mViewModel.getAllTagGenres().getValue();
            if (data == null) {
                binding.filterButton.setVisibility(View.GONE);
                mViewModel.refreshTagGenres(getViewLifecycleOwner());
                return;
            }
            FilterLayoutAdapter adapter = new FilterLayoutAdapter(data.getData());
            filterBinding.tagsGenreFilter.setAdapter(adapter);
            adapter.selectIds(selectedIds);
        });
        filterBinding.filter.setOnClickListener(v -> {
            Object genreAdapter = filterBinding.tagsGenreFilter.getAdapter();
            if (genreAdapter == null) {
                filterDialog.dismiss();
                return;
            }

            selectedIds = ((FilterLayoutAdapter) genreAdapter).getSelectedIds();
            if (selectedIds.length == 0) {
                Toast.makeText(requireContext(), "يرجى اختيار احد الوسوم",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            String[] names = ((FilterLayoutAdapter) genreAdapter).getSelectedNames();
            this.binding.selectedTags.setText(Constant.SELECTED_DESIGNS_TITLE);
            for (String name : names) {
                String tags = createTagNames(name, true);
                binding.selectedTags.setText(tags);

            }
            filterDialog.dismiss();
            mViewModel.getDesignsByTags(selectedIds)
                    .observe(getViewLifecycleOwner(), this::onDesingsArrive);
            binding.designsPb.setVisibility(View.VISIBLE);
            binding.designs.setVisibility(View.GONE);
            binding.noDesigns.setVisibility(View.GONE);

        });

    }
}