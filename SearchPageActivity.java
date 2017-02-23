package group_project.cis350.upenn.edu.a350_group_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;

import static group_project.cis350.upenn.edu.a350_group_project.R.id.searchButton;

/**
 * Created by johnquinn on 2/14/17.
 */

public class SearchPageActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener {

    private static final int RESULT_GO_TO_SEARCH_RESULTS = 3;
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
            case (searchButton) :
                Intent i = new Intent(this, SearchResultsActivity.class);
                i.putExtra("Search Category", searchCategory);
                //startActivityForResult(i, RESULT_GO_TO_SEARCH_RESULTS);
                startActivity(i);
                break;
            default :
                break;
        }
    }

}
