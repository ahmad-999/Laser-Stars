package com.kmm.laserstars.ui.tags;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.kmm.laserstars.R;
import com.kmm.laserstars.adapters.AllTagsAdapter;
import com.kmm.laserstars.adapters.GenreTagAdapter;
import com.kmm.laserstars.adapters.TagAdapter;
import com.kmm.laserstars.databinding.AddItemLayoutBinding;
import com.kmm.laserstars.databinding.TagsFragmentBinding;
import com.kmm.laserstars.models.Design;
import com.kmm.laserstars.models.GetData;
import com.kmm.laserstars.models.Tag;
import com.kmm.laserstars.models.TagGenre;
import com.kmm.laserstars.util.LoginStateChecker;

import java.util.ArrayList;

public class TagsFragment extends Fragment {

    private TagsViewModel mViewModel;
    private TagsFragmentBinding binding;
    private GenreTagAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(TagsViewModel.class);
        binding = TagsFragmentBinding.inflate(inflater, container, false);
        binding.addTag.setVisibility(View.GONE);
        mViewModel.getAllTags().observe(getViewLifecycleOwner(), this::onTagsArrive);
        AddItemLayoutBinding addItemLayoutBinding
                = AddItemLayoutBinding.inflate(inflater);
        addItemLayoutBinding.setTitle("يرجى ادخال اسم الوسم الجديد");
        addItemLayoutBinding.setHint("اسم الوسم");
        addItemLayoutBinding.setButtonText("اضافة");
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(addItemLayoutBinding.getRoot()).create();
        binding.addTag.setOnClickListener(view -> {
            addItemLayoutBinding.tagsGenreContainer.setVisibility(View.VISIBLE);
            dialog.show();
            ArrayList<TagGenre> genres = adapter.getGenreTags();
            String[] list = new String[genres.size() + 1];
            list[0] = "نوع جديد";

            for (int i = 1; i < list.length; i++) {
                list[i] = genres.get(i - 1).getName();
            }
            ArrayAdapter<String> _adapter = new ArrayAdapter<>(requireContext(),
                    android.R.layout.simple_list_item_1, list);
            addItemLayoutBinding.tagsGenre.setAdapter(_adapter);
            final TagGenre[] genre = {null};

            addItemLayoutBinding.tagsGenre.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i == 0) {
                        dialog.dismiss();
                        showAddNewGenreDialog();
                    } else {
                        genre[0] = genres.get(i - 1);
                    }
                }
            });
            addItemLayoutBinding.button.setOnClickListener(view1 -> {
                addItemLayoutBinding.button.setVisibility(View.GONE);
                addItemLayoutBinding.pb.setVisibility(View.VISIBLE);
                String tagName = addItemLayoutBinding.itemField.getEditableText().toString();
                String token = LoginStateChecker.getToken(requireContext());
                if (genre[0] == null) {
                    Toast.makeText(requireContext(), "يرجى اختيار نوع الوسم",
                            Toast.LENGTH_SHORT).show();
                    addItemLayoutBinding.button.setVisibility(View.VISIBLE);
                    addItemLayoutBinding.pb.setVisibility(View.GONE);
                    return;
                }
                mViewModel.addTag(token, tagName, genre[0].getId())
                        .observe(getViewLifecycleOwner(), data -> {
                            addItemLayoutBinding.button.setVisibility(View.VISIBLE);
                            addItemLayoutBinding.pb.setVisibility(View.GONE);
                            if (data != null && data.getCode() == 200) {
                                addItemLayoutBinding.tagsGenre.setAdapter(null);
                                dialog.dismiss();
                                adapter.addTag(data.getData());
                            }
                        });

            });
        });
        return binding.getRoot();
    }

    private void showAddNewGenreDialog() {
        AddItemLayoutBinding addItemLayoutBinding =
                AddItemLayoutBinding.inflate(getLayoutInflater());
        AlertDialog alertDialog = new AlertDialog.Builder(requireActivity())
                .setView(addItemLayoutBinding.getRoot())
                .show();
        addItemLayoutBinding.setTitle("يرجى ادخال نوع الوسم الجديد");
        addItemLayoutBinding.setHint("نوع الوسم");
        addItemLayoutBinding.setButtonText("اضافة");
        addItemLayoutBinding.button.setOnClickListener(v -> {
            addItemLayoutBinding.button.setVisibility(View.GONE);
            addItemLayoutBinding.pb.setVisibility(View.VISIBLE);
            String genre = addItemLayoutBinding.itemField.getEditableText().toString();
            String token = LoginStateChecker.getToken(requireContext());
            mViewModel.addNewTagGenre(genre, token).observe(getViewLifecycleOwner(),
                    data -> {
                        alertDialog.dismiss();
                        if (data == null) {
                            Toast.makeText(requireContext(), "خطأ في الشبكة",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        adapter.addTagGenre(data.getData());
                    });
        });
    }

    private void onTagsArrive(GetData<ArrayList<TagGenre>> data) {
        binding.addTag.setVisibility(View.VISIBLE);
        if (data == null) {
            Toast.makeText(requireContext(), "خطأ في الشبكة", Toast.LENGTH_SHORT).show();
            return;
        }

        adapter = new GenreTagAdapter(data.getData());
        String token = LoginStateChecker.getToken(requireContext());
        adapter.setOnTagItemClick((object, id, arr) -> {

            new AlertDialog.Builder(requireContext())
                    .setMessage("هل تريد حقاُ حذف هذا الوسم ؟ [" +
                            object.getName() + "]")
                    .setTitle("حذف الوسم")
                    .setNegativeButton("كلا", (dialogInterface, i) -> dialogInterface.dismiss())
                    .setPositiveButton("نعم", (dialogInterface, i) -> {
                        mViewModel.removeTag(token, id)
                                .observe(getViewLifecycleOwner(), data1 -> {
                                    if (data1 == null || data1.getCode() > 200) {
                                        dialogInterface.dismiss();
                                        Toast.makeText(requireContext(),
                                                "لا يمكن حذف هذا الوسم حاليا .. يرجى المحاولة لاحقا"
                                                , Toast.LENGTH_SHORT).show();

                                    } else {
                                        adapter.removeTag(arr[0], arr[1]);
                                        Toast.makeText(requireContext(),
                                                data1.getMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }).show();

        });
        adapter.setOnTagGenreLongClick((pos, id, object) -> {


            new AlertDialog.Builder(requireContext())
                    .setMessage("هل تريد حقاُ حذف هذا نوع من الوسوم مع كافة الوسوم الخاصة به ؟ [" +
                            object.getName() + "]")
                    .setTitle("حذف نوع وسوم")
                    .setNegativeButton("كلا", (dialogInterface, i) -> dialogInterface.dismiss())
                    .setPositiveButton("نعم", (dialogInterface, i) -> {
                        mViewModel.removeGenre(token, id).observe(getViewLifecycleOwner(),
                                removeData -> {
                                    if (removeData == null) {
                                        Toast.makeText(requireContext(), "خطأ في الشبكة",
                                                Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    dialogInterface.dismiss();
                                    Toast.makeText(requireContext(),
                                            removeData.getMsg(), Toast.LENGTH_SHORT).show();
                                    adapter.removeGenre(pos);
                                });
                    }).show();


        });
        binding.tags.setAdapter(adapter);
    }

}