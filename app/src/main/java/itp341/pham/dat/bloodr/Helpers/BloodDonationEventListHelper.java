package itp341.pham.dat.bloodr.Helpers;

import android.app.Activity;
import android.location.Address;
import android.location.Location;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import itp341.pham.dat.bloodr.BloodrApplication;
import itp341.pham.dat.bloodr.Model.BloodDonationEvent;

/**
 * Created by deptr on 5/5/2016.
 */
public class BloodDonationEventListHelper {
    // all internal data ----------------------------------------
    private Firebase mFirebaseEventsRef;

    private Activity mActivity;

    private ArrayList<BloodDonationEvent> mListEvent;
    // end internal data ----------------------------------------

    public BloodDonationEventListHelper(Activity activity) {
        mActivity = activity;

        //get ref to firebase
        Firebase mainRef = ((BloodrApplication) mActivity.getApplication())
                .getFirebaseReference();
        mFirebaseEventsRef = mainRef.child(FirebaseSchema.TABLE_EVENTS.NAME); // load get event table

        // add all events to mListEvent
//        mFirebaseEventsRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.getChildrenCount()>1) { // valid event
////                    BloodDonationEvent event = dataSnapshot.getValue(BloodDonationEvent.class);
////                    mListEvent.add(event);
//                    // TODO: 5/5/2016 implement array adapter
//                }
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
    }

    public void initEvents() {
        BloodDonationEvent event1 = new BloodDonationEvent("Give you Hope");
        event1.setAddress_("2700 Fruitland Ave, Vernon, CA 90058");
        event1.setBanner_url_("https://geo0.ggpht.com/cbk?panoid=SMo157Kb" +
                "F1z576f2d2S2rw&output=thumbnail&cb_cl" +
                "ient=search.TACTILE.gps&thumb=2&w=408&h=256&yaw=1" +
                "80.69791&pitch=0");
        event1.setDate_(new Date(2016, 5, 4, 15, 20));
        event1.setUrl_("http://www.redcross.org/");
        event1.setLogo_url_("http://www.dothan.org/images/pages/" +
                "N217/AmericanRedCross_LOGO2.png");
        event1.setOrganization_name_("American Red Cross");
        event1.setPhone_("(800) 733-2767");
        event1.setDescription_("walk-in & online registration");
        event1.setLocation_(33.995322, -118.222831);
        mFirebaseEventsRef.child(event1.toString()).setValue(event1);

        BloodDonationEvent event2 = new BloodDonationEvent("For the World");
        event2.setAddress_("403 W Adams Blvd, Los Angeles, CA 90007");
        event2.setBanner_url_("https://s3.amazonaws.com/playspacefinder.ka" +
                "boom.org/photos/photos/000/014/047/original/10145-14047.jpg?1378226561");
        event2.setDate_(new Date(2016, 7, 4, 10, 30));
        event2.setUrl_("http://ortho-institute.org/");
        event2.setLogo_url_("http://www.freelargeimages.com/wp-conte" +
                "nt/uploads/2015/05/Hospital_Logo_01.png");
        event2.setOrganization_name_("Orthopaedic Institute for Children");
        event2.setPhone_("(213) 742-1000");
        event2.setDescription_("online registration");
        event2.setLocation_(34.029295, -118.265683);
        mFirebaseEventsRef.child(event2.toString()).setValue(event2);

        if (1==1) {
            BloodDonationEvent event3 = new BloodDonationEvent("Save the People");
            event3.setOrganization_name_("Huntington Hospital");
            event3.setBanner_url_("http://media.merchantcircle.com/150" +
                    "51059/Huntington_oasis_full.jpeg");
            event3.setDate_(new Date(2016, 7, 4, 10, 30));
            event3.setUrl_("http://www.huntingtonhospital.com/Main/Home.aspx");
            event3.setLogo_url_("https://upload.wikimedia.org/wikipedia/e" +
                    "n/4/4f/Iloilo_Mission_Hospital_logo.png");
            event3.setPhone_("(626) 397-5000");
            event3.setDescription_("call for more info");
            event3.setLocation_(34.133729, -118.133331);
            mFirebaseEventsRef.child(event3.toString()).setValue(event3);
        }

        if (1==1) {
            BloodDonationEvent event3 = new BloodDonationEvent("Rescue Me");
            event3.setOrganization_name_("Centinela Medical Center");
            event3.setBanner_url_("http://www.srreesenthilagencies" +
                    ".com/v1/images/slider/bg1.jpg");
            event3.setDate_(new Date(2016, 7, 4, 10, 30));
            event3.setUrl_("http://www.centinelamed.com/");
            event3.setLogo_url_("http://djnrmh.doh.gov.ph" +
                    "/images/Images/LOGoDJNRMH.png");
            event3.setPhone_("(626) 397-5000");
            event3.setDescription_("call for more info");
            event3.setLocation_(33.790936, -118.166962);
            mFirebaseEventsRef.child(event3.toString()).setValue(event3);
        }

        if (1==1) {
            BloodDonationEvent event3 = new BloodDonationEvent("Health Me Please");
            event3.setOrganization_name_("PIH Health");
            event3.setBanner_url_("https://www.bannerhealth.com/NR/rd" +
                    "onlyres/03DB9B32-DFF6-4C2C-B5BF-C92C86AC9007/65610/GoodSam2011.jpg");
            event3.setDate_(new Date(2016, 7, 4, 10, 30));
            event3.setUrl_("http://www.pihhealth.org/");
            event3.setLogo_url_("https://upload.wikimedia.org/wikiped" +
                    "ia/en/f/fd/Indus_Hospital_logo.png");
            event3.setPhone_("(562) 698-0811");
            event3.setDescription_("come join us");
            event3.setLocation_(33.973625,-118.049930);
            mFirebaseEventsRef.child(event3.toString()).setValue(event3);
        }

        // populate more events
        for (int i =0; i< 5; i++){
            mFirebaseEventsRef.child("event " + i + " 2").setValue(event2);
            mFirebaseEventsRef.child("event " + i + " 1").setValue(event1);
        }
    }

    // all getter & setter --------------------------------------------------------
    public static double getLatitude(String location_) {
        double res = 0;

        String lat_str = location_.substring(0, location_.indexOf("||"));
        res = Double.parseDouble(lat_str);

        return res;
    }

    public static double getLongitude(String location_) {
        double res = 0;

        String lon_str = location_.substring(location_.indexOf("||") + 2);
        res = Double.parseDouble(lon_str);

        return res;
    }


    public Firebase getmFirebaseEventsRef() {
        return mFirebaseEventsRef;
    }
    // end getter & setter --------------------------------------------------------
}
