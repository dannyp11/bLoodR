package itp341.pham.dat.bloodr.Fragments;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import itp341.pham.dat.bloodr.Helpers.EventAdapter;
import itp341.pham.dat.bloodr.Helpers.LikedEventsAdapter;
import itp341.pham.dat.bloodr.MainActivity;
import itp341.pham.dat.bloodr.Model.BloodDonationEvent;
import itp341.pham.dat.bloodr.Model.User;
import itp341.pham.dat.bloodr.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LikedEventsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LikedEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LikedEventsFragment extends Fragment {
    // debug tool
    private static final String TAG = LikedEventsFragment.class.getSimpleName();

    // all argument names here -------------------------------
    public static final String BUNDLE_SINGLE_EVENT = "bloodr.single_event";
    // end argument names here -------------------------------

    // all widgets here -------------------------------
    private ListView lvEventList;
    // end widgets here -------------------------------

    // all internal objects ---------------------------
    private LikedEventsAdapter mEventAdapter;
    private User    mLoggedinUser;
    // end internal objects ---------------------------

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LikedEventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LikedEventsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LikedEventsFragment newInstance(String param1, String param2) {
        LikedEventsFragment fragment = new LikedEventsFragment();
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
        }

        mLoggedinUser = ((MainActivity) getActivity()).getLoggedInUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_liked_events, container, false);

        // all findview here ------------------------------------------------
        lvEventList = (ListView) v.findViewById(R.id.lvLikedEventList);
        // end findview here ------------------------------------------------

        // setup actionbar -------------------------------------------
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Liked Events");
        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setBackgroundDrawable(new ColorDrawable(
                        getResources().getColor(R.color.colorPrimary)));

        // all setup internal objects ---------------------------------------
        ArrayList<BloodDonationEvent> event_list = mLoggedinUser.getLiked_events_list_();
        // setup eventlist adapter
        mEventAdapter = new LikedEventsAdapter(getContext(), event_list);
        lvEventList.setAdapter(mEventAdapter);
        // end setup internal objects ---------------------------------------


        // all listeners ----------------------------------------------
        lvEventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BloodDonationEvent selected_event = (BloodDonationEvent) parent.getAdapter()
                        .getItem(position);

                FragmentManager fm = getFragmentManager();
                Fragment f = (Fragment) new SingleEventFragment();
                FragmentTransaction ft = fm.beginTransaction();

                // parse event to fragment f
                Bundle bundle = new Bundle();
                bundle.putSerializable(BUNDLE_SINGLE_EVENT, selected_event);
                f.setArguments(bundle);

                // fragment animation


                ft.replace(R.id.fragListDonationEvents, f, MainActivity.FRAG_TAG_SINGLE_EVENT);
                ft.addToBackStack(null);
                debug("fragment added");
                ft.commit();
            }
        });
        // end listeners ----------------------------------------------
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
        Log.v(TAG, "ddebug LikedEventListFragment.java: " + s);
    }
}
