package edu.upenn.benslist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by johnquinn on 2/15/17.
 */

public class FilterSearchResultsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_GO_BACK_TO_SEARCH_RESULTS = 5;

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    private String searchCategory;
    private HashMap<String, List<String>> selectedFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_results);
        this.searchCategory = getIntent().getStringExtra("Search Category");
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        this.selectedFilters = new HashMap<String, List<String>>();
        Button doneFilteringButton = (Button) findViewById(R.id.filterResultsDoneButton);
        doneFilteringButton.setOnClickListener(this);

        switch (searchCategory) {
            case "Furniture" :
                expandableListDetail = ExpandableListDataPump.getFurnitureData();
                break;
            case "Electronics" :
                expandableListDetail = ExpandableListDataPump.getElectronicsData();
                break;
            case "Books" :
                expandableListDetail = ExpandableListDataPump.getBooksData();
                break;
            case "Kitchen Supplies" :
                expandableListDetail = ExpandableListDataPump.getKitchenSuppliesData();
                break;
            case "Clothes" :
                expandableListDetail = ExpandableListDataPump.getClothesData();
                break;
            case "Services" :
                expandableListDetail = ExpandableListDataPump.getServicesData();
                break;
            case "All Categories" :
                expandableListDetail = ExpandableListDataPump.getOtherData();
                break;
            default :
                expandableListDetail = ExpandableListDataPump.getOtherData();
                break;
        }

        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new MyExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                //this may not give the right checkBox, but we'll see
                CheckBox checkBox = (CheckBox) findViewById(R.id.expandedListItem);
                String groupName = expandableListTitle.get(groupPosition);
                String itemName = expandableListDetail.get(groupName).get(childPosition);
                /*
                If a checkbox becomes checked, then we can add it to a HashMap<String, List<String>>
                which stores title -> list of itemName objects, telling us which things are checked.
                There should be a "done" button at the very bottom. When they are done, they can
                press the "done" button, and that will start a new intent back to the search results
                activity page (not this one, but the real one). This class's name should be changed
                to SearchResultsFilterActivity. When we create a new intent, we need to store
                this HashMap of which boxes are checked and send it with the intent. That way,
                when the SearchResultsActivity page starts up, it can receive this data, and update
                the search results that fit this HashMap.
                 */
                if (checkBox.isChecked()) {
                    addNewFilter(groupName, itemName);
                }
                else {
                    removeFilter(groupName, itemName);
                }

                Toast.makeText(
                        getApplicationContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();
                return false;
            }
        });
    }

    protected void addNewFilter(String groupName, String itemName) {
        if (selectedFilters.containsKey(groupName)) {
            selectedFilters.get(groupName).add(itemName);
        }
        else {
            List<String> list = new LinkedList<String>();
            list.add(itemName);
            selectedFilters.put(groupName, list);
        }
    }

    protected void removeFilter(String groupName, String itemName) {
        List<String> itemList = selectedFilters.get(groupName);
        if (itemList.size() == 1) {
            selectedFilters.remove(groupName);
        }
        else {
            itemList.remove(itemName);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.filterResultsDoneButton :
                Intent returnIntent = new Intent(this, SearchResultsActivity.class);
                ArrayList<String> filterCategories = new ArrayList<String>();

                for (Map.Entry<String, List<String>> entry : selectedFilters.entrySet()) {
                    filterCategories.add(entry.getKey());
                    ArrayList<String> attributes = new ArrayList<String>();
                    for (String attribute : entry.getValue()) {
                        attributes.add(attribute);
                    }
                    returnIntent.putExtra(entry.getKey(), attributes);
                }

                //returnIntent.putExtra("Filter Categories", filterCategories);
                //startActivityForResult(returnIntent, RESULT_GO_BACK_TO_SEARCH_RESULTS);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                break;
            default :
                break;
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
