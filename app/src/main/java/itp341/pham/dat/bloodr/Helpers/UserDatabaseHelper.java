package itp341.pham.dat.bloodr.Helpers;

import android.app.Activity;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import itp341.pham.dat.bloodr.BloodrApplication;
import itp341.pham.dat.bloodr.Model.User;

/**
 * Created by deptr on 5/7/2016.
 */
public class UserDatabaseHelper {

    // all internal objects ---------------------------
    Firebase mFirebaseUsersRef;
    Activity mActivity;

    private boolean has_user = false;
    private boolean has_valid_pw = false;
    private String mPhoneNumber, mPassword;
    private User mUser = null;
    // end internal objects ---------------------------


    public UserDatabaseHelper(Activity activity) {
        mActivity = activity;

        //get ref to firebase
        Firebase mainRef = ((BloodrApplication) mActivity.getApplication())
                .getFirebaseReference();
        mFirebaseUsersRef = mainRef.child(FirebaseSchema.TABLE_USERS.NAME); // load get user table
    }

    public boolean hasUser(String phone) {
        mPhoneNumber = phone;
        has_user = false;

        if (phone.length() <= 0) // invalid input
            return false;

        Firebase childFB = mFirebaseUsersRef.child(phone);
        debug("child fb " + childFB.toString());
        debug("fb path: " + childFB.getPath());
        childFB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() >= 2) { // user exists
                    has_user = true;
                    debug("has user exist");
                    return;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return has_user;
    }

    public boolean isValidPassword(String phone, final String password) {
        if (hasUser(phone)) { // user exists
            Firebase childFB = mFirebaseUsersRef.child(phone);
            childFB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);

                    if (user.getPassword_().equals(password)) {
                        // password is valid
                        has_valid_pw = true;
                        return;
                    } else {
                        has_valid_pw = false;
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            return has_valid_pw;
        } else {
            has_valid_pw = false;
            return false;
        }

    }

    public User getUser(String phone, final String password) {

        if (isValidPassword(phone, password)) {
            Firebase childFB = mFirebaseUsersRef.child(phone);
            childFB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mUser = dataSnapshot.getValue(User.class);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }

        return mUser;
    }

    // debug tool
    private static final String TAG = UserDatabaseHelper.class.getSimpleName();
    private void debug(String s) {
        Log.v(TAG, "ddebug UserDatabaseHelper.java: " + s);
    }
}
