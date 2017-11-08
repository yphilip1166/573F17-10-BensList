package edu.upenn.benslist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by johnquinn on 3/14/17.
 */

public class CheckoutProductActivity extends MyAppCompatActivity implements View.OnClickListener {

    private Product product;
    private double bidPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_product);
        this.product = (Product) getIntent().getExtras().getSerializable("Product");

        Button purchaseProductButton = (Button) findViewById(R.id.detailedListingConfirmPurchase);
        purchaseProductButton.setOnClickListener(this);

        Button leaveReviewButton = (Button) findViewById(R.id.submitReviewButton);
        leaveReviewButton.setOnClickListener(this);

        Button addToWishList = (Button) findViewById(R.id.AddToWishList);
        addToWishList.setOnClickListener(this);

        Button checkOutUploadersProfileButton =
                (Button) findViewById(R.id.detailedListingCheckUploadersPage);
        checkOutUploadersProfileButton.setOnClickListener(this);
        checkOutUploadersProfileButton.setText("Check Out " + product.getUploaderName() + "'s Profile");

        Button bidButton = (Button) findViewById(R.id.confirmBidButton);
        bidButton.setOnClickListener(this);

        setProductTextValues();

        addCommentsSection();
    }

    protected void setProductTextValues() {
        TextView productName = (TextView) findViewById(R.id.detailedListingProductName);
        productName.setText("Name: " + product.getName());

        TextView productDescription = (TextView) findViewById(R.id.detailedListingProductDescription);
        productDescription.setText("Description: " + product.getDescription());

        TextView productPrice = (TextView) findViewById(R.id.detailedListingProductPrice);
        productPrice.setText("Price: " + product.getPrice());

        TextView productLocation = (TextView) findViewById(R.id.detailedListingProductLocation);
        productLocation.setText("Location: " + product.getLocation());

        TextView uploaderPhoneNumber = (TextView) findViewById(R.id.detailedListingUploaderPhoneNumber);
        uploaderPhoneNumber.setText("Phone Number: " + product.getPhoneNumber());

        TextView uploaderName = (TextView) findViewById(R.id.detailedListingUploaderName);
        uploaderName.setText("Uploader Name: " + product.getUploaderName());

        TextView curBidPrice = (TextView) findViewById(R.id.detailedListingCurPrice);
        curBidPrice.setText("Cur Bid Price: " + product.curAuctionPrice);
    }

    protected void addCommentsSection() {
        int i = 1;
        LinearLayout rl = (LinearLayout) findViewById(R.id.purchaseProductLL);
        for (String comment : product.getReviews()) {
            TextView textView = new TextView(this);
            textView.setText("Comment " + i + ": " + comment + "\n\n");
            rl.addView(textView, i + 8);
            i++;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.detailedListingConfirmPurchase) : {
                Intent i = new Intent(this, ProductPurchaseConfirmationActivity.class);
                i.putExtra("UploaderID", product.getUploaderID());
                i.putExtra("ProductID", product.getProductID());
                startActivity(i);
                break;
            }

            case (R.id.submitReviewButton) :
                EditText editText = (EditText) findViewById(R.id.detailedListingEditReviewText);
                String review = editText.getText().toString();
                product.addReview(review);
                Intent intent = getIntent();
                intent.putExtra("ProductID", product.getProductID());
                finish();
                startActivity(intent);
                break;

            case (R.id.detailedListingCheckUploadersPage) :
                Intent newIntent = new Intent(this, ViewUsersProfileActivity.class);
                newIntent.putExtra("UserId", product.getUploaderID());
                startActivity(newIntent);
                break;


            case (R.id.AddToWishList) :
                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
                final String currentUserID = fbUser.getUid();
                DatabaseReference ref = mDatabase.child("users").child(currentUserID).child("productsInWishList").push();
                ref.setValue(product);
                break;

            // YHG20171107
            case (R.id.confirmBidButton) :
            {
                EditText bidPriceText = (EditText) findViewById(R.id.editBidPrice);
                bidPrice = Double.parseDouble(bidPriceText.getText().toString());
                Log.v("YHG", "curBidPrice: " + product.getCurAuctionPrice() + " editBidPrice: " + bidPrice);
                if(bidPrice >= product.getCurAuctionPrice()) {
                    Intent i = new Intent(this, ProductPurchaseConfirmationActivity.class);
                    i.putExtra("UploaderID", product.getUploaderID());
                    i.putExtra("ProductID", product.getProductID());
                    i.putExtra("isAuction", true);
                    i.putExtra("BidPrice", bidPrice);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(this, "Bid price must be higher than the current.", Toast.LENGTH_SHORT).show();
                }
                break;
            }



            default :
                break;
        }
    }

}
