package edu.upenn.benslist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by johnquinn on 2/16/17.
 */

public class ProductListingView extends LinearLayout {

    private Product product;

    public ProductListingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.product_listing_layout, this);

    }

    public ProductListingView(Context context, Product product) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.product_listing_layout, this);
        this.product = product;

        TextView productName = (TextView) findViewById(R.id.productListingProductName);
        productName.setText(product.getName());

        TextView productDescription = (TextView) findViewById(R.id.productListingProductDescription);
        productDescription.setText(product.getDescription());

        TextView productPrice = (TextView) findViewById(R.id.productListingProductPrice);
        productPrice.setText(product.getPrice());

        TextView productLocation = (TextView) findViewById(R.id.productListingProductLocation);
        productLocation.setText(product.getLocation());

        TextView uploaderPhoneNumber = (TextView) findViewById(R.id.productListingUploaderPhoneNumber);
        uploaderPhoneNumber.setText(product.getPhoneNumber());

        TextView uploaderName = (TextView) findViewById(R.id.productListingUploaderName);
        uploaderName.setText(product.getUploaderName());
    }

    protected Button getCheckOutThisListingButton() {
        return (Button) findViewById(R.id.productListingCheckOutListingButton);
    }


}
