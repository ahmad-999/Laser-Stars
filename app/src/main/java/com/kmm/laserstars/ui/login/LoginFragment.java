package com.kmm.laserstars.ui.login;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kmm.laserstars.activities.MainActivity;
import com.kmm.laserstars.R;
import com.kmm.laserstars.databinding.LoginFragmentBinding;
import com.kmm.laserstars.models.GetData;
import com.kmm.laserstars.models.User;
import com.kmm.laserstars.util.LoginStateChecker;

public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;
    private LoginFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding = LoginFragmentBinding.inflate(inflater, container, false);
        binding.loginButton.setOnClickListener(v -> {
            binding.loginPb.setVisibility(View.VISIBLE);
            binding.loginButton.setEnabled(false);
            String username = binding.userNameField.getEditableText().toString();
            String password = binding.passwordField.getEditableText().toString();
            if (username.length() < 5) {
                Toast.makeText(requireContext(), "name must be 5 letter length at least."
                        , Toast.LENGTH_LONG).show();
                return;
            }
            mViewModel.login(username, password).observe(getViewLifecycleOwner(), this::onLoginResponse);
        });
        return binding.getRoot();
    }

    private void onLoginResponse(GetData<User> data) {
        binding.loginPb.setVisibility(View.GONE);
        binding.loginButton.setEnabled(true);
        if (data == null || data.getData() == null) {
                Toast.makeText(requireContext(), "Network Error.", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(requireContext(), data.getMsg(), Toast.LENGTH_SHORT).show();
        User user = data.getData();
        LoginStateChecker.login(requireContext(), user.getToken(),user.getType());
        requireActivity().finish();
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        intent.putExtra("route_name",R.id.accountFragment);
        requireActivity().startActivity(intent);
    }


}