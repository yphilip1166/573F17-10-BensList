package edu.upenn.benslist;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by johnquinn on 3/13/17.
 */


public class User implements Serializable {

    private String name;
    private int age;
    private List<String> favoriteUsersIveBoughtFrom;
    private int sumRatings;
    private int numRatings;
    private double rating;
    private List<Product> productsIveUploaded;
    private List<Product> productsIveBought;

    private String email;
    private String address;
    private String interests;

    public User() {
        this.name = "";
        this.age = 0;
        this.sumRatings = 0;
        this.numRatings = 0;
        this.rating = 0.0;

        this.favoriteUsersIveBoughtFrom = new LinkedList<>();
        this.productsIveUploaded = new LinkedList<>();
        this.productsIveBought = new LinkedList<>();
        this.interests = "";
        this.address = "";
    }

    public void setProductsIveBought(List<Product> productsIveBought) {
        this.productsIveBought = productsIveBought;
    }


    public void setProductsIveUploaded(List<Product> productsIveUploaded) {
        this.productsIveUploaded = productsIveUploaded;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getInterests() {
        return interests;
    }
    public void setInterests(String interets) {
        this.interests = interets;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    protected List<String> getFavoriteUsersIveBoughtFrom() {
        if (favoriteUsersIveBoughtFrom == null) {
            favoriteUsersIveBoughtFrom = new LinkedList<String>();
        }
        return favoriteUsersIveBoughtFrom;
    }

    protected double addRating(int rating) {
        sumRatings += rating;
        numRatings++;
        setRating((((double) sumRatings) / numRatings));

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserID = fbUser.getUid();
        return ((double) sumRatings) / numRatings;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getRating() {
        return rating;
    }


    public List<Product> getProductsIveBought() {
        if (productsIveBought == null) {
            productsIveBought = new LinkedList<Product>();
        }
        return productsIveBought;
    }

    public List<Product> getProductsIveUploaded() {
        if (productsIveUploaded == null) {
            productsIveUploaded = new LinkedList<Product>();
        }
        return productsIveUploaded;
    }

    @Override
    public String toString() {
        return "User Name: " + name;
    }


}
