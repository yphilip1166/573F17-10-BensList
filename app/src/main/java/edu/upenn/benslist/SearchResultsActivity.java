package edu.upenn.benslist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by johnquinn on 2/16/17.
 */

public class SearchResultsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_GO_TO_FILTER_SEARCH_RESULTS = 4;
    private static final int RESULT_GO_TO_HOME_PAGE_FROM_SEARCH_RESULTS = 8;
    private String searchCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results_layout);
        //get searchCategory when we go from SearchPageActivity to here
        this.searchCategory = getIntent().getStringExtra("Search Category");
        //now have to check to see if this intent was started from FilterSearchResultsActivity
        //if so, then we have to retrieve all of the filters

        Button filterResultsButton = (Button) findViewById(R.id.filterSearchResultsButton);
        filterResultsButton.setOnClickListener(this);
        Button backToHomePageButton = (Button) findViewById(R.id.goBackToHomePageFromSearchResultsButton);
        backToHomePageButton.setOnClickListener(this);
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
                //startActivityForResult(intent, RESULT_GO_TO_HOME_PAGE_FROM_SEARCH_RESULTS);
                startActivity(intent);
                break;
            default :
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
