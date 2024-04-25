package com.skyncalci.CustomJavaFiles;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.skyncalci.R;
import com.skyncalci.ncalc.calculator.BasicCalculatorActivity;

public class NoInternet extends AppCompatActivity {

    private CardView retryBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_internet);

        retryBtn=findViewById(R.id.retryInternetBtn);

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckInternet.isNetworkAvailable(NoInternet.this)){
                    Intent intent=new Intent(NoInternet.this, BasicCalculatorActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(NoInternet.this, "Connection Established", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(NoInternet.this, "No connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
