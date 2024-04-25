package com.skyncalci.RemoteConfig;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.skyncalci.BuildConfig;
import com.skyncalci.R;
import com.skyncalci.ncalc.calculator.BasicCalculatorActivity;

public class AppIssue extends AppCompatActivity {

    private CardView issueBtn;
    private TextView issueBtnTxt,descTxt,titleTxt;

    private FirebaseRemoteConfig firebaseRemoteConfig;

    private boolean mpageFinish = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_issue);

        issueBtn=findViewById(R.id.issueBtn);
        issueBtnTxt=findViewById(R.id.issueBtnTV);
        descTxt=findViewById(R.id.issueDesc);
        titleTxt=findViewById(R.id.issueTitle);

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings.Builder configBuilder = new FirebaseRemoteConfigSettings.Builder();
        if (BuildConfig.DEBUG) {
            long cacheInterval = 0;
            configBuilder.setMinimumFetchIntervalInSeconds(cacheInterval);
        }
        firebaseRemoteConfig.setConfigSettingsAsync(configBuilder.build());
        firebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            issueBtnTxt.setText(firebaseRemoteConfig.getString("issueBtnTxt"));
                            descTxt.setText(firebaseRemoteConfig.getString("issueDesc"));
                            titleTxt.setText(firebaseRemoteConfig.getString("issueTitle"));
                            mpageFinish=firebaseRemoteConfig.getBoolean("mpageFinish");
                        } else {
                            Toast.makeText(AppIssue.this, "Fetch failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        issueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                if (mpageFinish){
                    BasicCalculatorActivity.basicCalcActivity.finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        if (mpageFinish){
            BasicCalculatorActivity.basicCalcActivity.finish();
        }
    }
}
