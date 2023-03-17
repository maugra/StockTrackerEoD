package com.example.stocktrackereod;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.stocktrackereod.databinding.FragmentSecondBinding;
import com.example.stocktrackereod.position.Position;

import java.util.Objects;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.submitNewPosition.setOnClickListener(view1 -> {
            String symbol = binding.symbolInput.getText().toString();
            int amount = Integer.parseInt(binding.amountInput.getText().toString());
            Position position = new Position();
            position.setSymbol(symbol);
            position.setAmount(amount);
            MainActivity mainActivity = (MainActivity) Objects.requireNonNull(getActivity());
            mainActivity.getPriceForPosition(position);
            mainActivity.getPortfolio().getPositions().add(position);
            mainActivity.dataSetChanged();
            NavHostFragment.findNavController(SecondFragment.this)
                    .navigate(R.id.action_SecondFragment_to_FirstFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}