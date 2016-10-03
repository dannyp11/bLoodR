package itp341.pham.dat.bloodr;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.firebase.client.Firebase;

/**
 * Created by deptr on 4/21/2016.
 */
public class BloodrApplication extends Application {

    private Firebase firebaseReference;

    @Override
    public void onCreate() {
        super.onCreate();

        //init firebase context and ref
        Firebase.setAndroidContext(this);
        firebaseReference = new Firebase(getString(R.string.database_url));
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public Firebase getFirebaseReference() {
        return firebaseReference;
    }
}
