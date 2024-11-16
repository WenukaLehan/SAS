package com.wlghost.sas.Helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.wlghost.sas.Activity.login_activity;

public class SessionManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LoginSession";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String KEY_EMAIL = "userEmail";
    private static final String KEY_NAME = "userName";
    private static final String KEY_USERID = "userId";
    private static final String KEY_USERTYPE = "userType";

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String email, String name, String userId, String userType) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_USERID, userId);
        editor.putString(KEY_USERTYPE, userType);


        editor.commit();
    }


    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public String getUserEmail() {
        return pref.getString(KEY_EMAIL, null);
    }

    public String getUserName() {
        return pref.getString(KEY_NAME, null);
    }

    public String getUserId() {
        return pref.getString(KEY_USERID, null);
    }
    public String getUserType() {
        return pref.getString(KEY_USERTYPE, null);
    }


    public void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(context, login_activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
