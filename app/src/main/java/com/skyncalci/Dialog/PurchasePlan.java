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
import com.skyncalci.ProPage;
import com.skyncalci.R;
import com.skyncalci.ncalc.calculator.BasicCalculatorActivity;

public class PurchasePlan extends AppCompatActivity {

    private CardView veriCard, buyBtn;
    private TextView btnTxt, errorCode, titlePurchase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_plan);

        bannerAd();

        Intent intent = this.getIntent();

        veriCard = findViewById(R.id.emailVeriCard);
        buyBtn = findViewById(R.id.emailVeriloginBtn);
        btnTxt = findViewById(R.id.emailVeriLoginBtnTV);
        titlePurchase = findViewById(R.id.titlePurchase);
        errorCode = findViewById(R.id.errorCode);

        if (intent.hasExtra("planPurchased")) {
            btnTxt.setText("Great");
            titlePurchase.setText("Yay");
            errorCode.setText("you have unlocked all the premium features, Enjoy.\n\nHappy Calculating.");
            buyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(PurchasePlan.this, BasicCalculatorActivity.class));
                    ProPage.propage.finish();
                    finish();
                }
            });
        } else if (intent.hasExtra("paymentFiled")) {
            btnTxt.setText("Retry");
            titlePurchase.setText("Uhh..Ohh..");
            errorCode.setText("Payment failed\n\n" + intent.getStringExtra("paymentFiled"));
            buyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        } else {
            btnTxt.setText("Buy now");
            titlePurchase.setText("Uhh..Ohh..");
            errorCode.setText("You didn't have any purchased plan please purchase one to enjoy this feature");
            buyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(PurchasePlan.this, ProPage.class));
                    finish();
                }
            });
        }

        Animation slide_up = AnimationUtils.loadAnimation(PurchasePlan.this,
                R.anim.zoom_in);
        veriCard.startAnimation(slide_up);
        veriCard.setVisibility(View.VISIBLE);
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
