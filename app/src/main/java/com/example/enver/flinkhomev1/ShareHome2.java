package com.example.enver.flinkhomev1;

import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShareHome2 extends AppCompatActivity {
    ImageButton imgLeft;
    Bundle bundle;
    String aa;
    ListView listview;
    ArrayList<String> data = new ArrayList<>();
    ArrayList<String> data2 = new ArrayList<>();
    ArrayList<String> data3 = new ArrayList<>();
    ArrayList<String> rooms = new ArrayList<>();
    ArrayList<String> devices = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_home2);
        if (isOnline() == false) {
            notOnline();
        } else {
            listview = (ListView) findViewById(R.id.listview);
            imgLeft = (ImageButton) findViewById(R.id.imgLeft);
            imgLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
            });
            bundle = getIntent().getExtras();
            aa = bundle.getString("RoomsDevices","");

            for(int i=0;i<aa.length();i++){
                String name = "";
                if(aa.charAt(i)=='$'){
                    i++;
                    while(i<aa.length() && aa.charAt(i)!='$' && aa.charAt(i)!='#'){
                        name+=aa.charAt(i);
                        i++;
                    }
                    i--;
                    data.add(name); data2.add("Room"); data3.add("Clickable");
                }
                name = "";
                if(aa.charAt(i)=='#'){
                    i++;
                    while(i<aa.length() && aa.charAt(i)!='$' && aa.charAt(i)!='#'){
                        name+=aa.charAt(i);
                        i++;
                    }
                    i--;
                    data.add(name); data2.add("Device"); data3.add("Notclickable");
                }
            }

            listview.setAdapter(new MyListAdapter(getApplicationContext(), R.layout.list_item_sharehome, data));
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                }
            });
        }

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
            if(data2.get(position).equals("Device")) layout = R.layout.list_item_sharehome2;
            else layout = R.layout.list_item_sharehome;
            ShareHome2.ViewHolder mainViewHolder = null;
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                final ShareHome2.ViewHolder viewHolder = new ShareHome2.ViewHolder();
                viewHolder.device = (CheckBox) convertView.findViewById(R.id.device);
                convertView.setTag(viewHolder);
                viewHolder.device.setText(getItem(position));
                if(data3.get(position).equals("Notclickable")) {
                    viewHolder.device.setClickable(false);
                }else {
                    viewHolder.device.setClickable(true);
                }

                if(data2.get(position).equals("Room")) {
                    viewHolder.device.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Integer i = 1;
                            while (data2.get(position+i).equals("Device")){
                              CheckBox c =  (CheckBox) listview.getItemAtPosition(position+i);
                                c.setClickable(true);
                                i++;
                            }
                        }
                    });
                }

            }
            else{
                mainViewHolder = (ShareHome2.ViewHolder) convertView.getTag();
                mainViewHolder.device.setText(getItem(position));
            }
            return convertView;
        }
    }
    public class ViewHolder{
        CheckBox device;
    }



    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public boolean isOnline() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mob_avail = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mWifi.isConnected() || mob_avail.isConnected()) return true;
        return false;
    }

    public void notOnline() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                final ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.nowifipopup, null);
                final PopupWindow popupWindow = new PopupWindow(container, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
                final View mDrawerLayout = (ConstraintLayout) findViewById(R.id.activity_share_home_2);
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
