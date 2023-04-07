package com.kmm.laserstars.ui.add_distributor;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kmm.laserstars.R;
import com.kmm.laserstars.databinding.AddDistributorFragmentBinding;
import com.kmm.laserstars.models.Errors;
import com.kmm.laserstars.models.GetData;
import com.kmm.laserstars.models.User;
import com.kmm.laserstars.util.LoginStateChecker;

import java.util.ArrayList;

public class AddDistributorFragment extends Fragment {

    private AddDistributorViewModel mViewModel;
    private AddDistributorFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(AddDistributorViewModel.class);
        binding = AddDistributorFragmentBinding.inflate(inflater, container, false);
        binding.createDistributor.setOnClickListener(v -> {
            String name = binding.disNameField.getEditableText().toString();
            String password = binding.disPasswordField.getEditableText().toString();
            String token = LoginStateChecker.getToken(requireContext());
            binding.createDistributor.setEnabled(false);
            binding.createPb.setVisibility(View.VISIBLE);
            mViewModel.createDistributor(token, name, password)
                    .observe(getViewLifecycleOwner(), this::onDataArrive);

        });
        return binding.getRoot();
    }

    private void onDataArrive(GetData<User> data) {
        binding.createDistributor.setEnabled(true);
        binding.disPasswordContainer.setError(null);
        binding.disNameContainer.setError(null);
        binding.createPb.setVisibility(View.GONE);
        if (data == null) {
            Toast.makeText(requireContext(), "Network Error.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (data.getCode() > 200) {
           ArrayList<Errors> errors = data.getErrors();
            for (Errors e:errors){
                if (e.getKey().equals("name")){
                    ArrayList<String>es = e.getErrors();
                    binding.disNameContainer.setError(es.get(0));
                }
                else if(e.getKey().equals("password")){
                    ArrayList<String>es = e.getErrors();
                    binding.disPasswordContainer.setError(es.get(0));
                }
            }
            return;
        }
        Toast.makeText(requireContext(), "Added Successfully.", Toast.LENGTH_SHORT).show();
        requireActivity().onBackPressed();


    }


}