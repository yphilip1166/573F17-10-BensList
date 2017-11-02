package edu.upenn.benslist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

/**
 * Created by johnquinn on 2/15/17.
 */

public class FilterSearchResultsActivity extends MyAppCompatActivity implements View.OnClickListener {

    /*
    TODO - completely changed this April 13th (JP)
     */

    private String searchQuery;
    private String searchCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_results);

        this.searchCategory = getIntent().getStringExtra("Search Category");
        this.searchQuery = getIntent().getStringExtra("Search Query");

        Button button = (Button)  findViewById(R.id.doneFilteringResultsButton);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.doneFilteringResultsButton) :
                Intent intent = new Intent(this, SearchResultsActivity.class);

                CheckBox lowPrice = (CheckBox) findViewById(R.id.lowPriceCheckBox);
                CheckBox medPrice = (CheckBox) findViewById(R.id.mediumPriceCheckBox);
                CheckBox highPrice = (CheckBox) findViewById(R.id.highPriceCheckBox);
                CheckBox closeLocation = (CheckBox) findViewById(R.id.closeLocationCheckBox);
                CheckBox mediumLocation = (CheckBox) findViewById(R.id.mediumLocationCheckBox);
                CheckBox farLocation = (CheckBox) findViewById(R.id.farLocationCheckBox);

                intent.putExtra("Low Price", lowPrice.isChecked());
                intent.putExtra("Medium Price", medPrice.isChecked());
                intent.putExtra("High Price", highPrice.isChecked());
                intent.putExtra("Close Location", closeLocation.isChecked());
                intent.putExtra("Medium Location", mediumLocation.isChecked());
                intent.putExtra("Far Location", farLocation.isChecked());
                intent.putExtra("Search Category", searchCategory);
                intent.putExtra("Search Query", searchQuery);

                startActivity(intent);
                break;

            default :
                break;
        }

    }

}
