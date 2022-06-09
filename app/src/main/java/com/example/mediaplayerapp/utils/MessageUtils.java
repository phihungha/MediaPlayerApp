package com.example.mediaplayerapp.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.StringRes;

public class MessageUtils {
    public static void makeShortToast(Context context, @StringRes int stringResId) {
        Toast.makeText(context, stringResId, Toast.LENGTH_SHORT).show();
    }

    public static void makeShortToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void displayError(Context context, String logTag, String message) {
        Log.e(logTag, message);
        makeShortToast(context, message);
    }
}
