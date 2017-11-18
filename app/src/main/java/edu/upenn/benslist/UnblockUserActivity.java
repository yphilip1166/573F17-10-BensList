
/**
 * Created by Shawn_J on 11/17/17.
 */

package edu.upenn.benslist;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UnblockUserActivity extends MyAppCompatActivity implements View.OnClickListener{

    private String blockedUserKey;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unblock_user);
        this.blockedUserKey = (String) getIntent().getExtras().getSerializable("blockedUser");
        Button unblockButton = (Button) findViewById(R.id.blockedUserUnblock);
        Button backButton = (Button) findViewById(R.id.blockedUserBack);
        unblockButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.blockedUserBack) :
                Intent back = new Intent(this, BlockedUsersActivity.class);
                setResult(RESULT_OK, back);
                finish();
                break;

            case (R.id.blockedUserUnblock) :

                final Intent returnIntent = new Intent(this, BlockedUsersActivity.class);

                FirebaseUser fbuser = FirebaseAuth.getInstance().getCurrentUser();
                userId = fbuser.getUid();
                DatabaseReference mUserReference = FirebaseDatabase.getInstance().getReference()
                        .child("users").child(fbuser.getUid());

                mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String userRefKey = "";
                        for (DataSnapshot keySnapshot : dataSnapshot.child(
                                "blockedUsers").getChildren()) {
                            String key = keySnapshot.getValue(String.class);
                            Log.v("debug",key);
                            Log.v("debug",blockedUserKey);
                            if (key.equals( blockedUserKey)) {
                                Log.v("debug","before remove");
                                keySnapshot.getRef().setValue(null);
                                Log.v("debug","after remove");
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
