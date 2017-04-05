package edu.upenn.benslist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
        this.product = (Product) getIntent().getExtras().getSerializable("Product");

        Button purchaseProductButton = (Button) findViewById(R.id.detailedListingConfirmPurchase);
        purchaseProductButton.setOnClickListener(this);

        Button leaveReviewButton = (Button) findViewById(R.id.submitReviewButton);
        leaveReviewButton.setOnClickListener(this);

        Button checkOutUploadersProfileButton =
                (Button) findViewById(R.id.detailedListingCheckUploadersPage);
        checkOutUploadersProfileButton.setOnClickListener(this);
        checkOutUploadersProfileButton.setText("Check Out " + product.getUploaderName() + "'s Profile");

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
