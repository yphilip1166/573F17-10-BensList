package edu.upenn.benslist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by johnquinn on 4/5/17.
 */


/*
This DOESN'T just display the products a user has uploaded. It can also display the products
that a user has bought in the past.
 */

public class ViewUploadedProductsActivity extends MyAppCompatActivity implements View.OnClickListener {

    private User user;
    private String userId;
    private String type;
    private ViewGroup mLinearLayout;
    private DatabaseReference mUserReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_uploaded_products_layout);
        this.userId = getIntent().getStringExtra("UserId");
        this.type = (String) getIntent().getStringExtra("Type");
        mUserReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userId);

        Button doneButton = (Button) findViewById(R.id.doneViewingProductsButton);

        doneButton.setOnClickListener(this);

        mLinearLayout = (ViewGroup) findViewById(R.id.uploadedProductsLinearLayout);


    }

    protected void onStart() {
        super.onStart();

        mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                if (type.equals("uploads")) {
                    List<Product> productsIveUploaded = new LinkedList<>();
                    Log.v("debug product ", dataSnapshot.child(
                            "productsIveUploaded").getChildrenCount()+"");
                    for (DataSnapshot productSnapshot : dataSnapshot.child(
                            "productsIveUploaded").getChildren()) {
                        Product product = productSnapshot.getValue(Product.class);
                        productsIveUploaded.add(product);
                    }
                    Log.v("debug product2 ", productsIveUploaded.size()+"");
                    addProductsToView(productsIveUploaded, name);
                }
                else if (type.equals("previousPurchases")) {
                    List<Product> productsIveBought = new LinkedList<>();
                    for (DataSnapshot productSnapshot : dataSnapshot.child(
                            "productsIveBought").getChildren()) {
                        Product product = productSnapshot.getValue(Product.class);
                        productsIveBought.add(product);
                    }
                    addProductsToView(productsIveBought, name);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void addProductsToView(List<Product> products, String name) {
        //add each product to the activity
        //Also be sure to remove all previous added listing to avoid duplicates
        mLinearLayout.removeAllViewsInLayout();
        final Context thisContext = this;

        for (final Product product : products) {

            View view = LayoutInflater.from(this).inflate(R.layout.product_listing_layout, mLinearLayout, false);

            TextView productName = (TextView) view.findViewById(R.id.productListingProductName);
            productName.setText("Name: " + product.getName());

            TextView productDescription = (TextView) view.findViewById(R.id.productListingProductDescription);
            productDescription.setText("Description: " + product.getDescription());

            TextView productCondition = (TextView) view.findViewById(R.id.productListingProductCondition);
            productCondition.setText("Condition: " + product.getCondition());

            TextView productPrice = (TextView) view.findViewById(R.id.productListingProductPrice);
            productPrice.setText("Price: " + product.getPrice());

            TextView productLocation = (TextView) view.findViewById(R.id.productListingProductLocation);
            productLocation.setText("Location: " + product.getLocation());

            TextView uploaderPhoneNumber = (TextView) view.findViewById(R.id.productListingUploaderPhoneNumber);
            uploaderPhoneNumber.setText("Phone Number: " + product.getPhoneNumber());

            TextView uploaderName = (TextView) view.findViewById(R.id.productListingUploaderName);
            uploaderName.setText("Uploader Name: " + name);

            Button checkOutButton = (Button) view.findViewById(R.id.productListingCheckOutListingButton);
            checkOutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(thisContext, CheckoutProductActivity.class);
                    i.putExtra("Product", (Serializable) product);
                    startActivity(i);
                }
            });

            mLinearLayout.addView(view);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.doneViewingProductsButton) :
                Intent returnIntent = new Intent(this, ViewUsersProfileActivity.class);
                setResult(RESULT_OK, returnIntent);
                finish();
                break;

            default :
                break;

        }
    }


}
