package com.example.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.catsearch.Breed;
import com.example.catsearch.R;
import com.example.catsearch.adapter.BreedAdapter;
import com.example.catsearch.data.AppDatabase;

/*
 *  FavouritesFragment.java
 *  This class defines the functionality of the 'favouritesFragment' layout.
 */

public class FavouritesFragment extends Fragment {
    // Define Java Variables:
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favourites, container, false);     // Connect to the layout file 'fragment_favourites.xml'.

        // Database:
        final AppDatabase db = AppDatabase.getInstance(getContext());
        Breed[] favouriteBreedsFromDB = db.breedDao().getAllFavourites();                           // Get all favourite Breeds from database.

        // Recycler View:
        recyclerView = v.findViewById(R.id.rv_fav);                                                 // 1.0) Link UI-Element (via it's ID 'rv_fav') to the Java variable 'recyclerView'.
        layoutManager = new LinearLayoutManager(getContext());                                      // 2.1) Create new Layout Manager
        recyclerView.setLayoutManager(layoutManager);                                               // 2.2) Assign Layout Manager to the RecyclerView it should manage.
        BreedAdapter breedAdapter = new BreedAdapter();                                             // 3.1) Create new Object of the class 'BreedAdapter' called 'breedAdapter'
        breedAdapter.setData(favouriteBreedsFromDB);                                                // 3.2) Call the method "setData()" in the class 'BreedAdapter' and pass this method all 'breed' objects from the API request.
        recyclerView.setAdapter(breedAdapter);                                                      // 3.3) Assign the Adapter object 'breedAdapter' to the RecyclerView it should manage.
        return v;
    }
}