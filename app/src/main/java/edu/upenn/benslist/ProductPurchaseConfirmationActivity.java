package edu.upenn.benslist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.util.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by johnquinn on 3/14/17.
 */

public class ProductPurchaseConfirmationActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener, View.OnClickListener {

    private String uploaderID;
    private String productID;
    private User uploader;
    private String rating;
    private DatabaseReference mDatabase;
    FirebaseUser fbUser;
    String currentUserID;
    String name;
    private boolean favorite;

    //YHG 20171107
    private boolean isAuction;
    private double bidPrice;

    //YP 20171108
    private int quantity;
    private int numItemsLeft;
    private boolean confirmed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_purchase_confirmation_layout);

        this.uploaderID = getIntent().getStringExtra("UploaderID");
        this.productID = getIntent().getStringExtra("ProductID");
        this.isAuction = getIntent().getBooleanExtra("isAuction", false);
        this.quantity = getIntent().getIntExtra("Quantity", 0);
        this.numItemsLeft = getIntent().getIntExtra("NumItemsLeft", 0);
        this.bidPrice = getIntent().getDoubleExtra("BidPrice", 0.0);


        Spinner spinner = (Spinner) findViewById(R.id.userRatingSpinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_rating_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        this.rating = "0"; //default rating
        favorite = false;

        Button addUserButton = (Button) findViewById(R.id.addUserToFavsButton);
        Button doneButton = (Button) findViewById(R.id.doneRatingButton);
        Button chatButton = (Button) findViewById(R.id.chatButton);
        chatButton.setText("CHAT TO CONFIRM WHERE & WHEN");
        addUserButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);
        chatButton.setOnClickListener(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserID = fbUser.getUid();
        DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference().child("users");
        FirebaseUser fbuser2 = FirebaseAuth.getInstance().getCurrentUser();
        final String currentUserID2 = fbuser2.getUid();

        mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = dataSnapshot.child(currentUserID2).child("name").getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(name == null)
            Log.v("YHG", "2, name is null");
        else
            Log.v("YHG", name);

        if(isAuction) {
            TextView bidPriceText = (TextView) findViewById(R.id.confirmedBidPrice);
            bidPriceText.setText("Confirmed Bid Price: " + this.bidPrice);
        } else {
            TextView quantityText = (TextView) findViewById(R.id.confirmedQuantity);
            quantityText.setText("Confirmed Quantity: " + this.quantity);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        rating = (String) parent.getItemAtPosition(position);
    }

    public void onNothingSelected(AdapterView<?> arg0) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.addUserToFavsButton) :
                //TODO - ADD THIS PERSON TO YOUR FAVORITES - where "user" is the person you want to add
                favorite = true;
                //DONE - check line below
                Toast.makeText(this, "Added user to favorite", Toast.LENGTH_SHORT).show();
                break;

            case (R.id.chatButton) :
                confirmed = true;
                Intent mIntent = new Intent(this, InboxMessageActivity.class);
                mIntent.putExtra("UserId", currentUserID);
                mIntent.putExtra("Name", name);
                startActivity(mIntent);
                break;

            case (R.id.doneRatingButton) :
                if(!confirmed) {
                    Toast.makeText(this, "Haven't confirmed with the owner yet.", Toast.LENGTH_SHORT).show();
                    break;
                }

                final Context thisContext = this;
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        int numRatings = 0;
                        int sumRatings = 0;
                        if (snapshot.child("users").child(uploaderID).child(
                                "numRatings").getValue() != null) {
                            numRatings = snapshot.child("users").child(uploaderID).child(
                                    "numRatings").getValue(Integer.class);
                            sumRatings = snapshot.child("users").child(uploaderID).child(
                                    "sumRatings").getValue(Integer.class);
                        }
                        numRatings++;
                        sumRatings += (Integer.parseInt(rating));
                        mDatabase.child("users").child(uploaderID).child("rating").setValue(sumRatings / numRatings);
                        mDatabase.child("users").child(uploaderID).child("numRatings").setValue(numRatings);
                        mDatabase.child("users").child(uploaderID).child("sumRatings").setValue(sumRatings);


                        if (favorite) {
                            String uploaderUserName = snapshot.child("users").child(
                                    uploaderID).child("name").getValue(String.class);
                            DatabaseReference ref = mDatabase.child("users").child(currentUserID).child(
                                    "favoriteUsersIveBoughtFrom").child(uploaderUserName);
                            ref.setValue(uploaderUserName);
                        }

                        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference()
                                .child("products").child(productID);
                        if(name == null)
                            Log.v("YHG", "1, name is null");
                        else
                            Log.v("YHG", name + "");
                        if(isAuction)
                        {
                            productRef.child("curAuctionPrice").setValue(bidPrice);
                            productRef.child("curBuyer").setValue(name);
                        }
                        else {
                            int remaining = numItemsLeft-quantity;
                            productRef.child("quantity").setValue(remaining);
                            Product product = snapshot.child("products").child(productID).getValue(Product.class);
                            DatabaseReference ref = mDatabase.child("users").child(currentUserID).child(
                                    "productsIveBought").push();
                            ref.setValue(product);
                        }

                        //double newRating = uploader.addRating(Integer.parseInt(rating));
                        //mDatabase.child("users").child(uploaderID).child("rating").setValue(newRating);
                        //DatabaseReference ref = productSnapshot.getRef();
                        //System.out.println(ref.getKey());

                        //mUserReference.child(currentUserID).child("productsIveUploaded").child(
                                //ref.getKey()).setValue(product.getName());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Intent i = new Intent(thisContext, HomePageActivity.class);
                startActivity(i);


                break;
            default :
                break;
        }
    }



}
