package itp341.pham.dat.bloodr.Fragments;


import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.Firebase;

import java.util.ArrayList;

import itp341.pham.dat.bloodr.Helpers.BloodDonationEventListHelper;
import itp341.pham.dat.bloodr.Helpers.EventAdapter;
import itp341.pham.dat.bloodr.MainActivity;
import itp341.pham.dat.bloodr.Model.BloodDonationEvent;
import itp341.pham.dat.bloodr.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class EventListFragment extends Fragment {
    // debug tool
    private static final String TAG = EventListFragment.class.getSimpleName();

    // all argument names here -------------------------------
    public static final String BUNDLE_SINGLE_EVENT = "bloodr.single_event";
    // end argument names here -------------------------------

    // all widgets here -------------------------------
    private ListView lvEventList;
    // end widgets here -------------------------------

    // all internal objects ---------------------------
    private BloodDonationEventListHelper mEventHelper;
    private ArrayList<BloodDonationEvent> mListEvents;
    private EventAdapter mEventAdapter;
    private Firebase mEventsRef;
    // end internal objects ---------------------------


    private OnFragmentInteractionListener mListener;

    public EventListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_event_list, container, false);

        // all findview here ------------------------------------------------
        lvEventList = (ListView) v.findViewById(R.id.lvEventList);
        // end findview here ------------------------------------------------

        // setup actionbar -------------------------------------------
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Events");
        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setBackgroundDrawable(new ColorDrawable(
                        getResources().getColor(R.color.colorPrimary)));

        // all setup internal objects ---------------------------------------
        mEventHelper = new BloodDonationEventListHelper(getActivity());
//        mEventHelper.initEvents(); // add sample events
        mEventsRef = mEventHelper.getmFirebaseEventsRef();

        // setup eventlist adapter
        mEventAdapter = new EventAdapter(getActivity(),
                BloodDonationEvent.class,
                R.layout.item_bdonation_event,
                mEventsRef,
                getContext()
        );
        lvEventList.setAdapter(mEventAdapter);

        // end setup internal objects ---------------------------------------

        // all listeners ----------------------------------------------------
        // TODO: 5/5/2016 onclick event to view single event. done
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
        // TODO: 5/5/2016 implement user show interest in event 
        // end listeners ----------------------------------------------------

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
        Log.v(TAG, "ddebug EventListFragment.java: " + s);
    }
}
