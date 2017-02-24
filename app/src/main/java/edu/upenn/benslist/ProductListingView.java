package edu.upenn.benslist;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by johnquinn on 2/16/17.
 */

public class ProductListingView extends LinearLayout {

    private ImageView image;
    private Button checkOutThisListingButton;
    private TextView productName, productDescription, productPrice, productLocation,
            uploaderPhoneNumber, uploaderName;

    public ProductListingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.product_listing_layout, this);

        image = (ImageView) findViewById(R.id.productListingImage);
        checkOutThisListingButton = (Button) findViewById(R.id.productListingCheckOutListingButton);
        productName = (TextView) findViewById(R.id.productListingProductName);
        productDescription = (TextView) findViewById(R.id.productListingProductDescription);
        productPrice = (TextView) findViewById(R.id.productListingProductPrice);
        productLocation = (TextView) findViewById(R.id.productListingProductLocation);
        uploaderPhoneNumber = (TextView) findViewById(R.id.productListingUploaderPhoneNumber);
        uploaderName = (TextView) findViewById(R.id.productListingUploaderName);
    }

    protected void setImageUri(Uri imagesUri) {
        image.setImageURI(imagesUri);
    }

    protected void setProductName(String name) {
        productName.setText(name);
    }

    protected void setProductDescription(String description) {
        productDescription.setText(description);
    }

    protected void setProductPrice(String price) {
        productPrice.setText(price);
    }

    protected void setProductLocation(String location) {
        productLocation.setText(location);
    }

    protected void setUploaderPhoneNumber(String number) {
        uploaderPhoneNumber.setText(number);
    }

    protected void setUploaderName(String name) {
        uploaderName.setText(name);
    }

}
