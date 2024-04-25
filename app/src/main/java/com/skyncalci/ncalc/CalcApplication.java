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

package com.skyncalci.ncalc;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.skyncalci.BuildConfig;
import com.skyncalci.Model.Data;
import com.skyncalci.R;
import com.skyncalci.RemoteConfig.AppIssue;
import com.skyncalci.RemoteConfig.AppUpdate;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


public class CalcApplication extends Application {
    public static final String CHANNEL_ID = "SKY!N Floating Mode";

    DatabaseReference reference;
    private FirebaseUser user;

    public static CalcApplication calcApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        PreferenceManager.setDefaultValues(this, R.xml.setting, false);

        calcApplication=this;

        MobileAds.initialize(getApplicationContext());

        SharedPreferences sharedPreferences2=getSharedPreferences("first",MODE_PRIVATE);
        SharedPreferences.Editor editor1=sharedPreferences2.edit();

        SharedPreferences sharedPreferences = getSharedPreferences("happy", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        boolean first=sharedPreferences2.getBoolean("fr",true);
        if (first){
            editor.putString("fr","fr");
            editor.apply();
            editor1.putBoolean("fr",false);
            editor1.apply();
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        SharedPreferences sharedPreferences1=getSharedPreferences("userData",MODE_PRIVATE);
        if (sharedPreferences1.contains("email") && sharedPreferences1.contains("password")){
            RequestQueue queue = Volley.newRequestQueue(CalcApplication.this);
            String url = "https://www.timeapi.io/api/Time/current/zone?timeZone=Asia/Kolkata";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject reader = new JSONObject(response);
                                String today = reader.getString("date");

                                reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Data model = snapshot.getValue(Data.class);
                                        String pla = model.getPurDat().toString();
                                        int dateDifference = (int) getDateDiff(new SimpleDateFormat("MM/dd/yyyy"), pla, today);

                                        if (sharedPreferences.contains("bs")) {
                                            if (dateDifference > 30) {
                                                editor.remove("bs");
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

                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                        if (sharedPreferences.contains("si")) {
                                            if (dateDifference > 90) {
                                                editor.remove("si");
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

                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                        if (sharedPreferences.contains("go")) {
                                            if (dateDifference > 180) {
                                                editor.remove("go");
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

                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                        if (sharedPreferences.contains("pt")) {
                                            if (dateDifference > 270) {
                                                editor.remove("pt");
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

                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                        if (sharedPreferences.contains("eg")) {
                                            if (dateDifference > 365) {
                                                editor.remove("eg");
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

                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(CalcApplication.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(CalcApplication.this, "EREEEEE. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(CalcApplication.this, "Err. " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(stringRequest);
        }
    }

    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "SKY!N Floating Mode",
                    NotificationManager.IMPORTANCE_LOW
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}