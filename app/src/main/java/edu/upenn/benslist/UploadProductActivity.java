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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UploadProductActivity extends MyAppCompatActivity implements AdapterView.OnItemSelectedListener,
View.OnClickListener {

    private static final int RESULT_LOAD_IMAGE = 1;
    private ImageView imageToUpload;
    private String itemCategory;
    private boolean isAuction;
    private String currentUserName;
    private Spinner spinner;
    private Spinner spinner2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);
        this.currentUserName = getIntent().getStringExtra("Logged In User Name");

        imageToUpload = (ImageView) findViewById(R.id.imageToUpload);
        Button uploadImageButton = (Button) findViewById(R.id.uploadPictureButton);
        Button doneButton = (Button) findViewById(R.id.doneButton);

        uploadImageButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);


        spinner = (Spinner) findViewById(R.id.productCategorySpinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.product_categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner2 = (Spinner) findViewById(R.id.isAuctionSpinner);
        spinner2.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.is_auction_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        isAuction = false;
        itemCategory = "Furniture";
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == spinner.getId()) {
            itemCategory = parent.getItemAtPosition(position).toString();
            Log.v("YHG", "Category spinner selected: "+itemCategory);
        }
        else if(parent.getId() == spinner2.getId())
        {
            if(position == 1)isAuction = true;
            else if(position == 0)isAuction = false;
            Log.v("YHG", "isAuction spinner selected: "+isAuction);
        }
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
                /*
                TODO - changes made april 13th by JP here
                 */


                Intent returnIntent = new Intent(this, HomePageActivity.class);

                EditText productName = (EditText) findViewById(R.id.editProductName);
                EditText productDescription = (EditText) findViewById(R.id.editProductDescription);
                EditText productLocation = (EditText) findViewById(R.id.editLocation);
                EditText productPhoneNumber = (EditText) findViewById(R.id.editPhoneNumber);

                EditText priceText = (EditText) findViewById(R.id.editPrice);
                EditText distanceText = (EditText) findViewById(R.id.editDistance);

                String price = priceText.getText().toString();
                String phoneNumber = productPhoneNumber.getText().toString();
                int phoneNumberlen = 0;
                for (char c: phoneNumber.toCharArray()) if (Character.isDigit(c)) phoneNumberlen++;

                if(productName.getText().toString().trim().equals("")||
                        productDescription.getText().toString().trim().equals("")||
                        productLocation.getText().toString().trim().equals("")||
                        productPhoneNumber.getText().toString().trim().equals("")||
                        priceText.getText().toString().trim().equals("")||
                        distanceText.getText().toString().trim().equals("")||
                        price.toString().trim().equals(""))
                {
                    Log.v("YHG", "Some field is missing");
                    Toast.makeText(v.getContext(), "Some required product information missing", Toast.LENGTH_LONG).show();
                    break;
                } else if (phoneNumberlen!=10) {
                    Toast.makeText(v.getContext(), "Phone number is not valid", Toast.LENGTH_LONG).show();
                    break;
                }

                Log.v("YHG", "Product Information:"+productName.getText().toString()  + "," +
                        productDescription.getText().toString() + "," +
                        productLocation.getText().toString() + "," +
                        productPhoneNumber.getText().toString() + "," +
                        priceText.getText().toString() + "," +
                        distanceText.getText().toString() + "," +
                        price.toString());

                //double distance = Double.parseDouble(distanceText.getText().toString());
                try {
                    double distance = Double.parseDouble(distanceText.getText().toString());
                    int decimalPoint = price.indexOf('.');
                    double priceAsDouble = 0.0;
                    if (decimalPoint == -1) {
                        priceAsDouble = Double.parseDouble(price);
                    }
                    else if (price.length() - decimalPoint - 1 == 2) {
                        priceAsDouble = Double.parseDouble(price);
                    }
                    else if (price.length() - decimalPoint - 1 == 1) {
                        priceAsDouble = Double.parseDouble(price);
                    }
                    else if (price.length() - decimalPoint == 1) {
                        //45.
                        priceAsDouble = Double.parseDouble(price.substring(0, price.length() - 1));
                    }
                    else {
                        priceAsDouble = Double.parseDouble(price.substring(0, decimalPoint + 3));
                    }

                    final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
                    final String currentUserID = fbUser.getUid();

                    DatabaseReference ref = mDatabase.child("products").push();

                    Product product = Product.writeNewProductToDatabase(productName.getText().toString(),
                            productDescription.getText().toString(), priceAsDouble,
                            productLocation.getText().toString(), productPhoneNumber.getText().toString(),
                            itemCategory, currentUserName, ref.getKey(), distance, isAuction);

                    ref.setValue(product);

                    ref = mDatabase.child("users").child(currentUserID).child("productsIveUploaded").push();
                    ref.setValue(product);

                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
                catch (NumberFormatException e) {
                    Toast.makeText(v.getContext(), "Not a number", Toast.LENGTH_LONG).show();
                }
                break;

            default :
                break;
        }
    }

    private int getPriceLevel(double price) {
        if (price < 0) {
            return -1;
        }
        if (price <= 99.99) {
            return 1;
        }
        else if (price <= 199.99) {
            return 2;
        }
        else {
            return 3;
        }
    }

    private int getLocationLevel(double distance) {
        if (distance < 0) {
            return -1;
        }
        if (distance <= 9.99) {
            return 1;
        }
        else if (distance <= 19.99) {
            return 2;
        }
        else {
            return 3;
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

}
