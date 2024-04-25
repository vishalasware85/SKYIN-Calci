package com.skyncalci.RemoteConfig;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.skyncalci.R;
import com.skyncalci.ncalc.calculator.BasicCalculatorActivity;

public class AppUpdate extends AppCompatActivity {

    private CardView updateNowBtn,laterBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_update);

        updateNowBtn=findViewById(R.id.updateNowBtn);
        laterBtn=findViewById(R.id.laterBtn);

        updateNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id="+getPackageName())));
                }catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=" +
                                    getPackageName())));
                }
            }
        });

        laterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                BasicCalculatorActivity.basicCalcActivity.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        BasicCalculatorActivity.basicCalcActivity.finish();
    }
}
