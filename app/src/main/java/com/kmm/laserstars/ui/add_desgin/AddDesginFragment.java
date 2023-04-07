package com.kmm.laserstars.ui.add_desgin;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kmm.laserstars.R;
import com.kmm.laserstars.adapters.AddDesignGenreAdapter;
import com.kmm.laserstars.adapters.TagAdapter;
import com.kmm.laserstars.databinding.AddDesginFragmentBinding;
import com.kmm.laserstars.databinding.DeleteDesignDialogBinding;
import com.kmm.laserstars.databinding.TagsBottomSheetAddDesignLayoutBinding;
import com.kmm.laserstars.models.Design;
import com.kmm.laserstars.models.GetData;
import com.kmm.laserstars.models.Tag;
import com.kmm.laserstars.models.TagGenre;
import com.kmm.laserstars.models.UserType;
import com.kmm.laserstars.services.API;
import com.kmm.laserstars.util.Constant;
import com.kmm.laserstars.util.LoginStateChecker;
import com.kmm.laserstars.util.TimerThread;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddDesginFragment extends Fragment {
    private AddDesginFragmentBinding binding;
    private AddDesginViewModel mViewModel;
    //    private TagAdapter adapter;
    private AddDesignGenreAdapter adapter;

    private String designImagePath = null, designVideoPath = null;
    private TimerThread timerThread = null, videoTimerThread = null;
    private Bitmap oldImage = null;
    private ExoPlayer exoPlayer = null;
    private boolean isVideoChanged = false;

    @Override
    public void onDetach() {
        super.onDetach();
        if (timerThread != null && timerThread.isAlive())
            timerThread.interrupt();
        if (videoTimerThread != null && videoTimerThread.isAlive())
            videoTimerThread.interrupt();
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        exoPlayer = new SimpleExoPlayer.Builder(requireContext()).build();
        binding = AddDesginFragmentBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(AddDesginViewModel.class);
        mViewModel.getAllTagGenres().observe(getViewLifecycleOwner(), this::onTagsGenreArrive);
        Bundle bundle = requireArguments();
        binding.chooseImg.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "choose image"), 101);
        });
        if (bundle.getSerializable("desgin") == null) {

            binding.createDesign.setOnClickListener(v -> {
                if (adapter == null) return;
                String token = LoginStateChecker.getToken(requireContext());
                int[] ids = adapter.getCheckedId();
                String designName = binding.designNameField.getEditableText().toString();
                String designDesc = binding.designDescField.getEditableText().toString();
                mViewModel.createDesign(token, designName, designDesc, designImagePath, ids,
                        designVideoPath, isVideoChanged)
                        .observe(getViewLifecycleOwner(), this::onDesignCreated);
            });
        } else {
            binding.deleteDesign.setVisibility(View.VISIBLE);
            binding.createDesign.setText("تحديث التصميم");
            Design design = (Design) requireArguments().getSerializable("desgin");
            binding.designDescField.setText(design.getDesc());
            binding.designNameField.setText(design.getName());
            binding.choosenImageContainer.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(API.RES_URL + design.getUrl())
                    .placeholder(R.drawable.ic_baseline_wifi_protected_setup_24)
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e,
                                                    Object model,
                                                    Target<Drawable> target,
                                                    boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource,
                                                       Object model,
                                                       Target<Drawable> target,
                                                       DataSource dataSource,
                                                       boolean isFirstResource) {
                            if (resource instanceof BitmapDrawable)
                                oldImage = ((BitmapDrawable) resource).getBitmap();

                            return false;
                        }
                    })
                    .into(binding.choosenImage);
            String token = LoginStateChecker.getToken(requireContext());
            binding.createDesign.setOnClickListener(v -> {
                if (adapter == null) return;

                int[] ids = adapter.getCheckedId();
                String designName = binding.designNameField.getEditableText().toString();
                String designDesc = binding.designDescField.getEditableText().toString();
                int id = design.getId();
                mViewModel.updateDesign(token, designName, designDesc, designImagePath, ids, id,
                        designVideoPath, isVideoChanged)
                        .observe(getViewLifecycleOwner(), this::onDesignCreated);
            });
            binding.deleteDesign.setOnClickListener(v -> {

                DeleteDesignDialogBinding deleteBinding =
                        DeleteDesignDialogBinding.inflate(getLayoutInflater());
                AlertDialog alertDialog = new AlertDialog.Builder(requireActivity())
                        .setView(deleteBinding.getRoot())
                        .show();
                deleteBinding.title.setText("هل ترغب بحذف هذا التصميم ؟");
                Glide.with(requireActivity())
                        .load(API.RES_URL + design.getUrl())
                        .into(deleteBinding.image);
                deleteBinding.btnNo.setOnClickListener(v1 -> alertDialog.dismiss());
                deleteBinding.btnYes.setOnClickListener(v1 -> {
                    int id = design.getId();
                    mViewModel.deleteDesign(token, id)
                            .observe(getViewLifecycleOwner(), this::onDesignCreated);
                    alertDialog.dismiss();
                });

            });
        }

        binding.editImage.setOnClickListener(v -> {
            if (mViewModel.getUploadModel().getValue() == null) return;
            if (mViewModel.getUploadModel().getValue()) {
                // disable edit image option or choose new image

                timerThread.interrupt();
                binding.circleProgress.setProgress(0F);
                binding.editImage.setVisibility(View.GONE);
                binding.circleProgress.setVisibility(View.GONE);

            } else {
                // open image chooser
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "اختيار صرة"), 102);

            }
        });
        binding.chooseVideo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("video/*");
            startActivityForResult(Intent.createChooser(intent, "اختيار فيديو"), 103);
        });
        binding.editVideo.setOnClickListener(v -> {
            if (mViewModel.getUploadVideoModel().getValue() == null) return;
            if (mViewModel.getUploadVideoModel().getValue()) {
                // disable edit image option or choose new image
                isVideoChanged = true;
                videoTimerThread.interrupt();
                binding.circleProgressVideo.setProgress(0F);
                binding.editVideo.setVisibility(View.GONE);
                binding.circleProgressVideo.setVisibility(View.GONE);

            } else {
                // open video chooser
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                startActivityForResult(Intent.createChooser(intent, "اختيار فيديو"), 104);

            }
        });
        mViewModel.getUploadModel().observe(getViewLifecycleOwner(),
                this::onModeChange);
        mViewModel.getUploadVideoModel().observe(getViewLifecycleOwner(),
                this::onModeChangeVideo);
        binding.tagsButton.setOnClickListener(v -> {
            BottomSheetDialog dialog = new BottomSheetDialog(requireActivity());
            TagsBottomSheetAddDesignLayoutBinding dialogBinding
                    = TagsBottomSheetAddDesignLayoutBinding.inflate(inflater);
            dialogBinding.tagsGenreRv.setAdapter(adapter);
            dialog.setContentView(dialogBinding.getRoot());
            dialogBinding.selectTags.setOnClickListener(vv -> dialog.dismiss());
            dialog.show();
        });
        return binding.getRoot();
    }


    private void onModeChangeVideo(Boolean value) {
        Uri uri;
        MediaSource mediaSource;
        if (value) {
            if (designVideoPath != null) {
                uri = Uri.parse(designVideoPath);
                isVideoChanged = true;
            } else return;
            mediaSource = buildMediaSource(uri);
            binding.videoContainer.setVisibility(View.VISIBLE);
            binding.editVideo.setImageResource(R.drawable.ic_baseline_check_24);
            binding.circleProgressVideo.setVisibility(View.VISIBLE);
            if (videoTimerThread != null)
                videoTimerThread.interrupt();
            videoTimerThread = new TimerThread(requireActivity(), binding, mViewModel);
            videoTimerThread.start();
        } else {
            Design design = (Design) requireArguments().getSerializable("desgin");
            if (designVideoPath == null) {
                if (design == null || design.getVideoURL() == null) return;
                else binding.videoContainer.setVisibility(View.VISIBLE);
            } else {
                if (design == null || design.getVideoURL() == null) {
                    binding.videoContainer.setVisibility(View.GONE);
                    binding.chooseVideo.setEnabled(true);
                    return;
                }
            }
            binding.editVideo.setImageResource(R.drawable.ic_baseline_edit_24);
            binding.circleProgressVideo.setVisibility(View.GONE);
            uri = Uri.parse(API.RES_URL + design.getVideoURL());
            isVideoChanged = false;
            mediaSource = buildMediaSource(uri);

        }
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = new SimpleExoPlayer.Builder(requireContext()).build();
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
            binding.video.setPlayer(exoPlayer);
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        DefaultDataSourceFactory dataSourceFactory = new
                DefaultDataSourceFactory(requireActivity(), "javiermarsicano-VR-app");
        // Create a media source using the supplied URI
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
    }

    private void onModeChange(Boolean value) {

        if (value) {
            binding.choosenImageContainer.setVisibility(View.VISIBLE);
            binding.editImage.setImageResource(R.drawable.ic_baseline_check_24);
            binding.circleProgress.setVisibility(View.VISIBLE);
            if (timerThread != null)
                timerThread.interrupt();
            timerThread = new TimerThread(requireActivity(), binding, mViewModel);
            timerThread.start();
        } else {
            binding.editImage.setImageResource(R.drawable.ic_baseline_edit_24);
            binding.circleProgress.setVisibility(View.GONE);
//            binding.choosenImageContainer.setVisibility(View.GONE);
            binding.choosenImage.setImageBitmap(oldImage);
        }
    }


    private void onDesignCreated(GetData<String> data) {
        if (data == null) {
            Toast.makeText(requireContext(), "خطأ في الشبكة", Toast.LENGTH_SHORT).show();
            return;
        }
        if (data.getCode() > 200) {
            Toast.makeText(requireContext(), data.getMsg(), Toast.LENGTH_SHORT).show();
            return;
        }
        if (data.getData() == null) {
            Toast.makeText(requireContext(), data.getMsg(), Toast.LENGTH_SHORT).show();
        } else Toast.makeText(requireContext(), data.getData(), Toast.LENGTH_SHORT).show();
        deleteFormCache();
        requireActivity().onBackPressed();
    }

    private void deleteFormCache() {
        if (designImagePath == null) return;
        File file = new File(designImagePath);
        file.delete();
        if (designVideoPath == null) return;
        file = new File(designVideoPath);
        file.delete();

    }

    private void onTagsGenreArrive(GetData<ArrayList<TagGenre>> data) {
        if (data == null) {
            Toast.makeText(requireContext(), "خطأ في الشبكة", Toast.LENGTH_SHORT).show();
            return;
        }
//        adapter = new TagAdapter(data.getData(), LoginStateChecker.getUserType(requireContext())
//                == UserType.admin);
//        adapter.setOnAddTagClick((pos, id, object) -> {
//            NavController controller = NavHostFragment.findNavController(this);
//            controller.navigate(R.id.action_addDesginFragment_to_tagsFragment);
//        });

        adapter = new AddDesignGenreAdapter(data.getData());
        binding.tagsButton.setVisibility(View.VISIBLE);


        Bundle bundle = requireArguments();
        if (bundle.getSerializable("desgin") != null) {
            Design design = (Design) bundle.getSerializable("desgin");
            ArrayList<Tag> desginTags = design.getTags();
            adapter.setTagsChecks(desginTags);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 @Nullable @org.jetbrains.annotations.Nullable Intent data) {

        if (requestCode == 101
                && resultCode == Activity.RESULT_OK
                && data != null
                && data.getData() != null) {
            binding.chooseImg.setEnabled(false);
            mViewModel.getUploadModel().postValue(true);
            designImagePath = getPathFromURI(data.getData(), true, false);

        }
        if (requestCode == 102
                && resultCode == Activity.RESULT_OK
                && data != null
                && data.getData() != null) {
            binding.chooseImg.setEnabled(false);
            binding.choosenImageContainer.setVisibility(View.VISIBLE);
            mViewModel.getUploadModel().postValue(true);
            designImagePath = getPathFromURI(data.getData(), false, false);
            Bitmap bitmap = BitmapFactory.decodeFile(designImagePath);
            if (bitmap == null) return;
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            if (!Constant.CHECK_IMAGE_DIM(height, width)) {
                Toast.makeText(requireContext(),
                        "لا يمكن استخدام هذه الصورة لانها لا تتبع حجم الصورة المطلوبة"
                        , Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == 103
                && resultCode == Activity.RESULT_OK
                && data != null
                && data.getData() != null) {
            binding.chooseVideo.setEnabled(false);
            mViewModel.getUploadVideoModel().postValue(true);
            designVideoPath = getPathFromURI(data.getData(), true, true);

        }
        if (requestCode == 104
                && resultCode == Activity.RESULT_OK
                && data != null
                && data.getData() != null) {
            binding.chooseVideo.setEnabled(false);
            binding.choosenImageContainer.setVisibility(View.VISIBLE);
            mViewModel.getUploadVideoModel().postValue(true);
            designVideoPath = getPathFromURI(data.getData(), false, true);

        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getPathFromURI(Uri contentUri, boolean isOld, boolean isVideo) {
        File cache = requireActivity().getCacheDir();
        if (!isVideo) {
            String fileName = "image.jpg";

            File image = new File(cache.getAbsolutePath() + "/" + fileName);
            try {
                if (image.exists())
                    image.delete();
                image.createNewFile();
//            Bitmap bitmap = Bitmap.createBitmap
                InputStream src = requireContext().getContentResolver().openInputStream(contentUri);
                Bitmap bitmap = BitmapFactory.decodeStream(src);
                OutputStream dst = new FileOutputStream(image);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, dst);
                if (isOld)
                    oldImage = bitmap;
                binding.choosenImage.setImageBitmap(bitmap);
//            bitmap.recycle();
//            byte[] buf = new byte[1024];
//            int len;
//            while ((len = src.read(buf)) > 0) {
//                dst.write(buf, 0, len);
//            }
//            src.close();
//            dst.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image.getAbsolutePath();
        } else {
            String fileName = "video.mp4";

            File video = new File(cache.getAbsolutePath() + "/" + fileName);
            try {
                if (video.exists())
                    video.delete();
                video.createNewFile();
                FileOutputStream dst =
                        new FileOutputStream(video);
                InputStream src = requireContext().
                        getContentResolver().openInputStream(contentUri);

                byte[] buf = new byte[1024];
                int len;
                while ((len = src.read(buf)) > 0) {
                    dst.write(buf, 0, len);
                }
                src.close();
                dst.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return video.getAbsolutePath();
        }
    }
}