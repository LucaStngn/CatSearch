package com.example.catsearch;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/*
 *  Breed.java
 *  This class defines how one 'Breed Object' looks like.
 *  It needs to be defined exactly the way the data is expected to be passed from the Cat API.
 */

@Entity(tableName = "Breeds")
public class Breed {

    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String temperament;
    private String origin;
    private String description;
    private String life_span;
    private String dog_friendly;
    private String wikipedia_url;
    private int favourite;
    private String url;                                                                             // Does actually not exist in the API response, but I add it in there, using String Slicing.
    private String metric;
    @Ignore
    private Weight weight;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemperament() {
        return temperament;
    }

    public void setTemperament(String temperament) {
        this.temperament = temperament;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLife_span() {
        return life_span;
    }

    public void setLife_span(String life_span) {
        this.life_span = life_span;
    }

    public String getDog_friendly() {
        return dog_friendly;
    }

    public void setDog_friendly(String dog_friendly) {
        this.dog_friendly = dog_friendly;
    }

    public String getWikipedia_url() {
        return wikipedia_url;
    }

    public void setWikipedia_url(String wikipedia_url) {
        this.wikipedia_url = wikipedia_url;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public Weight getWeight() {
        return weight;
    }

    public void setWeight(Weight weight) {
        this.weight = weight;
    }
}