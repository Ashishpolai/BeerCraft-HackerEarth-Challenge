package com.example.asis.thoughtworksproj;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by ashishkumarpolai on 7/14/2017.
 */

public class MyUtils {

    private static Animation fadeInAnime;
    private static final String SHARED_PREFERENCE_KEY = "beercraftsharedpreferenceashish007";
    private static final String SHARED_PREFERENCE_KEY_NUMBER = "beercraftsharedpreferenceashish007Number";

    public static void startAnimate(View animationView, int anime, Context instance)
    {
        fadeInAnime = AnimationUtils.loadAnimation(instance, anime);
        fadeInAnime.setFillAfter(true);
        animationView.startAnimation(fadeInAnime);
        fadeInAnime = null;
    }

    public static void hideKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void saveCartValuesSets(Context context, Set<String> mSet) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet(SHARED_PREFERENCE_KEY, mSet);
        editor.putInt(SHARED_PREFERENCE_KEY_NUMBER, mSet.size());
        editor.apply();
    }

    public static Set<String> getCartValuesSets(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getStringSet(SHARED_PREFERENCE_KEY, new HashSet<String>());
    }

    public static Integer getNoOfIncart(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(SHARED_PREFERENCE_KEY_NUMBER, 0);
    }
}
