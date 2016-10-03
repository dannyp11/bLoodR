package itp341.pham.dat.bloodr.Model;

import android.location.Address;
import android.location.Location;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by deptr on 5/5/2016.
 */
public class BloodDonationEvent implements Serializable {
    // all private data -----------------------------------------------------
    private String event_name_, organization_name_, phone_;
    private String address_;
    private String location_;
    private Date date_;
    private String url_, logo_url_, banner_url_;
    private String description_;
    // end private data -----------------------------------------------------

    public BloodDonationEvent() {
    }

    @Override
    public String toString() {
        String res = this.event_name_ + " at " + date_.toString();
        return res;
    }


    public BloodDonationEvent(String event_name_) {
        this.event_name_ = event_name_;
        location_ = "0||0";
    }

    // all getters & setters ---------------------------------------

    public String getEvent_name_() {
        return event_name_;
    }

    public void setEvent_name_(String event_name_) {
        this.event_name_ = event_name_;
    }

    public String getOrganization_name_() {
        return organization_name_;
    }

    public void setOrganization_name_(String organization_name_) {
        this.organization_name_ = organization_name_;
    }

    public String getPhone_() {
        return phone_;
    }

    public void setPhone_(String phone_) {
        this.phone_ = phone_;
    }

    public String getAddress_() {
        return address_;
    }

    public void setAddress_(String address_) {
        this.address_ = address_;
    }

    public Date getDate_() {
        return date_;
    }

    public void setDate_(Date date_) {
        this.date_ = date_;
    }

    public String getUrl_() {
        return url_;
    }

    public void setUrl_(String url_) {
        this.url_ = url_;
    }

    public String getLogo_url_() {
        return logo_url_;
    }

    public void setLogo_url_(String logo_url_) {
        this.logo_url_ = logo_url_;
    }

    public String getBanner_url_() {
        return banner_url_;
    }

    public void setBanner_url_(String banner_url_) {
        this.banner_url_ = banner_url_;
    }

    public String getDescription_() {
        return description_;
    }

    public void setDescription_(String desciption_) {
        this.description_ = desciption_;
    }

    public void setLocation_(double lat, double lon) {
        location_ = Double.toString(lat) + "||" + Double.toString(lon);
    }

    public String getLocation_() {
        return location_;
    }

    // end getters & setters ---------------------------------------
}
