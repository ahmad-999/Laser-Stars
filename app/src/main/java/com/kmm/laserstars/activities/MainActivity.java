package com.kmm.laserstars.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.customview.widget.Openable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kmm.laserstars.R;
import com.kmm.laserstars.databinding.MainActivityBinding;
import com.kmm.laserstars.ui.main.MainFragment;
import com.kmm.laserstars.util.LoginStateChecker;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    public MainActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        setTitle("ليزر ستارز");

        Intent intent = getIntent();
        if (intent.hasExtra("route_name")) {
            int id = intent.getIntExtra("route_name", R.id.homeFragment);
            View view = binding.bottomNavView.findViewById(id);
            view.performClick();
        }
        NavController controller = Navigation.findNavController(this, R.id.main_fragment);
        NavigationUI.setupWithNavController(binding.bottomNavView, controller);
        NavigationUI.setupWithNavController(binding.toolbar, controller);
        if (!LoginStateChecker.isLoggedIn(this) &&
                !LoginStateChecker.isUserAdmin(this)) {
            binding.bottomNavView.getMenu().removeItem(R.id.tagsFragment);
            binding.bottomNavView.invalidate();
        }
        binding.bottomNavView.setOnNavigationItemSelectedListener(item -> {
            NavigationUI.onNavDestinationSelected(item, controller, false);
            binding.toolbar.setNavigationIcon(null);
            return false;
        });
    }
}