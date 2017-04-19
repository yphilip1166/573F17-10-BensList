package edu.upenn.benslist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by johnquinn on 2/23/17.
 */

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_UPLOAD_PRODUCT = 2;
    private String currentUserName;
    private String currentUserID;
    DatabaseReference mUserReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_layout);

        Button uploadProductButton = (Button) findViewById(R.id.beginUploadingProductButton);
        Button searchProductsButton = (Button) findViewById(R.id.beginSearchingProductsButton);
        Button userProfilePage = (Button) findViewById(R.id.profilePage);
        Button searchUsers = (Button) findViewById(R.id.searchUsers);

        uploadProductButton.setOnClickListener(this);
        searchProductsButton.setOnClickListener(this);

        userProfilePage.setOnClickListener(this);
        searchUsers.setOnClickListener(this);

        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserID = fbUser.getUid();
        mUserReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(currentUserID);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final Context thisContext = this;

        mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUserName = dataSnapshot.child("name").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.beginUploadingProductButton) :
                Intent i = new Intent(this, UploadProductActivity.class);
                i.putExtra("Logged In User Name", currentUserName);
                startActivityForResult(i, RESULT_UPLOAD_PRODUCT);
                break;

            case (R.id.beginSearchingProductsButton) :
                Intent intent = new Intent(this, SearchPageActivity.class);
                startActivity(intent);
                break;
            case (R.id.profilePage) :
                Intent profileIntent = new Intent(this, UserProfile.class);
                startActivity(profileIntent);
                break;
            case (R.id.searchUsers) :
                Intent searchIntent = new Intent(this, SearchUsers.class);
                startActivity(searchIntent);
                break;
            case (R.id.mapButton) :
                System.out.println("clicked map button");
                Intent mapIntent = new Intent(this, MapsActivity.class);
                startActivity(mapIntent);
            default :
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
