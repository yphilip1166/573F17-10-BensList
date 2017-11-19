package edu.upenn.benslist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Shawn_J on 11/17/17.
 */

public class BlockedUsersActivity extends MyAppCompatActivity implements View.OnClickListener{

    private String currentUserName;
    private String userId;
    private DatabaseReference mUserReference;
    private ViewGroup mLinearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_listing_layout);
        this.currentUserName = getIntent().getStringExtra("Username");
        this.userId = getIntent().getStringExtra("UserId");
        mUserReference = FirebaseDatabase.getInstance().getReference()
                .child("users");

        Button doneButton = (Button) findViewById(R.id.doneViewingProductsButton);
        doneButton.setOnClickListener(this);

        mLinearLayout = (ViewGroup) findViewById(R.id.uploadedProductsLinearLayout);

    }

    protected void onStart() {
        super.onStart();
        mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ArrayList<String>> blockedUsers = new LinkedList<>();
                List<String> key = new LinkedList<String>();
                HashMap<ArrayList<String>, String> keyMap = new HashMap<ArrayList<String>, String>();
                for (DataSnapshot userSnapshot : dataSnapshot.child(userId).child(
                        "blockedUsers").getChildren()) {
                    String blockedUserId = userSnapshot.getValue(String.class);
                    key.add(blockedUserId);
                }

                for(String id : key){
                    DataSnapshot userSnapshot = dataSnapshot.child(id);
                    String rating = userSnapshot.child("rating").toString();
                    rating = rating.substring(37, rating.length()-1);
                    String name = userSnapshot.child("name").toString();
                    name = name.substring(35,name.length()-1);
                    ArrayList<String> userInfo = new ArrayList<String>();
                    userInfo.add(name);
                    userInfo.add(rating);
                    blockedUsers.add(userInfo);
                    keyMap.put(userInfo,id);

                }
                addBlockedUsersToView(blockedUsers, keyMap);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addBlockedUsersToView(List<ArrayList<String>> blockedUsers, final HashMap<ArrayList<String>, String> keyMap){
        final Context thisContext = this;

        for(final ArrayList<String> blockedUserInfo : blockedUsers){
            View view = LayoutInflater.from(this).inflate(R.layout.blocked_users_layout, mLinearLayout, false);

            TextView productName = (TextView) view.findViewById(R.id.blockedUserName);
            productName.setText("Name: " + blockedUserInfo.get(0));

            TextView rating = (TextView) view.findViewById(R.id.blockedUserRating);
            rating.setText("Rating: " + blockedUserInfo.get(1));


            Button unblockUserButton = (Button) view.findViewById(R.id.unblockUser);
            unblockUserButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent unblockUser = new Intent(thisContext, UnblockUserActivity.class);
                    unblockUser.putExtra("Username", userId);
                    unblockUser.putExtra("blockedUser", keyMap.get(blockedUserInfo));
                    startActivityForResult(unblockUser, 17);
                }
            });
            mLinearLayout.addView(view);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 17) {
            Intent refresh = new Intent(this, BlockedUsersActivity.class);
            FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
            String currentUserID = fbUser.getUid();
            refresh.putExtra("UserId", currentUserID);
            android.os.SystemClock.sleep(300);
            startActivity(refresh);
            this.finish();
        }

    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case (R.id.doneViewingProductsButton) :
                Intent intent = new Intent(this, UserProfileActivity.class);
                startActivity(intent);
                finish();
                break;
            default :
                break;

        }
    }
}
