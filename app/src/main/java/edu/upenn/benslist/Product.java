package edu.upenn.benslist;

import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by johnquinn on 3/13/17.
 */

public class Product implements Serializable {

    private ImageView image;
    private String name, description, price, location, phoneNumber, category;
    private User uploader;
    private Set<String> reviews;
    private static int numProducts = 0;

    public Product() {
        this.image = null;
        this.name = "";
        this.description = "";
        this.price = "";
        this.location = "";
        this.phoneNumber = "";
        this.category = "";
        this.uploader = null;
        this.reviews = new HashSet<>();
        this.numProducts++;
    }

    public Product(ImageView image, String name, String description, String price, String location,
                   String phoneNumber, String category, User uploader) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.price = price;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.category = category;
        this.uploader = uploader;
        this.reviews = new HashSet<>();
        this.numProducts++;
    }

    protected static void writeNewProductToDatabase(ImageView image, String name, String description,
                                                    String price, String location, String phoneNumber,
                                                    String category) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserID = fbUser.getUid();
        String productID = numProducts + "";
        User currentUser = User.getUserFromDatabase(currentUserID);
        Product newProduct = new Product(image, name, description, price, location, phoneNumber,
                category, currentUser);
        mDatabase.child("products").child(productID).setValue(newProduct);
    }

    protected void setUploader(User uploader) {
        this.uploader = uploader;
    }

    protected ImageView getImageView() {
        return image;
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

    protected String getCategory() {
        return category;
    }

    protected static List<Product> getProductsFromDatabase(final String searchCategory, String searchQuery) {
        final List<Product> products = new LinkedList<Product>();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("products");
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot product: snapshot.getChildren()) {
                    //RIGHT NOW, THIS ONLY SHOWS PRODUCTS IN THAT CATEGORY - doesn't handle search query
                    String currentProductCategory = (String) product.child("category").getValue();
                    if (currentProductCategory.equals(searchCategory)) {
                        products.add(product.getValue(Product.class));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return products;
    }

}
