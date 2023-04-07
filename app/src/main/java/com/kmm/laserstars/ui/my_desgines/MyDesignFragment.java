package com.kmm.laserstars.ui.my_desgines;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kmm.laserstars.R;
import com.kmm.laserstars.adapters.AllDesignAdapter;
import com.kmm.laserstars.databinding.DeleteDesignDialogBinding;
import com.kmm.laserstars.databinding.MyDesignFragmentBinding;
import com.kmm.laserstars.models.Design;
import com.kmm.laserstars.models.GetData;
import com.kmm.laserstars.services.API;
import com.kmm.laserstars.util.LoginStateChecker;

import java.util.ArrayList;

public class MyDesignFragment extends Fragment {

    private MyDesignViewModel mViewModel;
    private MyDesignFragmentBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = MyDesignFragmentBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(MyDesignViewModel.class);
        if (!LoginStateChecker.isLoggedIn(requireContext())) {
            NavController controller = NavHostFragment.findNavController(this);
            controller.navigate(R.id.action_myDesignsFragment_to_loginFragment);
        } else {
            String token = LoginStateChecker.getToken(requireContext());
            mViewModel.getMyDesigns(token).observe(getViewLifecycleOwner(), this::onAllDesignsArrive);
        }
        binding.addDesign.setOnClickListener(v -> {
            NavController controller = NavHostFragment.findNavController(this);
            controller.navigate(R.id.action_myDesignsFragment_to_addDesginFragment);
        });
        return binding.getRoot();
    }

    private void onAllDesignsArrive(GetData<ArrayList<Design>> data) {
        binding.designs.setVisibility(View.VISIBLE);
        binding.designsPb.setVisibility(View.GONE);
        if (data == null || data.getCode() != 200) {
            if (data == null) {
                Toast.makeText(requireContext(), "خطأ في الشبكة", Toast.LENGTH_SHORT).show();
            } else Toast.makeText(requireContext(), data.getMsg(), Toast.LENGTH_SHORT).show();
            return;
        }
        if (data.getData().size()==0){
            binding.noDesigns.setVisibility(View.VISIBLE);
            return;
        }
        AllDesignAdapter adapter = new AllDesignAdapter(data.getData());
        adapter.setItemOnLongClickListener((pos, id, object) -> {

            NavController controller = NavHostFragment.findNavController(this);
            Bundle bundle = new Bundle();
            bundle.putSerializable("desgin",object);
            controller.navigate(R.id.action_myDesignsFragment_to_addDesginFragment,bundle);
        });
        adapter.setItemOnClickListener((pos, id, object) -> {
            String imageUrl = object.getUrl();
            NavController controller = NavHostFragment.findNavController(this);
            Bundle bundle = new Bundle();
            bundle.putString("url",imageUrl);
            bundle.putSerializable("desgins",data.getData());
            controller.navigate(R.id.action_myDesignsFragment_to_designViewFragment,bundle);
        });
        binding.designs.setAdapter(adapter);

    }
}