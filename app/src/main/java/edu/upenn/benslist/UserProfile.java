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
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by tylerdouglas on 3/26/17.
 */

public class UserProfile extends AppCompatActivity {

    private User user;
    private FirebaseUser fbuser;
    private static final int REQUEST_CODE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_layout);

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


        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        fbuser = FirebaseAuth.getInstance().getCurrentUser();
        final String currentUserID = fbuser.getUid();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.hasChild(currentUserID));
                user = dataSnapshot.child(currentUserID).getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        setUserValues();
        createButtons();

    }

    protected void setUserValues() {
        EditText nameField = (EditText) findViewById(R.id.name);
        nameField.setText(fbuser.getDisplayName());

        EditText emailField = (EditText) findViewById(R.id.emailAddress);
        emailField.setText(fbuser.getEmail());

    }

    protected void createButtons() {

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
                startActivity(i);

            }
        });

        Button previousPurchases = (Button) findViewById(R.id.prevousProducts);
        previousPurchases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), PreviousPurchasedItems.class);
                startActivity(i);
            }
        });


        Button favoriteUsers = (Button) findViewById(R.id.favoriteUsers);
        favoriteUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), UserProfileActivity.class);
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

}
