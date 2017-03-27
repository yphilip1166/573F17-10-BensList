package edu.upenn.benslist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by johnquinn on 3/13/17.
 */

public class User implements Serializable {
    private String name;
    private int age;
    private Set<User> favoriteUsersIveBoughtFrom;
    private double rating;
    private int numRatings;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
        this.favoriteUsersIveBoughtFrom = new HashSet<>();
        this.rating = 0.0;
        this.numRatings = 0;
    }

    public User() {

    }

    protected String getName() {
        return name;
    }

    protected int getAge() {
        return age;
    }

    protected List<String> getFavoriteUsersNames() {
        List<String> favorites = new ArrayList<String>();
        for (User user : favoriteUsersIveBoughtFrom) {
            favorites.add(user.getName());
        }
        return favorites;
    }

    protected void addRating(int r) {
        rating *= numRatings;
        numRatings++;
        rating += r;
        rating /= numRatings;
    }

    @Override
    public String toString() {
        return "User Name: " + name;
    }


}
