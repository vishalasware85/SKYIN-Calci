package com.skyncalci;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment;

public class allFeaturesSheet extends SuperBottomSheetFragment {

    @SuppressLint("MissingSuperCall")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.all_features_layout, container, false);
    }

    @Override
    public float getCornerRadius() {
        return 100;
    }

    @Override
    public boolean animateCornerRadius() {
        return true;
    }

    @Override
    public boolean isSheetAlwaysExpanded() {
        return true;
    }

    @Override
    public boolean isSheetCancelableOnTouchOutside() {
        return true;
    }
}