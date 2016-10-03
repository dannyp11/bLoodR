package itp341.pham.dat.bloodr;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import itp341.pham.dat.bloodr.Fragments.EventListFragment;
import itp341.pham.dat.bloodr.Fragments.LikedEventsFragment;
import itp341.pham.dat.bloodr.Fragments.LoginFragment;
import itp341.pham.dat.bloodr.Fragments.SingleEventFragment;
import itp341.pham.dat.bloodr.Helpers.FirebaseSchema;
import itp341.pham.dat.bloodr.Model.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // activity code for other activities
    public static final int LOGIN_ACTIVITY_CODE = 1;
    public static final int EVENT_VIEW_ACTIVITY_CODE = 2;

    //fragment tags -----------------------------------------
    public static final String FRAG_TAG_LIST_EVENTS = "itp341.pham.dat.bloodr.frag_tag_listevents";
    public static final String FRAG_TAG_LIST_LIKED_EVENTS = "itp341.pham.dat.bloodr.frag_tag_listlikedevents";
    public static final String FRAG_TAG_SINGLE_EVENT = "itp341.pham.dat.bloodr.frag_tag_singleevent";

    // extra helpers
    public static final String EXTRA_USER = "com.itp341.pham.dat.bloodr.user";

    // internal data & objects
    private User mLoggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // instantiate eventlist fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragEventList = fm.findFragmentById(R.id.fragListDonationEvents);
        if (fragEventList == null) {
            fragEventList = new EventListFragment();
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragListDonationEvents, fragEventList, FRAG_TAG_LIST_EVENTS);
        ft.commit();

        // all setup mUser ----------------------------------------------------------
        final SharedPreferences sharedPreferences = getSharedPreferences(
                LoginActivity.LOGIN_PREFERENCE, Context.MODE_PRIVATE);
        String user_phone = sharedPreferences.getString(
                LoginActivity.PREF_LOGGEDIN_USERNAME, "");

        // setup database ---------------------------------------
        Firebase mainRef = ((BloodrApplication) getApplication())
                .getFirebaseReference();
        Firebase firebaseUserRef = mainRef
                .child(FirebaseSchema.TABLE_USERS.NAME); // load get user table
        Firebase childFB = firebaseUserRef.child(user_phone);
        childFB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // check if user exists
                if (dataSnapshot.getChildrenCount() > 2) {
                    //retrieve user from phone number
                    mLoggedInUser = dataSnapshot.getValue(User.class);

                    //all setup user info in drawer ----------------------------------------
                    TextView tvUserName = (TextView) findViewById(R.id.tv_nav_header_UserName);
                    TextView tvUserEmail = (TextView) findViewById(R.id.tv_nav_header_UserEmail);

                    tvUserName.setText(mLoggedInUser.getName_());
                    tvUserEmail.setText(mLoggedInUser.getEmail_());
                    //end setup user info in drawer ----------------------------------------
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        // end setup database ------------------------------------
        // all setup mUser ----------------------------------------------------------
    }

    // get user from login fragment
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) { // if user didn't use back button
            switch (requestCode) {
                case LOGIN_ACTIVITY_CODE:
                    try {
                        mLoggedInUser = (User) data.getSerializableExtra(EXTRA_USER);
                    } catch (Exception e) {
                        debug("Invalid user from login activity");
                    }
                    break;

                default:
                    break;
            }
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();

            // check if main fragment is not single event
            FragmentManager fm = getSupportFragmentManager();
            SingleEventFragment singleEventFragment = (SingleEventFragment)
                    fm.findFragmentByTag(FRAG_TAG_SINGLE_EVENT);
            if (singleEventFragment == null || !singleEventFragment.isVisible()) {
                // ask if user wanna quit
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Quit bLoodR")
                        .setMessage("Are you sure you want to quit?")
                        .setPositiveButton("Yay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // quit application
//                                int pid = android.os.Process.myPid();
//                                android.os.Process.killProcess(pid);
//                                System.exit(0);
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                                finish();
                            }
                        })
                        .setNegativeButton("Nay", null)
                        .show();
            } else {
                super.onBackPressed();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_bloodrEvents:
                // check if main fragment is not list event
                FragmentManager fm = getSupportFragmentManager();
                EventListFragment eventListFragment = (EventListFragment)
                        fm.findFragmentByTag(FRAG_TAG_LIST_EVENTS);
                if (eventListFragment == null || !eventListFragment.isVisible()) {
                    if (eventListFragment == null)
                        eventListFragment = new EventListFragment();

                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fragListDonationEvents, eventListFragment, FRAG_TAG_LIST_EVENTS);
                    ft.commit();
                }

                break;

            case R.id.nav_bloodrMap:
                break;

            case R.id.nav_interested_bloodrEvents:
                // check if main fragment is not list event
                fm = getSupportFragmentManager();
                LikedEventsFragment likedEventsFragment = (LikedEventsFragment)
                        fm.findFragmentByTag(FRAG_TAG_LIST_LIKED_EVENTS);
                if (likedEventsFragment == null || !likedEventsFragment.isVisible()) {
                    if (likedEventsFragment == null)
                        likedEventsFragment = new LikedEventsFragment();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fragListDonationEvents, likedEventsFragment, FRAG_TAG_LIST_LIKED_EVENTS);
                    ft.commit();
                }
                break;

            case R.id.nav_edit_profile:
                break;

            case R.id.nav_signout:
                // ask if user really wanna sign out
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_lock_power_off)
                        .setTitle("Sign Out")
                        .setMessage("Are you sure you want to sign out?")
                        .setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // set the has_userloggedin bit in shared pref
                                // save preferences ------------------------------------------------
                                SharedPreferences sharedPreferences = getSharedPreferences(
                                        LoginActivity.LOGIN_PREFERENCE, Context.MODE_PRIVATE);
                                SharedPreferences.Editor sharedpref_editor = sharedPreferences.edit();
                                sharedpref_editor.putBoolean(LoginActivity.PREF_HAS_USER_LOGGEDIN, false);
                                sharedpref_editor.putString(LoginActivity.PREF_LOGGEDIN_USERNAME, "");
                                sharedpref_editor.commit();
                                // save preferences ------------------------------------------------

                                // then go to sign in activity
                                finish();
                            }

                        })
                        .setNegativeButton("Nope", null)
                        .show();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public User getLoggedInUser() {
        return mLoggedInUser;
    }

    public void setmLoggedInUser(User mLoggedInUser) {
        this.mLoggedInUser = mLoggedInUser;

        // update USER firebase
        Firebase mainRef = ((BloodrApplication) getApplication())
                .getFirebaseReference();
        Firebase firebaseUserRef = mainRef
                .child(FirebaseSchema.TABLE_USERS.NAME); // load get user table
        firebaseUserRef.child(mLoggedInUser.getPhone()).setValue(mLoggedInUser);
    }

    // debug tool
    private static final String TAG = MainActivity.class.getSimpleName();

    private void debug(String s) {
        Log.v(TAG, "ddebug MainActivity.java: " + s);
    }
}
