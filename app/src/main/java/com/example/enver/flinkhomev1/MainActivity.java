package com.example.enver.flinkhomev1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

import butterknife.Bind;

public class MainActivity extends AppCompatActivity {
    EditText UsernameEt, PasswordEt;
    TextView en,bs,de,demo;
    Locale mylocale;
    Boolean showpw = false;
    SharedPreferences prefs;
    ProgressBar loadingdemo;
    View child;
    CountDownTimer co;
    @Bind(R.id.btn_reg) TextView _signupLink;
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ( isOnline()) {
            prefs = getSharedPreferences("X", MODE_PRIVATE);
            String logcheck = prefs.getString("log", "");
            String romcheck = prefs.getString("rum", "");
            String devcheck = prefs.getString("dev", "");
            String usern = prefs.getString("usr", "");
            String paswo = prefs.getString("pas", "");
            if(devcheck.equals("true")){
                String devname = prefs.getString("devn", "");
                ArrayList<String> data = new ArrayList<>();
                Bundle b = new Bundle();
                Set<String> set = prefs.getStringSet("Roomslist", null);
                data.addAll(set);
                Intent intent = new Intent(getApplicationContext(), Device.class);
                b.putString("devicename", devname);
                b.putStringArrayList("Roomslist",data);
                intent.putExtras(b);
                startActivity(intent);
                overridePendingTransition(R.anim.animation_on, R.anim.animation_off);
            }
            else if (romcheck.equals("true")) {
                String roomname = prefs.getString("room", "");
                Bundle b = new Bundle();
                b.putString("key", roomname);
                b.putBoolean("new", false);
                Intent intent = new Intent(getApplicationContext(), Room.class);
                intent.putExtras(b);
                startActivity(intent);
                overridePendingTransition(R.anim.animation_on, R.anim.animation_off);
            }
            else if (logcheck.equals("true")) {
                BackgroundWorker2 backgroundWorker = new BackgroundWorker2(this);
                backgroundWorker.execute("login", usern, paswo);
            }
            else {
                setTheme(R.style.AppTheme_Splash);
                setContentView(R.layout.activity_splash);
                UsernameEt = (EditText) findViewById(R.id.etUsername);
                PasswordEt = (EditText) findViewById(R.id.etPassword);
                if(prefs.getString("Logout","")!=null) UsernameEt.setText(prefs.getString("Logout",""));
                TextView viewpassword = (TextView) findViewById(R.id.showpassword);
                viewpassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!showpw) {
                            PasswordEt.setTransformationMethod(new PasswordTransformationMethod());
                            showpw = true;
                        }
                        else {
                            PasswordEt.setTransformationMethod(null);
                            showpw = false;
                        }
                    }
                });
                bs=(TextView)findViewById(R.id.bs);
                en=(TextView)findViewById(R.id.en);
                de=(TextView)findViewById(R.id.de);
                demo=(TextView)findViewById(R.id.demo);
                en.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saveUsername();
                        setLanguage("en");
                        overridePendingTransition(R.anim.animation_on, R.anim.animation_off);
                    }
                });
                bs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saveUsername();
                        setLanguage("bs");
                        overridePendingTransition(R.anim.animation_on, R.anim.animation_off);
                    }
                });
                de.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saveUsername();
                        setLanguage("de");
                        overridePendingTransition(R.anim.animation_on, R.anim.animation_off);
                    }
                });
                demo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadingdemo=(ProgressBar)findViewById(R.id.loadingdemo);
                        loadingdemo.setVisibility(View.VISIBLE);
                        new TestAsync().execute();
                        co = new CountDownTimer(10000,1000) {
                            @Override
                            public void onTick(long l) {}

                            @Override
                            public void onFinish() {
                                loadingdemo.setVisibility(View.INVISIBLE);
                                startActivity(new Intent(getApplicationContext(),Demo.class));
                            }
                        };

                    }
                });
            }
        }
        else {
            setContentView(R.layout.activity_splash);
            notOnline();
        }
    }

    public void saveUsername(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Logout", UsernameEt.getText().toString());
        editor.apply();
    }

    public void OnLogin(View view)
    {

        String username = UsernameEt.getText().toString();
        String password = PasswordEt.getText().toString();
        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("usr", username);
        editor.putString("pas",password);
        editor.commit();
        String type = "login";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type,username, password);
    }

    public void OpenReg(View view)
    {
        startActivity(new Intent(this, Register.class));
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void OpenForgotPassword(View view)
    {
        startActivity(new Intent(this, ForgotPassword.class));
        overridePendingTransition(R.anim.animation_on, R.anim.animation_off);
    }

    public void OpenHomePanel(View view)
    {
        startActivity(new Intent(this, HomePanel.class));
        overridePendingTransition(R.anim.animation_on, R.anim.animation_off);
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
                final View mDrawerLayout = (RelativeLayout) findViewById(R.id.activity_splash);
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

    protected void setLanguage(String language){
        mylocale=new Locale(language);
        Resources resources=getResources();
        DisplayMetrics dm=resources.getDisplayMetrics();
        Configuration conf= resources.getConfiguration();
        conf.locale=mylocale;
        resources.updateConfiguration(conf,dm);
        Intent refreshIntent=new Intent(MainActivity.this,MainActivity.class);
        finish();
        startActivity(refreshIntent);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    class TestAsync extends AsyncTask<Void, Integer, View>
    {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute (){}

        protected View doInBackground(Void...arg0) {
            child = getLayoutInflater().inflate(R.layout.activity_device, null);

            return child;
        }

        protected void onProgressUpdate(Integer...a){}

        protected void onPostExecute(View child) {
            co.onFinish();
        }
    }
}