package com.example.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.catsearch.R;
import com.example.fragments.FavouritesFragment;
import com.example.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/*
 *  MainActivity.java
 *  In this class handles what happens on App Start.
 *  The App always starts with a 'SearchFragment' loaded.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                                                         // onCreate (Guarantee user-friendliness)
        setContentView(R.layout.activity_main);                                                     // Set the activity to be used.

        // Link Java Variables to Layout-Elements:
        BottomNavigationView bnv1 = findViewById(R.id.bnvMain);                                     // Link UI-Element (via it's ID "bnvMain") to the Java variable "bnv1".

        // Initialize 'home fragment' on App Start:
        swapFragment(new SearchFragment());

        // BottomNavigationView:
        bnv1.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigationSearch:                                                     // If the BNV-element with the id 'navigationSearch' (In 'navigation.xml') is pressed:
                        swapFragment(new SearchFragment());                                         // place the 'SearchFragment' into the 'fragmentContainer'.
                        break;
                    case R.id.navigationFavourites:                                                 // If the BNV-element with the id 'navigationFavourites' (In 'navigation.xml') is pressed:
                        swapFragment(new FavouritesFragment());                                     // place the 'FavouritesFragment' into the 'fragmentContainer'.
                        break;
                }
                return true;
            }
        });
    }

    /**
     * Helper method to change the fragment displayed in the activity.
     * @param fmt: instance of the fragment that is supposed to go into the slot.
     */
    public void swapFragment(Fragment fmt) {
        FragmentManager FM = getSupportFragmentManager();                                           // Instantiate a new FragmentManager object 'FM'. (Needed to to the add / replace / remove commands)
        FragmentTransaction fmtTransaction = FM.beginTransaction();
        fmtTransaction.replace(R.id.fragmentContainer, fmt);
        fmtTransaction.commit();                                                                    // Execute the action above.
    }
}