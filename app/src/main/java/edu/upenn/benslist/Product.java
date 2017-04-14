package edu.upenn.benslist;

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

public class Product implements Serializable, Comparable {

    public String name, description, price, location, phoneNumber, category;
    public String uploaderID;
    public String uploaderName;
    public List<String> reviews;
    public String productID;
    public static int numProducts = 0;

    public Product() {
        this.name = "";
        this.description = "";
        this.price = "";
        this.location = "";
        this.phoneNumber = "";
        this.category = "";
        this.uploaderID = "";
        this.uploaderName = "";
        this.reviews = new LinkedList<>();
        numProducts++;
        this.productID = numProducts + "";
    }

    public Product(String name, String description, String price, String location,
                   String phoneNumber, String category, String uploaderID, String uploaderName, String productId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.category = category;
        this.uploaderID = uploaderID;
        this.uploaderName = uploaderName;
        this.reviews = new LinkedList<>();
        numProducts++;
        this.productID = productId;
    }

    //this functino works fine
    public static Product writeNewProductToDatabase(String name, String description,
                                                    String price, String location, String phoneNumber,
                                                    String category, String currentUserName, String productId) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserID = fbUser.getUid();
        Product newProduct = new Product(name, description, price, location, phoneNumber,
                category, currentUserID, currentUserName, productId);
        //mDatabase.child("products").child(newProduct.getProductID()).setValue(newProduct);
        return newProduct;
    }


    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    //haven't tested it out yet, but this function should work fine
    public void addReview(String review) {
        reviews.add(review);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("products").child(productID).child("reviews").setValue(reviews);
    }

    public List<String> getReviews() {
        return reviews;
    }

    public void setReviews(List<String> reviews) {
        this.reviews = reviews;
    }

    public String getUploaderID() {
        return uploaderID;
    }

    public void setUploaderID(String uploaderID) {
        this.uploaderID = uploaderID;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    //this function does NOT work yet
    public static List<Product> getProductsFromDatabaseSearch(final String searchCategory, String searchQuery) {
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

    //this function does NOT work yet
    protected static Product getProductFromDatabase(String productID) {
        final String uID = productID;
        final Set<Product> products = new HashSet<Product>();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("products");
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot product: snapshot.getChildren()) {
                    if (product.getKey().equals(uID)){
                        products.add(product.getValue((Product.class)));
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (products.size() == 1) {
            for (Product wantedProduct : products) {
                return wantedProduct;
            }
        }
        return null;
    }

    @Override
    public int compareTo(Object o) {
        if (o == null) {
            return 1;
        }

        Product that = (Product) o;
        int thisPrice = 0;
        int thatPrice = 0;

        try {
            thisPrice = Integer.parseInt(this.getPrice());
            thatPrice = Integer.parseInt(that.getPrice());
            if (thisPrice > thatPrice) {
                return 1;
            }
            else if (thisPrice < thatPrice) {
                return -1;
            }
            else {
                return 0;
            }
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public String toString() {
        String ans = name + "\n" + description;
        return ans;
    }

}
