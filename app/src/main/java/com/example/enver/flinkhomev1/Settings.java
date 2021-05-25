package com.example.enver.flinkhomev1;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import java.util.Locale;

public class Settings extends AppCompatActivity {
    ImageButton imgLeft;
    LayoutInflater layoutInflater;
    RelativeLayout activity;
    Button bs, en, de, close;
    Locale mylocale;
    Bundle bundle,b;
    String roomName,aparat;
    Button language;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (isOnline() == false) {
            notOnline();
        }
        else {
            language = (Button) findViewById(R.id.language);
            activity = (RelativeLayout) findViewById(R.id.activity_settings);
            bundle = getIntent().getExtras();
            roomName = bundle.getString("key");
            aparat = bundle.getString("aparati");
            imgLeft = (ImageButton) findViewById(R.id.imgLeft);
            imgLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = null;
                    if (aparat.equals("Room")) {
                        intent = new Intent(Settings.this, Room.class);
                        b = new Bundle();
                        b.putString("key", roomName);
                        b.putBoolean("new", false);
                        intent.putExtras(b);
                    }
                    else if (aparat.equals("Device")) {
                        intent = new Intent(Settings.this, Device.class);
                        intent.putExtras(bundle);
                    }
                    else if (aparat.equals("HomePanel")){//ERVEN
                        intent = new Intent(Settings.this, HomePanel.class);
                        intent.putExtras(bundle);
                    }
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
            });

            language.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final PopupWindow fadePopup = fade();
                    fadePopup.setAnimationStyle(R.style.animationName);
                    ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.languagepopup, null);
                    final PopupWindow LanguagePopup = new PopupWindow(container, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                    LanguagePopup.setAnimationStyle(R.style.animationName);
                    LanguagePopup.showAtLocation(activity, Gravity.CENTER, 0, 0);
                    LanguagePopup.setOutsideTouchable(true);
                    bs = (Button) container.findViewById(R.id.bs);
                    en = (Button) container.findViewById(R.id.en);
                    de = (Button) container.findViewById(R.id.de);
                    close = (Button) container.findViewById(R.id.close);

                    en.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setLanguage("en");
                            overridePendingTransition(R.anim.animation_on, R.anim.animation_off);
                        }
                    });
                    bs.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setLanguage("bs");
                            overridePendingTransition(R.anim.animation_on, R.anim.animation_off);
                        }
                    });
                    de.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setLanguage("de");
                            overridePendingTransition(R.anim.animation_on, R.anim.animation_off);
                        }
                    });
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            LanguagePopup.setAnimationStyle(R.style.animationName);
                            LanguagePopup.dismiss();
                            fadePopup.setAnimationStyle(R.style.animationName);
                            fadePopup.dismiss();
                        }
                    });

                    container.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            LanguagePopup.setAnimationStyle(R.style.animationName);
                            LanguagePopup.dismiss();
                            fadePopup.setAnimationStyle(R.style.animationName);
                            fadePopup.dismiss();
                            return true;
                        }
                    });
                }
            });
        }
    }

    public PopupWindow fade (){
        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup container1 = (ViewGroup) layoutInflater.inflate(R.layout.fadepopup,null);
        final PopupWindow fadePopup = new PopupWindow(container1, activity.getWidth(),activity.getHeight() ,false);
        fadePopup.showAtLocation(activity, Gravity.NO_GRAVITY,activity.getWidth()/2,activity.getHeight()/2);
        return fadePopup;
    }

    public void onBackPressed() {
        Intent intent=null;
        if(aparat.equals("Room")){
          intent = new Intent(Settings.this, Room.class);
        }
        else if (aparat.equals("Device")){
            intent = new Intent(Settings.this, Device.class);
        }
        b=new Bundle();
        b.putString("key", roomName);
        b.putBoolean("new", false);
        intent.putExtras(b);
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    protected void setLanguage(String language){
        mylocale=new Locale(language);
        Resources resources=getResources();
        DisplayMetrics dm=resources.getDisplayMetrics();
        Configuration conf= resources.getConfiguration();
        conf.locale=mylocale;
        resources.updateConfiguration(conf,dm);
        Intent refreshIntent=new Intent(Settings.this,Settings.class);
        refreshIntent.putExtras(bundle);
        finish();
        startActivity(refreshIntent);
    }

    public boolean isOnline(){
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mob_avail = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mWifi.isConnected() || mob_avail.isConnected()) return true;
        return false;
    }

    public void notOnline(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                final ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.nowifipopup,null);
                final PopupWindow popupWindow = new PopupWindow(container, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
                final View mDrawerLayout = (RelativeLayout) findViewById(R.id.activity_settings);
                popupWindow.setAnimationStyle(R.style.animationName);
                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        popupWindow.showAtLocation(mDrawerLayout, Gravity.NO_GRAVITY, 0, 0);
                    }
                }, 500);
                container.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        popupWindow.dismiss();
                        finish();
                        return false;
                    }
                });
                Button povezacuse = (Button) container.findViewById(R.id.povezacuse);
                povezacuse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                        finish();
                    }
                });
            }
        }, 200);
    }
}