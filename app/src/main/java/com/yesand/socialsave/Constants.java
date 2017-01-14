package com.yesand.socialsave;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Stephen on 1/12/2017.
 */

public class Constants {
    static final String NESSIE_ID = "nessieId";
    static final String GOAL = "goal";
    static final String NAME = "name";
    static final String LAST_SCORE = "score";
    static final String TOTAL_SAVINGS = "totalSavings";
    static final String HISTORY = "scoreHistory";
    static final String GROUP_ID = "groupId";
    static final boolean DEBUG_MODE = false;

    public static void error(String msg, boolean showToUser, Context context) {
        if (DEBUG_MODE || showToUser)
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        System.out.println("User Thrown Error: " + msg);
    }
}
