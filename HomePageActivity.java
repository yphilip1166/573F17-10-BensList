package group_project.cis350.upenn.edu.a350_group_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;


/**
 * Created by johnquinn on 2/23/17.
 */

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_GO_TO_SEARCH_PAGE_FROM_HOME = 6;
    private static final int RESULT_GO_TO_UPLOAD_PAGE_FROM_HOME = 7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_layout);

        Button uploadProductButton = (Button) findViewById(R.id.beginUploadingProductButton);
        Button searchProductsButton = (Button) findViewById(R.id.beginSearchingProductsButton);

        uploadProductButton.setOnClickListener(this);
        searchProductsButton.setOnClickListener(this);
        Log.d("debugging", "creating home activity");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.beginUploadingProductButton) :
                Intent i = new Intent(this, MainActivity.class);
                //startActivityForResult(i, RESULT_GO_TO_UPLOAD_PAGE_FROM_HOME);
                startActivity(i);
                break;
            case (R.id.beginSearchingProductsButton) :
                Intent intent = new Intent(this, SearchPageActivity.class);
                //startActivityForResult(intent, RESULT_GO_TO_SEARCH_PAGE_FROM_HOME);
                startActivity(intent);
                break;
            default :
                break;
        }
    }
}
