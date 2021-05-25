package com.example.enver.flinkhomev1;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.enver.flinkhomev1.esp.EsptouchDemoActivity;
import java.util.ArrayList;
import java.util.List;

public class SelectDevice extends AppCompatActivity {
    ArrayList<String> data = new ArrayList<>();
    ListView devicetypes;
    CheckBox check;
    Button next;
    Bundle  bundle;
    ImageButton imgLeft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_device);
        if (isOnline() == false) {
            notOnline();
        }
        else {
            bundle = new Bundle();
            devicetypes = (ListView) findViewById(R.id.devicetypes);
            imgLeft = (ImageButton) findViewById(R.id.imgLeft);
            imgLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    overridePendingTransition(R.anim.animation_on, R.anim.animation_off);
                }
            });
            devicetypes.setDivider(null);
            check = (CheckBox) findViewById(R.id.check);
            next = (Button) findViewById(R.id.next);
            next.setClickable(false);
            next.setBackgroundResource(R.drawable.step_button_disabled);
            data.add("Flink Cool");
            devicetypes.setAdapter(new SelectDevice.MyListAdapter(this, R.layout.list_item_types, data));
        }
    }
        private class MyListAdapter extends ArrayAdapter<String> {
            private int layout;
            private MyListAdapter(Context context, int resource, List<String> objects) {
                super(context, resource, objects);
                layout = resource;
            }

            @NonNull
            @Override
            public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                SelectDevice.ViewHolder mainViewHolder = null;
                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(layout, parent, false);
                    SelectDevice.ViewHolder viewHolder = new SelectDevice.ViewHolder();
                    viewHolder.check = (CheckBox) convertView.findViewById(R.id.check);
                    viewHolder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            bundle.clear();
                            bundle.putString("device", getItem(position));
                            if (next.isClickable()) {
                                next.setClickable(false);
                                next.setBackgroundResource(R.drawable.step_button_disabled);
                            } else {
                                next.setClickable(true);
                                next.setBackgroundResource(R.drawable.step_button_all);
                            }
                        }
                    });
                    convertView.setTag(viewHolder);
                    viewHolder.check.setText(getItem(position));
                }
                else {
                    mainViewHolder = (SelectDevice.ViewHolder) convertView.getTag();
                    mainViewHolder.check.setText(getItem(position));
                }
                return convertView;
            }
        }
        public class ViewHolder {
            CheckBox check;
        }
    public void Next (View view){
        Intent intent = new Intent(this,EsptouchDemoActivity.class);
        Bundle bandl = getIntent().getExtras();
        ArrayList<String> data = new ArrayList<>();
        data = bandl.getStringArrayList("sobe");
        bundle.putStringArrayList("sobe",data);
        bundle.putStringArrayList("datadevices",bandl.getStringArrayList("datadevices"));
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void onBackPressed() {
        finish();
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
                final View mDrawerLayout = (ConstraintLayout) findViewById(R.id.activity_select_device);
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