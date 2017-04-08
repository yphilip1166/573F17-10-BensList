package edu.upenn.benslist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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

        Spinner spinner = (Spinner) findViewById(R.id.userRatingSpinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_rating_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        this.rating = "1"; //default rating
        favorite = true;

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
        System.out.println(parent.getItemAtPosition(position));
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
                        User uploader = snapshot.child("users").child(uploaderID).getValue(User.class);

                        if (favorite) {
                            User currentUser = snapshot.child("users").child(
                                    currentUserID).getValue(User.class);
                            String currentUserName = currentUser.getName();
                            DatabaseReference ref = mDatabase.child("users").child(uploaderID).child(
                                    "favorites").push();
                            ref.setValue(currentUserName);
                        }

                        double newRating = uploader.addRating(Integer.parseInt(rating));
                        mDatabase.child("users").child(uploaderID).child("rating").setValue(newRating);
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
