package com.example.enver.flinkhomev1;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Sples extends AppCompatActivity {

    String selectedTemp="26C",devName,roomName;
    ConstraintLayout activity;
    TextView titlemain,titleroom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sples);

        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        Bundle b = getIntent().getExtras();
        titlemain = (TextView) findViewById(R.id.titlemain);
        titleroom = (TextView) findViewById(R.id.titleroom);
        activity = (ConstraintLayout) findViewById(R.id.activity_sples);
        devName = b.getString("devicename");
        roomName = prefs.getString("room", "");
        titlemain.setText(devName);
        titleroom.setText(roomName);
        selectedTemp = prefs.getString("SelectedTemp" + devName, "");
        if (!selectedTemp.equals("")) {
            backgroundChanger(Integer.parseInt(selectedTemp.substring(0, 2)) - 16);
        }
        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                overridePendingTransition(0, 0);
                activity.setAlpha(0);
                activity.animate().alpha(1).setDuration(200);
            }
        }, 1500);
    }


    public void backgroundChanger(Integer progress){
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
}
