package com.example.enver.flinkhomev1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.triggertrap.seekarc.SeekArc;

import org.eclipse.paho.android.service.MqttAndroidClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Demo extends AppCompatActivity {

    Button History,Editremote,Calendar;
    LinearLayout dim_layout,Cool, Dry, Heat, Auto, Fan1,Low,Mid,High,Max,One,Two,Three,Four,Five, humidity_room, temperature_room;
    String selectedMode="2",selectedFan="8",selectedSwing="13",selectedTemp="22C",selectedState="1",data1[],usern,roomName,devName,chipid,uniqueId,aa;
    ImageButton OnOff,Mode,Fan,Air,DeviceSettings,imgLeft,imgRight;
    LayoutInflater layoutInflater;
    PopupWindow popupWindow;
    RelativeLayout drawer_left,drawer_right;
    ActionBarDrawerToggle mDrawerToggle;
    TextView titlemain,titleroom,turnedOff,valueSeek,textFan,textMode,textSwing,menu_username,menu_email,stepen;
    boolean status=true;
    SeekArc sb;
    ImageView image;
    int numberOfRooms = 0;
    ProgressBar loadingrooms;
    Bundle b;
    MqttAndroidClient client;
    ArrayList<String> data = new ArrayList<>();
    SharedPreferences prefs;
    ConstraintLayout activity1;
    View child;
    CountDownTimer co,co1;
    Toast tost;
    CountDownTimer toastCountDown;
    ViewGroup kontenjer;
    ListView lv;
    SharedPreferences.Editor editor;
    DrawerLayout activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        prefs = getSharedPreferences("X", MODE_PRIVATE);
        editor = prefs.edit();
        editor.putString("SelectedModeDemodevice","2");
        editor.putString("SelectedFanDemodevice","7");
        editor.putString("SelectedSwingDemodevice","11");
        editor.apply();
        final Animation myFadeInAnimation = AnimationUtils.loadAnimation(Demo.this, R.anim.anima_device_on);
        final Animation myFadeOutAnimation = AnimationUtils.loadAnimation(Demo.this, R.anim.anima_device_off);
        Mode = (ImageButton) findViewById(R.id.Mode);
        Fan = (ImageButton) findViewById(R.id.Fan);
        History = (Button) findViewById(R.id.History);
        Air = (ImageButton) findViewById(R.id.Air);
        menu_email = (TextView) findViewById(R.id.menu_email);
        menu_username = (TextView) findViewById(R.id.menu_username);
        menu_email.setText("Demouser@mail.com");
        menu_username.setText("Demo Username");
        OnOff = (ImageButton) findViewById(R.id.OnOff);
        imgLeft = (ImageButton) findViewById(R.id.imgLeft);
        imgRight = (ImageButton) findViewById(R.id.imgRight);
        drawer_left = (RelativeLayout) findViewById(R.id.drawer_left);
        drawer_right = (RelativeLayout) findViewById(R.id.drawer_right);
        DeviceSettings = (ImageButton) findViewById(R.id.DeviceSettings);
        temperature_room = (LinearLayout) findViewById(R.id.temperature_room);
        humidity_room = (LinearLayout) findViewById(R.id.humidity_room);
        turnedOff = (TextView) findViewById(R.id.textViewOff);
        textFan = (TextView) findViewById(R.id.textFan);
        textSwing = (TextView) findViewById(R.id.textSwing);
        textMode = (TextView) findViewById(R.id.textMode);
        stepen = (TextView) findViewById(R.id.stepen);
        activity = (DrawerLayout) findViewById(R.id.activity_device);
        titlemain = (TextView) findViewById(R.id.titlemain);
        titleroom = (TextView) findViewById(R.id.titleroom);
        sb = (SeekArc) findViewById(R.id.tempslider);
        valueSeek = (TextView) findViewById(R.id.valueSeek);
        titlemain.setText("Demo Device");
        titleroom.setText("Demo Room");
        selectedTemp = "22";
        sb.setProgress(6);
        backgroundChanger1(6);

        sb.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {
            @Override
            public void onProgressChanged(SeekArc seekArc, int progress, boolean fromUser) {
                valueSeek.setText(Integer.toString(progress + 16));
                backgroundChanger1(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekArc sb) {
            }

            @Override
            public void onStopTrackingTouch(SeekArc sb) {
            }
        });

        Mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupWindow fadePopup = fade();
                fadePopup.setAnimationStyle(R.style.animationName);
                ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.menupopup, null);
                final PopupWindow MenuPopup = new PopupWindow(container, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
                MenuPopup.setAnimationStyle(R.style.animationName);
                MenuPopup.showAtLocation(activity, Gravity.BOTTOM, 0, 0);
                container.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        MenuPopup.setAnimationStyle(R.style.animationName);
                        MenuPopup.dismiss();
                        fadePopup.setAnimationStyle(R.style.animationName);
                        fadePopup.dismiss();
                        return true;
                    }
                });
                selectedMode = prefs.getString("SelectedModeDemodevice", "");
                Auto = (LinearLayout) container.findViewById(R.id.Auto);
                if (selectedMode.equals("2")) {
                    Auto.setBackground(getResources().getDrawable(R.drawable.device_popup_line_back));
                }
                Auto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editor.putString("SelectedModeDemodevice","2");
                        editor.apply();
                        MenuPopup.setAnimationStyle(R.style.animationName);
                        MenuPopup.dismiss();
                        fadePopup.setAnimationStyle(R.style.animationName);
                        fadePopup.dismiss();
                    }
                });
                Cool = (LinearLayout) container.findViewById(R.id.Cool);
                if (selectedMode.equals("4")) {
                    Cool.setBackground(getResources().getDrawable(R.drawable.device_popup_line_back));
                }
                Cool.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editor.putString("SelectedModeDemodevice","4");
                        editor.apply();
                        MenuPopup.setAnimationStyle(R.style.animationName);
                        MenuPopup.dismiss();
                        fadePopup.setAnimationStyle(R.style.animationName);
                        fadePopup.dismiss();
                    }
                });
                Dry = (LinearLayout) container.findViewById(R.id.Dry);
                if (selectedMode.equals("5")) {
                    Dry.setBackground(getResources().getDrawable(R.drawable.device_popup_line_back));
                }
                Dry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editor.putString("SelectedModeDemodevice","5");
                        editor.apply();
                        MenuPopup.setAnimationStyle(R.style.animationName);
                        MenuPopup.dismiss();
                        fadePopup.setAnimationStyle(R.style.animationName);
                        fadePopup.dismiss();
                    }
                });
                Fan1 = (LinearLayout) container.findViewById(R.id.Fan1);
                if (selectedMode.equals("3")) {
                    Fan1.setBackground(getResources().getDrawable(R.drawable.device_popup_line_back));
                }
                Fan1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editor.putString("SelectedModeDemodevice","3");
                        editor.apply();
                        MenuPopup.setAnimationStyle(R.style.animationName);
                        MenuPopup.dismiss();
                        fadePopup.setAnimationStyle(R.style.animationName);
                        fadePopup.dismiss();
                    }
                });
                Heat = (LinearLayout) container.findViewById(R.id.Heat);
                if (selectedMode.equals("6")) {
                    Heat.setBackground(getResources().getDrawable(R.drawable.device_popup_line_back));
                }
                Heat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editor.putString("SelectedModeDemodevice","6");
                        editor.apply();
                        MenuPopup.setAnimationStyle(R.style.animationName);
                        MenuPopup.dismiss();
                        fadePopup.setAnimationStyle(R.style.animationName);
                        fadePopup.dismiss();
                    }
                });
            }
        });


        Fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupWindow fadePopup = fade();
                fadePopup.setAnimationStyle(R.style.animationName);
                ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.fanpopup, null);
                final PopupWindow FanPopup = new PopupWindow(container, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
                FanPopup.setAnimationStyle(R.style.animationName);
                FanPopup.showAtLocation(activity, Gravity.CENTER, 0, 0);
                container.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        FanPopup.setAnimationStyle(R.style.animationName);
                        FanPopup.dismiss();
                        fadePopup.setAnimationStyle(R.style.animationName);
                        fadePopup.dismiss();
                        return true;
                    }
                });
                selectedFan = prefs.getString("SelectedFanDemodevice", "");
                Low = (LinearLayout) container.findViewById(R.id.Low);
                if (selectedFan.equals("7")) {
                    Low.setBackground(getResources().getDrawable(R.drawable.device_popup_line_back));
                }
                Low.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FanPopup.setAnimationStyle(R.style.animationName);
                        FanPopup.dismiss();
                        fadePopup.setAnimationStyle(R.style.animationName);
                        fadePopup.dismiss();
                        editor.putString("SelectedFanDemodevice","7");
                        editor.apply();
                    }
                });
                Mid = (LinearLayout) container.findViewById(R.id.Mid);
                if (selectedFan.equals("8")) {
                    Mid.setBackground(getResources().getDrawable(R.drawable.device_popup_line_back));
                }
                Mid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FanPopup.setAnimationStyle(R.style.animationName);
                        FanPopup.dismiss();
                        fadePopup.setAnimationStyle(R.style.animationName);
                        fadePopup.dismiss();
                        editor.putString("SelectedFanDemodevice","8");
                        editor.apply();
                    }
                });
                High = (LinearLayout) container.findViewById(R.id.High);
                if (selectedFan.equals("9")) {
                    High.setBackground(getResources().getDrawable(R.drawable.device_popup_line_back));
                }
                High.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FanPopup.setAnimationStyle(R.style.animationName);
                        FanPopup.dismiss();
                        fadePopup.setAnimationStyle(R.style.animationName);
                        fadePopup.dismiss();
                        editor.putString("SelectedFanDemodevice","9");
                        editor.apply();
                    }
                });
                Max = (LinearLayout) container.findViewById(R.id.Max);
                if (selectedFan.equals("10")) {
                    Max.setBackground(getResources().getDrawable(R.drawable.device_popup_line_back));
                }
                Max.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FanPopup.setAnimationStyle(R.style.animationName);
                        FanPopup.dismiss();
                        fadePopup.setAnimationStyle(R.style.animationName);
                        fadePopup.dismiss();
                        editor.putString("SelectedFanDemodevice","10");
                        editor.apply();
                    }
                });
            }
        });

        Air.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupWindow fadePopup = fade();
                fadePopup.setAnimationStyle(R.style.animationName);
                ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.airpopup, null);
                final PopupWindow AirPopup = new PopupWindow(container, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
                AirPopup.setAnimationStyle(R.style.animationName);
                AirPopup.showAtLocation(activity, Gravity.CENTER, 0, 0);
                container.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        AirPopup.setAnimationStyle(R.style.animationName);
                        AirPopup.dismiss();
                        fadePopup.setAnimationStyle(R.style.animationName);
                        fadePopup.dismiss();
                        return true;
                    }
                });
                selectedSwing = prefs.getString("SelectedSwingDemodevice", "");
                One = (LinearLayout) container.findViewById(R.id.One);
                if (selectedSwing.equals("11")) {
                    One.setBackground(getResources().getDrawable(R.drawable.device_popup_line_back));
                }
                One.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AirPopup.setAnimationStyle(R.style.animationName);
                        AirPopup.dismiss();
                        fadePopup.setAnimationStyle(R.style.animationName);
                        fadePopup.dismiss();
                        editor.putString("SelectedSwingDemodevice","11");
                        editor.apply();
                    }
                });
                Two = (LinearLayout) container.findViewById(R.id.Two);
                if (selectedSwing.equals("12")) {
                    Two.setBackground(getResources().getDrawable(R.drawable.device_popup_line_back));
                }
                Two.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AirPopup.setAnimationStyle(R.style.animationName);
                        AirPopup.dismiss();
                        fadePopup.setAnimationStyle(R.style.animationName);
                        fadePopup.dismiss();
                        editor.putString("SelectedSwingDemodevice","12");
                        editor.apply();
                    }
                });
                Three = (LinearLayout) container.findViewById(R.id.Three);
                if (selectedSwing.equals("13")) {
                    Three.setBackground(getResources().getDrawable(R.drawable.device_popup_line_back));
                }
                Three.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AirPopup.setAnimationStyle(R.style.animationName);
                        AirPopup.dismiss();
                        fadePopup.setAnimationStyle(R.style.animationName);
                        fadePopup.dismiss();
                        editor.putString("SelectedSwingDemodevice","13");
                        editor.apply();
                    }
                });
                Four = (LinearLayout) container.findViewById(R.id.Four);
                if (selectedSwing.equals("14")) {
                    Four.setBackground(getResources().getDrawable(R.drawable.device_popup_line_back));
                }
                Four.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AirPopup.setAnimationStyle(R.style.animationName);
                        AirPopup.dismiss();
                        fadePopup.setAnimationStyle(R.style.animationName);
                        fadePopup.dismiss();
                        editor.putString("SelectedSwingDemodevice","14");
                        editor.apply();
                    }
                });
                Five = (LinearLayout) container.findViewById(R.id.Five);
                if (selectedSwing.equals("15")) {
                    Five.setBackground(getResources().getDrawable(R.drawable.device_popup_line_back));
                }
                Five.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AirPopup.setAnimationStyle(R.style.animationName);
                        AirPopup.dismiss();
                        fadePopup.setAnimationStyle(R.style.animationName);
                        fadePopup.dismiss();
                        editor.putString("SelectedSwingDemodevice","15");
                        editor.apply();
                    }
                });
            }
        });


        DeviceSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                layoutInflater   = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container1 = (ViewGroup) layoutInflater.inflate(R.layout.fadepopup,null);
                final PopupWindow fadePopup = new PopupWindow(container1, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT ,false);
                fadePopup.showAtLocation(activity, Gravity.NO_GRAVITY,0,0);
                final ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.devicesettingspopup, null);
                final PopupWindow DeviceSettingsPopup = new PopupWindow(container, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                DeviceSettingsPopup.setAnimationStyle(R.style.animationName);
                DeviceSettingsPopup.showAtLocation(activity, Gravity.TOP, 140, 140);
                container.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        DeviceSettingsPopup.dismiss();
                        return true;
                    }
                });
                DeviceSettingsPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        fadePopup.dismiss();
                    }
                });
            }
        });

        final ImageView mImageViewFilling2 = (ImageView) findViewById(R.id.imageView4);
        final ImageView mImageViewFilling3 = (ImageView) findViewById(R.id.imageView3);
        mImageViewFilling3.setVisibility(View.INVISIBLE);
        turnedOff.setVisibility(View.INVISIBLE);
        OnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status) {
                        Mode.setClickable(false);
                        Fan.setClickable(false);
                        Air.setClickable(false);
                        Air.setClickable(false);
                        DeviceSettings.setClickable(false);
                        stepen.setVisibility(View.INVISIBLE);
                        stepen.startAnimation(myFadeOutAnimation);
                        temperature_room.setVisibility(View.INVISIBLE);
                        temperature_room.startAnimation(myFadeOutAnimation);
                        humidity_room.setVisibility(View.INVISIBLE);
                        humidity_room.startAnimation(myFadeOutAnimation);
                        valueSeek.setVisibility(View.INVISIBLE);
                        valueSeek.startAnimation(myFadeOutAnimation);
                        sb.setVisibility(View.INVISIBLE);
                        sb.startAnimation(myFadeOutAnimation);
                        turnedOff.setVisibility(View.VISIBLE);
                        turnedOff.startAnimation(myFadeInAnimation);
                        status = false;
                        mImageViewFilling3.setVisibility(View.INVISIBLE);
                        mImageViewFilling2.setVisibility(View.VISIBLE);
                        ((AnimationDrawable) mImageViewFilling2.getBackground()).start();
                } else {
                        Mode.setClickable(true);
                        Fan.setClickable(true);
                        Air.setClickable(true);
                        Air.setClickable(true);
                        DeviceSettings.setClickable(true);
                        stepen.setVisibility(View.VISIBLE);
                        stepen.startAnimation(myFadeInAnimation);
                        temperature_room.setVisibility(View.VISIBLE);
                        temperature_room.startAnimation(myFadeInAnimation);
                        humidity_room.setVisibility(View.VISIBLE);
                        humidity_room.startAnimation(myFadeInAnimation);
                        valueSeek.setVisibility(View.VISIBLE);
                        valueSeek.startAnimation(myFadeInAnimation);
                        sb.setVisibility(View.VISIBLE);
                        turnedOff.setVisibility(View.INVISIBLE);
                        turnedOff.startAnimation(myFadeOutAnimation);
                        sb.startAnimation(myFadeInAnimation);
                        status = true;
                        mImageViewFilling3.setVisibility(View.VISIBLE);
                        mImageViewFilling2.setVisibility(View.INVISIBLE);
                        ((AnimationDrawable) mImageViewFilling3.getBackground()).start();
                }
            }
        });


        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity.isDrawerOpen(drawer_left)) {
                    activity.closeDrawer(drawer_left);
                } else if (!activity.isDrawerOpen(drawer_left)) {
                    activity.openDrawer(drawer_left);
                }
                if (activity.isDrawerOpen(drawer_right)) {
                    activity.closeDrawer(drawer_right);
                }
            }
        });

        imgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity.isDrawerOpen(drawer_right)) {
                    activity.closeDrawer(drawer_right);
                } else if (!activity.isDrawerOpen(drawer_right)) {
                    activity.openDrawer(drawer_right);
                }
                if (activity.isDrawerOpen(drawer_left)) {
                    activity.closeDrawer(drawer_left);
                }
            }
        });

        data.add("Demo Room 1"); data.add("Demo Room 2");
        lv = (ListView) findViewById(R.id.listview);
        lv.setAdapter(new MyListAdapter(getApplicationContext(), R.layout.list_item, data));


    }

    private class MyListAdapter extends ArrayAdapter<String> {
        private int layout;
        private MyListAdapter(Context context, int resource, List<String> objects){
            super(context,resource,objects);
            layout = resource;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Demo.ViewHolder mainViewHolder = null;
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                Demo.ViewHolder viewHolder = new Demo.ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.tekst);
                convertView.setTag(viewHolder);
                viewHolder.title.setText(getItem(position));
            }
            else{
                mainViewHolder = (Demo.ViewHolder) convertView.getTag();
                mainViewHolder.title.setText(getItem(position));
            }
            return convertView;
        }
    }
    public class ViewHolder{
        TextView title;
    }

    public void backgroundChanger1(Integer progress){
        if( (progress + 16) <= 19 )
        {
            activity.setBackgroundResource(R.drawable.device_gradient1);
        }
        else if( (progress + 16) > 19 && (progress + 16) <=23)
        {
            activity.setBackgroundResource(R.drawable.device_gradient2);
        }

        else if( (progress + 16) > 23 && (progress + 16) <=27)
        {
            activity.setBackgroundResource(R.drawable.device_gradient3);
        }

        else if( (progress + 16) > 27 && (progress + 16) <=31)
        {
            activity.setBackgroundResource(R.drawable.device_gradient4);
        }
    }

    public PopupWindow fade (){
        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup container1 = (ViewGroup) layoutInflater.inflate(R.layout.fadepopup,null);
        final PopupWindow fadePopup = new PopupWindow(container1, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT ,false);
        fadePopup.showAtLocation(activity, Gravity.NO_GRAVITY,0,0);
        kontenjer = container1;
        return fadePopup;
    }


    public void Home(View v){
        activity.closeDrawer(drawer_right);
    }

    public void Myaccount(View v){}

    public void Logout(View v){}

    public void AddRoom(View v) {}

    public void Sharehome(View v){}

    public void closeLeftDrawer(View view){
        activity.closeDrawer(drawer_left);
    }
    public void closeRightDrawer(View view){
        activity.closeDrawer(drawer_right);
    }

    public void Settings(View v){
    }
    public void Helpsupport(View v){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.flinkaj.me"));
        startActivity(browserIntent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
}
