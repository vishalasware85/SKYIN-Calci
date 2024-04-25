package com.skyncalci.Dialog;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.skyncalci.R;
import com.skyncalci.calculator.LoginActivity;
import com.skyncalci.calculator.SignupActivity;

public class EmailVerificationDialog extends AppCompatActivity {

    private LottieAnimationView lottieAnimationView;
    private CardView loginBtn,veriCard;
    private LinearLayout linearLayout;
    private TextView emailVeriDesc,emailVeriBtnTV;
    private boolean loginFinish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_verification_dialog);

        bannerAd();

        lottieAnimationView=findViewById(R.id.emailVeriLottie);
        loginBtn=findViewById(R.id.emailVeriloginBtn);
        linearLayout=findViewById(R.id.emailVeriLinearLayout);
        emailVeriDesc=findViewById(R.id.emailVeriDesc);
        emailVeriBtnTV=findViewById(R.id.emailVeriLoginBtnTV);
        veriCard=findViewById(R.id.emailVeriCard);

        Animation slide_up= AnimationUtils.loadAnimation(EmailVerificationDialog.this,
                R.anim.zoom_in);
        veriCard.startAnimation(slide_up);
        veriCard.setVisibility(View.VISIBLE);

        Intent intent=this.getIntent();
        if (intent.hasExtra("firstEmail")){
            emailVeriDesc.setText(getResources().getString(R.string.emailVeriDesc));
            linearLayout.setVisibility(View.VISIBLE);
            emailVeriBtnTV.setText("Go to login");
            loginFinish=false;
        }else if (intent.hasExtra("secEmail")){
            emailVeriDesc.setText(getResources().getString(R.string.emailVeriDesc2));
            linearLayout.setVisibility(View.VISIBLE);
            emailVeriBtnTV.setText("Got it");
            loginFinish=true;
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loginFinish){
                    Intent intent=new Intent(EmailVerificationDialog.this, LoginActivity.class);
                    SignupActivity.signup.finish();
                    startActivity(intent);
                }
                finish();
            }
        });

        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) { }
            @Override
            public void onAnimationEnd(Animator animation) {
                lottieAnimationView.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationCancel(Animator animation) { }
            @Override
            public void onAnimationRepeat(Animator animation) { }
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
