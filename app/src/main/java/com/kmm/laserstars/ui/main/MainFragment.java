package com.kmm.laserstars.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kmm.laserstars.R;
import com.kmm.laserstars.activities.PlayVideoActivity;
import com.kmm.laserstars.adapters.AllDesignAdapter;
import com.kmm.laserstars.adapters.FilterLayoutAdapter;
import com.kmm.laserstars.databinding.MainFragmentBinding;
import com.kmm.laserstars.databinding.SearchFilterLayoutBinding;
import com.kmm.laserstars.models.Design;
import com.kmm.laserstars.models.GetData;
import com.kmm.laserstars.models.TagGenre;
import com.kmm.laserstars.services.API;
import com.kmm.laserstars.util.Constant;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.*;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private MainFragmentBinding binding;
    private AllDesignAdapter adapter = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = MainFragmentBinding.inflate(inflater, container, false);
        adapter = new AllDesignAdapter(new ArrayList<>());
        binding.designs.setItemAnimator(new ScaleInAnimator());
        binding.designs.setAdapter(adapter);

        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mViewModel.refreshTagGenres(getViewLifecycleOwner());
        mViewModel.getAllTagGenres().observe(getViewLifecycleOwner(), this::onAllTagsArrive);
        Intent intent = requireActivity().getIntent();
        if (intent.getExtras().getSerializable("designs") == null) {
            binding.refresh.setRefreshing(true);
            mViewModel.getAllDesigns().observe(getViewLifecycleOwner(), this::onAllDesignsArrive);

        } else {
            GetData<ArrayList<Design>> data = (GetData<ArrayList<Design>>)
                    intent.getExtras().getSerializable("designs");
            onAllDesignsArrive(data);
        }

        binding.refresh.setOnRefreshListener(() -> {
            adapter.clean();
            mViewModel.getAllDesigns()
                    .observe(getViewLifecycleOwner(), this::onAllDesignsArrive);

        });

        return binding.getRoot();
    }

    private void onAllTagsArrive(GetData<ArrayList<TagGenre>> data) {
        binding.searchByTags.setVisibility(View.VISIBLE);

    }

    private void onAllDesignsArrive(GetData<ArrayList<Design>> data) {
        binding.refresh.setRefreshing(false);

        binding.designs.setVisibility(View.VISIBLE);
        binding.designsPb.setVisibility(View.GONE);
        if (data == null || data.getCode() != 200) {
            if (data == null) {
                Toast.makeText(requireContext(), "خطأ في الشبكة", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), data.getMsg(), Toast.LENGTH_SHORT).show();

            }
            return;
        }
        if (data.getData().size() == 0) {
            binding.noDesigns.setVisibility(View.VISIBLE);
            binding.searchByTags.setVisibility(View.GONE);
            return;
        }

        adapter.setItemOnClickListener((pos, id, object) -> {
            if (object.getVideoURL() != null) {

                Intent intent = new Intent(requireActivity(), PlayVideoActivity.class);
                intent.putExtra(Constant.VIDEO_URL, object.getVideoURL());
                startActivity(intent);

            } else {
                String imageUrl = object.getUrl();
                NavController controller = NavHostFragment.findNavController(this);
                Bundle bundle = new Bundle();
                bundle.putString("url", imageUrl);
                bundle.putSerializable("desgins", data.getData());
                controller.navigate(R.id.action_homeFragment_to_designViewFragment, bundle);
            }

        });
        adapter.setOnDesignOwnerClickListener((pos, id, distributor) -> {
            NavController controller = NavHostFragment.findNavController(this);
            String name = distributor.getName();
            Bundle bundle = new Bundle();
            bundle.putString("name", name);
            bundle.putSerializable("distributor", distributor);
            controller.navigate(R.id.action_homeFragment_to_distributorDesignFragment, bundle);

        });

        for (int i = 0; i < data.getData().size(); i++) {
            if (this.isHidden() || this.isDetached()) break;
            int finalI = i;
            new Handler().postDelayed(() -> {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    if (binding.refresh.isRefreshing()) return;
                    adapter.addNewDesign(data.getData().get(finalI));
                });
            }, i * 300);
        }

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SearchFilterLayoutBinding filterBinding
                = SearchFilterLayoutBinding.inflate(getLayoutInflater());
        BottomSheetDialog filterDialog = new BottomSheetDialog(requireActivity());
        filterDialog.setOnDismissListener(dialog -> {
            Object genreAdapter = filterBinding.tagsGenreFilter.getAdapter();
            if (genreAdapter == null) {
                dialog.dismiss();
                return;
            }
            ((FilterLayoutAdapter) genreAdapter).clean();
            dialog.dismiss();
        });
        filterDialog.setContentView(filterBinding.getRoot());
        binding.searchByTags.setOnClickListener(v -> {
            filterDialog.show();
            GetData<ArrayList<TagGenre>> data = mViewModel.getAllTagGenres().getValue();
            if (data == null) {
                binding.searchByTags.setVisibility(View.GONE);
                mViewModel.refreshTagGenres(getViewLifecycleOwner());
                return;
            }
            FilterLayoutAdapter adapter = new FilterLayoutAdapter(data.getData());
            filterBinding.tagsGenreFilter.setAdapter(adapter);
        });
        filterBinding.filter.setOnClickListener(v -> {
            Object genreAdapter = filterBinding.tagsGenreFilter.getAdapter();
            if (genreAdapter == null) {
                filterDialog.dismiss();
                return;
            }
            int[] selectedTags = ((FilterLayoutAdapter) genreAdapter).getSelectedIds();
            if (selectedTags.length == 0) {
                Toast.makeText(requireContext(), "يرجى اختيار احد الوسوم",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            filterDialog.dismiss();

            NavController navController =
                    NavHostFragment.findNavController(this);
            Bundle bundle = new Bundle();
            bundle.putIntArray(Constant.SELECTED_IDS, selectedTags);
            String[] names = ((FilterLayoutAdapter) genreAdapter).getSelectedNames();
            bundle.putStringArray(Constant.SELECTED_NAMES, names);
            navController.navigate(R.id.action_homeFragment_to_searchByTagsFragment, bundle);
        });

    }
}