package com.example.enver.flinkhomev1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.enver.flinkhomev1.Calendar;
import com.example.enver.flinkhomev1.R;

import java.util.ArrayList;


public class AddCalendar extends AppCompatActivity {

    String selectedSwitch="",selectedMode="",selectedFan="",selectedSwing="",deviceName;
    Integer selectedTemp;
    LinearLayout On,Off,Auto,Fan1,Cool,Dry,Heat,Low,Mid,High,Max,fs1,fs2,fs3,fs4,fs5;
    ConstraintLayout activity;
    LayoutInflater layoutInflater;
    TimePicker timePicker;
    ArrayList<String> data = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcalendar);

        if(isOnline()==false){
            notOnline();
        }else {
            timePicker = (TimePicker) findViewById(R.id.timePicker);
            timePicker.setIs24HourView(true);
            Button Close = (Button) findViewById(R.id.Close);
            Button Save = (Button) findViewById(R.id.Save);
            Button ModeBtn = (Button) findViewById(R.id.ModeBtn);
            Button FanBtn = (Button) findViewById(R.id.FanBtn);
            Button SwingBtn = (Button) findViewById(R.id.SwingBtn);
            Button TempBtn = (Button) findViewById(R.id.TempBtn);
            Button SwitchBtn = (Button) findViewById(R.id.SwitchBtn);
            activity = (ConstraintLayout) findViewById(R.id.addcalendar);
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Bundle bundle = getIntent().getExtras();
            deviceName = bundle.getString("devicename");

            data = bundle.getStringArrayList("data");

            Close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), Calendar.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putStringArrayList("data", data);
                    bundle1.putString("devicename", deviceName);
                    intent.putExtras(bundle1);
                    startActivity(intent);
                }
            });
            Save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    data.add(deviceName);
                    Toast.makeText(getApplicationContext(), "Calendar added.", Toast.LENGTH_SHORT).show();
                }
            });
            SwingBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.airpopup, null);
                    final PopupWindow AirPopup = new PopupWindow(container, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
                    AirPopup.showAtLocation(activity, Gravity.CENTER, 0, 0);
                    container.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            AirPopup.dismiss();
                            return false;
                        }
                    });
                }
            });
            FanBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.fanpopup, null);
                    final PopupWindow FanPopup = new PopupWindow(container, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
                    FanPopup.showAtLocation(activity, Gravity.CENTER, 0, 0);
                    container.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            FanPopup.dismiss();
                            return false;
                        }
                    });
                }
            });
            ModeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.menupopup, null);
                    final PopupWindow MenuPopup = new PopupWindow(container, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
                    MenuPopup.showAtLocation(activity, Gravity.BOTTOM, 0, 0);
                    container.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            MenuPopup.dismiss();
                            return false;
                        }
                    });
                    Auto = (LinearLayout) container.findViewById(R.id.Auto);
                    if (selectedMode.equals("Auto")) {
                        Auto.setBackgroundColor(Color.RED);
                    }
                    Auto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Auto.setBackgroundColor(Color.RED);
                        }
                    });
                    Cool = (LinearLayout) container.findViewById(R.id.Cool);
                    if (selectedMode.equals("Cool")) {
                        Cool.setBackgroundColor(Color.RED);
                    }
                    Cool.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    Dry = (LinearLayout) container.findViewById(R.id.Dry);
                    if (selectedMode.equals("Dry")) {
                        Dry.setBackgroundColor(Color.RED);
                    }
                    Dry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    Fan1 = (LinearLayout) container.findViewById(R.id.Fan1);
                    if (selectedMode.equals("Fan")) {
                        Fan1.setBackgroundColor(Color.RED);
                    }
                    Fan1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    Heat = (LinearLayout) container.findViewById(R.id.Heat);
                    if (selectedMode.equals("Heat")) {
                        Heat.setBackgroundColor(Color.RED);
                    }
                    Heat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                }
            });
            SwitchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.onoffpopup, null);
                    On = (LinearLayout) container.findViewById(R.id.On);
                    Off = (LinearLayout) container.findViewById(R.id.Off);
                    if (selectedSwitch.equals("On")) On.setBackgroundColor(Color.RED);
                    else if (selectedSwitch.equals("Off")) Off.setBackgroundColor(Color.RED);
                    final PopupWindow OnOffPopup = new PopupWindow(container, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
                    OnOffPopup.showAtLocation(activity, Gravity.BOTTOM, 0, 0);
                    container.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            OnOffPopup.dismiss();
                            return false;
                        }
                    });
                    On.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            On.setBackgroundColor(Color.RED);
                            Off.setBackground(getResources().getDrawable(R.drawable.device_popup_line_back));
                            selectedSwitch = "On";
                        }
                    });
                    Off.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Off.setBackgroundColor(Color.RED);
                            On.setBackground(getResources().getDrawable(R.drawable.device_popup_line_back));
                            selectedSwitch = "Off";
                        }
                    });
                }
            });
            TempBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.temppopup, null);
                    final PopupWindow TempPopup = new PopupWindow(container, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
                    TempPopup.showAtLocation(activity, Gravity.BOTTOM, 0, 0);
                    container.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            TempPopup.dismiss();
                            return false;
                        }
                    });
                    NumberPicker numberPicker = (NumberPicker) container.findViewById(R.id.numberPicker);
                    numberPicker.setMinValue(16);
                    numberPicker.setMaxValue(31);
                    numberPicker.setDisplayedValues(new String[]{"16°", "17°", "18°", "19°", "20°", "21°", "22°", "23°", "24°", "25°", "26°", "27°", "28°", "29°", "30°", "31°"});
                }
            });
        }
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
                final View mDrawerLayout = (RelativeLayout) findViewById(R.id.addcalendar);
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
