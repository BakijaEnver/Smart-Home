package com.example.enver.flinkhomev1;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class Calendar extends AppCompatActivity {

    ArrayList<String> data = new ArrayList<>();
    FloatingActionButton batoncina;
    RelativeLayout activity;
    LayoutInflater layoutInflater;
    SharedPreferences prefs;
    String deviceName;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        if(isOnline()==false){
            notOnline();
        }else {
         //   batoncina = (FloatingActionButton) findViewById(R.id.batoncina);
            lv = (ListView) findViewById(R.id.listview);
            activity = (RelativeLayout) findViewById(R.id.activity_calendar);
            Bundle b = getIntent().getExtras();
            data = b.getStringArrayList("data");
            deviceName = b.getString("devicename");
           // lv.setAdapter(new Calendar.MyListAdapter(this, R.layout.list_item_calendar, data));
          /*  batoncina.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), AddCalendar.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("data", data);
                    bundle.putString("devicename", deviceName);
                    intent.putExtras(bundle);
                    finish();
                    startActivity(intent);
                }
            }); */
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
            Calendar.ViewHolder mainViewHolder = null;
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                Calendar.ViewHolder viewHolder = new Calendar.ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.NameOfDevice);
                viewHolder.Command = (TextView) convertView.findViewById(R.id.Command);
                viewHolder.Date = (TextView) convertView.findViewById(R.id.Date);
                convertView.setTag(viewHolder);
                viewHolder.title.setText( "Name of Device: "+getItem(position));
                viewHolder.Command.setText("Command: ");
                viewHolder.Date.setText("Date: ");
            }
            else{
                mainViewHolder = (Calendar.ViewHolder) convertView.getTag();
                mainViewHolder.title.setText("Name of Device: "+getItem(position));
                mainViewHolder.Command.setText("Command: ");
                mainViewHolder.Date.setText("Date: ");
            }
            return convertView;
        }
    }
    public class ViewHolder{
        TextView title,Command,Date;
    }

    public void Back (View view){
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
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
                final View mDrawerLayout = (RelativeLayout) findViewById(R.id.activity_calendar);
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
