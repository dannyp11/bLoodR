package itp341.pham.dat.bloodr.Helpers;

/**
 * Created by deptr on 4/20/2016.
 */
public class FirebaseSchema {
    public static final class TABLE_USERS{
        public static final String NAME = "Users";

        // public database key -------------------------------------------------
        public static final String KEY_NAME = "name";
        public static final String KEY_EMAIL = "email";
        public static final String KEY_PHONE = "phone";
        public static final String KEY_PASSWORD = "pw";
        public static final String KEY_BIRTHDAY = "birthday";
        public static final String KEY_BLOOD_TYPE = "blood_type";
        public static final String KEY_LOCATION = "location";
        public static final String KEY_OK_TO_CALL = "ok2call";
        // end public database key ---------------------------------------------
    }

    public static final class TABLE_EVENTS{
        public static final String NAME = "Events";

        // public database key -------------------------------------------------
        public static final String KEY_EVENT_NAME = "event_name";
        public static final String KEY_ORGANIZATION = "organization";
        public static final String KEY_ADDRESS = "address";
        public static final String KEY_LOGO_URL = "logo_url";
        public static final String KEY_URL = "url";
        public static final String KEY_BANNER_URL = "banner_url";
        public static final String KEY_DESCRIPTION = "description";
        public static final String KEY_DATE = "date";
        public static final String KEY_PHONE = "phone";
        // end public database key ---------------------------------------------
    }
}
