package edu.upenn.benslist;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by johnquinn on 3/13/17.
 */

public class User {

    private String name;
    private int age;
    private List<String> favoriteUsersIveBoughtFrom;
    private int sumRatings;
    private int numRatings;
    private double rating;

    public User() {
        this.name = "";
        this.age = 0;
        this.sumRatings = 0;
        this.numRatings = 0;
        this.rating = 0.0;
        favoriteUsersIveBoughtFrom = new LinkedList<>();
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
        this.favoriteUsersIveBoughtFrom = new LinkedList<>();
        this.sumRatings = 0;
        this.numRatings = 0;
        this.rating = 0.0;
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

    protected List<String> getFavoriteUsersNames() {
        List<String> favorites = new ArrayList<String>();
        for (String userID : favoriteUsersIveBoughtFrom) {
            favorites.add(User.getUserFromDatabase(userID).getName());
        }
        return favorites;
    }

    protected void addFavoriteUserLocally(String userID) {
        favoriteUsersIveBoughtFrom.add(userID);
    }

    protected static void addFavoriteUserToDatabase(String userID) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserID = fbUser.getUid();
        User currentUser = User.getUserFromDatabase(currentUserID);
        currentUser.addFavoriteUserLocally(userID);
        mDatabase.child("users").child(currentUserID).setValue(currentUser);
    }


    protected void addRating(int rating) {
        sumRatings += rating;
        numRatings++;
        setRating((((double) sumRatings) / numRatings));

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserID = fbUser.getUid();
        mDatabase.child("users").child(currentUserID).child("rating").setValue((((double) sumRatings) / numRatings));
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getRating() {
        return rating;
    }

    protected static User getUserFromDatabase(String userName) {
        final String uName = userName;
        final Set<User> users = new HashSet<User>();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        Log.d("debugging", "in get user from database");
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot user: snapshot.getChildren()) {
                    Log.d("debugging", "user's key is " + user.getKey());
                    User thisUser = user.getValue(User.class);
                    if (thisUser.getName().equals(uName)) {
                        users.add(thisUser);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (users.size() == 1) {
            for (User wantedUser : users) {
                return wantedUser;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "User Name: " + name;
    }


}
