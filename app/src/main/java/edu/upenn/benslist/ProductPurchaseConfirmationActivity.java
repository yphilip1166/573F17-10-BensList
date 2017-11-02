package edu.upenn.benslist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
    private boolean favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_purchase_confirmation_layout);

        this.uploaderID = getIntent().getStringExtra("UploaderID");
        this.productID = getIntent().getStringExtra("ProductID");

        Spinner spinner = (Spinner) findViewById(R.id.userRatingSpinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_rating_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        this.rating = "1"; //default rating
        favorite = false;

        Button addUserButton = (Button) findViewById(R.id.addUserToFavsButton);
        Button doneButton = (Button) findViewById(R.id.doneRatingButton);
        addUserButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserID = fbUser.getUid();
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
                break;

            case (R.id.doneRatingButton) :

                final String name = fbUser.getDisplayName();
                final Context thisContext = this;
                final String currentUserID = fbUser.getUid();
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

                        Product product = snapshot.child("products").child(productID).getValue(Product.class);
                        DatabaseReference ref = mDatabase.child("users").child(currentUserID).child(
                                "productsIveBought").push();
                        ref.setValue(product);



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

    /**
     * Code Snippet for adding the menu bar 3 points to select Logout, About, Home, Terms
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tools, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_about:
                //Go to About page
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_home:
                //Go to Home page
                intent = new Intent(this, HomePageActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_logout:
                //Logs out the current user and brings user to the logout page
                //Need to add code for actually logging out a user
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_terms:
                //Go to terms page
                intent = new Intent(this, TermsActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_forum:
                //Go to forum page
                intent = new Intent(this, PublicForumActivity.class);
                startActivity(intent);
                return true;

            default:
                //Could not recognize a button press
                Toast.makeText(this, "Could not recognize a button press", Toast.LENGTH_SHORT).show();
                return false;
        }
    }

}
