package com.example.fragments;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.catsearch.Breed;
import com.example.catsearch.R;
import com.example.catsearch.SearchResponse;
import com.example.catsearch.URLResponse;
import com.example.catsearch.adapter.BreedAdapter;
import com.example.catsearch.data.AppDatabase;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;

/*
 *  SearchFragment.java
 *  This class defines the functionality of the 'searchFragment' layout.
 */

public class SearchFragment extends Fragment {
    // Define Java Variables:
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String BreedSearch_API_URL = "https://api.thecatapi.com/v1/breeds/search?q=";           // API request URL 1/2 (For User search input).
    private String BreedID_API_URL = "https://api.thecatapi.com/v1/images/search?breed_ids=";       // API request URL 2/2 (For Image data of a certain breed).
    private String[] allURLs = new String[0];
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);         // Connect to the layout file 'fragment_search.xml'.
        final AppDatabase db = AppDatabase.getInstance(getContext());                               // Database Object

        // SearchView:
        SearchView srchMain = v.findViewById(R.id.srchMain);
        srchMain.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String SearchValue) {
                        if (!isOnline()) {
                            Toast t = Toast.makeText(getContext(), "No internet connection...", Toast.LENGTH_SHORT);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                        } else {
                        // Volley API Request 1/2 (SearchResponse):
                        RequestQueue queue = Volley.newRequestQueue(getContext());                                          // Instantiate the RequestQueue.
                        StringRequest SR1 = new StringRequest(Request.Method.GET, BreedSearch_API_URL + SearchValue,   // Request a string response from the provided URL.
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(final String response1) {
                                        if (response1.equals("[]")) {                                                       // If the API return nothing (an empty array)
                                            Toast t = Toast.makeText(getContext(), "No results found...", Toast.LENGTH_SHORT);
                                            t.setGravity(Gravity.CENTER, 0, 0);
                                            t.show();
                                        } else {
                                            // Load all the IDs:
                                            final SearchResponse[] searchResponse = new Gson().fromJson(response1, SearchResponse[].class);     // Workaround, as the API gives you an Array of Breed Objects and not an Object first. https://stackoverflow.com/questions/9598707/gson-throwing-expected-begin-object-but-was-begin-array

                                            // Load all the Image URLs (via IDs):
                                            RequestQueue queue2 = Volley.newRequestQueue(getContext());                                         // Instantiate the RequestQueue.
                                            for(int x=0; x<searchResponse.length; x++) {                                                        // For each breed received by searching, load the image.
                                                // Volley API Request 2/2 (URLResponse):
                                                StringRequest SR2 = new StringRequest(Request.Method.GET, BreedID_API_URL + searchResponse[x].getId(),   // Request a string response from the provided URL.
                                                        new Response.Listener<String>() {
                                                            public void onResponse(String response2) {
                                                                if (response2.equals("[]")) {
                                                                }
                                                                else {
                                                                    // Load all the URLs:
                                                                    allURLs = Arrays.copyOf(allURLs, allURLs.length + 1);                                 // Add one more slot
                                                                    allURLs[allURLs.length - 1] = new Gson().fromJson(response2, URLResponse[].class)[0].getUrl();   // Add the next URL in this last slot

                                                                    if (allURLs.length == searchResponse.length) {                                                   // When all URLs have been loaded:
                                                                        // Edit all the SearchResponses 'response1' (add all URLs into JSON response):
                                                                        int BreedCounter = 0, KlammerCounter = 0;
                                                                        String editedResponse = response1;
                                                                        for (int z = 0; z < editedResponse.length(); z++) {
                                                                            if (editedResponse.charAt(z) == '}') {
                                                                                KlammerCounter++;
                                                                                if (KlammerCounter != 0 & KlammerCounter%2 == 0) {                                   // If KlammerCounter is an even number (Every second closing '}')
                                                                                    editedResponse = editedResponse.substring(0, z) + ",\"url\":\"" + allURLs[BreedCounter] + "\"" + editedResponse.substring(z, editedResponse.length());
                                                                                    z = z + 9 + allURLs[BreedCounter].length();                                      // Resize z so that the next url is also added into the right spot
                                                                                    BreedCounter++;
                                                                                }
                                                                            }
                                                                        }

                                                                        Breed[] breedsFromJSON = new Gson().fromJson(editedResponse, Breed[].class);// Workaround, as the API gives you an Array of Breed Objects and not an Object first. https://stackoverflow.com/questions/9598707/gson-throwing-expected-begin-object-but-was-begin-array
                                                                        // Reorder the weight information:
                                                                        for (int y = 0; y < breedsFromJSON.length; y++) {
                                                                            if (breedsFromJSON[y].getWeight() != null) {
                                                                                breedsFromJSON[y].setMetric(breedsFromJSON[y].getWeight().getMetric());
                                                                            } else {
                                                                                breedsFromJSON[y].setMetric(getResources().getString(R.string.NoData));
                                                                            }
                                                                        }
                                                                        db.breedDao().insert(breedsFromJSON);                             // Save edited JSON response into database:, so I can access it later.

                                                                        // Recycler View:
                                                                        BreedAdapter breedAdapter = new BreedAdapter();                   // 3.1) Create new Object of the class 'BreedAdapter' called 'breedAdapter'
                                                                        breedAdapter.setData(breedsFromJSON);                             // 3.2) Call the method "setData()" in the class 'BreedAdapter' and pass this method all 'book' objects from the API request.
                                                                        recyclerView.setAdapter(breedAdapter);                            // 3.3) Assign the Adapter object 'breedAdapter' to the RecyclerView it should manage.
                                                                    }
                                                                }
                                                            }
                                                        }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        System.out.println("That didn't work!");
                                                    }
                                                });
                                                queue2.add(SR2);                                    // Add the request to the RequestQueue and execute the request.
                                            }
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println("That didn't work!");
                            }
                        });
                        queue.add(SR1);                                                             // Add the request to the RequestQueue and execute the request.
                            }
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                }
        );

        // Catch event on [x] button inside search view
        int searchCloseButtonId = srchMain.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeButton = srchMain.findViewById(searchCloseButtonId);
        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {                                                           // Reset the fragment
                FragmentManager FM = getFragmentManager();
                FragmentTransaction fmtTransaction = FM.beginTransaction();
                fmtTransaction.replace(R.id.fragmentContainer, new SearchFragment());
                fmtTransaction.commit();
            }
        });

        // Recycler View:
        recyclerView = v.findViewById(R.id.rv_srch);                                                // 1.0) Link UI-Element (via it's ID 'rv_srch') to the Java variable 'recyclerView'.
        layoutManager = new LinearLayoutManager(getContext());                                      // 2.1) Create new Layout Manager
        recyclerView.setLayoutManager(layoutManager);                                               // 2.2) Assign Layout Manager to the RecyclerView it should manage.
        return v;
    }

    // Method to check if the phone has working internet connection.
    private boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }
}