
/**
 * Created by Shawn_J on 11/7/17.
 */
package edu.upenn.benslist;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RemoveProductActivity extends MyAppCompatActivity implements View.OnClickListener{

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_product);
        this.product = (Product) getIntent().getExtras().getSerializable("Product");
        Button removeButton = (Button) findViewById(R.id.removeItemRemove);
        Button backButton = (Button) findViewById(R.id.removeItemBack);
        removeButton.setOnClickListener(this);
        backButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.removeItemBack) :
                Intent back = new Intent(this, EditListingActivity.class);
                back.putExtra("removedProductKey","");
                setResult(RESULT_OK, back);
                finish();
                break;

            case (R.id.removeItemRemove) :

                final Intent returnIntent = new Intent(this, EditListingActivity.class);

                FirebaseUser fbuser = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference mUserReference = FirebaseDatabase.getInstance().getReference()
                        .child("users").child(fbuser.getUid());
                mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String productRefKey = "";
                        for (DataSnapshot productSnapshot : dataSnapshot.child(
                                "productsIveUploaded").getChildren()) {
                            Product snapshotProduct = productSnapshot.getValue(Product.class);
                            if (snapshotProduct.getProductID().equals(product.getProductID())) {
                                productRefKey = productSnapshot.getKey();
                                FirebaseDatabase.getInstance().getReference()
                                        .child("users").child(product.getUploaderID()).child("productsIveUploaded")
                                        .child(productRefKey).removeValue();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                DatabaseReference deleteProductFromProducts = FirebaseDatabase.getInstance().getReference();
                deleteProductFromProducts.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String productRefKey = "";
                        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                        for (DataSnapshot productSnapshot : dataSnapshot.child("products").getChildren()) {
                            Product snapshotProduct = productSnapshot.getValue(Product.class);
                            if (snapshotProduct.getProductID().equals(product.getProductID())) {
                                productRefKey = productSnapshot.getKey();
                                mDatabase.child("products").child(productRefKey).removeValue();
                            }
                        }
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
}
