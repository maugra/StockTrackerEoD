package com.example.stocktrackereod;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocktrackereod.databinding.FragmentOverviewBinding;

public class OverviewFragment extends Fragment {

    private FragmentOverviewBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentOverviewBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity mainActivity = (MainActivity) requireActivity();

        RecyclerView recyclerView = view.findViewById(R.id.positions_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mainActivity.getAdapter());

    }

    @Override
    public void onResume(){
        super.onResume();
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.updatePortfolio();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

    }

}