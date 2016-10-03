package itp341.pham.dat.bloodr.Model;

import android.location.Location;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
 * Created by deptr on 4/20/2016.
 */
public class User implements Serializable {


    // all private data -----------------------------------------------------
    private String name_, email_, phone_, password_;
    private Date birth_day_;
    private String blood_type_;
    private boolean ok_to_call_ = false;
    private Location location_;
    private String zipcode_;

    private ArrayList<BloodDonationEvent> liked_events_list_ = new ArrayList<BloodDonationEvent>();
    private HashMap<String, BloodDonationEvent> liked_events_map_ = new HashMap<>();
    // end private data -----------------------------------------------------


    public User() {
    }

    public User(String phone) {
        this.phone_ = phone;
    }

    @Override
    public String toString() {
        return phone_;
    }

    public void addLikedEvent(BloodDonationEvent event) {
        if (!liked_events_list_.contains(event))
            liked_events_list_.add(event);

        if (!liked_events_map_.containsKey(event.toString())){
            liked_events_map_.put(event.toString(), event);
        }
    }

    public boolean hasLikedEvent(BloodDonationEvent event) {
//        return (liked_events_list_.contains(event));
        return (liked_events_map_.containsKey(event.toString()));
    }

    public void removeLikedEvent(BloodDonationEvent event) {
        if (hasLikedEvent(event)) {
            liked_events_list_.remove(event);
            liked_events_map_.remove(event.toString());
        }
    }

    // all getters & setters ---------------------------------------
    public ArrayList<BloodDonationEvent> getLiked_events_list_() {
        return liked_events_list_;
    }

    public String getZipcode_() {
        return zipcode_;
    }

    public void setZipcode_(String zipcode_) {
        this.zipcode_ = zipcode_;
    }

    public String getName_() {
        return name_;
    }

    public void setName_(String name_) {
        this.name_ = name_;
    }

    public String getEmail_() {
        return email_;
    }

    public void setEmail_(String email_) {
        this.email_ = email_;
    }

    public String getPhone() {
        return phone_;
    }

    public void setPhone(String phone) {
        this.phone_ = phone;
    }

    public String getPassword_() {
        return password_;
    }

    public void setPassword_(String password_) {
        this.password_ = password_;
    }

    public Date getBirth_day_() {
        return birth_day_;
    }

    public void setBirth_day_(Date birth_day_) {
        this.birth_day_ = birth_day_;
    }

    public String getBlood_type_() {
        return blood_type_;
    }

    public void setBlood_type_(String blood_type_) {
        this.blood_type_ = blood_type_;
    }

    public boolean isOk_to_call_() {
        return ok_to_call_;
    }

    public void setOk_to_call_(boolean ok_to_call_) {
        this.ok_to_call_ = ok_to_call_;
    }

    public Location getLocation_() {
        return location_;
    }

    public void setLocation_(Location location_) {
        this.location_ = location_;
    }
    // all getters & setters ---------------------------------------
}
