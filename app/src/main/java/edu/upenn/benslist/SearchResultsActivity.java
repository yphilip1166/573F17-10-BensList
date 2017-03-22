package edu.upenn.benslist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by johnquinn on 2/16/17.
 */

public class SearchResultsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_GO_TO_FILTER_SEARCH_RESULTS = 4;
    private static final int RESULT_GO_TO_HOME_PAGE_FROM_SEARCH_RESULTS = 8;
    private String searchCategory;
    private String searchQuery;
    private ArrayList<String> filterCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results_layout);
        this.searchCategory = getIntent().getStringExtra("Search Category");
        this.searchQuery = getIntent().getStringExtra("Search Query");
        this.filterCategories = getIntent().getStringArrayListExtra("Filter Categories");

        Button filterResultsButton = (Button) findViewById(R.id.filterSearchResultsButton);
        filterResultsButton.setOnClickListener(this);
        Button backToHomePageButton = (Button) findViewById(R.id.goBackToHomePageFromSearchResultsButton);
        backToHomePageButton.setOnClickListener(this);

        Log.d("debugging", "herereerere");

        addProductsFromSearch();
    }

    protected void addProductsFromSearch() {

        /*
        TODO - use searchCategory, searchQuery, and searchFilters to access the database
         */

        /*
        TODO - create function that returns set of Products that fit the search criteria above
         */

        /*
        This commented out line should actually return the search results. We are using a dummy
        function to get search results
         */
        //List<Product> productsFromSearch = Product.getProductsFromDatabaseSearch(searchCategory, searchQuery);

        //Dummy results
        List<Product> productsFromSearch = getExampleProductSearch();

        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT);
        ViewGroup mLinearLayout = (ViewGroup) findViewById(R.id.searchResultsLinearLayout);

        final Context thisContext = this;

        //add each product to the activity
        for (final Product product : productsFromSearch) {

            View view = LayoutInflater.from(this).inflate(R.layout.product_listing_layout, mLinearLayout, false);

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

            Button checkOutButton = (Button) findViewById(R.id.productListingCheckOutListingButton);
            checkOutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(thisContext, CheckoutProductActivity.class);
                    i.putExtra("ProductID", product.getProductID());
                    startActivity(i);
                }
            });

            mLinearLayout.addView(view);



            /*
            ProductListingView productView = new ProductListingView(this, product);
            final Context thisContext = this;
            Button checkOutListingButton = productView.getCheckOutThisListingButton();
            checkOutListingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(thisContext, CheckoutProductActivity.class);
                    i.putExtra("ProductID", product.getProductID());
                    startActivity(i);
                }
            });

            Log.d("debugging", "add new product to search results");
            sv.addView(productView, lp);
            */
        }
    }


    protected List<Product> getExampleProductSearch() {
        List<Product> products = new LinkedList<>();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserID = fbUser.getUid();

        Product product1 = new Product("Air Force Ones", "sick shoes", "$50", "Philadelphia", "123-456-7890",
                "Clothes", currentUserID, "JP");

        Product product2 = new Product("Jordan VIIs", "sicker shoes", "$10", "Philadelphia", "123-456-4560",
                "Clothes", currentUserID, "JP");

        Product product3 = new Product("Leather Couch", "good condition", "$50", "Philadelphia", "123-456-7890",
                "Furniture", currentUserID, "JP");

        Product product4 = new Product("Nike Crewneck", "Black", "$40", "Philadelphia", "123-456-7890",
                "Clothes", currentUserID, "JP");

        Product product5 = new Product("Blue Nike Elites", "sick socks", "$15", "Philadelphia", "123-456-7890",
                "Clothes", currentUserID, "JP");

        Product product6 = new Product("Nike Joggers", "Grey", "$75", "Philadelphia", "123-456-7890",
                "Clothes", currentUserID, "JP");

        products.add(product1);
        products.add(product2);
        products.add(product3);
        products.add(product4);
        products.add(product5);
        products.add(product6);
        return products;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.filterSearchResultsButton) :
                Intent i = new Intent(this, FilterSearchResultsActivity.class);
                i.putExtra("Search Category", searchCategory);
                startActivityForResult(i, RESULT_GO_TO_FILTER_SEARCH_RESULTS);
                break;
            case (R.id.goBackToHomePageFromSearchResultsButton) :
                Intent intent = new Intent(this, HomePageActivity.class);
                startActivity(intent);
                break;
            default :
                break;
        }
    }

    //GOING TO SWITCH THIS FROM START ACTIVITY FOR RESULT TO JUST START ACTIVITY

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
        finish();
        startActivity(getIntent());
        */


        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_GO_TO_FILTER_SEARCH_RESULTS && resultCode == RESULT_OK) {
            //ArrayList<String> filterCategories = getIntent().getStringArrayListExtra("Filter Categories");
            //continue from here
            switch (searchCategory) {
                case "Furniture" :
                    break;
                case "Electronics" :
                    break;
                case "Books" :
                    break;
                case "Kitchen Supplies" :
                    break;
                case "Clothes" :
                    break;
                case "Services" :
                    break;
                case "All Categories" :
                    break;
                default :
                    break;
            }
        }
    }


}
