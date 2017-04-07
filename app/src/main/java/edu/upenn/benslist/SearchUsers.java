package edu.upenn.benslist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

/**
 * Created by johnquinn on 3/31/17.
 */

public class SearchUsers extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_search_page_layout);

        Button searchButton = (Button) findViewById(R.id.searchUserButton);
        searchButton.setOnClickListener(this);

        Button doneButton = (Button) findViewById(R.id.goBackToHomePageButton);
        doneButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SearchView searchView = (SearchView) findViewById(R.id.editUserSearch);
        String searchQuery = searchView.getQuery().toString();

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


                break;

            case (R.id.goBackToHomePageButton) :
                Intent intent = new Intent(this, HomePageActivity.class);
                startActivity(intent);
                break;

            default :
                break;
        }
    }


}
