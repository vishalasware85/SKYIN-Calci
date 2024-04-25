package com.skyncalci.Dialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.skyncalci.R;

public class ErrorDialog extends AppCompatActivity {

    private TextView errorTxt;
    private CardView loginBtn, veriCard;;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.error_dialog);

        Intent intent=this.getIntent();

        bannerAd();

        errorTxt=findViewById(R.id.errorCode);
        loginBtn=findViewById(R.id.emailVeriloginBtn);
        veriCard=findViewById(R.id.emailVeriCard);

        Animation slide_up= AnimationUtils.loadAnimation(ErrorDialog.this,
                R.anim.zoom_in);
        veriCard.startAnimation(slide_up);
        veriCard.setVisibility(View.VISIBLE);

        if (intent.hasExtra("errorCode")){
            String str=intent.getStringExtra("errorCode");
            errorTxt.setText(str);
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void bannerAd(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        SharedPreferences sharedPreferences=getSharedPreferences("happy",MODE_PRIVATE);
        AdView mAdView = findViewById(R.id.adView);
        if (sharedPreferences.contains("fr")){
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }else {
            mAdView.setVisibility(View.GONE);
        }
    }
}
