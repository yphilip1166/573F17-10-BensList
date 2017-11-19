package edu.upenn.benslist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


/**
 * Created by johnquinn on 4/5/17.
 */


public class ViewUsersProfileActivity extends MyAppCompatActivity implements View.OnClickListener {

    private User user;
    private List<String> blockedUsers;
    private String userId;
    private String mUserId;
    private String name;
    private static final int RESULT_VIEW_UPLOADED_PRODUCTS = 10;
    private static final int RESULT_VIEW_FAVORITE_USERS = 11;
    private static final int RESULT_VIEW_PREVIOUS_PURCHASES = 12;
    TextView usersNameText;
    TextView usersAgeText;
    TextView usersRatingText;

    DatabaseReference mUserReference;
    DatabaseReference mCurrentReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_users_profile_layout);
        this.userId = getIntent().getStringExtra("UserId");
        name = "";
        blockedUsers = new LinkedList<>();

        Button viewProductsTheyveBoughtButton = (Button) findViewById(R.id.viewUploadedProductsButton);
        Button viewFavoriteUsersButton = (Button) findViewById(R.id.viewFavoriteUsersButton);
        Button previousPurchasesButton = (Button) findViewById(R.id.viewPreviousPurchasesButton);
        Button reportUserButton = (Button) findViewById(R.id.reportUserButton);
        Button messageUserButton = (Button) findViewById(R.id.messageUserButton);
        Button backButton = (Button) findViewById(R.id.backButton);

        usersNameText = (TextView) findViewById(R.id.usersNameTextField);
        usersAgeText = (TextView) findViewById(R.id.usersAgeTextField);
        usersRatingText = (TextView) findViewById(R.id.usersRatingTextField);

        mUserReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userId);

        viewProductsTheyveBoughtButton.setOnClickListener(this);
        viewFavoriteUsersButton.setOnClickListener(this);
        previousPurchasesButton.setOnClickListener(this);
        reportUserButton.setOnClickListener(this);
        messageUserButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ValueEventListener productListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                name = dataSnapshot.child("name").getValue(String.class)==null? "Unspecified User": dataSnapshot.child("name").getValue(String.class);
                usersNameText.setText("User's Name: " + name);
                String age= dataSnapshot.child("age").getValue(String.class)==null? "Undisclosed Age": dataSnapshot.child("age").getValue(String.class);
                usersAgeText.setText("User's Age: " + age);
                String rating= dataSnapshot.child("rating").getValue()==null? "0" : dataSnapshot.child("rating").getValue().toString();
                usersRatingText.setText("User's Rating: " + rating);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(ViewUsersProfileActivity.this, "Failed to load products.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };

        mUserReference.addValueEventListener(productListener);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.viewUploadedProductsButton) :
                Intent newIntent = new Intent(this, ViewUploadedProductsActivity.class);
                newIntent.putExtra("UserId", userId);
                newIntent.putExtra("Type", "uploads");
                startActivityForResult(newIntent, RESULT_VIEW_UPLOADED_PRODUCTS);
                finish();
                break;

            case (R.id.viewPreviousPurchasesButton) :
                Intent i = new Intent(this, ViewUploadedProductsActivity.class);
                i.putExtra("UserId", userId);
                i.putExtra("Type", "previousPurchases");
                startActivityForResult(i, RESULT_VIEW_PREVIOUS_PURCHASES);
                finish();
                break;

            case (R.id.viewFavoriteUsersButton) :
                Intent intent = new Intent(this, FavoriteUsersActivity.class);
                intent.putExtra("UserId", userId);
                startActivityForResult(intent, RESULT_VIEW_FAVORITE_USERS);
                finish();
                break;

            case (R.id.reportUserButton) :

                FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                //Get the current users information
                mUserId = mFirebaseUser.getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().
                        child("users").child(mUserId).child("blockedUsers").push();
                ref.setValue(userId);
                Intent iHome = new Intent(this, SearchUsers.class);
                startActivity(iHome);
                finish();
                break;

            case (R.id.messageUserButton) :
                Intent mIntent = new Intent(this, InboxMessageActivity.class);
                mIntent.putExtra("UserId", userId);
                mIntent.putExtra("Name", name);
                startActivity(mIntent);
                finish();
                break;

            case(R.id.backButton):
                Intent searchIntent = new Intent(this, HomePageActivity.class);
                startActivity(searchIntent);
                finish();
                break;

            default :
                break;
        }
    }

//    protected void blockUser() {
//        // Initialize Firebase User
//        FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//
//        //Get the current users information
//        mUserId = mFirebaseUser.getUid();
//        mCurrentReference = FirebaseDatabase.getInstance().getReference().
//                child("users").child(mUserId);
//        ValueEventListener blockedUserListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot userSnapshot : dataSnapshot.child("blockedUsers").getChildren()) {
//                    String curr = userSnapshot.getValue(String.class);
//                    //Used for testing purposes
//                    System.out.println("CURRENT USER IN THE LINKED LIST: " + curr);
//                    blockedUsers.add(curr);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(ViewUsersProfileActivity.this, "Failed to load user's name.",
//                        Toast.LENGTH_SHORT).show();
//            }
//        };
//
//        mCurrentReference.addValueEventListener(blockedUserListener);
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }



}
