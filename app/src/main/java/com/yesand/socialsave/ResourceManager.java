package com.yesand.socialsave;

import android.provider.ContactsContract;

import com.google.firebase.database.FirebaseDatabase;
import com.reimaginebanking.api.nessieandroidsdk.requestclients.NessieClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Stephen on 1/12/2017.
 */

public class ResourceManager {
    private static final String NESSIE_KEY = "df8d1057bebd8ef7053f2e96d0a52be0";
    private static final String FIREBASE_URL = "https://socialsave-822d9.firebaseio.com/";

    private static final SimpleDateFormat NESSIE_DATE_FORMAT = new SimpleDateFormat("yy-MM-dd");

    private static DatabaseReference CURR_USER = FirebaseDatabase.getInstance().getReference().
            child("users").child("Serina_me-com"); //hardcode default

    public static String getNessieKey(){
        return NESSIE_KEY;
    }
    public static String getFirebaseUrl(){
        return FIREBASE_URL;
    }

    public static NessieClient getNessieClient(){
        return NessieClient.getInstance(getNessieKey());
    }

    public static SimpleDateFormat getNessieDateFormat(){
        return NESSIE_DATE_FORMAT;
    }

    public static DatabaseReference getCurrUser() { return CURR_USER; }

    public static void setCurrUser(DatabaseReference user) { CURR_USER = user; }

    public static Calendar getStartOfThisWeek(){
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

        return cal;
    }

}
