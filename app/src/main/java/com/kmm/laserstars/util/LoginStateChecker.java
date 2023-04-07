package com.kmm.laserstars.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.kmm.laserstars.models.UserType;

public class LoginStateChecker {
    private static final String SHARED_NAME = "login";
    private static final String LOGIN_KEY = "login.key";
    private static final String TOKEN_KEY = "token.key";
    private static final String TYPE_KEY = "type.key";

    private static SharedPreferences getShared(Context context) {
        return context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);// /data/data/{package name}/shared/{filename}.xml
    }

    private static SharedPreferences.Editor getSharedEditor(Context context) {
        return getShared(context).edit();
    }

    public static boolean isLoggedIn(Context context) {
        return getShared(context).getBoolean(LOGIN_KEY, false);
    }

    public static void logout(Context context) {
        getSharedEditor(context)
                .remove(TOKEN_KEY)
                .remove(LOGIN_KEY)
                .remove(TYPE_KEY)
                .apply();
    }

    public static void login(Context context, String token, UserType type) {
        getSharedEditor(context)
                .putString(TOKEN_KEY, token)
                .putBoolean(LOGIN_KEY, true)
                .putString(TYPE_KEY, type.name())
                .apply();
    }

    public static UserType getUserType(Context context) {
        String type = getShared(context).getString(TYPE_KEY, UserType.UNKNOWN.name());
        return UserType.valueOf(type);
    }

    public static boolean isUserAdmin(Context context) {
        return getUserType(context) == UserType.admin;
    }

    public static String getToken(Context context) {
        return getShared(context).getString(TOKEN_KEY, null);
    }

}
