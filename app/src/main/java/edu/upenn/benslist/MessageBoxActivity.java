package edu.upenn.benslist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/*
* created by Hugang Yu on Dec/1/2017
 */

public class MessageBoxActivity extends MyAppCompatActivity implements View.OnClickListener{

    private ViewGroup messageHeadersLayout;
    private DatabaseReference mMessageReference;
    private DatabaseReference mUserReference;
    private ViewGroup mLinearLayout;
    private String userId;
    private class friendItem{
        public String id;
        public String name;
        public friendItem(String id, String name){
            this.id = id;
            this.name = name;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_box);
        userId = getIntent().getStringExtra("UserID");
        mUserReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userId);
        mLinearLayout = (ViewGroup) findViewById(R.id.messageHeadersLinearLayout);
    }

    @Override
    protected void onStart(){
        super.onStart();

        mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    List<String> allFriends = new ArrayList<String>();
                    for (DataSnapshot productSnapshot : dataSnapshot.child(
                            "friends").getChildren()) {
                        String friendId = productSnapshot.getValue(String.class);

                        allFriends.add(friendId);
                        //productsIveUploaded.add(product);
                    }
                    addFriendsToView(allFriends);
                    //Log.v("debug product2 ", productsIveUploaded.size()+"");
                    //addProductsToView(productsIveUploaded, name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addFriendsToView(List<String> friends){
        mLinearLayout.removeAllViewsInLayout();
        final Context thisContext = this;
        View view = LayoutInflater.from(this).inflate(R.layout.friend_box,mLinearLayout, false);
        for(final String friendName : friends){

            TextView fName = (TextView) view.findViewById(R.id.friendName);

            fName.setText("Name: " + friendName);

            Button checkOutButton = (Button) view.findViewById(R.id.checkoutFriendButton);

            checkOutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(thisContext, ViewUsersProfileActivity.class);
                    i.putExtra("UserId", friendName);
                    startActivity(i);
                }
            });

            mLinearLayout.addView(view);
        }

    }

    @Override
    public void onClick(View v) {
        // should refresh messages here

    }
}
