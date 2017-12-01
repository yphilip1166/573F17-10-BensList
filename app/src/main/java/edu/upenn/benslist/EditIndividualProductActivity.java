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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tylerdouglas on 4/19/17.
 */

public class EditIndividualProductActivity extends MyAppCompatActivity implements View.OnClickListener{

    private static final int RESULT_LOAD_IMAGE = 1;
    private ImageView imageToUpload;
    private String itemCategory;
    private String condition;
    private String currentUserName;
    private Product product;
    public static final String MESSAGES_CHILD = "inbox";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);
        this.currentUserName = getIntent().getStringExtra("Username");
        this.product = (Product) getIntent().getExtras().getSerializable("Product");
//        String productID= getIntent().getStringExtra("ProductID");
//        this.product =  FirebaseDatabase.getInstance().getReference()
//                .child("products").child(productID).getValue(Product.class);
        Log.v("wish ei: ", product.productID);
        Log.v("wish product in ei: ", product.toString());
        populateProductFields();

    }

    private void populateProductFields() {
        imageToUpload = (ImageView) findViewById(R.id.imageToUpload);
        Button uploadImageButton = (Button) findViewById(R.id.uploadPictureButton);
        Button doneButton = (Button) findViewById(R.id.doneButton);

        uploadImageButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);


        Spinner spinner = (Spinner) findViewById(R.id.productCategorySpinner);
        itemCategory = product.getCategory();
        ArrayList<String> categoryOptions = new ArrayList<String>
                (Arrays.asList(getResources().getStringArray(R.array.product_categories_array)));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                itemCategory = parentView.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.product_categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(categoryOptions.indexOf(itemCategory));


        Spinner conditionSpinner = (Spinner) findViewById(R.id.conditionSpinner);
        condition = product.getCondition();
        ArrayList<String> conditionOptions = new ArrayList<String>
                (Arrays.asList(getResources().getStringArray(R.array.condition_array)));
        conditionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                condition = parentView.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        ArrayAdapter<CharSequence> conditionAdapter = ArrayAdapter.createFromResource(this,
                R.array.condition_array, android.R.layout.simple_spinner_item);
        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conditionSpinner.setAdapter(conditionAdapter);
        conditionSpinner.setSelection(conditionOptions.indexOf(condition));

        EditText editProductName = (EditText) findViewById(R.id.editProductName);
        editProductName.setText(product.getName());

        EditText editProductDescription = (EditText) findViewById(R.id.editProductDescription);
        editProductDescription.setText(product.getDescription());

        EditText editPrice = (EditText) findViewById(R.id.editPrice);
        editPrice.setText(Double.toString(product.getPriceAsDouble()));

        EditText editLocation = (EditText) findViewById(R.id.editLocation);
        editLocation.setText(product.getLocation());

        EditText editDistance = (EditText) findViewById(R.id.editDistance);
        editDistance.setText(Double.toString(product.getDistance()));

        EditText editPhoneNumber = (EditText) findViewById(R.id.editPhoneNumber);
        editPhoneNumber.setText(product.getPhoneNumber());
        EditText editQuantity = (EditText) findViewById(R.id.editQuantity);
        editQuantity.setText(Integer.toString(product.getQuantity()));
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

                Intent returnIntent = new Intent(this, EditListingActivity.class);

                final FirebaseUser fbuser = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference mUserReference = FirebaseDatabase.getInstance().getReference();
                mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String productRefKey = "";
                        for (DataSnapshot productSnapshot : dataSnapshot.child("users").child(fbuser.getUid()).child(
                                "productsIveUploaded").getChildren()) {
                            Product snapshotProduct = productSnapshot.getValue(Product.class);
                            if (snapshotProduct.getProductID().equals(product.getProductID())) {
                                productRefKey = productSnapshot.getKey();
                            }
                            System.out.println("This is the current product " + productRefKey);
                        }

                        EditText productName = (EditText) findViewById(R.id.editProductName);
                        EditText productDescription = (EditText) findViewById(R.id.editProductDescription);
                        EditText productLocation = (EditText) findViewById(R.id.editLocation);
                        EditText productPhoneNumber = (EditText) findViewById(R.id.editPhoneNumber);
                        EditText priceText = (EditText) findViewById(R.id.editPrice);
                        EditText distanceText = (EditText) findViewById(R.id.editDistance);
                        EditText productQuantity = (EditText) findViewById(R.id.editQuantity);

                        String price = priceText.getText().toString();
                        double distance = Double.parseDouble(String.valueOf(String.valueOf(distanceText.getText())));
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

                        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference()
                                .child("users").child(product.getUploaderID()).child("productsIveUploaded")
                                .child(productRefKey);

                        String description = String.valueOf(productDescription.getText());
                        String location = String.valueOf(productLocation.getText());
                        String name = String.valueOf(productName.getText());
                        String phoneNumber = String.valueOf(productPhoneNumber.getText());
                        int priceCategory = getPriceLevel(priceAsDouble);
                        int locationCategory = getLocationLevel(distance);
                        int quantityAsInt = Integer.parseInt(productQuantity.getText().toString());
                        price = "$" + priceAsDouble;
                        int decimalIndex = price.indexOf('.');
                        if (price.length() - decimalIndex == 2) {
                            price += "0";
                        }

                        productRef.child("category").setValue(itemCategory);
                        productRef.child("description").setValue(description);
                        productRef.child("condition").setValue(condition);
                        productRef.child("distance").setValue(distance);
                        productRef.child("location").setValue(location);
                        productRef.child("name").setValue(name);
                        productRef.child("phoneNumber").setValue(phoneNumber);
                        productRef.child("price").setValue(price);
                        productRef.child("priceAsDouble").setValue(priceAsDouble);
                        productRef.child("priceCategory").setValue(priceCategory);
                        productRef.child("locationCategory").setValue(locationCategory);
                        productRef.child("quantity").setValue(quantityAsInt);
                        List<String> w= (List<String>)dataSnapshot.child("products").child(product.getProductID()).child("wisher").getValue();
                        productRef.child("wisher").setValue(dataSnapshot.child("products").child(product.getProductID()).child("wisher").getValue());
                        product.setWisher(w);
                        Log.v("wait addr: ", productRef.child("wisher").toString());
                        setGeneralProduct(product.getProductID(), description, distance, location, name,
                                phoneNumber, price, priceAsDouble, priceCategory, locationCategory,
                                quantityAsInt);
                        Log.v("wish update in edit:", currentUserName);
                        List<String> wx= product.getWisher();

                        if (w!=null) sendNotification(w);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                setResult(RESULT_OK, returnIntent);
                finish();
                break;
            default :
                break;
        }
    }

    private void sendNotification(List<String> ws){
        for (String toUserId: ws){
            String mUserId= "SYSTEM NOTIFICATION";
//            String toUserId= ;
            String channelID =  mUserId.compareTo(toUserId)>0? mUserId + toUserId: toUserId + mUserId;
            Log.v("wish ChID send: ", channelID);
            Message message = new Message("Product: "+ product.getName()+" in your wishlist has been updated.", "System Notification");
            FirebaseDatabase.getInstance().getReference().child(MESSAGES_CHILD).child(channelID).push().setValue(message);
        }
    }

    private void setGeneralProduct(String productID, String description, double distance,
                                   String location, String name, String phoneNumber, String price,
                                   double priceAsDouble, int priceCategory, int locationCategory,
                                   int quantity) {
        System.out.println("Product ID is: " + productID);
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference()
                .child("products").child(productID);

        productRef.child("category").setValue(itemCategory);
        productRef.child("description").setValue(description);
        productRef.child("condition").setValue(condition);
        productRef.child("distance").setValue(distance);
        productRef.child("location").setValue(location);
        productRef.child("name").setValue(name);
        productRef.child("phoneNumber").setValue(phoneNumber);
        productRef.child("price").setValue(price);
        productRef.child("priceAsDouble").setValue(priceAsDouble);
        productRef.child("priceCategory").setValue(priceCategory);
        productRef.child("locationCategory").setValue(locationCategory);
        productRef.child("quantity").setValue(quantity);

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
        finish();
    }


}
