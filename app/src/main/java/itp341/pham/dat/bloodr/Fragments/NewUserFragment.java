package itp341.pham.dat.bloodr.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.awt.font.TextAttribute;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import itp341.pham.dat.bloodr.BloodrApplication;
import itp341.pham.dat.bloodr.Helpers.FirebaseSchema;
import itp341.pham.dat.bloodr.Model.User;
import itp341.pham.dat.bloodr.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewUserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class NewUserFragment extends Fragment{
    // debug tool
    private static final String TAG = NewUserFragment.class.getSimpleName();

    // all widgets here -------------------------------
    private EditText etPhoneNumber, etPassword, etPassword2, etName;
    private EditText etEmail, etBirthday, etZipCode;
    private Spinner spinnerBloodType;
    private CheckBox cbOk2Call;
    private TextView btnClear;
    private Button btnRegister;
    private ImageView ivCheckUserName;
    private ImageButton btnOk2CallHelp;
    // end widgets here -------------------------------

    // all internal objects ---------------------------
    Firebase mFirebaseUsersRef;

    boolean isValidPhoneNumber = false;
    // end internal objects ---------------------------


    private OnFragmentInteractionListener mListener;

    public NewUserFragment() {
        // Required empty public constructor
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
        View v = inflater.inflate(R.layout.fragment_new_user, container, false);

        // all findview here ------------------------------------------------
        etBirthday = (EditText) v.findViewById(R.id.etNewUserBirthday);
        etName = (EditText) v.findViewById(R.id.etNewUserName);
        etPassword = (EditText) v.findViewById(R.id.etNewUserPassword);
        etPassword2 = (EditText) v.findViewById(R.id.etNewUserPassword2);
        etEmail = (EditText) v.findViewById(R.id.etNewUserEmail);
        etZipCode = (EditText) v.findViewById(R.id.etNewUserZipCode);

        etPhoneNumber = (EditText) v.findViewById(R.id.etNewUserPhoneNumber);
        ivCheckUserName = (ImageView) v.findViewById(R.id.ivNewUserCheckPhone);

        btnClear = (TextView) v.findViewById(R.id.btnNewUserClear);
        btnRegister = (Button) v.findViewById(R.id.btnNewUserRegister);

        cbOk2Call = (CheckBox) v.findViewById(R.id.cbNewUserOk2Call);
        btnOk2CallHelp = (ImageButton) v.findViewById(R.id.ivNewUserOk2CallHint);

        spinnerBloodType = (Spinner) v.findViewById(R.id.spinnerNewUserBloodType);
        // end findview here ------------------------------------------------

        //all setup internal objects ------------------------------------------

        //end setup internal objects ------------------------------------------

        // all listeners -----------------------------------------------------
        // ok2call hint button
        btnOk2CallHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),
                        R.string.txtOk2CallHint,
                        Toast.LENGTH_SHORT).show();
            }
        });

        // perform phone check, show valid or invalid imageview next to it & toast msg
        etPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                checkValidPhoneNumber();
                if (!hasFocus && etPhoneNumber.length() > 0) { // focus is out of etPhoneNumber
//                    debug("passed check phone number, isvalid bool: " + isValidPhoneNumber);
                    if (isValidPhoneNumber) { // phone is ok to register
                        ivCheckUserName.setBackground(v.getResources()
                                .getDrawable(R.drawable.ok_icon));
                        ivCheckUserName.setVisibility(View.VISIBLE);
                    } else {// phone is not ok to register
                        ivCheckUserName.setBackground(v.getResources()
                                .getDrawable(R.drawable.fail_icon));
                        ivCheckUserName.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(),
                                R.string.txtInvalidUsername,
                                Toast.LENGTH_SHORT).show();
                    }
                } else { // user focus on other things
                    ivCheckUserName.setVisibility(View.GONE);
                }
            }
        });

        // check if passwords match
        etPassword2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !hasMatchedPassword()) {
                    Toast.makeText(getContext(),
                            R.string.txtPasswordNotMatch,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // check if Zipcode is valid
        etZipCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !(etZipCode.getText().toString().length() == 5))
                    Toast.makeText(getContext(),
                            R.string.txtInvalidZipCode,
                            Toast.LENGTH_SHORT).show();
            }
        });

        // check if email is valid
        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !hasValidEmail()){
                    Toast.makeText(getContext(),
                            R.string.txtInvalidEmail,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidUser()) {
                    // main body code here
                    // create user then add to firebase

                    final String phone = etPhoneNumber.getText().toString();
                    final String password = etPassword.getText().toString();
                    final String email = etEmail.getText().toString();
                    final String name = etName.getText().toString();
                    final Date birthday = getDate(etBirthday.getText().toString());
                    final String location = etZipCode.getText().toString();
                    final boolean ok2call = cbOk2Call.isEnabled();
                    final String bloodtype = spinnerBloodType.getSelectedItem().toString();

                    User user = new User(phone);
                    user.setName_(name);
                    user.setPassword_(password);
                    user.setBlood_type_(bloodtype);
                    user.setBirth_day_(birthday);
                    user.setEmail_(email);
                    user.setZipcode_(location);
                    user.setOk_to_call_(ok2call);

                    mFirebaseUsersRef.child(user.getPhone()).setValue(user);

                    // go to sign in page and toast success registration
                    Toast.makeText(getContext(),
                            R.string.txtSuccessRegister,
                            Toast.LENGTH_SHORT).show();

                    FragmentManager fm = getFragmentManager();
                    if (fm.getBackStackEntryCount() > 0) { // popback to previous fragment
                        //Log.i("MainActivity", "popping backstack");
                        fm.popBackStack();
                    }

                }
            }
        });

        // clear all forms
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPhoneNumber.setText("");
                etZipCode.setText("");
                etBirthday.setText("");
                etName.setText("");
                etEmail.setText("");
                etPassword.setText("");
                etPassword2.setText("");
                cbOk2Call.setChecked(false);
                spinnerBloodType.setSelection(0);

                ivCheckUserName.setVisibility(View.GONE);
            }
        });

        // end listeners -----------------------------------------------------

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void debug(String s) {
        Log.v(TAG, "ddebug NewUserFragment.java: " + s);
    }

    // Check if input hase valid phone number, password, and others
    boolean isValidUser() {
        boolean res = true;

        // Phone number lookup
        checkValidPhoneNumber();
        res &= isValidPhoneNumber;
        res &= (etPhoneNumber.length() > 0);

        // Password match
        res &= hasMatchedPassword();

        // Email check
        res &= hasValidEmail();

        // Zipcode check
        res &= (etZipCode.getText().toString().length() == 5);

        return res;
    }

    // return true if email is valid format
    boolean hasValidEmail() {
        boolean res = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = etEmail.getText().toString();
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            res = true;
        }

        return res;
    }


    // return true if password fields are not empty and confirm pw matches pw
    boolean hasMatchedPassword() {
        boolean res = true;

        String pw1 = etPassword.getText().toString();
        String pw2 = etPassword2.getText().toString();
        res &= (pw1.length() > 0 && pw1.equals(pw2));

        return res;
    }

    // return true if entered phone number hasn't been registered
    void checkValidPhoneNumber() {
        final String entered_phone_number = etPhoneNumber.getText().toString();
//        debug("entered number" + entered_phone_number);

        if (entered_phone_number.length() > 0) { // user typed something
            Firebase childFB = mFirebaseUsersRef.child(entered_phone_number);
//            debug("fb path: " + childFB.getPath());
            childFB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() >= 2) { // user exists in database
                        isValidPhoneNumber = false;
//                        debug("user exists in database");
                    } else { // user not exists in database
                        isValidPhoneNumber = true;
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        } else { // user typed nothing
            isValidPhoneNumber = false;
        }
    }

    // return Date object of datestring, format must be "dd/mm/yyyy"
    Date getDate(String datestring){
        Date res = null;

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            res = formatter.parse(datestring);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return res;
    }
}
