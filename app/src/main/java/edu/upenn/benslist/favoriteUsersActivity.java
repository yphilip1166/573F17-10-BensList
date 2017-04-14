package edu.upenn.benslist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


/**
 * Created by johnquinn on 3/14/17.
 */

/*
This page simply displays a list of a user's favorite users that they've bought from.
 */

public class favoriteUsersActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_users);

        User user = (User) getIntent().getSerializableExtra("User");

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, user.getFavoriteUsersIveBoughtFrom());

        ListView listView = (ListView) findViewById(R.id.favoriteUsersList);
        listView.setAdapter(itemsAdapter);

        Button doneButton = (Button) findViewById(R.id.doneViewingFavoriteUsersButton);
        doneButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.viewUploadedProductsButton):
                Intent returnIntent = new Intent(this, ViewUsersProfileActivity.class);
                setResult(RESULT_OK, returnIntent);
                finish();
                break;

            default:
                break;
        }
    }



}
