package com.example.mediaplayerapp.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

public class LanguageConfig {
    public static ContextWrapper changeLanguage(Context context, String languageCode) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale systemLocale;
        systemLocale = configuration.getLocales().get(0);
        if (!languageCode.equals("") && !systemLocale.getLanguage().equals(languageCode)) {
            Locale locale = new Locale(languageCode);
            Locale.setDefault(locale);
            configuration.setLocale(locale);
            context = context.createConfigurationContext(configuration);
        }
        return new ContextWrapper(context);
    }
}
