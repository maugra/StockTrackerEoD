package com.example.stocktrackereod;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.stocktrackereod.databinding.FragmentAddPositionBinding;
import com.example.stocktrackereod.position.Position;

public class AddPositionFragment extends Fragment {

    private FragmentAddPositionBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentAddPositionBinding.inflate(inflater, container, false);
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
            MainActivity mainActivity = (MainActivity) requireActivity();
            mainActivity.getPriceForPosition(position);
            mainActivity.getPortfolio().getPositions().add(position);
            mainActivity.dataSetChanged();
            NavHostFragment.findNavController(AddPositionFragment.this)
                    .navigate(R.id.action_SecondFragment_to_FirstFragment);
        });
    }

    @Override
    public void onDestroyView() {
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.updatePortfolio();
        super.onDestroyView();
        binding = null;
    }

}