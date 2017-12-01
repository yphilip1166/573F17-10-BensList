package edu.upenn.benslist;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tylerdouglas on 3/27/17.
 */

public class MyProductsActivity extends MyAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_users);

        User user = (User) getIntent().getSerializableExtra("User");

        List<Product> uploadProducts= user.getProductsIveUploaded();
        List<String> uploadProductsStrings = new ArrayList<>();

        for (int i = 0; i < uploadProducts.size(); i++) {
            uploadProductsStrings.add(uploadProductsStrings.get(i).toString());
        }

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, uploadProductsStrings);

        ListView listView = (ListView) findViewById(R.id.favoriteUsersList);
        listView.setAdapter(itemsAdapter);
    }
}
