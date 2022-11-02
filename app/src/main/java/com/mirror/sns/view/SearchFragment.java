package com.mirror.sns.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mirror.sns.adapter.PostAdapter;
import com.mirror.sns.adapter.TagAdapter;
import com.mirror.sns.classes.Sns;
import com.mirror.sns.classes.Tag;
import com.mirror.sns.databinding.FragmentSearchBinding;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";


    private FragmentSearchBinding searchBinding;

    private PostAdapter postAdapter;
    private TagAdapter tagAdapter;

    public SearchFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        searchBinding = FragmentSearchBinding.inflate(inflater, container, false);
        return searchBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        searchBinding.postsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        searchBinding.postsRecyclerView.setHasFixedSize(true);
        postAdapter = new PostAdapter();
        searchBinding.postsRecyclerView.setAdapter(postAdapter);

        searchBinding.tagsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        searchBinding.tagsRecyclerView.setHasFixedSize(true);
        tagAdapter = new TagAdapter();
        searchBinding.tagsRecyclerView.setAdapter(tagAdapter);

        List<Tag> tagList = new ArrayList<>();

        tagList.add(new Tag(""));
        tagList.add(new Tag(""));
        tagList.add(new Tag(""));
        tagList.add(new Tag(""));
        tagList.add(new Tag(""));
        tagList.add(new Tag(""));
        tagList.add(new Tag(""));
        tagList.add(new Tag(""));

        tagAdapter.setTagList(tagList);


        List<Sns> snsList = new ArrayList<>();
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        postAdapter.setSnses(snsList);




    }


}
