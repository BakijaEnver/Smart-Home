package com.example.enver.flinkhomev1;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;

public class AddDevice extends AppCompatActivity {
    EditText device_name;
    Button add_device;
    String chipid,ip,pass,usern;
    CheckBox addremote;
    String uniqueId;
    String Roomname;
    ArrayList<String> datadevices = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        if(isOnline()){
            device_name =  (EditText) findViewById(R.id.editText);
            add_device = (Button) findViewById(R.id.add_device);
            addremote = (CheckBox) findViewById(R.id.addremote);
            Bundle b = getIntent().getExtras();
            String arej[]=b.getString("device_name").split(" ");
            chipid = arej[1];
            datadevices = b.getStringArrayList("datadevices");
            device_name.setText(b.getString("device_name"));
            ip = b.getString("ip");
            pass = b.getString("Uuid");
            SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
            usern = prefs.getString("usr","");
            Roomname = prefs.getString("room","");
            uniqueId = b.getString("uniqueId");

            add_device.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //CHECK IF THERE EXISTS A DEVICE WITH THE SAME NAME
                    if(nameExists(device_name.getText().toString())){
                        Toast.makeText(AddDevice.this, "A device with this name already exists in this room. Please enter a different name for your device.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        BackgroundWorker bw = new BackgroundWorker(getApplicationContext());
                        bw.execute("devicescommit", usern, device_name.getText().toString(), Roomname, ip, uniqueId, chipid);
                        if (addremote.isChecked()) {
                            final Dialog dialog = new Dialog(AddDevice.this);
                            dialog.getWindow().getAttributes().windowAnimations = R.style.animationName;
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dialog_demo_device);
                            dialog.show();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString("chipid", chipid);
                                    editor.putString("uuid", uniqueId);
                                    editor.commit();
                                    Intent intent = new Intent(getApplicationContext(), AddRemote.class);
                                    Bundle bandl = getIntent().getExtras();
                                    Bundle b = new Bundle();
                                    ArrayList<String> data = new ArrayList<>();
                                    data = bandl.getStringArrayList("sobe");
                                    b.putStringArrayList("Roomslist", data);
                                    b.putBoolean("backallowed", false);
                                    b.putString("devicename", device_name.getText().toString());
                                    intent.putExtras(b);
                                    dialog.dismiss();
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.animation_on, R.anim.animation_off);
                                }
                            }, 1500);
                        } else {
                            final Dialog dialog = new Dialog(AddDevice.this);
                            dialog.getWindow().getAttributes().windowAnimations = R.style.animationName;
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dialog_demo_device);
                            dialog.show();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString("chipid", chipid);
                                    editor.putString("uuid", uniqueId);
                                    editor.commit();
                                    Intent intent = new Intent(getApplicationContext(), Device.class);
                                    Bundle bandl = getIntent().getExtras();
                                    Bundle b = new Bundle();
                                    ArrayList<String> data = new ArrayList<>();
                                    data = bandl.getStringArrayList("sobe");
                                    b.putStringArrayList("Roomslist", data);
                                    b.putString("devicename", device_name.getText().toString());
                                    intent.putExtras(b);
                                    dialog.dismiss();
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.animation_on, R.anim.animation_off);
                                }
                            }, 1500);
                        }
                    }
                }
            });
        }
        else{
            notOnline();
        }

    }

    public boolean nameExists(String name){
        for(int i=0;i<datadevices.size();i++){
            if(datadevices.get(i).equals(name)) return true;
        }
        return false;
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
                final View mDrawerLayout = (ConstraintLayout) findViewById(R.id.activity_add_device);
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
    public void onBackPressed() { }

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
