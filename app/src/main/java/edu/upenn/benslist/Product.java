package edu.upenn.benslist;

import android.util.Log;

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

    public String name, description, condition, price, location, phoneNumber, category;
    public String uploaderID;
    public String uploaderName;
    public List<String> reviews;
    public String productID;
    public static int numProducts = 0;

    //TODO - new fields as of april 13th at 7:30pm:
    public double priceAsDouble;
    public double distance;
    public int priceCategory; //1, 2, or 3
    public int locationCategory; //1, 2, or 3

    //YHG 20171107
    public boolean isAuction;
    public boolean isAuctionClosed;
    public double curAuctionPrice;

    //YHG 20171119
    public String curBuyer;

    public List<String> wisher;
    public List<String> bider;
    //PY 20171108
    public int quantity;

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
        this.priceAsDouble = 0.0;
        this.distance = 0.0;
        this.priceCategory = -1;
        this.locationCategory = -1;
        this.isAuction = false;
        this.isAuctionClosed = true;
        this.curAuctionPrice = 0.0;
        this.curBuyer = "";
        this.wisher = new LinkedList<>();
        this.bider = new LinkedList<>();
        this.condition = "Not Available";
        this.quantity = 1;

    }


    public Product(String name, String description, String condition, double priceAsDouble, String location,
                   String phoneNumber, String category, String uploaderID, String uploaderName, String productId,
                   double distance, boolean isAuction, int quantity) {
        this.name = name;
        this.description = description;
        this.condition = condition;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.category = category;
        this.uploaderID = uploaderID;
        this.uploaderName = uploaderName;
        this.reviews = new LinkedList<>();
        numProducts++;
        this.productID = productId;
        this.isAuction = isAuction;
        this.isAuctionClosed = false;
        this.curAuctionPrice = priceAsDouble;
        this.curBuyer = "";
        this.wisher = new LinkedList<>();
        this.bider= new LinkedList<>();
        this.quantity = quantity;

        this.priceAsDouble = priceAsDouble;
        this.price = "$" + priceAsDouble;
        int decimalIndex = price.indexOf('.');
        if (price.length() - decimalIndex == 2) {
            price += "0";
        }

        if (priceAsDouble < 0) {
            this.priceCategory = -1;
        }
        if (priceAsDouble <= 99.99) {
            this.priceCategory = 1;
        }
        else if (priceAsDouble <= 199.99) {
            this.priceCategory = 2;
        }
        else {
            this.priceCategory = 3;
        }

        if (distance < 0) {
            this.locationCategory = -1;
        }
        if (distance <= 9.99) {
            this.locationCategory = 1;
        }
        else if (distance <= 19.99) {
            this.locationCategory = 2;
        }
        else {
            this.locationCategory = 3;
        }
        this.distance = distance;
    }


    //this is for generating auction product - YHG
    public static Product writeNewProductToDatabase(String name, String description, String condition,
                                                    double priceAsDouble, String location, String phoneNumber,
                                                    String category, String currentUserName, String productId,
                                                    double distance, boolean isAuction, int quantity) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserID = fbUser.getUid();
        Product newProduct = new Product(name, description,condition, priceAsDouble, location, phoneNumber,
                category, currentUserID, currentUserName, productId, distance, isAuction, quantity);

        return newProduct;
    }


    public void setPriceAsDouble(double priceAsDouble) {
        this.priceAsDouble = priceAsDouble;
    }

    public double getPriceAsDouble() {
        return priceAsDouble;
    }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public void setPriceCategory(int priceCategory) {
        this.priceCategory = priceCategory;
    }

    public int getPriceCategory() {
        return priceCategory;
    }

    public void setLocationCategory(int locationCategory) {
        this.locationCategory = locationCategory;
    }

    public int getLocationCategory() {
        return locationCategory;
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

    //SJ 20171116
    public void setCondition(String condition) {this.condition = condition; }

    public String getCondition() { return condition; }

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

    //YHG 20171107
    public void setIsAuction(boolean isAuction){this.isAuction = isAuction;};
    public boolean getIsAuction(){return this.isAuction;};

    public void setIsAuctionClosed(boolean isAuction){this.isAuctionClosed = isAuction;};
    public boolean getIsAuctionClosed(){return this.isAuctionClosed;};

    public void setCurAuctionPrice(double price){this.curAuctionPrice = price;}
    public double getCurAuctionPrice(){return curAuctionPrice;}

    public void setCurBuyer(String curBuyer){this.curBuyer = curBuyer;}
    public String getCurBuyer(){return this.curBuyer;}

    public String getAuctionString(){
        if(isAuction)return "YES";
        else return "NO";
    }


    // 11.16.2017 Carlton
    public void addWisher(String w){
        wisher.add(w);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("products").child(productID).child("wisher").setValue(wisher);
    }

    public void addBider(String b){
        bider.add(b);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("products").child(productID).child("bider").setValue(bider);
    }

    public void removeWisher(String w){
        wisher.remove(w);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("products").child(productID).child("wisher").setValue(wisher);
    }

    public void removeBider(String b){
        bider.remove(b);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("products").child(productID).child("bider").setValue(bider);
    }

    public List<String> getWisher() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        List<String> temp = new LinkedList<>();
        return wisher;
    }

    public List<String> getBider() {
        return bider;
    }

    public void setWisher(List<String> w) {this.wisher=w;}

    public void setBider(List<String> b) {this.bider= b;}

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
        double thisPrice = priceAsDouble;
        double thatPrice = that.getPriceAsDouble();

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

    @Override
    public String toString() {
        String ans = name + "\n" + description + "\n" + wisher.size();
        return ans;
    }

}
