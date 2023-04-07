package com.kmm.laserstars.ui.design_view;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.kmm.laserstars.activities.MainActivity;
import com.kmm.laserstars.R;
import com.kmm.laserstars.adapters.DesginImageSliderAdapter;
import com.kmm.laserstars.databinding.DesignViewFragmentBinding;
import com.kmm.laserstars.models.Design;
import com.kmm.laserstars.util.Constant;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DesignViewFragment extends Fragment {

    private DesignViewViewModel mViewModel;
    private DesignViewFragmentBinding binding;
    private boolean isSlider = false;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        ((MainActivity) requireActivity()).getSupportActionBar().hide();
        ((MainActivity) requireActivity()).binding.bottomNavView.setVisibility(View.GONE);
        requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        ((MainActivity) requireActivity()).getSupportActionBar().show();
        ((MainActivity) requireActivity()).binding.bottomNavView.setVisibility(View.VISIBLE);
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onDetach();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(DesignViewViewModel.class);
        binding = DesignViewFragmentBinding.inflate(inflater, container, false);
        String url = requireArguments().getString(Constant.IMAGE_URL, "");
        if (url.equals("")) {
            Toast.makeText(requireContext(), "Error on loading image url.",
                    Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
        }
        ArrayList<Design> desgins = (ArrayList<Design>)
                requireArguments().getSerializable(Constant.DESIGNS);
        DesginImageSliderAdapter adapter = new DesginImageSliderAdapter(desgins);
        binding.imageSlider.setSliderAdapter(adapter);
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM);
        binding.imageSlider.setScrollTimeInSec(2);
        binding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.ZOOMOUTTRANSFORMATION);
//        binding.imageSlider.startAutoCycle();

        for (int i = 0; i < desgins.size(); i++) {
            if (desgins.get(i).getUrl().equals(url)) {
                binding.imageSlider.setCurrentPagePosition(i);
                break;
            }
        }
        binding.toggleSlider.setOnClickListener(v -> {
            if (!isSlider) {
                binding.toggleSlider.setImageResource(R.drawable.ic_baseline_stop_24);
                binding.imageSlider.setAutoCycle(true);
                binding.imageSlider.startAutoCycle();

            } else {
                binding.toggleSlider.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                binding.imageSlider.setAutoCycle(false);
                binding.imageSlider.stopAutoCycle();
            }
            isSlider = !isSlider;

        });

        return binding.getRoot();
    }


}