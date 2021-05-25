package com.example.enver.flinkhomev1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SwithCom extends AppCompatActivity {

    ArrayList<String> dataPower = new ArrayList<>();
    ListView commandsPower;
    LayoutInflater layoutInflater;
    RelativeLayout activity;
    String espResponse="no";
    SwithCom.CountDownTimer brojac;
    TextView textCommandPopup;
    String title ="";
    ImageView success;
    ImageView failed;
    ImageView imgCommandPopup;
    TextView textWaiting;
    SharedPreferences prefs;
    String usern,roomName,devName,aa,AREJ[];
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swith_com);
        bundle = getIntent().getExtras();
        devName = bundle.getString("devicename");

        if(isOnline()) {
            activity = (RelativeLayout) findViewById(R.id.swithCom_activity);
            commandsPower = (ListView) findViewById(R.id.commandsPower);
            commandsPower.setDivider(null);
            prefs = getSharedPreferences("X",MODE_PRIVATE);
            usern = prefs.getString("usr","");
            roomName = prefs.getString("room","");
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            dataPower.add("1");
            dataPower.add("32");
            commandsPower.setAdapter(new SwithCom.MyGridAdapter(this, R.layout.list_item_command, dataPower));
            AREJ = new String[2];
            String roomNameurl = roomName.replace(" ", "%20");
            String deviceNameurl = devName.replace(" ", "%20");
            Integer intager=0;
            getData("http://159.203.107.114/flinkphp/commandget.php?Username=" + usern +"&Devicename="+ deviceNameurl +"&Roomname=" + roomNameurl + "&Commandname=ComO");
            Pattern pattern = Pattern.compile("((\\w|\\.|\\@|_|-|\\s)+)");
            Matcher matcher = pattern.matcher(aa);
            System.out.println(aa);
            while (matcher.find()) {
                if(matcher.group().charAt(0) == 'C'){
                    matcher.find();
                    AREJ[intager]=matcher.group();
                    intager++;
                }
            }
        }
        else {
            notOnline();
        }
    }

    private class MyGridAdapter extends ArrayAdapter<String> {
        private int layout;
        private MyGridAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            layout = resource;
        }
        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            SwithCom.ViewHolder mainViewHolder = null;
            if(AREJ[position].equals("1"))  layout = R.layout.list_item_command_added;
            if(getItem(position).equals("1"))
                title = "ON";
            else if(getItem(position).equals("32"))
                title = "OFF";
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                SwithCom.ViewHolder viewHolder = new SwithCom.ViewHolder();
                viewHolder.command = (TextView) convertView.findViewById(R.id.command);
                convertView.setTag(viewHolder);
                viewHolder.command.setText(title);
                viewHolder.command.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final PopupWindow fadePopup =  fade();
                        fadePopup.setAnimationStyle(R.style.animationName);
                        final ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.commandpopup,null);
                        final PopupWindow CommandPopup = new PopupWindow(container, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                        CommandPopup.setAnimationStyle(R.style.animationName);
                        CommandPopup.showAtLocation(activity, Gravity.CENTER,0,0);
                        TextView commandname = (TextView) container.findViewById(R.id.commandname);
                        textCommandPopup = (TextView) container.findViewById(R.id.textCommandPopup);
                        success = (ImageView) container.findViewById(R.id.success);
                        failed = (ImageView) container.findViewById(R.id.failed);
                        imgCommandPopup = (ImageView) container.findViewById(R.id.imgCommandPopup);
                        textWaiting = (TextView) container.findViewById(R.id.textWaiting);
                        success.setVisibility(View.INVISIBLE);
                        failed.setVisibility(View.INVISIBLE);
                        if(getItem(position).equals("1"))
                            title = "ON";
                        else if(getItem(position).equals("32"))
                            title = "OFF";
                        commandname.setText(title);

                        //CONNECT TO BROKER
                        String clientId = MqttClient.generateClientId();
                        final MqttAndroidClient client = new MqttAndroidClient(getApplicationContext(), "tcp://159.203.107.114:1883", clientId);
                        final MqttConnectOptions options = new MqttConnectOptions();
                        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
                        options.setUserName("vehabija");
                        options.setPassword("vehabija".toCharArray());
                        try {
                            IMqttToken token =  client.connect(options);
                            token.setActionCallback(new IMqttActionListener() {
                                @Override
                                public void onSuccess(IMqttToken asyncActionToken) {
                                    SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
                                    final SharedPreferences.Editor editor = prefs.edit();
                                    final String chipid = prefs.getString("chipid", "");
                                    final String uniqueId = prefs.getString("uuid","");
                                    final String usern = prefs.getString("usr","");
                                    String topic1 = chipid + "/" + uniqueId + "/addCommand";
                                    //   Toast.makeText(getApplicationContext(),topic1,Toast.LENGTH_SHORT).show();
                                    String payload = "{\"command\":\""+getItem(position)+"\",\"status\":\"add\"}";
                                    byte[] encodedPayload = new byte[0];
                                    try {
                                        encodedPayload = payload.getBytes("UTF-8");
                                        MqttMessage message1 = new MqttMessage(encodedPayload);
                                        IMqttToken clientpublish = client.publish(topic1, message1);
                                        clientpublish.setActionCallback(new IMqttActionListener() {
                                            @Override
                                            public void onSuccess(IMqttToken asyncActionToken) {
                                                //SUBSCRIBE TO GET RESPONSE FROM ESP
                                                String topic = uniqueId+"/"+chipid+"/addCommand";
                                                int qos = 1;
                                                try {
                                                    IMqttToken subToken = client.subscribe(topic, qos);
                                                    subToken.setActionCallback(new IMqttActionListener() {
                                                        @Override
                                                        public void onSuccess(IMqttToken asyncActionToken) {

                                                            MqttCallback mqttCallback = new MqttCallback() {
                                                                @Override
                                                                public void connectionLost(Throwable cause) {
                                                                    //COMMAND FAIL
                                                                    imgCommandPopup.setVisibility(View.INVISIBLE);
                                                                    textWaiting.setVisibility(View.INVISIBLE);
                                                                  //  failed.setVisibility(View.VISIBLE);
                                                                  //  commandStatus(textCommandPopup,getResources().getString(R.string.command_add_failed),fadePopup,CommandPopup);
                                                                }

                                                                @Override
                                                                public void messageArrived(String topic, MqttMessage message) throws Exception {
                                                                    espResponse = message.toString();
                                                                    client.disconnect();
                                                                }

                                                                @Override
                                                                public void deliveryComplete(IMqttDeliveryToken token) {}
                                                            };
                                                            client.setCallback(mqttCallback);
                                                        }

                                                        @Override
                                                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                                            //COMMAND FAIL
                                                            imgCommandPopup.setVisibility(View.INVISIBLE);
                                                            textWaiting.setVisibility(View.INVISIBLE);
                                                            failed.setVisibility(View.VISIBLE);
                                                            commandStatus(textCommandPopup,getResources().getString(R.string.command_add_failed),fadePopup,CommandPopup);
                                                        }
                                                    });
                                                } catch (MqttException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                                //COMMAND FAIL
                                                imgCommandPopup.setVisibility(View.INVISIBLE);
                                                textWaiting.setVisibility(View.INVISIBLE);
                                                failed.setVisibility(View.VISIBLE);
                                                commandStatus(textCommandPopup,getResources().getString(R.string.command_add_failed),fadePopup,CommandPopup);
                                            }
                                        });
                                    } catch (UnsupportedEncodingException | MqttException e) {
                                        e.printStackTrace();
                                    }

                                    brojac = new CountDownTimer(30000,1000){
                                        @Override
                                        public void onTick(long l) {
                                            if (espResponse.equals("yes")){
                                                espResponse="no";
                                                imgCommandPopup.setVisibility(View.INVISIBLE);
                                                textWaiting.setVisibility(View.INVISIBLE);
                                                commandStatus(textCommandPopup,getResources().getString(R.string.command_added),fadePopup,CommandPopup);
                                                success.setVisibility(View.VISIBLE);
                                                if(getItem(position).length()==1){
                                                    editor.putBoolean(usern+"fanSpeed"+getItem(position),true);
                                                }
                                                else if(getItem(position).charAt(0)=='1' || getItem(position).charAt(0)=='2' || getItem(position).charAt(0)=='3'){
                                                    editor.putBoolean(usern+getItem(position)+"AC",true);
                                                }
                                                else {
                                                    editor.putBoolean(usern + getItem(position), true);
                                                }
                                                editor.commit();
                                                brojac.cancel();
                                            }
                                        }
                                        @Override
                                        public void onFinish() {
                                            if(espResponse.equals("no")){
                                                //COMMAND FAIL
                                                imgCommandPopup.setVisibility(View.INVISIBLE);
                                                textWaiting.setVisibility(View.INVISIBLE);
                                                failed.setVisibility(View.VISIBLE);
                                                commandStatus(textCommandPopup,getResources().getString(R.string.command_add_failed),fadePopup,CommandPopup);
                                            }
                                            else if (espResponse.equals("yes")){
                                                espResponse = "no";
                                                imgCommandPopup.setVisibility(View.INVISIBLE);
                                                textWaiting.setVisibility(View.INVISIBLE);
                                                commandStatus(textCommandPopup,getResources().getString(R.string.command_added),fadePopup,CommandPopup);
                                                success.setVisibility(View.VISIBLE);
                                                if(getItem(position).length()==1){
                                                    editor.putBoolean(usern+"fanSpeed"+getItem(position),true);
                                                }
                                                else if(getItem(position).charAt(0)=='1' || getItem(position).charAt(0)=='2' || getItem(position).charAt(0)=='3'){
                                                    editor.putBoolean(usern+getItem(position)+"AC",true);
                                                }else {
                                                    editor.putBoolean(usern + getItem(position), true);
                                                }
                                                editor.putBoolean(usern+getItem(position),true);
                                                editor.commit();
                                            }
                                        }
                                    }.start();
                                }
                                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                    //COMMAND FAIL
                                    imgCommandPopup.setVisibility(View.INVISIBLE);
                                    textWaiting.setVisibility(View.INVISIBLE);
                                    failed.setVisibility(View.VISIBLE);
                                    commandStatus(textCommandPopup,getResources().getString(R.string.command_add_failed),fadePopup,CommandPopup);
                                }
                            });
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                        //  Toast.makeText(SwithCom.this, espResponse, Toast.LENGTH_SHORT).show();
                        final Button Komanda2 = (Button) container.findViewById(R.id.Komanda2);
                        Komanda2.setClickable(false);
                        if (espResponse.equals("yes")) {
                            if (Komanda2.isClickable()) {
                                Komanda2.setClickable(false);
                            } else {
                                Komanda2.setClickable(true);
                            }
                        }

                        Komanda2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                              try {
                                        IMqttToken token = client.connect(options);
                                        token.setActionCallback(new IMqttActionListener() {
                                            @Override
                                            public void onSuccess(IMqttToken asyncActionToken) {
                                                SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
                                                final SharedPreferences.Editor editor = prefs.edit();
                                                final String chipid = prefs.getString("chipid", "");
                                                final String uniqueId = prefs.getString("uuid", "");
                                                final String usern = prefs.getString("usr", "");
                                                String topic1 = chipid + "/" + uniqueId + "/addCommand";
                                                //  Toast.makeText(getApplicationContext(),topic1,Toast.LENGTH_SHORT).show();
                                                String payload = "{\"command\":\"" + getItem(position) + "\",\"status\":\"decode\"}";
                                                byte[] encodedPayload = new byte[0];
                                                try {
                                                    encodedPayload = payload.getBytes("UTF-8");
                                                    MqttMessage message1 = new MqttMessage(encodedPayload);
                                                    IMqttToken clientpublish = client.publish(topic1, message1);
                                                    clientpublish.setActionCallback(new IMqttActionListener() {
                                                        @Override
                                                        public void onSuccess(IMqttToken asyncActionToken) {
                                                            String a = "";
                                                            if (getItem(position).equals("1"))
                                                                a = "ComOn";
                                                            else if (getItem(position).equals("32"))
                                                                a = "ComOff";
                                                            BackgroundWorker backgroundWorker = new BackgroundWorker(getApplicationContext());
                                                            backgroundWorker.execute("commandcommit", usern, devName, roomName, a);
                                                            if (espResponse.equals("yes")) {
                                                                imgCommandPopup.setVisibility(View.INVISIBLE);
                                                                textWaiting.setVisibility(View.INVISIBLE);
                                                                commandStatus(textCommandPopup, getResources().getString(R.string.command_added), fadePopup, CommandPopup);
                                                                success.setVisibility(View.VISIBLE);
                                                                espResponse = "no";
                                                                if (getItem(position).length() == 1) {
                                                                    editor.putBoolean(usern + "fanSpeed" + getItem(position), true);
                                                                }
                                                                else if (getItem(position).charAt(0) == '1' || getItem(position).charAt(0) == '2' || getItem(position).charAt(0) == '3') {
                                                                    editor.putBoolean(usern + getItem(position) + "AC", true);
                                                                }
                                                                else {
                                                                    editor.putBoolean(usern + getItem(position), true);
                                                                }
                                                                editor.commit();
                                                                brojac.cancel();
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                                        }
                                                    });
                                                }
                                                catch (UnsupportedEncodingException | MqttException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                            }
                                        });
                                    }
                                    catch (MqttException e) {
                                        e.printStackTrace();
                                    }
                            }
                        });
                        /* Button Komanda = (Button) container.findViewById(R.id.Komanda);
                        Komanda.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                espResponse="yes";
                                String a ="";
                                if(getItem(position).equals("On")) a = "ComOn";
                                else if(getItem(position).equals("Off")) a = "ComOff";
                                BackgroundWorker backgroundWorker = new BackgroundWorker(getApplicationContext());
                                backgroundWorker.execute("commandcommit",usern,devName,roomName,a);

                                //CONNECT TO BROKER
                                String clientId = MqttClient.generateClientId();
                                final MqttAndroidClient client = new MqttAndroidClient(getApplicationContext(), "tcp://159.203.107.114:1883",
                                        clientId);
                                MqttConnectOptions options = new MqttConnectOptions();
                                options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
                                options.setUserName("vehabija");
                                options.setPassword("vehabija".toCharArray());
                                try {
                                    IMqttToken token =  client.connect(options);
                                    token.setActionCallback(new IMqttActionListener() {
                                        @Override
                                        public void onSuccess(IMqttToken asyncActionToken) {
                                            SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
                                            String chipid = prefs.getString("chipid", "");
                                            String uniqueId = prefs.getString("uuid","");
                                            String topic1 = chipid + "/" + uniqueId + "/controlCommand";
                                         //   Toast.makeText(getApplicationContext(),topic1,Toast.LENGTH_SHORT).show();
                                            String payload = "{\"command\":\"krmaca\",\"status\":\"add\"}";
                                            byte[] encodedPayload = new byte[0];
                                            try {
                                                encodedPayload = payload.getBytes("UTF-8");
                                                MqttMessage message1 = new MqttMessage(encodedPayload);
                                                IMqttToken clientpublish = client.publish(topic1, message1);
                                                clientpublish.setActionCallback(new IMqttActionListener() {
                                                    @Override
                                                    public void onSuccess(IMqttToken asyncActionToken) {
                                                    }

                                                    @Override
                                                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                                    }
                                                });
                                            } catch (UnsupportedEncodingException | MqttException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {}
                                    });
                                } catch (MqttException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        */
                        container.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                String text = getResources().getString(R.string.please_wait);
                                final Toast toast = Toast.makeText(SwithCom.this, text, Toast.LENGTH_SHORT);
                                View toastView = toast.getView();
                                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                                toastMessage.setTextSize(14);
                                toastMessage.setShadowLayer(0, 0, 0, 0);
                                toastMessage.setTextColor(getApplicationContext().getResources().getColor(R.color.colorPrimaryWhite));
                                toastMessage.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
                                toast.setGravity(Gravity.BOTTOM,0,20);
                                toastMessage.setCompoundDrawablePadding(15);
                                toastView.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.toast_back));
                                toast.show();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        toast.cancel();
                                    }
                                }, 1500);
                                return true;
                            }
                        });

                    }
                });
            }
            else{
                mainViewHolder = (SwithCom.ViewHolder) convertView.getTag();
                mainViewHolder.command.setText(getItem(position));
            }
            layout = R.layout.list_item_command;
            return convertView;
        }
    }

    public class ViewHolder{
        TextView command;
    }

    public void commandStatus(TextView textView,String string, final PopupWindow popupWindow1, final PopupWindow popupWindow2){
        textView.setText(string);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //popupWindow1.dismiss();
                popupWindow2.dismiss();
                Intent refreshIntent=new Intent(SwithCom.this,SwithCom.class);
                refreshIntent.putExtras(bundle);
                finish();
                startActivity(refreshIntent);
                overridePendingTransition(R.anim.animation_on, R.anim.animation_off);
            }
        }, 1500);
    }

    public PopupWindow fade (){
        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup container1 = (ViewGroup) layoutInflater.inflate(R.layout.fadepopup_light,null);
        final PopupWindow fadePopup = new PopupWindow(container1, activity.getWidth(),activity.getHeight() ,false);
        fadePopup.showAtLocation(activity, Gravity.NO_GRAVITY,activity.getWidth()/2,activity.getHeight()/2);
        return fadePopup;
    }

    public abstract class CountDownTimer {
        /**
         * Millis since epoch when alarm should stop.
         */
        private final long mMillisInFuture;
        /**
         * The interval in millis that the user receives callbacks
         */
        private final long mCountdownInterval;

        private long mStopTimeInFuture;

        private boolean mCancelled = false;
        /**
         * @param millisInFuture The number of millis in the future from the call
         * to {@link #start()} until the countdown is done and {@link #onFinish()}
         * is called.
         * @param countDownInterval The interval along the way to receive
         * {@link #onTick(long)} callbacks.
         */
        public CountDownTimer(long millisInFuture, long countDownInterval) {
            mMillisInFuture = millisInFuture;
            mCountdownInterval = countDownInterval;
        }
        /**
         * Cancel the countdown.
         *
         * Do not call it from inside CountDownTimer threads
         */
        public final void cancel() {
            mHandler.removeMessages(MSG);
            mCancelled = true;
        }
        /**
         * Start the countdown.
         */
        public synchronized final SwithCom.CountDownTimer start() {
            if (mMillisInFuture <= 0) {
                onFinish();
                return this;
            }
            mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
            mHandler.sendMessage(mHandler.obtainMessage(MSG));
            mCancelled = false;
            return this;
        }
        /**
         * Callback fired on regular interval.
         * @param millisUntilFinished The amount of time until finished.
         */
        public abstract void onTick(long millisUntilFinished);
        /**
         * Callback fired when the time is up.
         */
        public abstract void onFinish();

        private static final int MSG = 1;
        // handles counting down
        private Handler mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                synchronized(SwithCom.CountDownTimer.this) {
                    final long millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime();
                    if (millisLeft <= 0) {
                        onFinish();
                    }
                    else if (millisLeft < mCountdownInterval) {
                        // no tick, just delay until done
                        sendMessageDelayed(obtainMessage(MSG), millisLeft);
                    }
                    else {
                        long lastTickStart = SystemClock.elapsedRealtime();
                        onTick(millisLeft);
                        // take into account user's onTick taking time to execute
                        long delay = lastTickStart + mCountdownInterval - SystemClock.elapsedRealtime();
                        // special case: user's onTick took more than interval to
                        // complete, skip to next interval
                        while (delay < 0) delay += mCountdownInterval;
                        if (!mCancelled) {
                            sendMessageDelayed(obtainMessage(MSG), delay);
                        }
                    }
                }
            }
        };
    }

    public void Back(View view){
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
                final View mDrawerLayout = (RelativeLayout) findViewById(R.id.swithCom_activity);
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

    public void moveOn2(View view){
        finish();
        Intent intent = new Intent(getApplicationContext(),Device.class);
        Bundle b = getIntent().getExtras();
        intent.putExtras(b);
        startActivity(intent);
        overridePendingTransition(R.anim.animation_on, R.anim.animation_off);
    }

    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
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
            aa = result;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}