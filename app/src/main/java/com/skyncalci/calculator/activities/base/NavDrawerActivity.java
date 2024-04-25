/*
 * Copyright (C) 2018 Duy Tran Le
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.skyncalci.calculator.activities.base;

import static com.skyncalci.calculator.symja.models.TrigItem.TRIG_TYPE.EXPAND;
import static com.skyncalci.calculator.symja.models.TrigItem.TRIG_TYPE.EXPONENT;
import static com.skyncalci.calculator.symja.models.TrigItem.TRIG_TYPE.REDUCE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.floating_bubble.overlay.OverlayPermission;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skyncalci.Dialog.ContactUsDialog;
import com.skyncalci.Dialog.PurchasePlan;
import com.skyncalci.Floating.SimpleHoverMenu;
import com.skyncalci.Model.Data;
import com.skyncalci.ProPage;
import com.skyncalci.R;
import com.skyncalci.calculator.LoginActivity;
import com.skyncalci.calculator.symja.activities.DerivativeActivity;
import com.skyncalci.calculator.symja.activities.ExpandAllExpressionActivity;
import com.skyncalci.calculator.symja.activities.FactorExpressionActivity;
import com.skyncalci.calculator.symja.activities.FactorPrimeActivity;
import com.skyncalci.calculator.symja.activities.IdeActivity;
import com.skyncalci.calculator.symja.activities.IntegrateActivity;
import com.skyncalci.calculator.symja.activities.LimitActivity;
import com.skyncalci.calculator.symja.activities.ModuleActivity;
import com.skyncalci.calculator.symja.activities.NumberActivity;
import com.skyncalci.calculator.symja.activities.PermutationActivity;
import com.skyncalci.calculator.symja.activities.PiActivity;
import com.skyncalci.calculator.symja.activities.PrimitiveActivity;
import com.skyncalci.calculator.symja.activities.SimplifyExpressionActivity;
import com.skyncalci.calculator.symja.activities.SolveEquationActivity;
import com.skyncalci.calculator.symja.activities.TrigActivity;
import com.skyncalci.calculator.symja.activities.TrigActivityExponent;
import com.skyncalci.calculator.symja.activities.TrigActivityReduce;
import com.skyncalci.ncalc.calculator.BasicCalculatorActivity;
import com.skyncalci.ncalc.calculator.LogicCalculatorActivity;
import com.skyncalci.ncalc.document.MarkdownListDocumentActivity;
import com.skyncalci.ncalc.geom2d.GeometryDescartesActivity;
import com.skyncalci.ncalc.graph.GraphActivity;
import com.skyncalci.ncalc.matrix.MatrixCalculatorActivity;
import com.skyncalci.ncalc.settings.SettingsActivity;
import com.skyncalci.ncalc.systemequations.SystemEquationActivity;
import com.skyncalci.ncalc.unitconverter.UnitCategoryActivity;

/**
 * Created by Duy on 19/7/2016
 */
