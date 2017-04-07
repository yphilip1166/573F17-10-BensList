package edu.upenn.benslist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by johnquinn on 2/16/17.
 */

public class SearchResultsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_GO_TO_FILTER_SEARCH_RESULTS = 4;
    private String searchCategory;
    private String searchQuery;
    private ArrayList<String> filterCategories;
    private ViewGroup mLinearLayout;
    private boolean sortByPrice;

    private DatabaseReference mProductReference;
    private ValueEventListener mProductListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results_layout);

        // Get search specifications from intent
        this.searchCategory = getIntent().getStringExtra("Search Category");
        this.searchQuery = getIntent().getStringExtra("Search Query");
        this.filterCategories = getIntent().getStringArrayListExtra("Filter Categories");
        this.sortByPrice = getIntent().getBooleanExtra("Sort By Price", false);

        // Initialize Database
        mProductReference = FirebaseDatabase.getInstance().getReference()
                .child("products");

        //Initialize views
        Button filterResultsButton = (Button) findViewById(R.id.filterSearchResultsButton);
        filterResultsButton.setOnClickListener(this);
        Button backToHomePageButton = (Button) findViewById(R.id.goBackToHomePageFromSearchResultsButton);
        backToHomePageButton.setOnClickListener(this);
        Button sortByPriceButton = (Button) findViewById(R.id.sortResultsByPriceButton);
        sortByPriceButton.setOnClickListener(this);

        mLinearLayout = (ViewGroup) findViewById(R.id.searchResultsLinearLayout);
    }

    @Override
    public void onStart() {
        super.onStart();

        ValueEventListener productListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Product> products = new LinkedList<>();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    if (product.getCategory().equals(searchCategory) &&
                            product.getName().contains(searchQuery)) {
                        products.add(product);
                    }

                    System.out.println(product.getName());
                }
                addProductsFromSearch(products);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(SearchResultsActivity.this, "Failed to load products.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };

        mProductReference.addValueEventListener(productListener);

    }

    protected void addProductsFromSearch(List<Product> products) {

        Collections.sort(products);

        /*
        TODO - use searchCategory, searchQuery, and searchFilters to access the database
         */

        /*
        TODO - create function that returns set of Products that fit the search criteria above
         */

        final Context thisContext = this;

        //add each product to the activity
        for (final Product product : products) {

            View view = LayoutInflater.from(this).inflate(R.layout.product_listing_layout, mLinearLayout, false);

            TextView productName = (TextView) view.findViewById(R.id.productListingProductName);
            productName.setText("Name: " + product.getName());

            TextView productDescription = (TextView) view.findViewById(R.id.productListingProductDescription);
            productDescription.setText("Description: " + product.getDescription());

            TextView productPrice = (TextView) view.findViewById(R.id.productListingProductPrice);
            productPrice.setText("Price: " + product.getPrice());

            TextView productLocation = (TextView) view.findViewById(R.id.productListingProductLocation);
            productLocation.setText("Location: " + product.getLocation());

            TextView uploaderPhoneNumber = (TextView) view.findViewById(R.id.productListingUploaderPhoneNumber);
            uploaderPhoneNumber.setText("Phone Number: " + product.getPhoneNumber());

            TextView uploaderName = (TextView) view.findViewById(R.id.productListingUploaderName);
            uploaderName.setText("Uploader Name: " + product.getUploaderName());

            Button checkOutButton = (Button) view.findViewById(R.id.productListingCheckOutListingButton);
            checkOutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(thisContext, CheckoutProductActivity.class);
                    i.putExtra("Product", (Serializable) product);
                    startActivity(i);
                }
            });

            mLinearLayout.addView(view);
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
            case (R.id.sortResultsByPriceButton) :
                Intent newIntent = new Intent(this, SearchResultsActivity.class);
                newIntent.putExtra("Search Category", searchCategory);
                newIntent.putExtra("Search Query", searchQuery);
                newIntent.putExtra("Sort By Price", true);
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

    /**
     * Code Snippet for adding the menu bar 3 points to select Logout, About, Home, Terms
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tools, menu);
        return true;
    }

    /**
     * Handle the button presses
     * TODO add code that will log the user out when they click logout
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_about:
                //Go to About page
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_home:
                //Go to Home page
                intent = new Intent(this, HomePageActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_logout:
                //Logs out the current user and brings user to the logout page
                //Need to add code for actually logging out a user
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_terms:
                //Go to terms page
                intent = new Intent(this, TermsActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_forum:
                //Go to forum page
                intent = new Intent(this, PublicForumActivity.class);
                startActivity(intent);
                return true;

            default:
                //Could not recognize a button press
                Toast.makeText(this, "Could not recognize a button press", Toast.LENGTH_SHORT).show();
                return false;
        }
    }


}
