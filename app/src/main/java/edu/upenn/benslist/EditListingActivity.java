package edu.upenn.benslist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tylerdouglas on 4/19/17.
 */

public class EditListingActivity extends MyAppCompatActivity implements View.OnClickListener{

    private User user;
    private String userId;
    private String type;
    private ViewGroup mLinearLayout;
    private DatabaseReference mUserReference;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_listing_layout);
        this.userId = getIntent().getStringExtra("UserId");
        this.type = "uploads";
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
                    for (DataSnapshot productSnapshot : dataSnapshot.child(
                            "productsIveUploaded").getChildren()) {
                        Product product = productSnapshot.getValue(Product.class);
                        productsIveUploaded.add(product);

                    }
                    addProductsToView(productsIveUploaded, name);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void addProductsToView(List<Product> products, String name) {
        //add each product to the activity
        final Context thisContext = this;

        for (final Product product : products) {

            View view = LayoutInflater.from(this).inflate(R.layout.edit_products_listing_layout, mLinearLayout, false);

            TextView productName = (TextView) view.findViewById(R.id.productListingProductName);
            System.out.println("product name is: " + product.getName());
            productName.setText("Name: " + product.getName());

            TextView productDescription = (TextView) view.findViewById(R.id.productListingProductDescription);
            System.out.println("product description is: " + product.getDescription());
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

            TextView productQuantity = (TextView) view.findViewById(R.id.productListingProductQuantity);
            productQuantity.setText("Number of Items Left: " + product.getQuantity());

            Button removeButton = (Button) view.findViewById(R.id.removeItem);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent removeProduct = new Intent(thisContext, RemoveProductActivity.class);
                    removeProduct.putExtra("Username", userId);
                    removeProduct.putExtra("Product", (Serializable) product);
                    startActivityForResult(removeProduct, 16);
                }
            });

            Button checkOutButton = (Button) view.findViewById(R.id.editProductListing);
            checkOutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent editProduct = new Intent(thisContext, EditIndividualProductActivity.class);
                    editProduct.putExtra("Username", userId);
                    editProduct.putExtra("Product", (Serializable) product);
                    startActivityForResult(editProduct, 15);
                }
            });


            mLinearLayout.addView(view);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode == 15 || requestCode == 16)) {
            Intent refresh = new Intent(this, EditListingActivity.class);
            FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
            String currentUserID = fbUser.getUid();
            refresh.putExtra("UserId", currentUserID);
            android.os.SystemClock.sleep(300);
            startActivity(refresh);
            this.finish();
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
