package itp341.pham.dat.bloodr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import itp341.pham.dat.bloodr.Fragments.LoginFragment;

public class LoginActivity extends AppCompatActivity {

    // shared prefs for user login
    public static final String LOGIN_PREFERENCE = "com.itp341.pham.dat.bloodr.login_prefs";
    public static final String PREF_HAS_USER_LOGGEDIN = "com.itp341.pham.dat.bloodr.has_userloggedin";
    public static final String PREF_LOGGEDIN_USERNAME = "com.itp341.pham.dat.bloodr.loggenin_username";
    public static final String PREF_LOGGEDIN_PASSWORD = "com.itp341.pham.dat.bloodr.loggenin_password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragLogin = fm.findFragmentById(R.id.fraglogin);
        if (fragLogin == null){
            fragLogin = LoginFragment.newInstance();
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fraglogin, fragLogin);
        ft.commit();
    }
}
