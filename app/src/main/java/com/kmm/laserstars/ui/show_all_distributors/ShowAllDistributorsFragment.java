package com.kmm.laserstars.ui.show_all_distributors;

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

import com.kmm.laserstars.R;
import com.kmm.laserstars.adapters.DistributorsAdapter;
import com.kmm.laserstars.databinding.ShowAllDistributorsFragmentBinding;
import com.kmm.laserstars.models.GetData;
import com.kmm.laserstars.models.User;
import com.kmm.laserstars.models.UserType;
import com.kmm.laserstars.util.Constant;
import com.kmm.laserstars.util.LoginStateChecker;

import java.util.ArrayList;

public class ShowAllDistributorsFragment extends Fragment {

    private ShowAllDistributorsViewModel mViewModel;
    private ShowAllDistributorsFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ShowAllDistributorsViewModel.class);
        binding = ShowAllDistributorsFragmentBinding.inflate(inflater, container, false);
        mViewModel.getAllDistributors().observe(getViewLifecycleOwner(), this::onUsersArrive);

        if (LoginStateChecker.isLoggedIn(requireContext())) {
            if (LoginStateChecker.getUserType(requireContext()) == UserType.admin)
                binding.addDistributor.setVisibility(View.VISIBLE);
        }
        binding.addDistributor.setOnClickListener(v -> {
            NavController controller = NavHostFragment.findNavController(this);
            controller.navigate(R.id.action_showAllDistributorsFragment_to_addDistributorFragment);
        });
        return binding.getRoot();
    }

    private void onUsersArrive(GetData<ArrayList<User>> data) {
        binding.designsPb.setVisibility(View.GONE);
        binding.distMenu.setVisibility(View.VISIBLE);
        if (data == null || data.getData() == null) {
            Toast.makeText(requireContext(), "خطأ في الشبكة", Toast.LENGTH_SHORT).show();
            return;
        }
        if (data.getData().size() == 0) {
            binding.noDestributor.setVisibility(View.VISIBLE);
        }
        ArrayList<User> distributors = data.getData();
        DistributorsAdapter adapter = new DistributorsAdapter(distributors);
        binding.distMenu.setAdapter(adapter);
        if (LoginStateChecker.isUserAdmin(requireContext())) {
            adapter.setOnItemLongClickListener((pos, id, object) -> {
                new AlertDialog.Builder(requireContext())
                        .setTitle("حذف موزع")
                        .setMessage("هل تريد حقا حذف هذا الموزع ؟ [" +
                                object.getName() + "]")
                        .setNegativeButton("كلا", (dialogInterface, i) -> dialogInterface.dismiss())
                        .setPositiveButton("نعم", (dialogInterface, i) -> {
                            String token = LoginStateChecker.getToken(requireContext());
                            mViewModel.deleteDis(token, object.getId())
                                    .observe(getViewLifecycleOwner(), data1 -> {
                                        Toast.makeText(requireContext(), data1.getMsg()
                                                , Toast.LENGTH_SHORT).show();
                                        if (data1.getCode() == 200) {
                                            adapter.removeDis(pos);
                                        }
                                    });
                        })
                        .show();

            });
        }
        adapter.setOnItemClickListener((pos, id, object) -> {
            NavController controller = NavHostFragment.findNavController(this);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.DISTRIBUTOR, object);
            bundle.putString("name", object.getName());
            controller.navigate(R.id.action_showAllDistributorsFragment_to_distributorDesignFragment
                    , bundle);
        });
    }

}