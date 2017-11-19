package edu.upenn.benslist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.sendbird.android.SendBird;

/**
 * Created by johnquinn on 2/14/17.
 */

public class SearchPageActivity extends MyAppCompatActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener {

    private String searchCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page_layout);

        Spinner spinner = (Spinner) findViewById(R.id.productCategorySearchSpinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.product_categories_search_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        this.searchCategory = "Furniture";

        Button searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        searchCategory = parent.getItemAtPosition(position).toString();
    }

    public void onNothingSelected(AdapterView<?> arg0) {

    }

    @Override
    public void onClick(View v) {
        SearchView searchView = (SearchView) findViewById(R.id.editSearch);
        String searchQuery = searchView.getQuery().toString();

        switch (v.getId()) {
            case (R.id.searchButton) :
                Intent i = new Intent(this, SearchResultsActivity.class);
                i.putExtra("Search Category", searchCategory);
                i.putExtra("Search Query", searchQuery);
                i.putExtra("Sort By Price", false);
                startActivity(i);
                finish();
                break;

            default :
                break;
        }
    }


}
