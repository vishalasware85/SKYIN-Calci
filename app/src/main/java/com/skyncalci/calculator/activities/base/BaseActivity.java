/*
 * Copyright (C) 2018 Duy Tran Le
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.skyncalci.calculator.activities.base;

import android.animation.Animator;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.kobakei.ratethisapp.RateThisApp;
import com.skyncalci.BuildConfig;
import com.skyncalci.R;
import com.skyncalci.calculator.evaluator.MathEvaluator;
import com.skyncalci.calculator.history.DatabaseHelper;
import com.skyncalci.ncalc.settings.CalculatorSetting;
import com.skyncalci.ncalc.userinterface.FontManager;
import com.skyncalci.ncalc.userinterface.ThemeManager;
import com.skyncalci.ncalc.utils.DLog;


/**
 * Created by Duy on 19/7/2016
 */
public abstract class BaseActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String TAG = "MainActivity";
    protected CalculatorSetting mCalculatorSetting;
    protected DatabaseHelper mHistoryDatabase;
    protected CalculatorSetting mSetting;


    protected void playAnimation(Animator animator) {
        animator.start();
    }


    /**
     * set theme and init mHistoryDatabase for history
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCalculatorSetting = new CalculatorSetting(this);
        mHistoryDatabase = new DatabaseHelper(this);
        mSetting = new CalculatorSetting(this);

        setTheme(false);  //set theme for app
        setFullScreen();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mCalculatorSetting != null) {
            mCalculatorSetting.registerOnSharedPreferenceChangeListener(this);
        }
        if (!DLog.UI_TESTING_MODE) {
            // Monitor launch times and interval from installation
            RateThisApp.onStart(this);
            // If the criteria is satisfied, "Rate this app" dialog will be shown
            RateThisApp.showRateDialogIfNeeded(this);
        }
    }

    @Override
    protected void onDestroy() {
        if (mCalculatorSetting != null) {
            mCalculatorSetting.unregisterOnSharedPreferenceChangeListener(this);
        }
        super.onDestroy();
    }

    private void setFullScreen() {
        if (mSetting.useFullScreen()) {
            hideStatusBar();
        } else {
            showStatusBar();
        }
    }

    public void showStatusBar() {
        if (Build.VERSION.SDK_INT < 30) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public void hideStatusBar() {
        if (Build.VERSION.SDK_INT < 30) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * set theme for app
     *
     * @param recreate - call method onCreate
     */
    protected void setTheme(boolean recreate) {
        String name = mCalculatorSetting.getString(getResources().getString(R.string.key_pref_theme), "");
        ThemeManager themeManager = new ThemeManager(getApplicationContext());
        int themeId = themeManager.getTheme(name);
        if (themeId != ThemeManager.THEME_NOT_FOUND) {
            super.setTheme(themeId);
            if (recreate) recreate();
            Log.d(TAG, "Set theme ok");
        } else {
            Log.d(TAG, "Theme not found");
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.d(TAG, "onSharedPreferenceChanged: " + s);
        if (s.equalsIgnoreCase(getResources().getString(R.string.key_pref_theme))) {
            setTheme(true);
        } else if (s.equalsIgnoreCase(getString(R.string.key_pref_lang))) {
            recreate();
        } else if (s.equalsIgnoreCase(getString(R.string.key_pref_font))) {
            //reload type face
            FontManager.loadTypefaceFromAsset(this);
            recreate();
        } else if (s.equalsIgnoreCase(getString(R.string.key_hide_status_bar))) {
            setFullScreen();
        } else {
            MathEvaluator.initFromSetting(mSetting);
        }
    }

    /**
     * share app
     */
    protected void shareApp() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        intent.setType("text/plain");
        startActivity(intent);
    }


    public void gotoPlayStore() {
        gotoPlayStore(BuildConfig.APPLICATION_ID);
    }

    public void gotoPlayStore(String appId) {
        Uri uri = Uri.parse("market://details?id=" + appId);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + appId)));
        }
    }

    @SuppressWarnings("unused")
    protected void hideKeyboard(EditText editText) {
        if (editText != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        } else {
            // Check if no view has focus:
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
    }

    protected void hideKeyboard() {

        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Call after {@link #setContentView(int)}
     */
    @SuppressWarnings("ConstantConditions")
    protected void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        }
    }

}
