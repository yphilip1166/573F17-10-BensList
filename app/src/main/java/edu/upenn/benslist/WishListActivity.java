package edu.upenn.benslist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Carlton on 11/7/17.
 */

public class WishListActivity extends MyAppCompatActivity{
    private ViewGroup mLinearLayout;
    private String userId;
    private DatabaseReference mUserReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_listing_layout);
        this.userId = getIntent().getStringExtra("UserId");
        mUserReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userId);
        mLinearLayout = (ViewGroup) findViewById(R.id.productListingLinearLayout);
    }

    @Override
    public void onStart() {
        super.onStart();
        mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Product> products = new LinkedList<>();
                for (DataSnapshot productSnapshot : dataSnapshot.child("productsInWishList").getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    if (product!=null) products.add(product);
                }
                addProductsFromWishList(products);
                Log.v("in wish list", products.size()+"");
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(WishListActivity.this, "Failed to load wish list.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        });
    }

    protected void addProductsFromWishList(List<Product> products) {
        mLinearLayout.removeAllViewsInLayout();
        final Context thisContext = this;

        //add each product to the activity
        for (final Product product : products) {

            View view = LayoutInflater.from(this).inflate(R.layout.product_listing_layout, mLinearLayout, false);

            TextView productName = (TextView) view.findViewById(R.id.productListingProductName);
            productName.setText("Name: " + product.getName());

            TextView productDescription = (TextView) view.findViewById(R.id.productListingProductDescription);
            productDescription.setText("Description: " + product.getDescription());

            TextView productPrice = (TextView) view.findViewById(R.id.productListingProductPrice);
            productPrice.setText("Price: " + product.getPrice());

            TextView productLocation = (TextView) view.findViewById(R.id.productListingProductLocation);
            productLocation.setText("Location: " + product.getLocation());

            TextView uploaderPhoneNumber = (TextView) view.findViewById(R.id.productListingUploaderPhoneNumber);
            uploaderPhoneNumber.setText("Phone Number: " + product.getPhoneNumber());

            TextView uploaderName = (TextView) view.findViewById(R.id.productListingUploaderName);
            uploaderName.setText("Uploader Name: " + product.getUploaderName());

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
}
