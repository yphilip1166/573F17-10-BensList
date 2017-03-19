package edu.upenn.benslist;

import android.net.Uri;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by johnquinn on 3/13/17.
 */

public class Product implements Serializable {

    private Uri uri;
    private String name, description, price, location, phoneNumber;
    private User uploader;
    private Set<String> reviews;


    public Product(Uri uri, String name, String description, String price, String location,
                   String phoneNumber, User uploader) {
        this.uri = uri;
        this.name = name;
        this.description = description;
        this.price = price;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.uploader = uploader;
        this.reviews = new HashSet<>();
    }

    protected void setUploader(User uploader) {
        this.uploader = uploader;
    }

    protected void addProductToDatabase() {
        //MAX - INSERT YOUR CODE HERE
    }

    protected Uri getUri() {
        return uri;
    }

    protected String getName() {
        return name;
    }

    protected String getDescription() {
        return description;
    }

    protected String getPrice() {
        return price;
    }

    protected String getLocation() {
        return location;
    }

    protected String getPhoneNumber() {
        return phoneNumber;
    }

    protected String getUploaderName() {
        return uploader.getName();
    }

    protected void addReview(String review) {
        reviews.add(review);
    }

    protected Set<String> getReviews() {
        return reviews;
    }

    protected User getUploader() {
        return uploader;
    }


}
