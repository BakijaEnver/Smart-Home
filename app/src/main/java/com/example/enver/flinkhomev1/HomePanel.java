package com.example.enver.flinkhomev1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.lang.String.*;

public class HomePanel extends AppCompatActivity{
    Toolbar t1;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    TextView Username,Email,menu_username,menu_email,welcome_text_room_device;
    ImageButton imgLeft,imgRight;
    RelativeLayout drawer_right;
    PopupWindow popupWindow;
    LayoutInflater layoutInflater;
    Button addRoomStart;
    ArrayList<String> data = new ArrayList<>();
    int numberOfRooms = 0;
    String data1[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("log","true");
        editor.commit();
        setContentView(R.layout.activity_home_panel);
        if(isOnline()==false){
            notOnline();
        }
        else {
            t1 = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(t1);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_home_panel);
            imgLeft = (ImageButton) findViewById(R.id.imgLeft);
            imgLeft.setVisibility(View.INVISIBLE);
            Username = (TextView) findViewById(R.id.menu_username);
            Email = (TextView) findViewById(R.id.menu_email);
            imgRight = (ImageButton) findViewById(R.id.imgRight);
            addRoomStart = (Button) findViewById(R.id.addRoomStart);
            menu_email = (TextView) findViewById(R.id.menu_email);
            menu_username = (TextView) findViewById(R.id.menu_username);
            welcome_text_room_device = (TextView) findViewById(R.id.welcome_text_room_device);
            prefs = getSharedPreferences("X", MODE_PRIVATE);
            String usern = prefs.getString("usr", "");
            StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
            getData("http://159.203.107.114/flinkphp/rooms1.php?Username=" + usern);
            String podaci = data1[0];
            Pattern pattern = Pattern.compile("((\\w|\\.|\\@|_|-)+)");
            Matcher matcher = pattern.matcher(podaci);
            while (matcher.find()) {
                if (matcher.group().equals("Email")) {
                    matcher.find();
                    menu_email.setText(matcher.group());
                }
                if (matcher.group().equals("Numberofrooms")) {
                    matcher.find();
                    numberOfRooms = Integer.parseInt(matcher.group());
                }
                if (matcher.group().equals("Username")) {
                    matcher.find();
                    menu_username.setText(matcher.group());
                }
            }

            if (numberOfRooms == 0) {
                addRoomStart.setVisibility(View.VISIBLE);
                welcome_text_room_device.setText("Hey " + usern + getResources().getString(R.string.dont_have_any_room));
            }
            else {
                addRoomStart.setVisibility(View.GONE);
            }

            getData("http://159.203.107.114/flinkphp/rooms.php?Username=" + usern);
            for (int i = 0; i < numberOfRooms; i++) {
                podaci = data1[i];
                pattern = Pattern.compile("(([A-Z]|[a-z]|\\s|\\d|\\w)+)");
                matcher = pattern.matcher(podaci);
                matcher.find();
                matcher.find();
                data.add(matcher.group());
            }

            //      lv.setAdapter(new MyListAdapter(this, R.layout.list_item, data));
            //Armin////////////////////////////////
            //  lv.setDivider(null);
            ////////////////////////////////////
            /*   lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Bundle b = new Bundle();
                    b.putString("key", data.get(position));
                    b.putBoolean("new", false);
                    Intent intent = new Intent(getApplicationContext(), Room.class);
                    intent.putExtras(b);
                    startActivity(intent);
                    ///////////////////////////////////////////
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    ///////////////////////////////////////////
                }
             }); */

            drawer_right = (RelativeLayout) findViewById(R.id.drawer_right);
            mDrawerLayout.setDrawerListener(mDrawerToggle);
            /* imgLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mDrawerLayout.isDrawerOpen(drawer_left)) {
                        mDrawerLayout.closeDrawer(drawer_left);

                    } else if (!mDrawerLayout.isDrawerOpen(drawer_left)) {
                        mDrawerLayout.openDrawer(drawer_left);
                    }
                    if (mDrawerLayout.isDrawerOpen(drawer_right)) {
                        mDrawerLayout.closeDrawer(drawer_right);
                    }
                }
            });*/
            imgRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mDrawerLayout.isDrawerOpen(drawer_right)) {
                        mDrawerLayout.closeDrawer(drawer_right);
                    } else if (!mDrawerLayout.isDrawerOpen(drawer_right)) {
                        mDrawerLayout.openDrawer(drawer_right);
                    }
                   /* if (mDrawerLayout.isDrawerOpen(drawer_left)) {
                        mDrawerLayout.closeDrawer(drawer_left);
                    }*/
                }
            });
        }
    }



    public void Logout(View v){
        final PopupWindow fadePopup = fade();
        fadePopup.setAnimationStyle(R.style.animationName);
        ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.logoutpopup,null);
        final PopupWindow DeleteDevicePopup = new PopupWindow(container, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        DeleteDevicePopup.setAnimationStyle(R.style.animationName);
        DeleteDevicePopup.showAtLocation(mDrawerLayout, Gravity.CENTER,0,0);
        Button Yes = (Button) container.findViewById(R.id.Yes);
        Button No = (Button) container.findViewById(R.id.No);
        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteDevicePopup.setAnimationStyle(R.style.animationName);
                DeleteDevicePopup.dismiss();
                fadePopup.setAnimationStyle(R.style.animationName);
                fadePopup.dismiss();
            }
        });
        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.putString("Logout",menu_username.getText().toString());
                editor.commit();
                Intent launchNextActivity;
                launchNextActivity = new Intent(getApplicationContext(),MainActivity.class);
                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(launchNextActivity);
                overridePendingTransition(R.anim.animation_on, R.anim.animation_off);
            }
        });

        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                fadePopup.setAnimationStyle(R.style.animationName);
                fadePopup.dismiss();
                DeleteDevicePopup.setAnimationStyle(R.style.animationName);
                DeleteDevicePopup.dismiss();
                return true;
            }
        });
   }

    public void AddRoom(View v){
        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.roompop,null);
        ViewGroup container1 = (ViewGroup) layoutInflater.inflate(R.layout.fadepopup,null);
        popupWindow = new PopupWindow(container, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        final PopupWindow fadePopup = new PopupWindow(container1, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT,false);
        fadePopup.showAtLocation(mDrawerLayout,Gravity.NO_GRAVITY,0,0);
        popupWindow.setAnimationStyle(R.style.animationName);
        fadePopup.setAnimationStyle(R.style.animationName);
        popupWindow.showAtLocation(mDrawerLayout, Gravity.CENTER, 0, 0);
        Button btn = (Button)container.findViewById(R.id.adRoom);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText roomName = (EditText)container.findViewById(R.id.roomName);
                if(roomName.getText().toString().isEmpty()) {
                    LayoutInflater inflater = getLayoutInflater();
                    // Inflate the Layout
                    View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.custom_toast_layout));
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.please_enter_new_name, Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(14);
                    toastMessage.setShadowLayer(0, 0, 0, 0);
                    toastMessage.setTextColor(getResources().getColor(R.color.colorPrimaryWhite));
                    toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.warning_icon, 0, 0, 0);
                    toastMessage.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    toast.setGravity(Gravity.BOTTOM,0,20);
                    toastMessage.setCompoundDrawablePadding(15);
                    toastView.setBackground(getResources().getDrawable(R.drawable.toast_back));
                    toast.show();
                    }
                    else {
                    data.add(valueOf(roomName.getText()));
                    fadePopup.setAnimationStyle(R.style.animationName);
                    fadePopup.dismiss();
                    popupWindow.setAnimationStyle(R.style.animationName);
                    popupWindow.dismiss();
                    numberOfRooms++;
                    SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
                    String type = "roomscommit";
                    BackgroundWorker backgroundWorker1 = new BackgroundWorker(getApplicationContext());
                    backgroundWorker1.execute(type, prefs.getString("usr", ""), valueOf(numberOfRooms), roomName.getText().toString());
                    addRoomStart.setVisibility(View.GONE);
                    Bundle b = new Bundle();
                    b.putString("key", roomName.getText().toString());
                    b.putBoolean("new", true);
                    Intent intent = new Intent(getApplicationContext(), Room.class);
                    intent.putExtras(b);
                    startActivity(intent);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    overridePendingTransition(R.anim.animation_on, R.anim.animation_off);
                    /*
                    ListView lv = (ListView) findViewById(R.id.listview);
                    lv.setAdapter(new MyListAdapter(getApplicationContext(), R.layout.list_item, data));
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            Toast.makeText(HomePanel.this, "List item " + position,Toast.LENGTH_SHORT).show();
                        }
                    });*/
                    }
            }
        });
        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popupWindow.setAnimationStyle(R.style.animationName);
                popupWindow.dismiss();
                return true;
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                fadePopup.setAnimationStyle(R.style.animationName);
                fadePopup.dismiss();
            }
        });
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
        } catch (Exception e) {
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

    public void Home(View v){
        mDrawerLayout.closeDrawer(drawer_right);
    }

   public void Myaccount(View v){
       startActivity(new Intent(this, MyAccount.class));
       overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
   }
    public void Sharehome(View v){
        startActivity(new Intent(this, ShareHome.class));
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
    public void Settings(View v){
        finish();
        Intent intent = new Intent(this, Settings.class);
        Bundle b = new Bundle();//ERVEN
        b.putString("key","No room");
        b.putString("aparati","HomePanel");
        intent.putExtras(b);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
    public void Helpsupport(View v){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.flinkaj.me"));
        startActivity(browserIntent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private class MyListAdapter extends ArrayAdapter<String>{
        private int layout;
        private MyListAdapter(Context context, int resource, List<String> objects){
            super(context,resource,objects);
            layout = resource;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder mainViewHolder = null;
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.tekst);
                convertView.setTag(viewHolder);
                viewHolder.title.setText(getItem(position));
            }
            else{
                mainViewHolder = (ViewHolder) convertView.getTag();
                mainViewHolder.title.setText(getItem(position));
            }
            return convertView;
        }
    }
    public class ViewHolder{
        TextView title;
    }

    public PopupWindow fade (){
        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup container1 = (ViewGroup) layoutInflater.inflate(R.layout.fadepopup,null);
        final PopupWindow fadePopup = new PopupWindow(container1, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ,false);
        fadePopup.showAtLocation(mDrawerLayout, Gravity.NO_GRAVITY,0,0);
        return fadePopup;
    }

    public boolean isOnline(){
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mob_avail = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mWifi.isConnected() || mob_avail.isConnected()) return true;
        return false;
    }

    public void closeRightDrawer(View view){
        mDrawerLayout.closeDrawer(drawer_right);
    }


    public void notOnline(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                final ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.nowifipopup,null);
                final PopupWindow popupWindow = new PopupWindow(container, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
                final View mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_home_panel);
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
