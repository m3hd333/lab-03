package com.example.listycitylab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {
    private EditText cityNameEditText;
    private EditText provinceNameEditText;
    private OnFragmentInteractionListener listener;
    private City existingCity;

    public interface OnFragmentInteractionListener {
        void onOkPressed(City newCity); // For adding a new city
        void onEditPressed(City existingCity, String newCityName, String newProvinceName); // For editing
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    // newInstance method for creating fragment (can be used for adding without a city)
    public static AddCityFragment newInstance() {
        return new AddCityFragment();
    }

    // newInstance method for editing an existing city
    public static AddCityFragment newInstance(City city) {
        AddCityFragment fragment = new AddCityFragment();
        Bundle args = new Bundle();
        args.putSerializable("city_to_edit", city);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        cityNameEditText = view.findViewById(R.id.edit_text_city_name);
        provinceNameEditText = view.findViewById(R.id.edit_text_province_name);

        String dialogTitle = "Add City"; // Default title

        // Check if a city was passed for editing
        if (getArguments() != null && getArguments().containsKey("city_to_edit")) {
            existingCity = (City) getArguments().getSerializable("city_to_edit");
            if (existingCity != null) {
                cityNameEditText.setText(existingCity.getCityName());
                provinceNameEditText.setText(existingCity.getProvinceName());
                dialogTitle = "Edit City";
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(dialogTitle)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", (dialog, which) -> {
                    String cityName = cityNameEditText.getText().toString();
                    String provinceName = provinceNameEditText.getText().toString();

                    if (cityName.isEmpty()) { // Basic validation
                        cityNameEditText.setError("City name cannot be empty");
                        return;
                    }

                    if (existingCity != null) {
                        // Editing existing city
                        listener.onEditPressed(existingCity, cityName, provinceName);
                    } else {
                        // Adding new city
                        listener.onOkPressed(new City(cityName, provinceName));
                    }
                })
                .create();
    }
}
