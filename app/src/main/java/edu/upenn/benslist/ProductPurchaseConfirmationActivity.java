package edu.upenn.benslist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Created by johnquinn on 3/14/17.
 */

public class ProductPurchaseConfirmationActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener, View.OnClickListener {

    private String uploaderID;
    private int rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_purchase_confirmation_layout);

        this.uploaderID = getIntent().getStringExtra("UploaderID");

        Spinner spinner = (Spinner) findViewById(R.id.userRatingSpinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_rating_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        this.rating = 1; //default rating

        Button addUserButton = (Button) findViewById(R.id.addUserToFavsButton);
        Button doneButton = (Button) findViewById(R.id.doneRatingButton);
        addUserButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        rating = (int) parent.getItemAtPosition(position);
    }

    public void onNothingSelected(AdapterView<?> arg0) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.addUserToFavsButton) :
                //TODO - ADD THIS PERSON TO YOUR FAVORITES - where "user" is the person you want to add

                //DONE - check line below
                User.addFavoriteUserToDatabase(uploaderID);
                break;

            case (R.id.doneRatingButton) :
                User user = User.getUserFromDatabase(uploaderID);
                user.addRating(rating);
                Intent i = new Intent(this, HomePageActivity.class);
                startActivity(i);
                break;
            default :
                break;
        }
    }

}
