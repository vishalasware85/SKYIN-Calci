package com.skyncalci.calculator;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.skyncalci.CustomJavaFiles.CheckInternet;
import com.skyncalci.CustomJavaFiles.NoInternet;
import com.skyncalci.Dialog.EmailVerificationDialog;
import com.skyncalci.PrivacyPolicy;
import com.skyncalci.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private String deviceID;
    private DatabaseReference ref;
    private Query query;
    private TextInputEditText emailET,nameET,mobET,passET,cfPassET;
    private CardView continueBtn;
    private TextView loginBtn,privacyPolicyTxt;
    private ImageView backBtn;
    private String nameSTR,emailSTR,mobileSTR,passSTR,cfPassSTR;

    private KProgressHUD kProgressHUD;

    public static Activity signup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        if (!CheckInternet.isNetworkAvailable(SignupActivity.this)){
            Intent intent=new Intent(SignupActivity.this, NoInternet.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }

        signup=this;

        auth = FirebaseAuth.getInstance();

        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        emailET=findViewById(R.id.emailSignupET);
        nameET=findViewById(R.id.nameSignupET);
        mobET=findViewById(R.id.mobileSignupET);
        passET=findViewById(R.id.passSignupET);
        cfPassET=findViewById(R.id.confPassSignupET);
        continueBtn=findViewById(R.id.signupContinueBtn);
        loginBtn=findViewById(R.id.signupLoginTxt);
        privacyPolicyTxt=findViewById(R.id.privacyPolicyTxt);
        backBtn=findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        query = ref.orderByChild("deviceID").equalTo(deviceID);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailET.getText().toString().trim().equalsIgnoreCase("")){
                    emailET.setError("Please enter valid email id");
                    emailET.requestFocus();
                    return;
                }
                if (nameET.getText().toString().trim().equalsIgnoreCase("")){
                    nameET.setError("Please enter your name");
                    nameET.requestFocus();
                    return;
                }
                if (mobET.getText().toString().trim().equalsIgnoreCase("")){
                    mobET.setError("Please enter your mobile number");
                    mobET.requestFocus();
                    return;
                }
                if (passET.getText().toString().trim().equalsIgnoreCase("")){
                    passET.setError("Please enter password");
                    passET.requestFocus();
                    return;
                }
                if (cfPassET.getText().toString().trim().equalsIgnoreCase("")){
                    cfPassET.setError("Please confirm your password");
                    cfPassET.requestFocus();
                    return;
                }

                mobileSTR=mobET.getText().toString();
                if (mobileSTR.length() != 10){
                    mobET.setError("Please enter valid mobile no.");
                    mobET.requestFocus();
                    return;
                }
                passSTR= passET.getText().toString();
                if (passSTR.length() <= 5){
                    passET.setError("Minimum 6 digits are required");
                    passET.requestFocus();
                    return;
                }
                cfPassSTR=cfPassET.getText().toString();
                if (!passSTR.equals(cfPassSTR)){
                    cfPassET.setError("Password doesn't match");
                    cfPassET.requestFocus();
                    return;
                }
                emailSTR=emailET.getText().toString();
                nameSTR=nameET.getText().toString();

                kProgressHUD = KProgressHUD.create(SignupActivity.this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("Hang on")
                        .setDetailsLabel("Creating your existence...")
                        .setCancellable(false)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                        .show();

                queryAccountExistence(emailSTR, passSTR);
            }
        });

        privacyPolicyTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, PrivacyPolicy.class));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignupActivity.this,LoginActivity.class);
                //Intent intent=new Intent(SignupActivity.this,Mpage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void queryAccountExistence(final String email,final String pass) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        Query query = ref.orderByChild("deviceID").equalTo(deviceID);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //device already registered
                    Toast.makeText(SignupActivity.this,
                            "This device is already registered on another email, please login",
                            Toast.LENGTH_SHORT).show();
                    kProgressHUD.dismiss();
                } else {
                    //device id not found
                    createAccount(email, pass);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void createAccount(final String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Registration success:
                            final FirebaseUser user = auth.getCurrentUser();
                            assert user != null;
                            //send email verification link
                            auth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                updateUi(user, email);
                                            } else {
                                                Toast.makeText(SignupActivity.this, "Error: "
                                                                + task.getException().getMessage(),
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            kProgressHUD.dismiss();
                            //Registration failed:
                            Toast.makeText(SignupActivity.this, "Error: " +
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void updateUi(FirebaseUser user, String email) {
        String refer = email.substring(0, email.lastIndexOf("@"));
        String referCode = refer.replace(".", "");

        //identify that this user already sign up

        HashMap<String, Object> map = new HashMap<>();
        map.put("name", nameSTR);
        map.put("email", email);
        map.put("pass", passSTR);
        map.put("uid", user.getUid());
        map.put("image", " ");
        map.put("coins", 0);
        map.put("referCode", referCode);
        map.put("deviceID", deviceID);
        map.put("pla","fr");
        map.put("mob",mobileSTR);
        map.put("purDat","");

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1); // to get yesterday date
        Date previousDate = calendar.getTime();

        String dateString = dateFormat.format(previousDate);

//        FirebaseDatabase.getInstance().getReference().child("Daily Check")
//                .child(user.getUid())
//                .child("date")
//                .setValue(dateString);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");

        reference.child(user.getUid())
                .setValue(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent=new Intent(SignupActivity.this, EmailVerificationDialog.class);
                            intent.putExtra("firstEmail","firstEmail");
                            startActivity(intent);
                        } else {
                            Toast.makeText(SignupActivity.this, "Error: " +
                                            task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        if (kProgressHUD!=null){
                            kProgressHUD.dismiss();
                        }
                    }
                });
    }
}