public abstract class NavDrawerActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected final Handler handler = new Handler();
    protected DrawerLayout mDrawerLayout;

    DatabaseReference reference;
    private FirebaseUser user;

    /**
     * call on user click back
     */
    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        } else
            super.onBackPressed();
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawers();
    }

    SwitchMaterial switchCompat;
    private CardView cardView,loginCard;
    private LinearLayout linearLayout,loginlayout;
    private TextView orTxt,purchaseTxt;

    @Override
    protected void onResume() {
        super.onResume();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        setupHeaderNavigation(navigationView);

        SharedPreferences sharedPreferences=getSharedPreferences("userData",MODE_PRIVATE);
        if (sharedPreferences.contains("email") && sharedPreferences.contains("password")){
            orTxt.setVisibility(View.GONE);
            loginCard.setVisibility(View.GONE);

            reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Data model = snapshot.getValue(Data.class);
                    String pla=model.getPla().toString();
                    if (pla.equals("fr")){
                        cardView.setVisibility(View.VISIBLE);
                        purchaseTxt.setText("Free");
                    }
                    if (pla.equals("bs")){
                        cardView.setVisibility(View.GONE);
                        purchaseTxt.setText("Basic");
                    }
                    if (pla.equals("si")){
                        cardView.setVisibility(View.GONE);
                        purchaseTxt.setText("Silver");
                    }
                    if (pla.equals("go")){
                        cardView.setVisibility(View.GONE);
                        purchaseTxt.setText("Gold");
                    }
                    if (pla.equals("pt")){
                        cardView.setVisibility(View.GONE);
                        purchaseTxt.setText("Platinum");
                    }
                    if (pla.equals("eg")){
                        cardView.setVisibility(View.GONE);
                        purchaseTxt.setText("Ever-Green");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(NavDrawerActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

    private void setupHeaderNavigation(NavigationView navigationView) {
        View header = navigationView.getHeaderView(0);
        cardView=header.findViewById(R.id.upgradeHeadBtn);
        loginCard=header.findViewById(R.id.loginHeadBtn);
        linearLayout=header.findViewById(R.id.upgradeHeadLayout);
        loginlayout=header.findViewById(R.id.loginHeadLayout);
        orTxt=header.findViewById(R.id.orHeadTxt);
        purchaseTxt=header.findViewById(R.id.purchaseTxtHeader);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        SharedPreferences sharedPreferences=getSharedPreferences("happy",MODE_PRIVATE);
        AdView mAdView = header.findViewById(R.id.adView);
        if (sharedPreferences.contains("fr")){
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }else {
            mAdView.setVisibility(View.GONE);
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.setVisibility(View.VISIBLE);
                Animation animation = new TranslateAnimation(0, cardView.getWidth()+linearLayout.getWidth(),0, 0);
                animation.setDuration(550);
                animation.setFillAfter(false);
                animation.setInterpolator(new AccelerateDecelerateInterpolator());
                linearLayout.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        closeDrawer();
                        linearLayout.setVisibility(View.GONE);
                        Intent intent =new Intent(NavDrawerActivity.this, ProPage.class);
                        startActivity(intent);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
            }
        });

        loginCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginlayout.setVisibility(View.VISIBLE);
                Animation animation = new TranslateAnimation(0, loginCard.getWidth()+loginlayout.getWidth(),0, 0);
                animation.setDuration(550);
                animation.setFillAfter(false);
                animation.setInterpolator(new AccelerateDecelerateInterpolator());
                loginlayout.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        closeDrawer();
                        loginlayout.setVisibility(View.GONE);
                        Intent intent =new Intent(NavDrawerActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
            }
        });
        header.findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
            }
        });
    }

    private static final int REQUEST_CODE_HOVER_PERMISSION=300;
    boolean mPermissionsRequested = false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_HOVER_PERMISSION && resultCode==RESULT_OK){
            Intent startHoverIntent = new Intent(NavDrawerActivity.this, SimpleHoverMenu.class);
            startService(startHoverIntent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        closeDrawer();
        Intent intent;

        SharedPreferences sharedPreferences=getSharedPreferences("happy",MODE_PRIVATE);
        SharedPreferences sharedPreferences1=getSharedPreferences("userData",MODE_PRIVATE);
        switch (id) {
            case R.id.nav_switch:{
                SwitchMaterial drawerSwitch = (SwitchMaterial) item.getActionView().findViewById(R.id.switch_id);
                Intent startHoverIntent = new Intent(NavDrawerActivity.this, SimpleHoverMenu.class);
                if (sharedPreferences.contains("fr")){
                    Intent intent1=new Intent(NavDrawerActivity.this, PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else {
                        if (drawerSwitch.isChecked()){
                            stopService(startHoverIntent);
                            drawerSwitch.setChecked(false);
                        }else {
                            if (!mPermissionsRequested && !OverlayPermission.hasRuntimePermissionToDrawOverlay(NavDrawerActivity.this)) {
                                @SuppressWarnings("NewApi")
                                Intent myIntent = OverlayPermission.createIntentToRequestOverlayPermission(NavDrawerActivity.this);
                                startActivityForResult(myIntent, REQUEST_CODE_HOVER_PERMISSION);
                            } else {
                                startService(startHoverIntent);
                            }
                            drawerSwitch.setChecked(true);
                        }
                        drawerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    if (!mPermissionsRequested && !OverlayPermission.hasRuntimePermissionToDrawOverlay(NavDrawerActivity.this)) {
                                        @SuppressWarnings("NewApi")
                                        Intent myIntent = OverlayPermission.createIntentToRequestOverlayPermission(NavDrawerActivity.this);
                                        startActivityForResult(myIntent, REQUEST_CODE_HOVER_PERMISSION);
                                    } else {
                                        startService(startHoverIntent);
                                    }
                                } else {
                                    stopService(startHoverIntent);
                                }
                            }
                        });
                    }
                break;
            }
            case R.id.contact:{
                startActivity(new Intent(NavDrawerActivity.this, ContactUsDialog.class));
                break;
            }
            case R.id.action_all_functions: {
                intent = new Intent(getApplicationContext(), MarkdownListDocumentActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.action_ide_mode:
                intent = new Intent(getApplicationContext(), IdeActivity.class);
                postStartActivity(intent);
                break;
            case R.id.nav_sci_calc:
                intent = new Intent(getApplicationContext(), BasicCalculatorActivity.class);
                postStartActivity(intent);
                break;
            case R.id.nav_graph:
                if (sharedPreferences.contains("fr")){
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else {
                    intent = new Intent(getApplicationContext(), GraphActivity.class);
                    postStartActivity(intent);
                }
                break;
            case R.id.nav_unit:
                if (sharedPreferences.contains("fr")){
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else {
                    intent = new Intent(getApplicationContext(), UnitCategoryActivity.class);
                    postStartActivity(intent);
                }
                break;
            case R.id.nav_base:
                intent = new Intent(getApplicationContext(), LogicCalculatorActivity.class);
                postStartActivity(intent);
                break;
            case R.id.nav_geometric_descartes:
                if (sharedPreferences.contains("fr")){
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else {
                    intent = new Intent(getApplicationContext(), GeometryDescartesActivity.class);
                    postStartActivity(intent);
                }
                break;
            case R.id.nav_setting:
                intent = new Intent(getApplicationContext(), SettingsActivity.class);
                postStartActivity(intent);
                break;
            case R.id.nav_matrix:
                if (sharedPreferences.contains("fr")){
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else {
                    intent = new Intent(getApplicationContext(), MatrixCalculatorActivity.class);
                    postStartActivity(intent);
                }
                break;
            case R.id.system_equation:
                if (sharedPreferences.contains("fr")){
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else {
                    intent = new Intent(getApplicationContext(), SystemEquationActivity.class);
                    postStartActivity(intent);
                }
                break;
            case R.id.nav_solve_equation:
                if (sharedPreferences.contains("fr")){
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else {
                    intent = new Intent(getApplicationContext(), SolveEquationActivity.class);
                    postStartActivity(intent);
                }
                break;
            case R.id.nav_simplify_equation:
                if (sharedPreferences.contains("fr")){
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else {
                    intent = new Intent(getApplicationContext(), SimplifyExpressionActivity.class);
                    postStartActivity(intent);
                }
                break;
            case R.id.nav_factor_equation:
                if (sharedPreferences.contains("fr")){
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else {
                    intent = new Intent(getApplicationContext(), FactorExpressionActivity.class);
                    postStartActivity(intent);
                }
                break;
            case R.id.nav_derivitive:
                if (sharedPreferences.contains("fr")){
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else {
                    intent = new Intent(getApplicationContext(), DerivativeActivity.class);
                    postStartActivity(intent);
                }
                break;
            case R.id.nav_expand_binomial:
                if (sharedPreferences.contains("fr")){
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else {
                    intent = new Intent(getApplicationContext(), ExpandAllExpressionActivity.class);
                    postStartActivity(intent);
                }
                break;
            case R.id.nav_limit:
                if (sharedPreferences.contains("fr")){
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else {
                    intent = new Intent(getApplicationContext(), LimitActivity.class);
                    postStartActivity(intent);
                }
                break;
            case R.id.nav_integrate:
                if (sharedPreferences.contains("fr")){
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else {
                    intent = new Intent(getApplicationContext(), IntegrateActivity.class);
                    postStartActivity(intent);
                }
                break;
            case R.id.nav_primitive:
                if (sharedPreferences.contains("fr")){
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else {
                    intent = new Intent(getApplicationContext(), PrimitiveActivity.class);
                    postStartActivity(intent);
                }
                break;
            case R.id.nav_rate:
                gotoPlayStore();
                break;
            case R.id.nav_prime_factor:
                if (sharedPreferences.contains("fr")){
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else {
                    intent = new Intent(getApplicationContext(), FactorPrimeActivity.class);
                    postStartActivity(intent);
                }
                break;
            case R.id.nav_mod:
                if (sharedPreferences.contains("fr")){
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else {
                    intent = new Intent(getApplicationContext(), ModuleActivity.class);
                    postStartActivity(intent);
                }
                break;
            case R.id.nav_trig_expand:
                if (sharedPreferences.contains("fr")){
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else {
                    intent = new Intent(getApplicationContext(), TrigActivity.class);
                    intent.putExtra(TrigActivity.TYPE, EXPAND);
                    postStartActivity(intent);
                }
                break;
            case R.id.nav_trig_reduce:
                if (sharedPreferences.contains("fr")){
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else {
                    intent = new Intent(getApplicationContext(), TrigActivityReduce.class);
                    intent.putExtra(TrigActivity.TYPE, REDUCE);
                    postStartActivity(intent);
                }
                break;
            case R.id.nav_trig_to_exp:
                if (sharedPreferences.contains("fr")){
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else {
                    intent = new Intent(getApplicationContext(), TrigActivityExponent.class);
                    intent.putExtra(TrigActivity.TYPE, EXPONENT);
                    postStartActivity(intent);
                }
                break;
            case R.id.nav_permutation:
                if (sharedPreferences.contains("fr")){
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else {
                    intent = new Intent(getApplicationContext(), PermutationActivity.class);
                    if (this instanceof PermutationActivity) {
                        finish();
                    }
                    intent.putExtra(PermutationActivity.TYPE_NUMBER, PermutationActivity.TYPE_PERMUTATION);
                    postStartActivity(intent);
                }
                break;
            case R.id.nav_combination:
                if (sharedPreferences.contains("fr")){
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else {
                    intent = new Intent(getApplicationContext(), PermutationActivity.class);
                    if (this instanceof PermutationActivity) {
                        finish();
                    }
                    intent.putExtra(PermutationActivity.TYPE_NUMBER, PermutationActivity.TYPE_COMBINATION);
                    postStartActivity(intent);
                }
                break;
            case R.id.nav_catalan:
                if (sharedPreferences.contains("fr")){
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else {
                    intent = new Intent(getApplicationContext(), NumberActivity.class);
                    if (this instanceof NumberActivity) {
                        finish();
                    }
                    intent.putExtra(NumberActivity.DATA, NumberActivity.NumberType.CATALAN);
                    postStartActivity(intent);
                }
                break;
            case R.id.nav_fibo:
                if (sharedPreferences.contains("fr")){
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else {
                    intent = new Intent(getApplicationContext(), NumberActivity.class);
                    if (this instanceof NumberActivity) {
                        finish();
                    }
                    intent.putExtra(NumberActivity.DATA, NumberActivity.NumberType.FIBONACCI);
                    postStartActivity(intent);
                }
                break;
            case R.id.nav_prime:
                if (sharedPreferences.contains("fr")){
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else {
                    intent = new Intent(getApplicationContext(), NumberActivity.class);
                    if (this instanceof NumberActivity) {
                        finish();
                    }
                    intent.putExtra(NumberActivity.DATA, NumberActivity.NumberType.PRIME);
                    postStartActivity(intent);
                }
                break;
            case R.id.action_divisors:
                if (sharedPreferences.contains("fr")){
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else {
                    intent = new Intent(getApplicationContext(), NumberActivity.class);
                    if (this instanceof NumberActivity) {
                        finish();
                    }
                    intent.putExtra(NumberActivity.DATA, NumberActivity.NumberType.DIVISORS);
                    postStartActivity(intent);
                }
                break;
            case R.id.action_pi_number:
                if (sharedPreferences.contains("fr")){
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else if (!sharedPreferences1.contains("email") && !sharedPreferences1.contains("password")) {
                    Intent intent1=new Intent(NavDrawerActivity.this,PurchasePlan.class);
                    intent1.putExtra("currPlan","fr");
                    startActivity(intent1);
                }else {
                    intent = new Intent(getApplicationContext(), PiActivity.class);
                    postStartActivity(intent);
                }
                break;
        }
        return true;
    }

    private void postStartActivity(final Intent intent) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        }, 100);
    }

}
