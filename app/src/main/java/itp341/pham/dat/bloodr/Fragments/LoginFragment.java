package itp341.pham.dat.bloodr.Fragments;

// Fragment import

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

// Firebase import
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import itp341.pham.dat.bloodr.BloodrApplication;
import itp341.pham.dat.bloodr.Helpers.FirebaseSchema;
import itp341.pham.dat.bloodr.LoginActivity;
import itp341.pham.dat.bloodr.MainActivity;
import itp341.pham.dat.bloodr.Model.User;
import itp341.pham.dat.bloodr.R;

public class LoginFragment extends Fragment {
    // debug tool
    private static final String TAG = LoginFragment.class.getSimpleName();

    // all widgets here -------------------------------
    private EditText etPhoneNumber, etPassword;
    private Button btnSignin;
    private TextView tvRegister;
    // end widgets here -------------------------------

    // all internal objects ---------------------------
    Firebase mFirebaseUsersRef;
    // end internal objects ---------------------------


    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get ref to firebase
        Firebase mainRef = ((BloodrApplication) getActivity().getApplication())
                .getFirebaseReference();
        mFirebaseUsersRef = mainRef.child(FirebaseSchema.TABLE_USERS.NAME); // load get user table
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        // all findview here ------------------------------------------------
        tvRegister = (TextView) v.findViewById(R.id.tvRegister);
        btnSignin = (Button) v.findViewById(R.id.btnSignIn);
        etPassword = (EditText) v.findViewById(R.id.etPassword);
        etPhoneNumber = (EditText) v.findViewById(R.id.etPhoneNumber);
        // end findview here ------------------------------------------------

        //all setup internal objects ------------------------------------------
        User user = new User("123456788");
        user.setName_("Tommy");
        user.setPassword_("123");
        mFirebaseUsersRef.child(user.getPhone()).setValue(user);

        etPassword.setText(""); // clear password
        //end setup internal objects ------------------------------------------

        // check if user already logged in, if so, go directly to main activity
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                LoginActivity.LOGIN_PREFERENCE, Context.MODE_PRIVATE);

        boolean hasUserLoggedin = sharedPreferences.getBoolean(
                LoginActivity.PREF_HAS_USER_LOGGEDIN, false);

        debug("hasUserLoggedin " + hasUserLoggedin);

        if (hasUserLoggedin) { // if user already logged in, go to main activity
            // get user info
            final String username = sharedPreferences.getString(LoginActivity.PREF_LOGGEDIN_USERNAME, "");

            Firebase childFB = mFirebaseUsersRef.child(username);
            childFB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //retrieve user from phone number
                    User user = dataSnapshot.getValue(User.class);

                    if (user.getPassword_() != null) {
                        // tell user that he's logged in
                        Toast.makeText(getContext(),
                                "Welcome back, " + user.getName_(), Toast.LENGTH_SHORT).show();

                        // go to main activity
                        Intent intentMainActivity = new Intent(getActivity(), MainActivity.class);
                        intentMainActivity.putExtra(MainActivity.EXTRA_USER, user);
                        getActivity().startActivityForResult(intentMainActivity,
                                MainActivity.LOGIN_ACTIVITY_CODE);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }

        // check if user already logged in, if so, go directly to main activity


        // all listeners -----------------------------------------------------
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String entered_phone_num = etPhoneNumber.getText().toString();
                final String entered_password = etPassword.getText().toString();

                debug("entered number" + entered_phone_num);
                if (entered_phone_num.length() > 0) {
                    Firebase childFB = mFirebaseUsersRef.child(entered_phone_num);
                    debug("fb path: " + childFB.getPath());
                    childFB.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount() >= 2) { // user exists
                                User user = dataSnapshot.getValue(User.class);
                                debug("user added2");
                                int pw_tries = 10;

                                if (user.getPassword_() != null) { // user has pw
                                    debug("user exists");
                                    if (entered_password.equals(user.getPassword_())) { // login successful
                                        Toast.makeText(getContext(),
                                                "Login successfully", Toast.LENGTH_SHORT).show();

                                        // save preferences ----------------------------------------------------------
                                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                                                LoginActivity.LOGIN_PREFERENCE, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor sharedpref_editor = sharedPreferences.edit();
                                        sharedpref_editor.putBoolean(LoginActivity.PREF_HAS_USER_LOGGEDIN, true);
                                        sharedpref_editor.putString(LoginActivity.PREF_LOGGEDIN_USERNAME, user.getPhone());
                                        sharedpref_editor.commit();
                                        // save preferences ----------------------------------------------------------

                                        // go to main activity
                                        Intent intentMainActivity = new Intent(getActivity(), MainActivity.class);
                                        intentMainActivity.putExtra(MainActivity.EXTRA_USER, user);
                                        getActivity().startActivityForResult(intentMainActivity,
                                                MainActivity.LOGIN_ACTIVITY_CODE);

                                        // clear password field
                                        etPassword.setText("");

                                    } else { // wrong password
                                        Toast.makeText(getContext(),
                                                "Wrong password, please try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else { // user not exists, suggests to go to signup page
                                Toast.makeText(getContext(),
                                        "Phone not exists, please check again or press New User button",
                                        Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                } else { // if user does not type anything in  phone number block
                    Toast.makeText(getContext(),
                            "Please enter phone number",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                Fragment f = (Fragment) new NewUserFragment();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fraglogin, f, "New_User");
                ft.addToBackStack(null);
                debug("fragment added");
                ft.commit();
            }
        });
        // end listeners -----------------------------------------------------

        return v;


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void debug(String s) {
        Log.v(TAG, "ddebug LoginFragment.java: " + s);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
