package com.kmm.laserstars.activities.splash;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.kmm.laserstars.activities.MainActivity;
import com.kmm.laserstars.databinding.ActivitySplashBinding;
import com.kmm.laserstars.ui.login.LoginViewModel;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    private SplashViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        binding.loadingPb.setVisibility(View.VISIBLE);
        mViewModel.getAllDesigns().observe(this,
                data -> {
                    binding.loadingPb.setVisibility(View.GONE);
                    if (data == null) {
                        Toast.makeText(this, "Network Error.", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra("designs", data);
                        startActivity(intent);
                        finish();
                    }
                });
    }
}