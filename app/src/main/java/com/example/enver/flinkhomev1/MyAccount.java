package com.example.enver.flinkhomev1;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyAccount extends AppCompatActivity {
    String data1[];
    String name,surname;
    EditText editFirstname,editLastname,oldPassword,newPassword,newPassword1;
    String usern;
    ImageButton imgLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        if (isOnline() == false) {
            notOnline();
        }
        else {
            SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
            usern = prefs.getString("usr", "");
            editFirstname = (EditText) findViewById(R.id.editFirstname);
            editLastname = (EditText) findViewById(R.id.editLastname);
            imgLeft = (ImageButton) findViewById(R.id.imgLeft);
            imgLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
            });
            StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
            getData("http://159.203.107.114/flinkphp/namesurname.php?Username=" + usern);
            String podaci = data1[0];
            Pattern pattern = Pattern.compile("((\\w|\\.|\\@|_|-|\\s)+)");
            Matcher matcher = pattern.matcher(podaci);
            int i = 0;
            while (matcher.find()) {
                if (i == 1) name = matcher.group();
                if (i == 3) surname = matcher.group();
                i++;
            }
            editFirstname.setHint(name);
            editLastname.setHint(surname);
            System.out.println(usern + editFirstname.getHint().toString());
        }
    }

    public void getData(String urln) {
        InputStream is=null;
        String line = null;
        String result = null;
        try {
            URL url = new URL(urln);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            is = new BufferedInputStream(con.getInputStream());
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JSONArray js = new JSONArray(result);
            JSONObject jo = null;
            data1 = new String[js.length()];
            for (int i = 0; i < js.length(); i++) {
                jo = js.getJSONObject(i);
                data1[i] = js.getString(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void changeName(View view){
        if(editFirstname.getText().toString().isEmpty()){
            LayoutInflater inflater = getLayoutInflater();
            // Inflate the Layout
            View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.custom_toast_layout));
            Toast toast = Toast.makeText(this, R.string.please_enter_new_name, Toast.LENGTH_LONG);
            View toastView = toast.getView();
            TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
            toastMessage.setTextSize(14);
            toastMessage.setShadowLayer(0, 0, 0, 0);
            toastMessage.setTextColor(this.getResources().getColor(R.color.colorPrimaryWhite));
            toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.warning_icon, 0, 0, 0);
            toastMessage.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
            toast.setGravity(Gravity.BOTTOM,0,20);
            toastMessage.setCompoundDrawablePadding(15);
            toastView.setBackground(this.getResources().getDrawable(R.drawable.toast_back));
            toast.show();
        }
        else {
            String type = "name";
            BackgroundWorker bc = new BackgroundWorker(getApplicationContext());
            bc.execute(type, usern, editFirstname.getText().toString(),"");
            editFirstname.setHint(editFirstname.getText().toString());
            editFirstname.setText("");
        }
    }

    public void changeSurname(View view){
        if(editLastname.getText().toString().isEmpty()){
            LayoutInflater inflater = getLayoutInflater();
            // Inflate the Layout
            View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.custom_toast_layout));
            Toast toast = Toast.makeText(this, R.string.please_enter_new_surname, Toast.LENGTH_LONG);
            View toastView = toast.getView();
            TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
            toastMessage.setTextSize(14);
            toastMessage.setShadowLayer(0, 0, 0, 0);
            toastMessage.setTextColor(this.getResources().getColor(R.color.colorPrimaryWhite));
            toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.warning_icon, 0, 0, 0);
            toastMessage.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
            toast.setGravity(Gravity.BOTTOM,0,20);
            toastMessage.setCompoundDrawablePadding(15);
            toastView.setBackground(this.getResources().getDrawable(R.drawable.toast_back));
            toast.show();
        }
        else {
            String type = "surname";
            BackgroundWorker bc = new BackgroundWorker(getApplicationContext());
            bc.execute(type, usern, editLastname.getText().toString(),"");
            editLastname.setHint(editLastname.getText().toString());
            editLastname.setText("");
        }
    }
    public void changePassword(View view){
        oldPassword = (EditText) findViewById(R.id.oldPassword);
        newPassword = (EditText)findViewById(R.id.newPassword);
        newPassword1 = (EditText)findViewById(R.id.newPassword1);
        if(!newPassword1.getText().toString().equals(newPassword.getText().toString())){
            LayoutInflater inflater = getLayoutInflater();
            // Inflate the Layout
            View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.custom_toast_layout));
            Toast toast = Toast.makeText(this, R.string.password_do_not_match, Toast.LENGTH_LONG);
            View toastView = toast.getView();
            TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
            toastMessage.setTextSize(14);
            toastMessage.setShadowLayer(0, 0, 0, 0);
            toastMessage.setTextColor(this.getResources().getColor(R.color.colorPrimaryWhite));
            toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.warning_icon, 0, 0, 0);
            toastMessage.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
            toast.setGravity(Gravity.BOTTOM,0,20);
            toastMessage.setCompoundDrawablePadding(15);
            toastView.setBackground(this.getResources().getDrawable(R.drawable.toast_back));
            toast.show();
            newPassword.setText(""); newPassword1.setText("");
        }
        else {
            String type = "password";
            SharedPreferences prefs = getSharedPreferences("X",MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.commit();
            BackgroundWorker bc = new BackgroundWorker(getApplicationContext());
            bc.execute(type, usern, oldPassword.getText().toString(),newPassword.getText().toString());
            newPassword.setText(""); newPassword1.setText(""); oldPassword.setText("");
        }
    }

    public void onBackPressed() {
        finish();
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
                final View mDrawerLayout = (RelativeLayout) findViewById(R.id.activity_my_account);
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