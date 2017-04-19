package edu.upenn.benslist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by johnquinn on 3/31/17.
 */

public class SearchUsers extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference mUserReference;
    String searchQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_search_page_layout);

        Button searchButton = (Button) findViewById(R.id.searchUserButton);
        searchButton.setOnClickListener(this);

        Button doneButton = (Button) findViewById(R.id.goBackToHomePageButton);
        doneButton.setOnClickListener(this);
        mUserReference = FirebaseDatabase.getInstance().getReference()
                .child("users");
    }

    @Override
    public void onClick(View v) {
        SearchView searchView = (SearchView) findViewById(R.id.editUserSearch);
        searchQuery = searchView.getQuery().toString();
        final Context thisContext = this;
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        final String mUserID = fbUser.getUid();

        switch (v.getId()) {
            case (R.id.searchUserButton) :

                /*
                TODO - actually implement the following line of code (MAX)
                Do the DataSnapshot thing. Iterate over the database, obtain a user object, and check
                to see if the current user's name matches the searchQuery exactly. If it's an exact match,
                then start a new intent for the activity ViewUsersProfileActivity. Make sure that
                before you call startActivity, you do intent.putExtra("User", currentUser) where
                currentUser is the User object that matches the searchQuery.
                If there is no match, then simply call Toast for 5 seconds saying "No match". You don't
                have to start a new intent in this case. The user will simply just stay on this page.
                 */

                ValueEventListener userListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Set<String> mBlockedUsers = new HashSet<String>();
                        for (DataSnapshot userSnapshot : dataSnapshot.child(mUserID).child(
                                "blockedUsers").getChildren()) {
                            mBlockedUsers.add(userSnapshot.getValue(String.class));
                        }
                        boolean isBlocked = false;
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            //Get the blocked users of the current user you are looking at
                            String key = userSnapshot.getKey().toString();

                            //User user = userSnapshot.getValue(User.class);
                            System.out.println("The key is: " + key);
                            System.out.println(userSnapshot.child("name").getValue(String.class));
                            if (userSnapshot.child("name").getValue(String.class).equals(searchQuery)) {
                                if (mBlockedUsers.contains(key)) {
                                    isBlocked = true;
                                    Toast.makeText(SearchUsers.this, "You have blocked this user.",
                                            Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                //Get the blocked users of the current user you are looking at
                                for (DataSnapshot blockedUserSnapshot : userSnapshot.child(
                                        "blockedUsers").getChildren()) {
                                    System.out.println("Current blocked user id: " +
                                            blockedUserSnapshot.getValue(String.class));
                                    if (blockedUserSnapshot.getValue(String.class).equals(mUserID)) {
                                        isBlocked = true;
                                        Toast.makeText(SearchUsers.this, "This user has blocked you.",
                                                Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                }
                                if (!isBlocked) {
                                    Intent i = new Intent(thisContext, ViewUsersProfileActivity.class);
                                    i.putExtra("UserId", userSnapshot.getKey());
                                    startActivity(i);
                                }
                            }
                        }
                        if (!isBlocked) {
                            Toast.makeText(SearchUsers.this, "Cannot find user.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        Toast.makeText(SearchUsers.this, "Failed to find user.",
                                Toast.LENGTH_SHORT).show();
                        // [END_EXCLUDE]
                    }
                };

                mUserReference.addValueEventListener(userListener);


                break;

            case (R.id.goBackToHomePageButton) :
                Intent intent = new Intent(this, HomePageActivity.class);
                startActivity(intent);
                break;
    }


}
}
