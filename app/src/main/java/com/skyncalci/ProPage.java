package com.skyncalci;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.skyncalci.calculator.SignupActivity;
import com.skyncalci.Dialog.PurchasePlan;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ProPage extends AppCompatActivity implements PaymentResultWithDataListener {

    private LinearLayout shineLayout, layoutBasic, layoutSilver, layoutGold, layoutPlat, accessBtn;
    private TextView buyTxt, txtBasic, txtSilver, txtGold, txtPlat;
    private CardView shineCard, cardBasic, cardSilver, cardGold, cardPlat;
    private ImageView backBtn;
    private allFeaturesSheet superBottomSheetFragment;

    public static ProPage propage;

    DatabaseReference reference;
    private FirebaseUser user;

    private boolean bs = false, si = false, go = false, pt = false, eg = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pro_page);

        propage = this;

        Checkout.preload(this);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        shineCard = findViewById(R.id.shinecard);
        cardBasic = findViewById(R.id.shinecardBasic);
        cardSilver = findViewById(R.id.shinecardSilver);
        cardGold = findViewById(R.id.shinecardGold);
        cardPlat = findViewById(R.id.shinecardPlat);
        shineLayout = findViewById(R.id.shineLayout);
        layoutBasic = findViewById(R.id.shineLayoutBasic);
        layoutSilver = findViewById(R.id.shineLayoutSilver);
        layoutGold = findViewById(R.id.shineLayoutGold);
        layoutPlat = findViewById(R.id.shineLayoutPlay);
        buyTxt = findViewById(R.id.butBtn);
        txtBasic = findViewById(R.id.butBtnBasic);
        txtSilver = findViewById(R.id.butBtnSilver);
        txtGold = findViewById(R.id.butBtnGold);
        txtPlat = findViewById(R.id.butBtnPlat);
        backBtn = findViewById(R.id.backBtn);
        accessBtn = findViewById(R.id.accessBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SharedPreferences sharedPreferences1 = getSharedPreferences("userData", MODE_PRIVATE);

        shineCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shineLayout.setVisibility(View.VISIBLE);
                Animation animation = new TranslateAnimation(0, shineCard.getWidth() + shineLayout.getWidth(), 0, 0);
                animation.setDuration(550);
                animation.setFillAfter(false);
                animation.setInterpolator(new AccelerateDecelerateInterpolator());
                shineLayout.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        shineLayout.setVisibility(View.GONE);
                        if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                            Intent intent1 = new Intent(ProPage.this, SignupActivity.class);
                            startActivity(intent1);
                            finish();
                        } else {
                            startPayment("55400", "Purchasing 365 days of premium", "Evergreen Plan");
                            bs = false;
                            si = false;
                            go = false;
                            pt = false;
                            eg = true;
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        });

        cardBasic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutBasic.setVisibility(View.VISIBLE);
                Animation animation = new TranslateAnimation(0, cardBasic.getWidth() + layoutBasic.getWidth(), 0, 0);
                animation.setDuration(550);
                animation.setFillAfter(false);
                animation.setInterpolator(new AccelerateDecelerateInterpolator());
                layoutBasic.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        layoutBasic.setVisibility(View.GONE);
                        if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                            Intent intent1 = new Intent(ProPage.this, SignupActivity.class);
                            startActivity(intent1);
                            finish();
                        } else {
                            startPayment("4900", "Purchasing 30 days of premium", "Basic Plan");
                            bs = true;
                            si = false;
                            go = false;
                            pt = false;
                            eg = false;
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        });

        cardSilver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutSilver.setVisibility(View.VISIBLE);
                Animation animation = new TranslateAnimation(0, cardSilver.getWidth() + layoutSilver.getWidth(), 0, 0);
                animation.setDuration(550);
                animation.setFillAfter(false);
                animation.setInterpolator(new AccelerateDecelerateInterpolator());
                layoutSilver.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        layoutSilver.setVisibility(View.GONE);
                        if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                            Intent intent1 = new Intent(ProPage.this, SignupActivity.class);
                            startActivity(intent1);
                            finish();
                        } else {
                            startPayment("14200", "Purchasing 90 days of premium", "Silver Plan");
                            bs = false;
                            si = true;
                            go = false;
                            pt = false;
                            eg = false;
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        });

        cardGold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutGold.setVisibility(View.VISIBLE);
                Animation animation = new TranslateAnimation(0, cardGold.getWidth() + layoutGold.getWidth(), 0, 0);
                animation.setDuration(550);
                animation.setFillAfter(false);
                animation.setInterpolator(new AccelerateDecelerateInterpolator());
                layoutGold.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        layoutGold.setVisibility(View.GONE);
                        if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                            Intent intent1 = new Intent(ProPage.this, SignupActivity.class);
                            startActivity(intent1);
                            finish();
                        } else {
                            startPayment("27900", "Purchasing 180 days of premium", "Gold Plan");
                            bs = false;
                            si = false;
                            go = true;
                            pt = false;
                            eg = false;
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        });

        cardPlat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutPlat.setVisibility(View.VISIBLE);
                Animation animation = new TranslateAnimation(0, cardPlat.getWidth() + layoutPlat.getWidth(), 0, 0);
                animation.setDuration(550);
                animation.setFillAfter(false);
                animation.setInterpolator(new AccelerateDecelerateInterpolator());
                layoutPlat.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        layoutPlat.setVisibility(View.GONE);
                        if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                            Intent intent1 = new Intent(ProPage.this, SignupActivity.class);
                            startActivity(intent1);
                            finish();
                        } else {
                            startPayment("41400", "Purchasing 270 days of premium", "Platinum Plan");
                            bs = false;
                            si = false;
                            go = false;
                            pt = true;
                            eg = false;
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        });

        accessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                superBottomSheetFragment = new allFeaturesSheet();
                superBottomSheetFragment.show(getSupportFragmentManager(), "allFeaturesSheet");
            }
        });
    }

    public void startPayment(String amount, String description, String planName) {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        SharedPreferences sharedPreferences1 = getSharedPreferences("userData", MODE_PRIVATE);
        String email = sharedPreferences1.getString("email", "");
        String mob = sharedPreferences1.getString("mob", "");
        final Activity activity = this;

        final Checkout co = new Checkout();
        co.setFullScreenDisable(true);
        co.setKeyID("rzp_live_Gul5e0EfevPl6e");
        try {
            JSONObject options = new JSONObject();
            options.put("name", "SKY!N Calci " + planName);
            options.put("description", description);
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", amount);

            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact", mob);

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    private KProgressHUD kProgressHUD;

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        kProgressHUD = KProgressHUD.create(ProPage.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Managing your data...")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        RequestQueue queue = Volley.newRequestQueue(ProPage.this);
        String url = "https://www.timeapi.io/api/Time/current/zone?timeZone=Asia/Kolkata";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject reader = new JSONObject(response);
                            String today = reader.getString("date");

                            HashMap<String, Object> map = new HashMap<>();
                            map.put("purDat", today);
                            if (bs) {
                                map.put("pla", "bs");
                            }
                            if (si) {
                                map.put("pla", "si");
                            }
                            if (go) {
                                map.put("pla", "go");
                            }
                            if (pt) {
                                map.put("pla", "pt");
                            }
                            if (eg) {
                                map.put("pla", "eg");
                            }

                            reference.child(user.getUid())
                                    .updateChildren(map)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                SharedPreferences sharedPreferences = getSharedPreferences("happy", MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.remove("fr");
                                                if (bs) {
                                                    editor.putString("bs", "bs");
                                                }
                                                if (si) {
                                                    editor.putString("si", "si");
                                                }
                                                if (go) {
                                                    editor.putString("go", "go");
                                                }
                                                if (pt) {
                                                    editor.putString("pt", "pt");
                                                }
                                                if (eg) {
                                                    editor.putString("eg", "eg");
                                                }
                                                editor.apply();
                                                kProgressHUD.dismiss();
                                                Intent intent = new Intent(ProPage.this, PurchasePlan.class);
                                                intent.putExtra("planPurchased", "planPurchased");
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        } catch (JSONException e) {
                            e.printStackTrace();
                            kProgressHUD.dismiss();
                            Toast.makeText(ProPage.this, "EREEEEE. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProPage.this, "Err. " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Intent intent = new Intent(ProPage.this, PurchasePlan.class);
        intent.putExtra("paymentFiled", "" + s.toString());
        startActivity(intent);
    }
}
