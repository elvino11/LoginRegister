package com.example.loginregister.data;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreference {

    private static final String PREFS_NAME = "user_pref";

    private static final String USERNAME = "USERNAME";
    private static final String NAME = "NAME";
    private static final String ID = "ID";

    private static final String ISLOGIN = "ISLOGIN";

    private final SharedPreferences preferences;

    public UserPreference(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setUser(User user) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(NAME, user.name);
        editor.putString(USERNAME, user.username);
        editor.putString(ID, user.id);
        editor.putBoolean(ISLOGIN, user.isLogin);
        editor.apply();
    }

    public User getUser() {
        User user = new User();
        user.setName(preferences.getString(NAME, ""));
        user.setUsername(preferences.getString(USERNAME, ""));
        user.setId(preferences.getString(ID, ""));
        user.setLogin(preferences.getBoolean(ISLOGIN, false));

        return user;

    }
}
