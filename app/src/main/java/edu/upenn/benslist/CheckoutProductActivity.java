package edu.upenn.benslist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by johnquinn on 3/14/17.
 */

public class CheckoutProductActivity extends AppCompatActivity implements View.OnClickListener {

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_product);
        String productID = (String) getIntent().getStringExtra("ProductID");
        this.product = Product.getProductFromDatabase(productID);

        Button purchaseProductButton = (Button) findViewById(R.id.detailedListingConfirmPurchase);
        purchaseProductButton.setOnClickListener(this);

        Button leaveReviewButton = (Button) findViewById(R.id.submitReviewButton);
        leaveReviewButton.setOnClickListener(this);

        Button checkOutUploadersProfileButton =
                (Button) findViewById(R.id.detailedListingCheckUploadersPage);
        checkOutUploadersProfileButton.setOnClickListener(this);

        String uploaderName = product.getUploaderName();
        //checkOutUploadersProfileButton.setText("Check Out " + uploaderName + "'s Profile");
        checkOutUploadersProfileButton.setText("Check Out " + product.getUploaderID() + "'s Profile");

        addCommentsSection();
    }

    protected void addCommentsSection() {
        int i = 1;
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.purchaseProductRL);
        for (String comment : product.getReviews()) {
            TextView textView = new TextView(this);
            textView.setText("Comment " + i + ": " + comment + "\n\n");
            rl.addView(textView);
            i++;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.detailedListingConfirmPurchase) :
                //TODO - notify the uploader that someone bought their product - next iteration??
                Intent i = new Intent(this, ProductPurchaseConfirmationActivity.class);
                i.putExtra("UploaderID", product.getUploaderID());
                startActivity(i);
                break;

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
                Intent newIntent = new Intent(this, UserProfileActivity.class);
                newIntent.putExtra("UserID", product.getUploaderID());
                startActivity(newIntent);
                break;

            default :
                break;
        }
    }


}
