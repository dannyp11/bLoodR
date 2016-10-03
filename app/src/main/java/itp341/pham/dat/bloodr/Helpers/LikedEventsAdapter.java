package itp341.pham.dat.bloodr.Helpers;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import itp341.pham.dat.bloodr.Model.BloodDonationEvent;
import itp341.pham.dat.bloodr.R;

/**
 * Created by deptr on 5/8/2016.
 */
public class LikedEventsAdapter extends ArrayAdapter<BloodDonationEvent> {
    public LikedEventsAdapter(Context context, ArrayList<BloodDonationEvent> list_events) {
        super(context, R.layout.item_bdonation_event, list_events);
        mContext = context;
    }

    private Context mContext;
    private LinearLayout llLayout;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BloodDonationEvent event = (BloodDonationEvent) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_bdonation_event, parent, false);
        }

        // lookup view for data population
        ImageView ivEventLogo = (ImageView) convertView.findViewById(R.id.ivEventItemLogo);
        TextView tvEventName = (TextView) convertView.findViewById(R.id.tvEventItemName);
        TextView tvEventLocationDate = (TextView) convertView.findViewById(R.id.tvEventItemLocationDate);
        TextView tvEventTimeOrganization = (TextView) convertView.findViewById(R.id.tvEventItemTimeOrganization);

        llLayout = (LinearLayout) convertView.findViewById(R.id.layoutEventItem);
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
        Glide.with((Activity) getContext())
                .load(logo)
                .placeholder(R.drawable.blood_donate_icon)
                .centerCrop()
                .into(ivEventLogo);


        return convertView;
    }
}
