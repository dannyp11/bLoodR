package itp341.pham.dat.bloodr.Fragments;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;

//GMap APIs
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import itp341.pham.dat.bloodr.Helpers.BloodDonationEventListHelper;
import itp341.pham.dat.bloodr.MainActivity;
import itp341.pham.dat.bloodr.Model.BloodDonationEvent;
import itp341.pham.dat.bloodr.Model.User;
import itp341.pham.dat.bloodr.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SingleEventFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SingleEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SingleEventFragment extends Fragment implements OnMapReadyCallback {

    // debug tool
    private static final String TAG = SingleEventFragment.class.getSimpleName();

    // all widgets here -------------------------------
    private TextView tvEventname, tvOrganization, tvTimeDate, tvAddress, tvUrl, tvDescription;
    private ImageView ivBanner, ivLogo;
    private GoogleMap mMap;
    private SupportMapFragment fragMap;

    private ImageButton btnCall, btnInternet, btnLike;

    ScrollView mainScrollView;
    // end widgets here -------------------------------

    // all internal objects ---------------------------
    Firebase mFirebaseEventsRef;
    private BloodDonationEvent mBloodDonationEvent;
    // end internal objects ---------------------------

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SingleEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SingleEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SingleEventFragment newInstance(String param1, String param2) {
        SingleEventFragment fragment = new SingleEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

            // get selected Event
            mBloodDonationEvent = (BloodDonationEvent) getArguments()
                    .getSerializable(EventListFragment.BUNDLE_SINGLE_EVENT);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_single_event, container, false);

        // all findview here ------------------------------------------------
        fragMap = (SupportMapFragment) getChildFragmentManager().
                findFragmentById(R.id.fragGoogleMap);
        mainScrollView = (ScrollView) v.findViewById(R.id.svSingleEvent);

        tvEventname = (TextView) v.findViewById(R.id.tvSingleEventName);
        tvOrganization = (TextView) v.findViewById(R.id.tvSingleEventOrganiaztion);
        tvUrl = (TextView) v.findViewById(R.id.tvSingleEventUrl);
        tvTimeDate = (TextView) v.findViewById(R.id.tvSingleEventTimeDate);
        tvAddress = (TextView) v.findViewById(R.id.tvSingleEventAddress);
        tvDescription = (TextView) v.findViewById(R.id.tvSingleEventDescription);

        ivBanner = (ImageView) v.findViewById(R.id.ivEventBanner);
        ivLogo = (ImageView) v.findViewById(R.id.ivSingleEventLogo);

        btnCall = (ImageButton) v.findViewById(R.id.btnCall);
        btnInternet = (ImageButton) v.findViewById(R.id.btnWebsite);
        btnLike = (ImageButton) v.findViewById(R.id.btnShowInterest);
        // end findview here ------------------------------------------------

        // setup actionbar -------------------------------------------
        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setTitle(mBloodDonationEvent.getEvent_name_());

        //all setup internal objects ------------------------------------------
        fragMap.getMapAsync(this);

        // all enable gmap move inside fragment-----------------------------------
        ImageView transparentImageView = (ImageView) v.findViewById(R.id.iv_transparent);
        transparentImageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });
        // end enable gmap move inside fragment-----------------------------------

        // populate data on layout --------------
        // set banner
        Glide.with(getActivity())
                .load(mBloodDonationEvent.getBanner_url_())
                .centerCrop()
                .into(ivBanner);

        // set logo
        Glide.with(getActivity())
                .load(mBloodDonationEvent.getLogo_url_())
                .placeholder(R.drawable.blood_donate_icon)
                .centerCrop()
                .into(ivLogo);

        // event name
        tvEventname.setText(mBloodDonationEvent.getEvent_name_());
        tvEventname.setVisibility(View.GONE);

        // time & date ------------------------------
        final Date date = mBloodDonationEvent.getDate_();
        final String mm = String.format("%02d", date.getMonth());
        final String dd = String.format("%02d", date.getDate());
        final String yyyy = String.format("%04d", date.getYear());

        final String ampm = (date.getHours() > 12) ? "pm" : "am";
        final String hh = String.format("%02d", date.getHours() % 12);
        final String min = String.format("%02d", date.getMinutes());

        final String timenDate = hh + ":" + min + ampm + " - " + mm + "/" + dd + "/" + yyyy;
        tvTimeDate.setText(timenDate);

        // address ---------------------------------------
        final String location = mBloodDonationEvent.getLocation_();
        final double lat = BloodDonationEventListHelper.getLatitude(location);
        final double lon = BloodDonationEventListHelper.getLongitude(location);

        Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0) {
            final String address = addresses.get(0).getAddressLine(0);
            final String city = addresses.get(0).getLocality();
            final String state = addresses.get(0).getAdminArea();
            final String zip_code = addresses.get(0).getPostalCode();
            final String addressTxt = address + '\n'
                    + city + ", " + state + ", " + zip_code;

            tvAddress.setText(addressTxt);
        }

        // url
        tvUrl.setText(mBloodDonationEvent.getUrl_());
        tvUrl.setMovementMethod(LinkMovementMethod.getInstance());

        //description
        tvDescription.setText(mBloodDonationEvent.getDescription_());

        // organiztion name
        tvOrganization.setText(mBloodDonationEvent.getOrganization_name_());

        // like/unlike button
        MainActivity mainActivity = (MainActivity) getActivity();
        User user = mainActivity.getLoggedInUser();
        if (user.hasLikedEvent(mBloodDonationEvent)){
            // if user already liked this event
            btnLike.setBackground(getResources().getDrawable(R.drawable.unlike));
        }
        //end setup internal objects ------------------------------------------


        // all listeners ------------------------------------------------------

        // go to call app
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                String event_phone_number = mBloodDonationEvent.getPhone_()
                        .replaceAll("\\D+", "").trim();
                callIntent.setData(Uri.parse("tel:" + event_phone_number));
                startActivity(callIntent);
            }
        });

        // go to website
        btnInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentWeb = new Intent();
                intentWeb.setAction(Intent.ACTION_VIEW);
                intentWeb.addCategory(Intent.CATEGORY_BROWSABLE);
                intentWeb.setData(Uri.parse(mBloodDonationEvent.getUrl_()));
                startActivity(intentWeb);
            }
        });

        // show liked
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                User user = mainActivity.getLoggedInUser();
                debug("user is " + user.getName_());
                debug("added " + mBloodDonationEvent.getEvent_name_());

                if (user.hasLikedEvent(mBloodDonationEvent)){
                    // unlike function
                    user.removeLikedEvent(mBloodDonationEvent);
                    Toast.makeText(getContext(),
                            R.string.txtShowUnlikedEvent,
                            Toast.LENGTH_SHORT).show();
                    btnLike.setBackground(getResources().getDrawable(R.drawable.like));
                } else{
                    // like function
                    Toast.makeText(getContext(),
                            R.string.txtShowLikedEvent,
                            Toast.LENGTH_SHORT).show();
                    user.addLikedEvent(mBloodDonationEvent);
                    btnLike.setBackground(getResources().getDrawable(R.drawable.unlike));
                }
                mainActivity.setmLoggedInUser(user);
            }
        });

        // end listeners ------------------------------------------------------

        return v;
    }

    // set map location
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        final String location = mBloodDonationEvent.getLocation_();
        final double lat = BloodDonationEventListHelper.getLatitude(location);
        final double lon = BloodDonationEventListHelper.getLongitude(location);

        LatLng latLng = new LatLng(lat, lon);

        // add marker
        mMap.addMarker(new MarkerOptions()
                .position(latLng).title(mBloodDonationEvent.getEvent_name_()));

        // change focus of map
        CameraUpdate center =
                CameraUpdateFactory.newLatLng(latLng);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);

        // set zoom control
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        // set get location

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
        Log.v(TAG, "ddebug SingleEventFragment.java: " + s);
    }
}
