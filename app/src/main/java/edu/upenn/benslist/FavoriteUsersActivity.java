package edu.upenn.benslist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by johnquinn on 3/14/17.
 */

/*
This page simply displays a list of a user's favorite users that they've bought from.
 */

public class FavoriteUsersActivity extends MyAppCompatActivity implements View.OnClickListener {

    private String userId;
    ListView listView;
    DatabaseReference mUserReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_users);

        //User user = (User) getIntent().getSerializableExtra("User");
        this.userId = getIntent().getStringExtra("UserId");
        mUserReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userId);



        listView = (ListView) findViewById(R.id.favoriteUsersList);


        Button doneButton = (Button) findViewById(R.id.doneViewingFavoriteUsersButton);
        doneButton.setOnClickListener(this);
    }

    protected void onStart() {
        super.onStart();

        final Context thisContext = this;

        mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> favoriteUsers = new ArrayList<String>();
                for (DataSnapshot favoriteUser : dataSnapshot.child(
                        "favoriteUsersIveBoughtFrom").getChildren()) {
                    favoriteUsers.add(favoriteUser.getValue(String.class));
                }
                ArrayAdapter<String> itemsAdapter =
                        new ArrayAdapter<String>(thisContext, android.R.layout.simple_list_item_1,
                                favoriteUsers);
                listView.setAdapter(itemsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.doneViewingFavoriteUsersButton):
                Intent returnIntent = new Intent(this, ViewUsersProfileActivity.class);
                setResult(RESULT_OK, returnIntent);
                finish();
                break;

            default:
                break;
        }
    }

}
