package com.app.letuscs.localstorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Map;

public class SharedPreferenceUtil {

    private static SharedPreferences sharedPreferences = null;

    private static SharedPreferences.Editor editor = null;


    public static void init(Context mcontext) {

        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(mcontext);
            editor = sharedPreferences.edit();
        }

    }

    public static void clear() {
        editor.clear();
        save();
    }

    /**
     * Puts new Key and its Values into SharedPreference map.
     *
     * @param key
     * @param value
     */
    public static void putValue(String key, String value) {
        editor.putString(key, value);
        save();
    }

    /**
     * Puts new Key and its Values into SharedPreference map.
     *
     * @param key
     * @param value
     *//*
    public static void putValue(String key, int value) {
        editor.putInt(key, value);
        save();
    }

    *//**
     * Puts new Key and its Values into SharedPreference map.
     *
     * @param key
     * @param value
     *//*
    public static void putValue(String key, long value) {
        editor.putLong(key, value);
        save();
    }

    *//**
     * Puts new Key and its Values into SharedPreference map.
     *
     * @param key
     * @param value
     *//*
    public static void putValue(String key, boolean value) {
        editor.putBoolean(key, value);
        save();
    }*/

    /**
     * saves the values from the editor to the SharedPreference
     */
    public static void save() {
        editor.commit();
        editor.apply();
    }

    /**
     * returns a values associated with a Key default value ""
     *
     * @return String
     */
    public static String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    /**
     * returns a values associated with a Key default value -1
     *
     * @return String
     */
    public static int getInt(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    /**
     * returns a values associated with a Key default value -1
     *
     * @return String
     */
    public static long getLong(String key, long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }

    /**
     * returns a values associated with a Key default value false
     *
     * @return String
     */
    public static boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    /**
     * Checks if key is exist in SharedPreference
     *
     * @param key
     * @return boolean
     */
    public static boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    /**
     * returns map of all the key value pair available in SharedPreference
     *
     * @return Map<String, ?>
     */
    public static Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }
}
