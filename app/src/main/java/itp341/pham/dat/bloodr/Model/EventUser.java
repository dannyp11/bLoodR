package itp341.pham.dat.bloodr.Model;

/**
 * Created by deptr on 5/7/2016.
 */
public class EventUser {
    // TODO: 5/7/2016 user object of each event, used for calling users

    private String name_, phone_, email_;
    private boolean ok2call_;
    private String zipcode_;



    public EventUser(String phone_) {
        this.phone_ = phone_;
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

    public boolean isOk2call_() {
        return ok2call_;
    }

    public void setOk2call_(boolean ok2call_) {
        this.ok2call_ = ok2call_;
    }

    public String getZipcode_() {
        return zipcode_;
    }

    public void setZipcode_(String zipcode_) {
        this.zipcode_ = zipcode_;
    }

    public String getPhone_() {
        return phone_;
    }
}
