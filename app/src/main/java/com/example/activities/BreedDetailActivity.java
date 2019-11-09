package com.example.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.catsearch.Breed;
import com.example.catsearch.R;
import com.example.catsearch.data.AppDatabase;
import com.example.catsearch.data.BreedDAO;

/*
 *  BreedDetailActivity.java
 *  In this class is all the detail breed data handled.
 *  (Read from the database and set into the Apps views)
 *  It also handles the 'favourites function' of the app.
 */

public class BreedDetailActivity extends AppCompatActivity {
    // Define Java Variables:
    private TextView txt1, txt2, txt3, txt4, txt5, txt6, txt7, txt8;
    private ImageView img1;
    private Button btn1;
    private String BreedID;
    private Breed breed;
    private BreedDAO breedDAO = AppDatabase.getInstance(this).breedDao();                           // DAO Object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                                                         // Standard code to initialize the Main View.
        setContentView(R.layout.activity_search_detail);                                            // Set the View to be created according to the Java Class.

        // Explicit Intent handling:
        Intent intent = getIntent();                                                                // Get the intent that was used to travel to this activity.
        BreedID = intent.getStringExtra("BreedID");                                          // Get the extra passed data (the 'BreedID' of the selected book). The ID has to have the same name, as where the intent was created!
        breed = breedDAO.getBreedByID(BreedID);

        // Link Java Variables to Layout-Elements:
        txt1 = findViewById(R.id.txtTitle);                                                         // Link UI-Element (via it's ID 'txtTitle') to the Java variable 'txt1'.
        txt2 = findViewById(R.id.txtDescription);                                                   // Link UI-Element (via it's ID 'txtDescription') to the Java variable 'txt2'.
        txt3 = findViewById(R.id.txtWeight);                                                        // Link UI-Element (via it's ID 'txtWeight') to the Java variable 'txt3'.
        txt4 = findViewById(R.id.txtTemperament);                                                   // Link UI-Element (via it's ID 'txtTemperament') to the Java variable 'txt4'.
        txt5 = findViewById(R.id.txtOrigin);                                                        // Link UI-Element (via it's ID 'txtOrigin') to the Java variable 'txt5'.
        txt6 = findViewById(R.id.txtLifeSpan);                                                      // Link UI-Element (via it's ID 'txtLifeSpan') to the Java variable 'txt6'.
        txt7 = findViewById(R.id.txtWikipediaLink);                                                 // Link UI-Element (via it's ID 'txtWikipediaLink') to the Java variable 'txt7'.
        txt8 = findViewById(R.id.txtDogFriendlinessLevel);                                          // Link UI-Element (via it's ID 'txtDogFriendlinessLevel') to the Java variable 'txt8'.
        img1 = findViewById(R.id.imgPhoto);                                                         // Link UI-Element (via it's ID 'imgPhoto') to the Java variable 'img1'.
        btn1 = findViewById(R.id.btnFav);                                                           // Link UI-Element (via it's ID 'btnFav') to the Java variable 'btn1'.

        // Set values to show the data of the selected 'breed':
        if (breed.getName() != null) {
            txt1.setText(breed.getName());
        } else {
            txt1.setText(R.string.NoData);
        }
        if (breed.getDescription() != null) {
            txt2.setText(breed.getDescription());
        } else {
            txt2.setText(R.string.NoData);
        }
        if (breed.getMetric() != null) {
            txt3.setText(breed.getMetric());
        } else {
            txt3.setText(R.string.NoData);
        }
        if (breed.getTemperament() != null) {
            txt4.setText(breed.getTemperament());
        } else {
            txt4.setText(R.string.NoData);
        }
        if (breed.getOrigin() != null) {
            txt5.setText(breed.getOrigin());
        } else {
            txt5.setText(R.string.NoData);
        }
        if (breed.getLife_span() != null) {
            txt6.setText(breed.getLife_span());
        } else {
            txt6.setText(R.string.NoData);
        }
        if (breed.getWikipedia_url() != null) {
            txt7.setText(breed.getWikipedia_url());
        } else {
            txt7.setText(R.string.NoData);
        }
        if (breed.getDog_friendly() != null) {
            txt8.setText(breed.getDog_friendly());
        } else {
            txt8.setText(R.string.NoData);
        }
        if (breed.getUrl() != null) {                                                               // If pictures exist:
            Glide.with(this).load(breed.getUrl()).into(img1);                               // Use Glide library, to load image from URL into ImageView 'img1'.
        }

        // Favourite Icon (Current Value)
        if(breedDAO.getFavouriteValueByID(BreedID) == 1) {
            btn1.setBackgroundResource(R.drawable.ic_star_black_24dp);                              // Load "Is-favourite" icon
        } else {
            btn1.setBackgroundResource(R.drawable.ic_star_border_black_24dp);                       // Load "Is-Not-favourite" icon
        }

        // Favourite Button:
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int isFavourite = breedDAO.getFavouriteValueByID(BreedID);                          // Get current value from database.
                if(isFavourite == 1) {
                    btn1.setBackgroundResource(R.drawable.ic_star_border_black_24dp);               // Switch Button icon
                    breedDAO.setFavouriteValueByID(BreedID,0);                             // Change value in database
                } else {
                    btn1.setBackgroundResource(R.drawable.ic_star_black_24dp);                      // Switch Button icon
                    breedDAO.setFavouriteValueByID(BreedID,1);                             // Change value in database
                }
            }
        });
    }
}