package com.skyncalci.calculator.symja.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.skyncalci.R;
import com.skyncalci.calculator.activities.base.BaseEvaluatorActivity;
import com.skyncalci.calculator.evaluator.EvaluateConfig;
import com.skyncalci.calculator.evaluator.MathEvaluator;
import com.skyncalci.calculator.evaluator.thread.Command;
import com.skyncalci.ncalc.document.MarkdownListDocumentActivity;
import com.gx.common.collect.Lists;

import java.util.ArrayList;

public class IdeActivity extends BaseEvaluatorActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBtnEvaluate.setText(R.string.eval);
    }

    @Override
    public void clickHelp() {
        Intent intent = new Intent(this, MarkdownListDocumentActivity.class);
        startActivity(intent);
    }

    @Nullable
    @Override
    public Command<ArrayList<String>, String> getCommand() {
        return new Command<ArrayList<String>, String>() {
            @WorkerThread
            @Override
            public ArrayList<String> execute(String input) {
                EvaluateConfig config = EvaluateConfig.loadFromSetting(getApplicationContext());
                String fraction = MathEvaluator.getInstance().evaluateWithResultAsTex(input,
                        config.setEvalMode(EvaluateConfig.FRACTION));
                return Lists.newArrayList(fraction);
            }
        };
    }
}
