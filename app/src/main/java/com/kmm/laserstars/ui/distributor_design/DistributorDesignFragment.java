package com.kmm.laserstars.ui.distributor_design;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kmm.laserstars.R;
import com.kmm.laserstars.activities.MainActivity;
import com.kmm.laserstars.adapters.AllDesignAdapter;
import com.kmm.laserstars.databinding.DistributorDesignFragmentBinding;
import com.kmm.laserstars.models.User;
import com.kmm.laserstars.services.API;
import com.kmm.laserstars.util.Constant;
import com.kmm.laserstars.util.ImageLoaderRequestListener;

import org.jetbrains.annotations.NotNull;

public class DistributorDesignFragment extends Fragment {

    private DistributorDesignViewModel mViewModel;
    private DistributorDesignFragmentBinding binding;
    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        ((MainActivity) requireActivity()).getSupportActionBar().hide();
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        ((MainActivity) requireActivity()).getSupportActionBar().show();
        super.onDetach();

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(DistributorDesignViewModel.class);
        binding = DistributorDesignFragmentBinding.inflate(inflater, container, false);
        User distributor = (User) requireArguments().getSerializable(Constant.DISTRIBUTOR);
        if (distributor == null) {
            return binding.getRoot();
        }
        binding.providerName.setTitle(distributor.getName());
        Glide.with(this)
                .load(API.RES_URL + distributor.getAvatar())
                .listener(new ImageLoaderRequestListener<>(binding.serviceIconProgress, binding.providerImage))
                .into(binding.providerImage);
        AllDesignAdapter adapter = new AllDesignAdapter(distributor.getGroup().getDesigns());
        adapter.setItemOnClickListener((pos, id, object) -> {
            String url = object.getUrl();
            NavController controller = NavHostFragment.findNavController(this);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.DESIGNS, distributor.getGroup().getDesigns());
            bundle.putString(Constant.IMAGE_URL, url);
            controller.navigate(R.id.action_distributorDesignFragment_to_designViewFragment, bundle);
        });
        binding.designs.setAdapter(adapter);

        return binding.getRoot();
    }
}