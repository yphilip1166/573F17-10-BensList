
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


public class RemoveWishListActivity extends MyAppCompatActivity implements View.OnClickListener{

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wish_list_remove_layout);
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
                Intent back = new Intent(this, WishListActivity.class);
                setResult(RESULT_OK, back);
                finish();
                break;

            case (R.id.removeItemRemove) :

                Intent returnIntent = new Intent(this, WishListActivity.class);

                FirebaseUser fbuser = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference mUserReference = FirebaseDatabase.getInstance().getReference()
                        .child("users").child(fbuser.getUid());
                final String currentUserID = fbuser.getUid();
                mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String productRefKey = "";
                        for (DataSnapshot productSnapshot : dataSnapshot.child(
                                "productsInWishList").getChildren()) {
                            Product snapshotProduct = productSnapshot.getValue(Product.class);
                            if (snapshotProduct.getProductID().equals(product.getProductID())) {
                                productRefKey = productSnapshot.getKey();
                                FirebaseDatabase.getInstance().getReference()
                                        .child("users").child(currentUserID).child("productsInWishList")
                                        .child(productRefKey).removeValue();
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
