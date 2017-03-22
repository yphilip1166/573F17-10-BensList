package edu.upenn.benslist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import java.util.ArrayList;
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

        addProductsFromSearch();
    }

    protected void addProductsFromSearch() {

        /*
        TODO - use searchCategory, searchQuery, and searchFilters to access the database
         */

        /*
        TODO - create function that returns set of Products that fit the search criteria above
         */

        //DONE - check line below
        List<Product> productsFromSearch = Product.getProductsFromDatabase(searchCategory, searchQuery);


        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT);
        ScrollView sv = (ScrollView) findViewById(R.id.search_results_layout);

        //add each product to the activity
        for (final Product product : productsFromSearch) {
            ProductListingView productView = new ProductListingView(this, product);
            sv.addView(productView);
            final Context thisContext = this;
            Button checkOutListingButton = productView.getCheckOutThisListingButton();
            checkOutListingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(thisContext, CheckoutProductActivity.class);
                    i.putExtra("Product", product);
                    startActivity(i);
                }
            });
        }
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
