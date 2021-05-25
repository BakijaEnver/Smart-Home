package com.example.enver.flinkhomev1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;

public class Register extends AppCompatActivity {
    EditText name, surname, email, username, password,confirm_password, phone;
    CountDownTimer co;

    @Bind(R.id.btn_hasAcc) TextView _loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (isOnline() == false) {
            notOnline();
        }
        else {
            name = (EditText) findViewById(R.id.et_name);
            surname = (EditText) findViewById(R.id.et_surname);
            email = (EditText) findViewById(R.id.et_email);
            username = (EditText) findViewById(R.id.et_username);
            password = (EditText) findViewById(R.id.et_password);
            confirm_password = (EditText) findViewById(R.id.et_confirm_password);
        }
    }

    public void onReg(View view)
    {
        boolean valid = true;
        String str_name = name.getText().toString();
        String str_surname = surname.getText().toString();
        String str_email = email.getText().toString();
        String str_username = username.getText().toString();
        String str_password = password.getText().toString();
        String str_confirm_password = confirm_password.getText().toString();
        if (str_name.isEmpty() || str_name.length() < 3) {
            name.setError(String.valueOf(getResources().getString(R.string.at_least_3)));
            valid = false;
        }
        else {
            name.setError(null);
        }
        if (str_surname.isEmpty() || str_surname.length() < 3) {
            surname.setError(String.valueOf(getResources().getString(R.string.at_least_3)));
            valid = false;
        }
        else{
            surname.setError(null);
        }
        if (str_email.length() == 0 || !android.util.Patterns.EMAIL_ADDRESS.matcher(str_email).matches()) {
            email.setError(String.valueOf(getResources().getString(R.string.valid_email_address)));
            valid = false;
        }
        else{
            email.setError(null);
        }
        if (str_username.isEmpty() || str_username.length() < 3) {
            username.setError(String.valueOf(getResources().getString(R.string.at_least_3)));
            valid = false;
        }
        else{
            username.setError(null);
        }
        if (str_password.length() == 0 || str_password.length() < 4 ) {
            password.setError(String.valueOf(getResources().getString(R.string.between_4_10)));
            valid = false;
        }
        else{
            password.setError(null);
        }
        if (str_confirm_password.isEmpty() || str_confirm_password.length() < 4  || !(str_confirm_password.equals(str_password))) {
            confirm_password.setError(String.valueOf(getResources().getString(R.string.password_do_not_match)));
            valid = false;
        }
        else{
            confirm_password.setError(null);
            valid = true;
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.faderegister, null);
            final PopupWindow fadePopup = new PopupWindow(container,  ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);
            fadePopup.setAnimationStyle(R.style.animationName);
            final View mDrawerLayout = (ScrollView) findViewById(R.id.activity_register);
            fadePopup.showAtLocation(mDrawerLayout, Gravity.NO_GRAVITY,0, 0);
            SharedPreferences preferences = getSharedPreferences("X",MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Logout", str_username);
            editor.apply();
            String type = "register";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this,fadePopup);
            backgroundWorker.execute(type, str_name, str_surname, str_email, str_username, str_password);

        }
    }
    public void OpenLogin(View view)
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
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
                final View mDrawerLayout = (ScrollView) findViewById(R.id.activity_register);
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



}