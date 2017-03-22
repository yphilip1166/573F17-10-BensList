package edu.upenn.benslist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by johnquinn on 3/14/17.
 */

/*
As of now, this activity is supposed to act as someone's profile page. It is NOT where a user can
edit his profile.
 */

    //TODO - TYLER - this file is for you to edit how you wish (including user_profile.xml)
public class UserProfileActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile); //TYLER - design user_profile.xml to your liking
        this.user = (User) getIntent().getSerializableExtra("User"); //when called from CheckoutProductActivity

        //this next list displays the perosn's favorite users that they've bought from
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, user.getFavoriteUsersNames());

        ListView listView = (ListView) findViewById(R.id.favoriteUsersList);
        listView.setAdapter(itemsAdapter);

        //FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        /*

        Clicking on one of the user's of a person's favorite users doesn't do anything right now,
        but it can be implemented by implementing this setOnItemClickListener function

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                list.remove(item);
                                adapter.notifyDataSetChanged();
                                view.setAlpha(1);
                            }
                        });
            }

        });
        */
    }



}
