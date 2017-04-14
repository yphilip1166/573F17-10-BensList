package edu.upenn.benslist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;


/**
 * Created by johnquinn on 4/5/17.
 */


public class ViewUsersProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private User user;
    private static final int RESULT_VIEW_UPLOADED_PRODUCTS = 10;
    private static final int RESULT_VIEW_FAVORITE_USERS = 11;
    private static final int RESULT_VIEW_PREVIOUS_PURCHASES = 12;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_users_profile_layout);
        this.user = (User) getIntent().getSerializableExtra("User");

        Button viewProductsTheyveBoughtButton = (Button) findViewById(R.id.viewUploadedProductsButton);
        Button viewFavoriteUsersButton = (Button) findViewById(R.id.viewFavoriteUsersButton);
        Button previousPurchasesButton = (Button) findViewById(R.id.viewPreviousPurchasesButton);
        Button reportUserButton = (Button) findViewById(R.id.reportUserButton);

        Button messageUserButton = (Button) findViewById(R.id.messageUserButton);

        TextView usersNameText = (TextView) findViewById(R.id.usersNameTextField);
        TextView usersAgeText = (TextView) findViewById(R.id.usersAgeTextField);
        TextView usersRatingText = (TextView) findViewById(R.id.usersRatingTextField);

        usersNameText.setText("User's Name: " + user.getName());
        usersAgeText.setText("User's Age: " + user.getAge());
        usersRatingText.setText("User's Rating: " + user.getRating());

        viewProductsTheyveBoughtButton.setOnClickListener(this);
        viewFavoriteUsersButton.setOnClickListener(this);
        previousPurchasesButton.setOnClickListener(this);
        reportUserButton.setOnClickListener(this);

        messageUserButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.viewUploadedProductsButton) :
                Intent newIntent = new Intent(this, ViewUploadedProductsActivity.class);
                newIntent.putExtra("User", (Serializable) user);
                newIntent.putExtra("Type", "uploads");
                startActivityForResult(newIntent, RESULT_VIEW_UPLOADED_PRODUCTS);
                break;

            case (R.id.viewPreviousPurchasesButton) :
                Intent i = new Intent(this, ViewUploadedProductsActivity.class);
                i.putExtra("User", (Serializable) user);
                i.putExtra("Type", "previousPurchases");
                startActivityForResult(i, RESULT_VIEW_PREVIOUS_PURCHASES);
                break;

            case (R.id.viewFavoriteUsersButton) :
                Intent intent = new Intent(this, favoriteUsersActivity.class);
                intent.putExtra("User", (Serializable) user);
                startActivityForResult(intent, RESULT_VIEW_FAVORITE_USERS);
                break;

            case (R.id.reportUserButton) :
                //all reporting does is decrease their rating
                /*
                TODO - Josh
                 */
                user.addRating(-10);
                break;

            case (R.id.messageUserButton) :
                Intent mIntent = new Intent(this, InboxMessageActivity.class);
                /*
                Todo get the user email and pass it into the InboxMessageActivity. Fix line below
                 */
                mIntent.putExtra("Email", user.getEmail());
                mIntent.putExtra("Name", user.getName());
                startActivity(mIntent);
                break;

            default :
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}
