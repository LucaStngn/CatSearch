package com.example.catsearch.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.activities.BreedDetailActivity;
import com.example.catsearch.Breed;
import com.example.catsearch.R;

/*
 *  BreedAdapter.java
 *  This class handles the data of each 'cat_chunk' loaded.
 *  And also what happens if you click on one of these chunks.
 */

public class BreedAdapter extends RecyclerView.Adapter<BreedAdapter.BreedViewHolder> {
    private Breed[] list;                                                                            // DATA: Create Array of 'Breed Objects', called 'list'.

    // Give data to the adapter:
    public void setData(Breed[] allBreeds) {
        this.list = allBreeds;                                                                      // Assign all the 'breed' objects from the database to the Array of 'breed' objects.
    }

    // Function 1/3: Create new views (The 'chunks'):
    @NonNull @Override
    public BreedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chunk_cat, parent, false);    // Create a new view from the layout file 'chunk_cat'. (One of this view represents one 'chunk_cat')
        return new BreedViewHolder(view);                                                                                // Create an instance of my custom ViewHolder for this 'chunk_cat' view.
    }

    // Function 2/3: Replace the contents of all RecyclerView elements (Gets executed for every RecyclerView element):
    public void onBindViewHolder(@NonNull BreedAdapter.BreedViewHolder holder, final int position) {                     // CustomViewHolder
        final Breed breedAtPosition = list[position];                                                                    // Get the ID of the current view.
        holder.bind(breedAtPosition);
    }

    // Function 3/3: Return the amount of items in the dataset:
    @Override
    public int getItemCount() {
        return list.length;
    }

    public static class BreedViewHolder extends RecyclerView.ViewHolder {                           // Custom ViewHolder (inherited from the default ViewHolder) that represents one item in the RecyclerView. It doesn't have data when it's first constructed. We assign the data in onBindViewHolder.
        private ImageView img1;
        private TextView txt1;
        private View viw1;
        public BreedViewHolder(View v) {                                                            // This constructor is used in onCreateViewHolder.
            super(v);                                                                               // Runs the constructor for the ViewHolder superclass
            viw1 = v;                                                                               // View needed later for the explicit intent.
            img1 = v.findViewById(R.id.imgPhoto);                                                   // Link UI-Element (via it's ID "imgPhoto") to the Java variable "img1".
            txt1 = v.findViewById(R.id.txtTitle);                                                   // Link UI-Element (via it's ID "txtTitle") to the Java variable "txt1".
        }

        private void bind(final Breed breed) {
            txt1.setText(breed.getName());                                                          // Assign 'title' data to the view elements inside the RecyclerView.
            if (breed.getUrl() != null) {                                                           // If pictures exist:
                Glide.with(viw1.getContext()).load(breed.getUrl()).into(img1);                      // Use Glide library, to load image from URL into ImageView 'img1'.
            }

            // Explicit intent to 'BreedDetailActivity.java':
            viw1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context C = view.getContext();                                                  // We need to get the context of the view that has been clicked on, to be able to use it's "startActivity()" method. (Activity Classes have that automatically, as they are subclasses of the "Context Class", but our "BreedAdapter Class" doesn't have that. So we need to "import" the context first.
                    Intent intent = new Intent(C, BreedDetailActivity.class);                       // Define intent (exists only inside the method) and which other activity to go to.
                    intent.putExtra("BreedID", breed.getId());                               // Key: 'BreedID' is the identifier. Value: is the BreedID, of the book that has been clicked on. (Pass Data)
                    C.startActivity(intent);                                                        // Execution.
                }
            });
        }
    }
}