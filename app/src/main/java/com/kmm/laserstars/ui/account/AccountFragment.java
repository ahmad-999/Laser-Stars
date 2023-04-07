package com.kmm.laserstars.ui.account;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
import com.kmm.laserstars.activities.MainActivity;
import com.kmm.laserstars.R;
import com.kmm.laserstars.databinding.AccountFragmentBinding;
import com.kmm.laserstars.models.GetData;
import com.kmm.laserstars.models.User;
import com.kmm.laserstars.services.API;
import com.kmm.laserstars.util.LoginStateChecker;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static android.app.Activity.RESULT_OK;

public class AccountFragment extends Fragment {

    private AccountViewModel mViewModel;
    private AccountFragmentBinding binding;
    private static int USER_ID = -1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = AccountFragmentBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        if (!LoginStateChecker.isLoggedIn(requireContext())) {
            NavController controller = NavHostFragment.findNavController(this);
            controller.navigate(R.id.action_accountFragment_to_loginFragment);
        } else {
            binding.logout.setVisibility(View.VISIBLE);
            binding.logout.setOnClickListener(v -> {
                LoginStateChecker.logout(requireContext());
                Intent intent = new Intent(requireActivity(), MainActivity.class);
                startActivity(intent);
                requireActivity().finish();
            });
            String token = LoginStateChecker.getToken(requireContext());
            mViewModel.me(token).observe(getViewLifecycleOwner(), this::onMyDataArrive);
        }
        binding.saveEdit.setOnClickListener(v -> {
            if (USER_ID == -1) {
                Toast.makeText(requireContext(), "Network Error", Toast.LENGTH_SHORT).show();

                return;
            }
            String fileName = "avatar.jpg";
            String token = LoginStateChecker.getToken(requireContext());
            String name = binding.userNameField.getEditableText().toString();
            String phone = binding.phoneField.getEditableText().toString();
            File cache = requireActivity().getCacheDir();
            File image = new File(cache.getAbsolutePath() + "/" + fileName);
            try {
                if (image.exists())
                    image.delete();
                image.createNewFile();
                Bitmap bitmap = ((BitmapDrawable) binding.avatar.getDrawable()).getBitmap();
                OutputStream dst = new FileOutputStream(image);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, dst);
                mViewModel.updateUser(token, USER_ID, name, phone, image)
                        .observe(getViewLifecycleOwner(), this::onDataUpdated);
                binding.loginPb.setVisibility(View.VISIBLE);
                binding.saveEdit.setEnabled(false);
            } catch (IOException ignored) {
            }

        });
        return binding.getRoot();
    }

    private void onDataUpdated(GetData<String> data) {
        binding.loginPb.setVisibility(View.GONE);
        binding.saveEdit.setEnabled(true);
        if (data == null) {
            Toast.makeText(requireContext(), "Network Error.", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(requireContext(), data.getMsg(), Toast.LENGTH_SHORT).show();
        requireActivity().onBackPressed();
    }

    private void onMyDataArrive(GetData<User> data) {
        binding.groupField.setEnabled(false);
        binding.saveEdit.setVisibility(View.VISIBLE);
        binding.editAvatar.setVisibility(View.VISIBLE);
        binding.avatar.setVisibility(View.VISIBLE);
        binding.userNameContainer.setVisibility(View.VISIBLE);
        binding.phoneContainer.setVisibility(View.VISIBLE);
        binding.groupContainer.setVisibility(View.GONE);
        binding.loadingPb.setVisibility(View.GONE);
        if (data == null || data.getData() == null) {
            Toast.makeText(requireContext(), "Network Error.", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = data.getData();
        USER_ID = user.getId();
        binding.groupContainer.setVisibility(View.GONE);
        binding.groupField.setText(user.getGroup().getName());
        binding.userNameField.setText(user.getName());
        binding.phoneField.setText(user.getPhone());
        if (user.getAvatar() == null || user.getAvatar().equals("")) {
            Glide.with(this)
                    .load(R.drawable.laser_cutting_machine)
                    .into(binding.avatar);
        } else {
            Glide.with(this)
                    .load(API.RES_URL + user.getAvatar())
                    .into(binding.avatar);
        }
        binding.editAvatar.setOnClickListener(view -> {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(requireContext(), this);
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 @Nullable Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri uri = result.getUri();
                Glide.with(requireContext())
                        .load(uri)
                        .into(binding.avatar);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}