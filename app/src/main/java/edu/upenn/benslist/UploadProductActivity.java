package edu.upenn.benslist;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class UploadProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
View.OnClickListener {


    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_GO_TO_HOME_PAGE = 2;

    private ImageView imageToUpload;
    private Button uploadImageButton;
    private Button doneButton;
    private EditText nameText, descriptionText, priceText, locationText, phoneNumberText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);

        imageToUpload = (ImageView) findViewById(R.id.imageToUpload);
        uploadImageButton = (Button) findViewById(R.id.uploadPictureButton);
        nameText = (EditText) findViewById(R.id.editProductName);
        descriptionText = (EditText) findViewById(R.id.editProductDescription);
        priceText = (EditText) findViewById(R.id.editPrice);
        locationText = (EditText) findViewById(R.id.editLocation);
        phoneNumberText = (EditText) findViewById(R.id.editPhoneNumber);
        doneButton = (Button) findViewById(R.id.doneButton);

        uploadImageButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);



        Spinner spinner = (Spinner) findViewById(R.id.productCategorySpinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.product_categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
    }

    public void onNothingSelected(AdapterView<?> arg0) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.uploadPictureButton) :
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;
            case (R.id.doneButton) :
                Log.d("debugging", "clicking done button");
                Intent i = new Intent(this, HomePageActivity.class);
                //startActivityForResult(i, RESULT_GO_TO_HOME_PAGE);
                startActivity(i);
                break;
            default :
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageSelected = data.getData();
            imageToUpload.setImageURI(imageSelected);
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
     * TODO link the home button to the home page
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

            default:
                //Could not recognize a button press
                Toast.makeText(this, "Could not recognize a button press", Toast.LENGTH_SHORT).show();
                return false;
        }
    }

}
