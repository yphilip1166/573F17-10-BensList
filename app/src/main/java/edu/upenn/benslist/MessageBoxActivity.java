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

    private class Friend{
        public String fId;
        public String fName;
        public Friend(String fi, String fn){
            fId = fi;
            fName = fn;
        }
    }

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
        this.userId = getIntent().getStringExtra("UserId");
        Log.v("YHG", "UserId in MessageBox " + this.userId);
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
                    Log.v("YHG","enter messageBox onDataChange");
                    List<Friend> allFriends = new ArrayList<Friend>();
                    for (DataSnapshot productSnapshot : dataSnapshot.child(
                            "friends").getChildren()) {
                        String friends= productSnapshot.getValue(String.class);
                        String[] ss = friends.split(",");
                        if(ss.length != 2) continue;
                        String friendId = ss[0];
                        String friendName = ss[1];

                        Log.v("YHG","looping friendID "+friendId);



                        allFriends.add(new Friend(friendId, friendName));
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

    private void addFriendsToView(List<Friend> friends){

        mLinearLayout.removeAllViewsInLayout();
        final Context thisContext = this;

        for(final Friend friend : friends){
            String friendName = friend.fName;
            final String friendId = friend.fId;
            Log.v("YHG", "friend name: " + friendName);
            View view = LayoutInflater.from(this).inflate(R.layout.friend_box,mLinearLayout, false);

            TextView fName = (TextView) view.findViewById(R.id.friendName);

            fName.setText("Name: " + friendName);

            Button checkOutButton = (Button) view.findViewById(R.id.checkoutFriendButton);

            checkOutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(thisContext, ViewUsersProfileActivity.class);
                    i.putExtra("UserId", friendId);
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
