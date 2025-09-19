package com.example.listycitylab3;

import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton; // Import for FAB
import java.util.ArrayList;
import java.util.Arrays;

// Implement the listener interface from AddCityFragment
public class MainActivity extends AppCompatActivity implements AddCityFragment.OnFragmentInteractionListener {

    private ArrayList<City> dataList;
    private ListView cityList;
    private CityList cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        City[] cities = {
                new City("Edmonton", "AB"),
                new City("Vancouver", "BC"),
                new City("Moscow", "RU"),
                new City("Sydney", "NSW"),
                new City("Berlin", "DE"),
                new City("Vienna", "AT"),
                new City("Tokyo", "JP"),
                new City("Beijing", "CN"),
                new City("Osaka", "JP"),
                new City("New Delhi", "IN")
        };

        dataList = new ArrayList<>(Arrays.asList(cities));
        
        cityList = findViewById(R.id.city_list);
        cityAdapter = new CityList(this, dataList);
        cityList.setAdapter(cityAdapter);

        // Setup FloatingActionButton
        FloatingActionButton fab = findViewById(R.id.fab_add_city);
        fab.setOnClickListener(v -> {
            AddCityFragment.newInstance().show(getSupportFragmentManager(), "ADD_CITY");
        });

        // Setup ListView item click listener for editing
        cityList.setOnItemClickListener((parent, view, position, id) -> {
            City cityToEdit = dataList.get(position);
            AddCityFragment.newInstance(cityToEdit).show(getSupportFragmentManager(), "EDIT_CITY");
        });
    }

    @Override
    public void onOkPressed(City newCity) {
        dataList.add(newCity);
        cityAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEditPressed(City existingCity, String newCityName, String newProvinceName) {
        
        int index = dataList.indexOf(existingCity);
        if (index != -1) {
            City cityInList = dataList.get(index);
            cityInList.setCityName(newCityName);
            cityInList.setProvinceName(newProvinceName);
        } else {

            boolean foundAndUpdated = false;
            for (City city : dataList) {
                if (city.getCityName().equals(existingCity.getCityName()) && 
                    city.getProvinceName().equals(existingCity.getProvinceName())) { 
                    city.setCityName(newCityName);
                    city.setProvinceName(newProvinceName);
                    foundAndUpdated = true;
                    break;
                }
            }
        }
        cityAdapter.notifyDataSetChanged();
    }
}
