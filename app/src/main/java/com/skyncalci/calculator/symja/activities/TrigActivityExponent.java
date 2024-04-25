package com.skyncalci.calculator.symja.activities;

import static com.skyncalci.calculator.symja.models.TrigItem.TRIG_TYPE.EXPAND;
import static com.skyncalci.calculator.symja.models.TrigItem.TRIG_TYPE.EXPONENT;
import static com.skyncalci.calculator.symja.models.TrigItem.TRIG_TYPE.REDUCE;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import com.gx.common.collect.Lists;
import com.skyncalci.BuildConfig;
import com.skyncalci.R;
import com.skyncalci.calculator.activities.base.BaseEvaluatorActivity;
import com.skyncalci.calculator.evaluator.EvaluateConfig;
import com.skyncalci.calculator.evaluator.MathEvaluator;
import com.skyncalci.calculator.evaluator.thread.Command;
import com.skyncalci.calculator.symja.models.TrigItem;

import java.util.ArrayList;

public class TrigActivityExponent extends BaseEvaluatorActivity {
    public static final String TYPE = "TrigActivity";
    public static final String TAG = "TrigActivity";
    private int mType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mType = intent.getIntExtra(TYPE, -1);
        if (mType == -1) {
            Toast.makeText(this, "Bundle nullable!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        init();
    }

    /**
     * - set title for activity
     * - Set text for button solve
     * - if this is first start activity or debug enable, give example data
     */
    private void init() {
        mBtnEvaluate.setText(R.string.eval);
        boolean started;
        switch (mType) {
            case EXPAND:
                setTitle(R.string.tit_trig_expand);
                started = mPreferences.getBoolean(TAG + "expand", false);
                if (!started || BuildConfig.DEBUG) {
                    mInputFormula.setText("Sin(2x)");
                    mPreferences.edit().putBoolean(TAG + "expand", true).apply();
                }
                break;
            case REDUCE:
                setTitle(R.string.tit_trig_reduce);
                started = mPreferences.getBoolean(TAG + "reduce", false);
                if (!started || BuildConfig.DEBUG) {
                    mInputFormula.setText("2*Sin(x)*Cos(x)");
                    mPreferences.edit().putBoolean(TAG + "reduce", true).apply();
                }
                break;
            case EXPONENT:
                setTitle(R.string.tit_trig_to_exp);
                started = mPreferences.getBoolean(TAG + "exponent", false);
                if (!started || BuildConfig.DEBUG) {
                    mInputFormula.setText("Sin(x)");
                    mPreferences.edit().putBoolean(TAG + "exponent", true).apply();
                }
                break;

        }
    }

    @Override
    protected String getExpression() {
        TrigItem item = new TrigItem(mInputFormula.getCleanText());
        switch (mType) {
            case EXPAND:
                item.setType(EXPAND);
                break;
            case REDUCE:
                item.setType(REDUCE);
                break;
            case EXPONENT:
                item.setType(EXPONENT);
                break;
        }
        return item.getInput();
    }

    @Override
    public void clickHelp() {
    }


    @Override
    public Command<ArrayList<String>, String> getCommand() {
        return new Command<ArrayList<String>, String>() {
            @WorkerThread
            @Override
            public ArrayList<String> execute(String input) {

                String fraction = MathEvaluator.getInstance().evaluateWithResultAsTex(input,
                        EvaluateConfig.loadFromSetting(getApplicationContext())
                                .setEvalMode(EvaluateConfig.FRACTION));
                return Lists.newArrayList(fraction);
            }
        };
    }
}
