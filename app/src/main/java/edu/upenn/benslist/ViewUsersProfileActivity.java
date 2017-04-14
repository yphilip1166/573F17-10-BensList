package edu.upenn.benslist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by johnquinn on 4/5/17.
 */


public class ViewUsersProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private User user;
    private String userId;
    private String name;
    private static final int RESULT_VIEW_UPLOADED_PRODUCTS = 10;
    private static final int RESULT_VIEW_FAVORITE_USERS = 11;
    private static final int RESULT_VIEW_PREVIOUS_PURCHASES = 12;
    TextView usersNameText;
    TextView usersAgeText;
    TextView usersRatingText;

    DatabaseReference mUserReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_users_profile_layout);
        this.userId = getIntent().getStringExtra("UserId");
        name = "";

        Button viewProductsTheyveBoughtButton = (Button) findViewById(R.id.viewUploadedProductsButton);
        Button viewFavoriteUsersButton = (Button) findViewById(R.id.viewFavoriteUsersButton);
        Button previousPurchasesButton = (Button) findViewById(R.id.viewPreviousPurchasesButton);
        Button reportUserButton = (Button) findViewById(R.id.reportUserButton);

        Button messageUserButton = (Button) findViewById(R.id.messageUserButton);

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        ValueEventListener productListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                name = dataSnapshot.child("name").getValue(String.class);

                usersNameText.setText("User's Name: " + name);
                usersAgeText.setText("User's Age: " + dataSnapshot.child("age").getValue(String.class));
                usersRatingText.setText("User's Rating: " + dataSnapshot.child("rating").getValue(String.class));
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
                break;

            case (R.id.viewPreviousPurchasesButton) :
                Intent i = new Intent(this, ViewUploadedProductsActivity.class);
                i.putExtra("UserId", userId);

                i.putExtra("Type", "previousPurchases");
                startActivityForResult(i, RESULT_VIEW_PREVIOUS_PURCHASES);
                break;

            case (R.id.viewFavoriteUsersButton) :

                Intent intent = new Intent(this, favoriteUsersActivity.class);
                intent.putExtra("UserId", userId);
                startActivityForResult(intent, RESULT_VIEW_FAVORITE_USERS);
                break;

            case (R.id.reportUserButton) :
                //all reporting does is decrease their rating
                user.addRating(-10);
                break;

            case (R.id.messageUserButton) :
                Intent mIntent = new Intent(this, InboxMessageActivity.class);
                /*
                Todo get the user email and pass it into the InboxMessageActivity. Fix line below
                 */
                mIntent.putExtra("UserId", userId);
                mIntent.putExtra("Name", name);
                startActivity(mIntent);
                break;

            default :
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}
