package com.skyncalci;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.skyncalci.CustomJavaFiles.CheckInternet;
import com.skyncalci.CustomJavaFiles.NoInternet;
import com.skyncalci.Dialog.PasswordResetDialog;

import java.util.Objects;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText emailET;
    private CardView continueBtn;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_pass_activity);

        if (!CheckInternet.isNetworkAvailable(ForgotPasswordActivity.this)){
            Intent intent=new Intent(ForgotPasswordActivity.this, NoInternet.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }

        auth = FirebaseAuth.getInstance();

        emailET=findViewById(R.id.emailForgotPassET);
        continueBtn=findViewById(R.id.forgotPassContinueBtn);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailET.getText().toString().trim().equalsIgnoreCase("")){
                    emailET.setError("Please enter your email id");
                    emailET.requestFocus();
                    return;
                }

                auth.sendPasswordResetEmail(emailET.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Intent intent=new Intent(ForgotPasswordActivity.this, PasswordResetDialog.class);
                                    intent.putExtra("success","success");
                                    startActivity(intent);
                                }else {
                                    Intent intent=new Intent(ForgotPasswordActivity.this, PasswordResetDialog.class);
                                    intent.putExtra("fail", Objects.requireNonNull(task.getException()).getMessage());
                                    startActivity(intent);
                                }
                            }
                        });
            }
        });
    }
}
