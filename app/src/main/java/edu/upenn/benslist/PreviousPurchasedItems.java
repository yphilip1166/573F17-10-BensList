package edu.upenn.benslist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tylerdouglas on 3/27/17.
 */

public class PreviousPurchasedItems extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_users);

        User user = (User) getIntent().getSerializableExtra("User");

        List<Product> purchasedProducts= user.getProductsIveBought();
        List<String> purchasedProductsString = new ArrayList<>();

        for (int i = 0; i < purchasedProducts.size(); i++) {
            purchasedProductsString.add(purchasedProducts.get(i).toString());
        }

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, purchasedProductsString);

        ListView listView = (ListView) findViewById(R.id.favoriteUsersList);
        listView.setAdapter(itemsAdapter);
    }
}
