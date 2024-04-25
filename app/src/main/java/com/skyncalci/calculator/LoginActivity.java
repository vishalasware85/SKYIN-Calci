package com.skyncalci.calculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.skyncalci.CustomJavaFiles.CheckInternet;
import com.skyncalci.CustomJavaFiles.NoInternet;
import com.skyncalci.Dialog.EmailVerificationDialog;
import com.skyncalci.Dialog.ErrorDialog;
import com.skyncalci.ForgotPasswordActivity;
import com.skyncalci.Model.Data;
import com.skyncalci.R;
import com.skyncalci.ncalc.CalcApplication;
import com.skyncalci.ncalc.calculator.BasicCalculatorActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailET, passET;
    private CardView continueBtn;
    private TextView registerBtn, forgotPassBtn;
    private ImageView backBtn;

    private String emailSTR, passSTR;
    private FirebaseAuth auth;
    DatabaseReference reference;
    private FirebaseUser user;

    private KProgressHUD kProgressHUD;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        if (!CheckInternet.isNetworkAvailable(LoginActivity.this)){
            Intent intent=new Intent(LoginActivity.this, NoInternet.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }

        FirebaseApp.initializeApp(LoginActivity.this);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        user = auth.getCurrentUser();

        emailET = findViewById(R.id.emailLoginET);
        passET = findViewById(R.id.passLoginET);
        continueBtn = findViewById(R.id.loginContinueBtn);
        registerBtn = findViewById(R.id.loginRegisterTxt);
        forgotPassBtn = findViewById(R.id.forgotPassBtn);
        backBtn=findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailET.getText().toString().trim().equalsIgnoreCase("")) {
                    emailET.setError("Please enter your email id");
                    emailET.requestFocus();
                    return;
                }
                if (passET.getText().toString().trim().equalsIgnoreCase("")) {
                    passET.setError("Please enter your password");
                    passET.requestFocus();
                    return;
                }

                emailSTR = emailET.getText().toString();
                passSTR = passET.getText().toString();

                signIn(emailSTR, passSTR);
            }
        });

        forgotPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        SharedPreferences sharedPreferences=getSharedPreferences("userData",MODE_PRIVATE);
        if (sharedPreferences.contains("email") && sharedPreferences.contains("password")){
            Intent intent=new Intent(LoginActivity.this, BasicCalculatorActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void signIn(String email, String password) {
        kProgressHUD = KProgressHUD.create(LoginActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Logging you in...")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //login success
                            FirebaseUser user = auth.getCurrentUser();
                            //Check if user is verified
                            assert user != null;
                            if (user.isEmailVerified()) {
                                SharedPreferences sharedPreferences=getSharedPreferences("userData",MODE_PRIVATE);
                                SharedPreferences.Editor editor=sharedPreferences.edit();

                                SharedPreferences sharedPreferences1=getSharedPreferences("happy",MODE_PRIVATE);
                                SharedPreferences.Editor editor1=sharedPreferences1.edit();
                                editor.putString("email",emailSTR);
                                editor.putString("password",passSTR);

                                HashMap<String, Object> map = new HashMap<>();
                                map.put("pass", passSTR);
                                reference.child(user.getUid())
                                        .updateChildren(map)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    kProgressHUD.dismiss();

                                                    kProgressHUD = KProgressHUD.create(LoginActivity.this)
                                                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                                            .setLabel("Please wait")
                                                            .setDetailsLabel("Reading your data...")
                                                            .setCancellable(false)
                                                            .setAnimationSpeed(2)
                                                            .setDimAmount(0.5f)
                                                            .show();

                                                    reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            Data model = snapshot.getValue(Data.class);
                                                            String pla=model.getPla().toString();
                                                            String purDat=model.getPurDat().toString();

                                                            editor.putString("mob",model.getMob());
                                                            editor.apply();

                                                            if (pla.equals("fr")){
                                                                Toast.makeText(LoginActivity.this, "You didn't have any active plan", Toast.LENGTH_SHORT).show();
                                                                SharedPreferences happy=getSharedPreferences("happy",MODE_PRIVATE);
                                                                SharedPreferences.Editor editor=happy.edit();
                                                                editor.putString("fr","fr");
                                                                editor.apply();
                                                                kProgressHUD.dismiss();
                                                                startActivity(new Intent(LoginActivity.this, BasicCalculatorActivity.class));
                                                                finish();
                                                            }
                                                            if (pla.equals("bs")){
                                                                Toast.makeText(LoginActivity.this, "Congrats, you have active Basic plan enjoy all the premium features", Toast.LENGTH_SHORT).show();
                                                                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                                                                String url = "https://www.timeapi.io/api/Time/current/zone?timeZone=Asia/Kolkata";
                                                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                                                        new Response.Listener<String>() {
                                                                            @Override
                                                                            public void onResponse(String response) {
                                                                                try {
                                                                                    JSONObject reader = new JSONObject(response);
                                                                                    String today = reader.getString("date");

                                                                                    int dateDifference = (int) getDateDiff(new SimpleDateFormat("MM/dd/yyyy"), purDat, today);
                                                                                    if (dateDifference > 30) {
                                                                                        editor.putString("fr", "fr");
                                                                                        editor.apply();

                                                                                        HashMap<String, Object> map = new HashMap<>();
                                                                                        map.put("pla", "fr");
                                                                                        reference.child(user.getUid())
                                                                                                .updateChildren(map)
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if (task.isSuccessful()) {
                                                                                                            startActivity(new Intent(LoginActivity.this, BasicCalculatorActivity.class));
                                                                                                            finish();
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                    } else {
                                                                                        editor.putString("bs", "bs");
                                                                                        editor1.remove("fr");
                                                                                        editor1.apply();
                                                                                        editor.apply();
                                                                                        kProgressHUD.dismiss();
                                                                                        BasicCalculatorActivity.basicCalcActivity.finish();
                                                                                        startActivity(new Intent(LoginActivity.this, BasicCalculatorActivity.class));
                                                                                        finish();
                                                                                    }
                                                                                } catch (JSONException e) {
                                                                                    e.printStackTrace();
                                                                                    Toast.makeText(LoginActivity.this, "EREEEEE. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        }, new Response.ErrorListener() {
                                                                    @Override
                                                                    public void onErrorResponse(VolleyError error) {
                                                                        Toast.makeText(LoginActivity.this, "Err. " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                                queue.add(stringRequest);
                                                            }
                                                            if (pla.equals("si")){
                                                                Toast.makeText(LoginActivity.this, "Congrats, you have active Silver plan enjoy all the premium features", Toast.LENGTH_SHORT).show();
                                                                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                                                                String url = "https://www.timeapi.io/api/Time/current/zone?timeZone=Asia/Kolkata";
                                                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                                                        new Response.Listener<String>() {
                                                                            @Override
                                                                            public void onResponse(String response) {
                                                                                try {
                                                                                    JSONObject reader = new JSONObject(response);
                                                                                    String today = reader.getString("date");

                                                                                    int dateDifference = (int) getDateDiff(new SimpleDateFormat("MM/dd/yyyy"), purDat, today);
                                                                                    if (dateDifference > 90) {
                                                                                        editor.putString("fr", "fr");
                                                                                        editor.apply();

                                                                                        HashMap<String, Object> map = new HashMap<>();
                                                                                        map.put("pla", "fr");
                                                                                        reference.child(user.getUid())
                                                                                                .updateChildren(map)
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if (task.isSuccessful()) {
                                                                                                            startActivity(new Intent(LoginActivity.this, BasicCalculatorActivity.class));
                                                                                                            finish();
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                    } else {
                                                                                        editor.putString("si", "si");
                                                                                        editor1.remove("fr");
                                                                                        editor1.apply();
                                                                                        editor.apply();
                                                                                        kProgressHUD.dismiss();
                                                                                        BasicCalculatorActivity.basicCalcActivity.finish();
                                                                                        startActivity(new Intent(LoginActivity.this, BasicCalculatorActivity.class));
                                                                                        finish();
                                                                                    }
                                                                                } catch (JSONException e) {
                                                                                    e.printStackTrace();
                                                                                    Toast.makeText(LoginActivity.this, "EREEEEE. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        }, new Response.ErrorListener() {
                                                                    @Override
                                                                    public void onErrorResponse(VolleyError error) {
                                                                        Toast.makeText(LoginActivity.this, "Err. " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                                queue.add(stringRequest);
                                                            }
                                                            if (pla.equals("go")){
                                                                Toast.makeText(LoginActivity.this, "Congrats, you have active Gold plan enjoy all the premium features", Toast.LENGTH_SHORT).show();
                                                                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                                                                String url = "https://www.timeapi.io/api/Time/current/zone?timeZone=Asia/Kolkata";
                                                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                                                        new Response.Listener<String>() {
                                                                            @Override
                                                                            public void onResponse(String response) {
                                                                                try {
                                                                                    JSONObject reader = new JSONObject(response);
                                                                                    String today = reader.getString("date");

                                                                                    int dateDifference = (int) getDateDiff(new SimpleDateFormat("MM/dd/yyyy"), purDat, today);
                                                                                    if (dateDifference > 180) {
                                                                                        editor.putString("fr", "fr");
                                                                                        editor.apply();

                                                                                        HashMap<String, Object> map = new HashMap<>();
                                                                                        map.put("pla", "fr");
                                                                                        reference.child(user.getUid())
                                                                                                .updateChildren(map)
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if (task.isSuccessful()) {
                                                                                                            startActivity(new Intent(LoginActivity.this, BasicCalculatorActivity.class));
                                                                                                            finish();
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                    } else {
                                                                                        editor.putString("go", "go");
                                                                                        editor1.remove("fr");
                                                                                        editor1.apply();
                                                                                        editor.apply();
                                                                                        kProgressHUD.dismiss();
                                                                                        BasicCalculatorActivity.basicCalcActivity.finish();
                                                                                        startActivity(new Intent(LoginActivity.this, BasicCalculatorActivity.class));
                                                                                        finish();
                                                                                    }
                                                                                } catch (JSONException e) {
                                                                                    e.printStackTrace();
                                                                                    Toast.makeText(LoginActivity.this, "EREEEEE. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        }, new Response.ErrorListener() {
                                                                    @Override
                                                                    public void onErrorResponse(VolleyError error) {
                                                                        Toast.makeText(LoginActivity.this, "Err. " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                                queue.add(stringRequest);
                                                            }
                                                            if (pla.equals("pt")){
                                                                Toast.makeText(LoginActivity.this, "Congrats, you have active Platinum plan enjoy all the premium features", Toast.LENGTH_SHORT).show();
                                                                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                                                                String url = "https://www.timeapi.io/api/Time/current/zone?timeZone=Asia/Kolkata";
                                                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                                                        new Response.Listener<String>() {
                                                                            @Override
                                                                            public void onResponse(String response) {
                                                                                try {
                                                                                    JSONObject reader = new JSONObject(response);
                                                                                    String today = reader.getString("date");

                                                                                    int dateDifference = (int) getDateDiff(new SimpleDateFormat("MM/dd/yyyy"), purDat, today);
                                                                                    if (dateDifference > 270) {
                                                                                        editor.putString("fr", "fr");
                                                                                        editor.apply();

                                                                                        HashMap<String, Object> map = new HashMap<>();
                                                                                        map.put("pla", "fr");
                                                                                        reference.child(user.getUid())
                                                                                                .updateChildren(map)
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if (task.isSuccessful()) {
                                                                                                            startActivity(new Intent(LoginActivity.this, BasicCalculatorActivity.class));
                                                                                                            finish();
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                    } else {
                                                                                        editor.putString("pt", "pt");
                                                                                        editor1.remove("fr");
                                                                                        editor1.apply();
                                                                                        editor.apply();
                                                                                        kProgressHUD.dismiss();
                                                                                        BasicCalculatorActivity.basicCalcActivity.finish();
                                                                                        startActivity(new Intent(LoginActivity.this, BasicCalculatorActivity.class));
                                                                                        finish();
                                                                                    }
                                                                                } catch (JSONException e) {
                                                                                    e.printStackTrace();
                                                                                    Toast.makeText(LoginActivity.this, "EREEEEE. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        }, new Response.ErrorListener() {
                                                                    @Override
                                                                    public void onErrorResponse(VolleyError error) {
                                                                        Toast.makeText(LoginActivity.this, "Err. " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                                queue.add(stringRequest);
                                                            }
                                                            if (pla.equals("eg")){
                                                                Toast.makeText(LoginActivity.this, "Congrats, you have active Ever-Green plan enjoy all the premium features", Toast.LENGTH_SHORT).show();
                                                                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                                                                String url = "https://www.timeapi.io/api/Time/current/zone?timeZone=Asia/Kolkata";
                                                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                                                        new Response.Listener<String>() {
                                                                            @Override
                                                                            public void onResponse(String response) {
                                                                                try {
                                                                                    JSONObject reader = new JSONObject(response);
                                                                                    String today = reader.getString("date");

                                                                                    int dateDifference = (int) getDateDiff(new SimpleDateFormat("MM/dd/yyyy"), purDat, today);
                                                                                    if (dateDifference > 365) {
                                                                                        editor.putString("fr", "fr");
                                                                                        editor.apply();

                                                                                        HashMap<String, Object> map = new HashMap<>();
                                                                                        map.put("pla", "fr");
                                                                                        reference.child(user.getUid())
                                                                                                .updateChildren(map)
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if (task.isSuccessful()) {
                                                                                                            startActivity(new Intent(LoginActivity.this, BasicCalculatorActivity.class));
                                                                                                            finish();
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                    } else {
                                                                                        editor.putString("eg", "eg");
                                                                                        editor1.remove("fr");
                                                                                        editor1.apply();
                                                                                        editor.apply();
                                                                                        kProgressHUD.dismiss();
                                                                                        BasicCalculatorActivity.basicCalcActivity.finish();
                                                                                        startActivity(new Intent(LoginActivity.this, BasicCalculatorActivity.class));
                                                                                        finish();
                                                                                    }
                                                                                } catch (JSONException e) {
                                                                                    e.printStackTrace();
                                                                                    Toast.makeText(LoginActivity.this, "EREEEEE. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        }, new Response.ErrorListener() {
                                                                    @Override
                                                                    public void onErrorResponse(VolleyError error) {
                                                                        Toast.makeText(LoginActivity.this, "Err. " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                                queue.add(stringRequest);
                                                            }
                                                        }
                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                            SharedPreferences sharedPreferences=getSharedPreferences("userData",MODE_PRIVATE);
                                                            SharedPreferences.Editor editor=sharedPreferences.edit();
                                                            editor.remove("email");
                                                            editor.remove("password");
                                                            editor.apply();

                                                            Intent intent=new Intent(LoginActivity.this, ErrorDialog.class);
                                                            intent.putExtra("errorCode",error.getMessage().toString());
                                                            startActivity(intent);
                                                        }
                                                    });
                                                } else {
                                                    kProgressHUD.dismiss();
                                                    SharedPreferences sharedPreferences=getSharedPreferences("userData",MODE_PRIVATE);
                                                    SharedPreferences.Editor editor=sharedPreferences.edit();
                                                    editor.remove("email");
                                                    editor.remove("password");
                                                    editor.apply();

                                                    Intent intent=new Intent(LoginActivity.this, ErrorDialog.class);
                                                    intent.putExtra("errorCode",task.getException().getMessage());
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                            } else {
                                kProgressHUD.dismiss();
                                Intent intent =new Intent(LoginActivity.this, EmailVerificationDialog.class);
                                intent.putExtra("secEmail","secEmail");
                                startActivity(intent);
                            }
                        } else {
                            kProgressHUD.dismiss();
                            SharedPreferences sharedPreferences=getSharedPreferences("userData",MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.remove("email");
                            editor.remove("password");
                            editor.apply();

                            Intent intent=new Intent(LoginActivity.this, ErrorDialog.class);
                            intent.putExtra("errorCode",task.getException().getMessage());
                            startActivity(intent);
                        }
                    }
                });
    }

    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}

