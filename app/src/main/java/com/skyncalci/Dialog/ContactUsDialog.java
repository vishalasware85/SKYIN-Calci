package com.skyncalci.Dialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.skyncalci.CustomJavaFiles.CheckInternet;
import com.skyncalci.CustomJavaFiles.NoInternet;
import com.skyncalci.R;

public class ContactUsDialog extends AppCompatActivity {

    private CardView dialogCard,gotitBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us_dialog);

        if (!CheckInternet.isNetworkAvailable(ContactUsDialog.this)){
            Intent intent=new Intent(ContactUsDialog.this, NoInternet.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }

        bannerAd();

        dialogCard=findViewById(R.id.contactUsDialogCard);
        gotitBtn=findViewById(R.id.gotitContactUsBtn);

        Animation slide_up= AnimationUtils.loadAnimation(ContactUsDialog.this,
                R.anim.zoom_in);
        dialogCard.startAnimation(slide_up);
        dialogCard.setVisibility(View.VISIBLE);

        gotitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
