package itp341.pham.dat.bloodr.Helpers;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import itp341.pham.dat.bloodr.Model.BloodDonationEvent;
import itp341.pham.dat.bloodr.R;

/**
 * Created by deptr on 5/5/2016.
 * used to populate bloodr event list in main activity
 */
public class EventAdapter extends FirebaseListAdapter<BloodDonationEvent> {

    private Context mContext;
    private LinearLayout llLayout;

    public EventAdapter(Activity activity, Class<BloodDonationEvent> modelClass,
                        int modelLayout, Firebase ref, Context context) {
        super(activity, modelClass, modelLayout, ref);
        mContext = context;
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_bdonation_event, parent, false);
//        }
//
//        // lookup view for data population
//        ImageView ivEventLogo = (ImageView) convertView.findViewById(R.id.ivEventItemLogo);
//        TextView tvEventName = (TextView) convertView.findViewById(R.id.tvEventItemName);
//        TextView tvEventLocationDate = (TextView) convertView.findViewById(R.id.tvEventItemLocationDate);
//        TextView tvEventTimeOrganization = (TextView) convertView.findViewById(R.id.tvEventItemTimeOrganization);
//
//        // set image and data from event --------------------------------------------
//        BloodDonationEvent event = getItem(position);
//
//        final String logo = event.getLogo_url_();
//        final Location location = event.getLocation_();
//        final String organization = event.getOrganization_name_();
//        final String event_name = event.getEvent_name_();
//
//        // get city name
//        Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
//        String city = new String("");
//
//        List<Address> addresses = null;
//        try {
//            addresses = gcd.getFromLocation(location.getLatitude(),
//                    location.getLongitude(), 1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (addresses.size() > 0){ // found city
//            city = addresses.get(0).getLocality();
//            debug("city: " + city);
//        }
//
//
//        // done set image and data from event ----------------------------------------
//
//        return convertView;
//    }

    @Override
    protected void populateView(View view, BloodDonationEvent event, int i) {
        // lookup view for data population
        ImageView ivEventLogo = (ImageView) view.findViewById(R.id.ivEventItemLogo);
        TextView tvEventName = (TextView) view.findViewById(R.id.tvEventItemName);
        TextView tvEventLocationDate = (TextView) view.findViewById(R.id.tvEventItemLocationDate);
        TextView tvEventTimeOrganization = (TextView) view.findViewById(R.id.tvEventItemTimeOrganization);

        llLayout = (LinearLayout) view.findViewById(R.id.layoutEventItem);
        // set image and data from event --------------------------------------------
        final String logo = event.getLogo_url_();
        final String banner = event.getBanner_url_();
        final String location = event.getLocation_();
        final String organization = event.getOrganization_name_();
        final String event_name = event.getEvent_name_();
        final Date date = event.getDate_();

        // set event name
        tvEventName.setText(event_name);

        // set city and date
        // get city name
        String city = "Los Angeles";
        Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(
                    BloodDonationEventListHelper.getLatitude(location),
                    BloodDonationEventListHelper.getLongitude(location),
                    1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0) {
            city = addresses.get(0).getLocality();
        }

        String mm = String.format("%02d", date.getMonth());
        String dd = String.format("%02d", date.getDate());
        String yyyy = String.format("%04d", date.getYear());

        tvEventLocationDate.setText(city + " - "
                + mm + "/" + dd + "/" + yyyy);

        // set time and organization name
        String ampm = (date.getHours() > 12) ? "pm" : "am";
        tvEventTimeOrganization.setText(
                date.getHours() % 12 + ":"
                        + date.getMinutes() + ampm + " - "
                        + organization
        );

        // set logo
        Glide.with(mActivity)
                .load(logo)
                .placeholder(R.drawable.blood_donate_icon)
                .centerCrop()
                .into(ivEventLogo);

        // set background banner
//        Glide.with(mActivity)
//                .load(banner)
//                .into(new SimpleTarget<GlideDrawable>() {
//                    @Override
//                    public void onResourceReady(GlideDrawable resource,
//                                                GlideAnimation<? super GlideDrawable> glideAnimation) {
//                        resource.setAlpha(50);
//
////                        AbsListView.LayoutParams params = (AbsListView.LayoutParams)
////                                llLayout.getLayoutParams();
////                        int height = params.height;
//                        llLayout.setBackground(resource);
////                        params.height = height;
////                        llLayout.setLayoutParams(params);
//
//                    }
//                });


        // done set image and data from event ----------------------------------------
    }

    // debug tool
    private static final String TAG = EventAdapter.class.getSimpleName();

    private void debug(String s) {
        Log.v(TAG, "ddebug EventAdapter.java: " + s);
    }
}
