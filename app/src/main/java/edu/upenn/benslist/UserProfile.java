package edu.upenn.benslist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sendbird.android.SendBird;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by tylerdouglas on 3/26/17.
 */

public class UserProfile extends AppCompatActivity {

    private FirebaseUser fbuser;
    private Boolean submitMode;
    private static final int REQUEST_CODE = 5;
    DatabaseReference mDatabase;
    String currentUserID;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_layout);
        submitMode = false;

        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),REQUEST_CODE);
            }

        });


        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        fbuser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserID = fbuser.getUid();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(currentUserID).getValue(User.class);
                setUserValues(user);
                createButtons(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    protected void setUserValues(User user) {
        EditText nameField = (EditText) findViewById(R.id.name);
        nameField.setText(user.getName());

        EditText emailField = (EditText) findViewById(R.id.emailAddress);
        emailField.setText(fbuser.getEmail());

        EditText address = (EditText) findViewById(R.id.address);
        String homeAddress = (user.getHomeAddress().equals("")) ? "Enter Home Address" : user.getHomeAddress() ;
        address.setText(homeAddress);

        EditText interests = (EditText) findViewById(R.id.interests);
        String userInterests = (user.getInterets().equals("")) ? "Enter Interests" : user.getInterets();
        interests.setText(userInterests);

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        int rating = Double.valueOf(user.getRating()/2.0).intValue(); //divide by 2 because rating is out of 5
        ratingBar.setNumStars(rating);

    }

    protected void createButtons(final User user) {

        EditText nameField = (EditText) findViewById(R.id.name);
        nameField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    EditText nameFieldInner = (EditText) findViewById(R.id.name);
                    nameFieldInner.setText(event.getCharacters());
                    handled = true;
                }
                return handled;
            }
        });

        Button viewMyProducts = (Button) findViewById(R.id.viewProductsButton);
        viewMyProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), MyProductsActivity.class);
                i.putExtra("User", user);
                startActivity(i);

            }
        });

        Button previousPurchases = (Button) findViewById(R.id.prevousProducts);
        previousPurchases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), PreviousPurchasedItems.class);
                i.putExtra("User", user);
                startActivity(i);
            }
        });


        Button favoriteUsers = (Button) findViewById(R.id.favoriteUsers);
        favoriteUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), favoriteUsersActivity.class);
                i.putExtra("User", user);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
                imageButton.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Log.d("debugging", "FileNotFound exception for choosing profile picture");
            } catch (IOException e) {
                Log.d("debugging", "IOException exception for choosing profile picture");

            }
        }
    }

    /**
     * Checks if UserProfile is in submitMode.
     * If SubmitMode = true then it changes all EditText Fields to editable
     * If SubmitMode = false then it changes all EditText fields to non-editable
     */
    private void submitMode() {
        EditText nameField = (EditText) findViewById(R.id.name);
        EditText address = (EditText) findViewById(R.id.address);
        EditText interests = (EditText) findViewById(R.id.interests);
        EditText emailAddress = (EditText) findViewById(R.id.emailAddress);

        if (submitMode) {
            nameField.setEnabled(true);
            address.setEnabled(true);
            interests.setEnabled(true);
            emailAddress.setEnabled(true);
        } else {
            nameField.setEnabled(false);
            address.setEnabled(false);
            interests.setEnabled(false);
            emailAddress.setEnabled(false);
        }
    }

    /**
     * Code Snippet for adding the menu bar 3 points to select Logout, About, Home, Terms
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        this.menu = menu;
        inflater.inflate(R.menu.tools, menu);
        inflater.inflate(R.menu.submit, menu);

        return true;
    }

    /**
     * Handle the button presses
     * TODO add code that will log the user out when they click logout
     */
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
                SendBird.disconnect(new SendBird.DisconnectHandler() {
                    @Override
                    public void onDisconnected() {
                        // You are disconnected from SendBird.
                    }
                });
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_terms:
                //Go to terms page
                intent = new Intent(this, TermsActivity.class);
                startActivity(intent);
                return true;

            case R.id.Edit:
                //Enable Editting fields
                MenuItem editButton = menu.findItem(R.id.Edit);
                if (submitMode) {
                    submitMode = false;
                    EditText nameField = (EditText) findViewById(R.id.name);
                    EditText address = (EditText) findViewById(R.id.address);
                    EditText interests = (EditText) findViewById(R.id.interests);
                    EditText emailAddress = (EditText) findViewById(R.id.emailAddress);
                    mDatabase.child(currentUserID).child("email").setValue(String.valueOf(emailAddress.getText()));
                    mDatabase.child(currentUserID).child("name").setValue(String.valueOf(nameField.getText()));
                    mDatabase.child(currentUserID).child("address").setValue(String.valueOf(address.getText()));
                    mDatabase.child(currentUserID).child("interests").setValue(String.valueOf(interests.getText()));

                    editButton.setTitle("Edit");

                } else {
                    submitMode = true;
                    editButton.setTitle("Submit");
                }
                submitMode();
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
