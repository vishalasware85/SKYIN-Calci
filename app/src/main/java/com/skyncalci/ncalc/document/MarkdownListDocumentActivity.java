package com.skyncalci.ncalc.document;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.skyncalci.R;
import com.skyncalci.calculator.activities.base.BaseActivity;

public class MarkdownListDocumentActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_fragment_content);

        setupToolbar();
        setTitle(R.string.documentation);
        MarkdownListDocumentFragment fragment = MarkdownListDocumentFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment).commitAllowingStateLoss();
    }
}
