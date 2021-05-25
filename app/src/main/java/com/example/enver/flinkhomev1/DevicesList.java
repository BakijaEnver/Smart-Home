package com.example.enver.flinkhomev1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.PopupWindow;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.params.BasicHttpParams;

public class DevicesList extends AppCompatActivity {
    ArrayList<String> data = new ArrayList<>();
    ListView devices;
    String aa,devicetype,uniqueId,okcheck="",usern,pass,chipid,ip;
    CheckBox device;
    String ajpi[] = new String[10];
    String Uuid[] = new String[10];
    Intent intent;
    ConstraintLayout activity;
    Bundle b;
    LayoutInflater layoutInflater;
    Button moveon,okk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_list);
        if(isOnline()==false){
            notOnline();
        }
        else {
            devices = (ListView) findViewById(R.id.devices);
            device = (CheckBox) findViewById(R.id.device);
            moveon = (Button) findViewById(R.id.moveon);
            moveon.setClickable(false);
            moveon.setBackgroundResource(R.drawable.step_button_disabled);
            activity = (ConstraintLayout) findViewById(R.id.activity_devices_list);
            SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
            devicetype = prefs.getString("device", "");
            usern = prefs.getString("usr", "");
            Integer count = 0, count1;
            count1 = prefs.getInt("count1", count);
            for (int i = 1; i <= count1; i++) {
                String sel = "esp" + String.valueOf(i);
                String esp = prefs.getString(sel, "");
                ajpi[i] = prefs.getString(sel + "ip", "");
                Uuid[i] = prefs.getString(sel + "Uuid", "");
                data.add(esp);
            }
            devices.setAdapter(new DevicesList.MyListAdapter(this, R.layout.list_item_device, data));
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
            DevicesList.ViewHolder mainViewHolder = null;
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                DevicesList.ViewHolder viewHolder = new DevicesList.ViewHolder();
                viewHolder.device = (CheckBox) convertView.findViewById(R.id.device);
                convertView.setTag(viewHolder);
                viewHolder.device.setText(getItem(position));
                viewHolder.device.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        uniqueId = UUID.randomUUID().toString();
                        uniqueId = uniqueId.replace("-","");
                        if(moveon.isClickable()){
                            moveon.setClickable(false);
                            moveon.setBackgroundResource(R.drawable.step_button_disabled);
                        }
                        else {
                            moveon.setClickable(true);
                            moveon.setBackgroundResource(R.drawable.step_button_all);
                        }
                        intent = new Intent(getApplicationContext(),AddDevice.class);
                        Bundle bandl = getIntent().getExtras();
                        ArrayList<String> data = new ArrayList<>();
                        data = bandl.getStringArrayList("sobe");
                        b = new Bundle();
                        b.putStringArrayList("datadevices",bandl.getStringArrayList("datadevices"));
                        b.putStringArrayList("sobe",data);
                        b.putString("device_name",getItem(position));
                        b.putString("ip",ajpi[position+1]);
                        b.putString("Uuid",Uuid[position+1]);
                        b.putString("uniqueId",uniqueId);
                        String arej[]=getItem(position).split(" ");
                        chipid = arej[1];
                        pass = Uuid[position+1];
                        intent.putExtras(b);
                        ip = ajpi[position+1];
                    }
                });
            }
            else{
                mainViewHolder = (DevicesList.ViewHolder) convertView.getTag();
                mainViewHolder.device.setText(getItem(position));
            }
            return convertView;
        }
    }
    public class ViewHolder{
        CheckBox device;
    }

    public void moveOn (View view){
        String clientId = MqttClient.generateClientId();
        final MqttAndroidClient client1 = new MqttAndroidClient(this.getApplicationContext(), "tcp://159.203.107.114:1883", clientId);
        MqttConnectOptions options = new MqttConnectOptions();
        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.configpopupnovi,null);
        final PopupWindow TimeOutPopup = new PopupWindow(container, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        TimeOutPopup.setAnimationStyle(R.style.animationName);
        TimeOutPopup.showAtLocation(activity, Gravity.NO_GRAVITY,0,0);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TimeOutPopup.setAnimationStyle(R.style.animationName);
                TimeOutPopup.dismiss();
            }
        }, 10000);
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
        options.setUserName("vehabija");
        options.setPassword("vehabija".toCharArray());
        try {
            IMqttToken token =  client1.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    String topic = uniqueId+"/"+chipid+"/+";
                    int qos = 1;
                    try {
                        IMqttToken subToken = client1.subscribe(topic, qos);
                        subToken.setActionCallback(new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                final int TIMEOUT_MILLISEC = 10000;  // = 10 seconds
                                String postMessage="{\"Username\":\""+usern+"\"," + "\"Password\":\""+pass+"\"," + "\"Uuid\":\""+uniqueId+"\"}";
                                cz.msebera.android.httpclient.params.HttpParams httpParams = new BasicHttpParams();
                                cz.msebera.android.httpclient.params.HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
                                cz.msebera.android.httpclient.params.HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
                                final HttpClient client = new DefaultHttpClient(httpParams);
                                HttpPost request = new HttpPost("http://"+ip+"/config");
                                try {
                                    request.setEntity(new ByteArrayEntity(
                                            postMessage.toString().getBytes("UTF8")));
                                }
                                catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    client.execute(request);
                                }
                                catch (IOException e) {
                                    e.printStackTrace();
                                }
                                MqttCallback mqttCallback = new MqttCallback() {
                                    @Override
                                    public void connectionLost(Throwable cause) {}

                                    @Override
                                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                                        okcheck=message.toString();
                                        String topic1 =  chipid+"/"+uniqueId+"/ok";
                                        String payload = "LUK ARAUND MAJ LITL BEJBI";
                                        byte[] encodedPayload = new byte[0];
                                        try {
                                            encodedPayload = payload.getBytes("UTF-8");
                                            MqttMessage message1 = new MqttMessage(encodedPayload);
                                            IMqttToken clientpublish = client1.publish(topic1, message1);
                                            clientpublish.setActionCallback(new IMqttActionListener() {
                                                @Override
                                                public void onSuccess(IMqttToken asyncActionToken) {
                                                }
                                                @Override
                                                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                                    final PopupWindow fadePopup = fade();
                                                    fadePopup.setAnimationStyle(R.style.animationName);
                                                    ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.timeoutpopup,null);
                                                    final PopupWindow TimeOutPopup = new PopupWindow(container, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                                                    TimeOutPopup.showAtLocation(activity, Gravity.CENTER,0,0);
                                                    TimeOutPopup.setAnimationStyle(R.style.animationName);
                                                    okk = (Button) container.findViewById(R.id.okk);
                                                    okk.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            /*Bundle b = new Bundle();
                                                            b.putString("device",devicetype);
                                                            Intent intent = new Intent(getApplicationContext(),EsptouchDemoActivity.class);
                                                            intent.putExtras(b);
                                                            startActivity(intent);*/
                                                            finish();
                                                        }
                                                    });
                                                }
                                            });
                                        } catch (UnsupportedEncodingException | MqttException e) {
                                            e.printStackTrace();
                                        }
                                        client1.unsubscribe(topic);
                                        client1.disconnect();
                                    }
                                    @Override
                                    public void deliveryComplete(IMqttDeliveryToken token) {}
                                };
                                client1.setCallback(mqttCallback);
                            }
                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {}
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    //Toast.makeText(getApplicationContext(),exception.toString(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        new CountDownTimer(10000,1000){
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                if(!okcheck.equals("OK")){
                    final PopupWindow fadePopup = fade();
                    ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.timeoutpopup,null);
                    final PopupWindow TimeOutPopup = new PopupWindow(container, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                    TimeOutPopup.showAtLocation(activity, Gravity.CENTER,0,0);
                    TimeOutPopup.setAnimationStyle(R.style.animationName);
                    okk = (Button) container.findViewById(R.id.okk);
                    okk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            /*Bundle b = new Bundle();
                            b.putString("device",devicetype);
                            Intent intent = new Intent(getApplicationContext(),EsptouchDemoActivity.class);
                            intent.putExtras(b);
                            startActivity(intent);*/
                            finish();
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        }
                    });
                }
                else{
                    Intent intent = new Intent(getApplicationContext(),AddDevice.class);
                    intent.putExtras(b);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            }
        }.start();


        if(okcheck.equals("OK")) {
            startActivity(intent);
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
            aa=result;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public PopupWindow fade (){
        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup container1 = (ViewGroup) layoutInflater.inflate(R.layout.fadepopup,null);
        final PopupWindow fadePopup = new PopupWindow(container1, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT,false);
        fadePopup.showAtLocation(activity, Gravity.NO_GRAVITY,0,0);
        return fadePopup;
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
                final View mDrawerLayout = (ConstraintLayout) findViewById(R.id.activity_devices_list);
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
                        popupWindow.setAnimationStyle(R.style.animationName);
                        popupWindow.dismiss();
                        finish();
                    }
                });
            }
        }, 200);
    }
}
