package com.example.enver.flinkhomev1.esp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.enver.flinkhomev1.DevicesList;
import com.example.enver.flinkhomev1.R;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EsptouchDemoActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "EsptouchDemoActivity";
    String data1[];
    String aa;
    Integer count1=0;
    ConstraintLayout activity;
    LayoutInflater layoutInflater;
    private TextView mTvApSsid;
    private EditText mEdtApPassword;
    private Button mBtnConfirm;
    private EspWifiAdminSimple mWifiAdmin;
    private Spinner mSpinnerTaskCount;
    SharedPreferences prefs;
    String uniqueId;
    String usern;
    String pass;
    Bundle b;
    String devicetype;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esptouch_demo);
        if (isOnline()) {
            activity = (ConstraintLayout) findViewById(R.id.activity_esptouchdemo);
            b = getIntent().getExtras();
            devicetype = b.getString("device");
            StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
            prefs = getSharedPreferences("X", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("device", devicetype);
            usern = prefs.getString("usr", "");
            getData("http://159.203.107.114/flinkphp/espass.php?Username=" + usern);
            String podaci = aa;
            Pattern pattern = Pattern.compile("(\\w|\\+|\\/)+");
            Matcher matcher = pattern.matcher(podaci);
            while (matcher.find()) {
                matcher.find();
                pass = matcher.group();
            }
            //Toast.makeText(getApplicationContext(),usern+" "+pass,Toast.LENGTH_SHORT).show();
            mWifiAdmin = new EspWifiAdminSimple(this);
            mTvApSsid = (TextView) findViewById(R.id.tvApSssidConnected);
            mEdtApPassword = (EditText) findViewById(R.id.edtApPassword);
            mBtnConfirm = (Button) findViewById(R.id.btnConfirm);
            mBtnConfirm.setOnClickListener(this);
            initSpinner();
        }
    }

    private void initSpinner()
    {
        mSpinnerTaskCount = (Spinner) findViewById(R.id.spinnerTaskResultCount);
        int[] spinnerItemsInt = getResources().getIntArray(R.array.taskResultCount);
        int length = spinnerItemsInt.length;
        Integer[] spinnerItemsInteger = new Integer[length];
        for(int i=0;i<length;i++)
        {
            spinnerItemsInteger[i] = spinnerItemsInt[i];
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, spinnerItemsInteger);
        mSpinnerTaskCount.setAdapter(adapter);
        mSpinnerTaskCount.setSelection(1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // display the connected ap's ssid
        String apSsid = mWifiAdmin.getWifiConnectedSsid();
        if (apSsid != null) {
            mTvApSsid.setText(apSsid);
        } else {
            mTvApSsid.setText("");
        }
        // check whether the wifi is connected
        boolean isApSsidEmpty = TextUtils.isEmpty(apSsid);
        mBtnConfirm.setEnabled(!isApSsidEmpty);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnConfirm) {
            String apSsid = mTvApSsid.getText().toString();
            String apPassword = mEdtApPassword.getText().toString();
            String apBssid = mWifiAdmin.getWifiConnectedBssid();
            String taskResultCountStr = Integer.toString(5);
            if (__IEsptouchTask.DEBUG) {
                Log.d(TAG, "mBtnConfirm is clicked, mEdtApSsid = " + apSsid + ", " + " mEdtApPassword = " + apPassword);
            }
            new EsptouchAsyncTask3().execute(apSsid, apBssid, apPassword, taskResultCountStr);
        }
    }


    private void onEsptoucResultAddedPerform(final IEsptouchResult result) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                String text = result.getBssid() + " is connected to the WI-FI.";
                Toast toast = Toast.makeText(EsptouchDemoActivity.this, text, Toast.LENGTH_LONG);
                View toastView = toast.getView();
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(14);
                toastMessage.setShadowLayer(0, 0, 0, 0);
                toastMessage.setTextColor(getApplicationContext().getResources().getColor(R.color.colorPrimaryWhite));
                toastMessage.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
                toast.setGravity(Gravity.FILL_HORIZONTAL, 10, 20);
                toastView.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.toast_back_blue));
                toast.setMargin(30,30);
                toast.show();
            }

        });
    }

    private IEsptouchListener myListener = new IEsptouchListener() {

        @Override
        public void onEsptouchResultAdded(final IEsptouchResult result) {
            onEsptoucResultAddedPerform(result);
        }
    };

    @SuppressWarnings("deprecation")
    private class EsptouchAsyncTask3 extends AsyncTask<String, Void, List<IEsptouchResult>> {
         ProgressDialog mProgressDialog = new ProgressDialog(EsptouchDemoActivity.this);
        private IEsptouchTask mEsptouchTask;
        // without the lock, if the user tap confirm and cancel quickly enough,
        // the bug will arise. the reason is follows:
        // 0. task is starting created, but not finished
        // 1. the task is cancel for the task hasn't been created, it do nothing
        // 2. task is created
        // 3. Oops, the task should be cancelled, but it is running
        private final Object mLock = new Object();

        @Override
        protected void onPreExecute() {
            /*mProgressDialog.setMessage("FlinkTouch is configuring, please wait for a moment...");
            mProgressDialog.setIndeterminateDrawable(getResources()
                    .getDrawable(R.drawable.progress));

            mProgressDialog.setCanceledOnTouchOutside(false);

            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    synchronized (mLock) {
                        if (__IEsptouchTask.DEBUG) {
                            Log.i(TAG, "progress dialog is canceled");
                        }
                        if (mEsptouchTask != null) {
                            mEsptouchTask.interrupt();
                        }
                    }
                }
            });
            mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                    "Waiting...", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            mProgressDialog.show();

            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setEnabled(false);*/
         /*   final Dialog dialog = new Dialog(EsptouchDemoActivity.this);
            dialog.getWindow().getAttributes().windowAnimations = R.style.animationName;
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.esptouchdialog);
            dialog.show();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
        /*alertDialog.setMessage("Welcome!");
        alertDialog.show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            }, 30000);
            */
            layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup container1 = (ViewGroup) layoutInflater.inflate(R.layout.fadenovi,null);
            final PopupWindow fadePopup = new PopupWindow(container1, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT ,true);
            fadePopup.showAtLocation(activity, Gravity.NO_GRAVITY,0,0);
            fadePopup.setAnimationStyle(R.style.animationName);

            /*ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.esptouchdialog,null);
            final PopupWindow TimeOutPopup = new PopupWindow(container, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            TimeOutPopup.setAnimationStyle(R.style.animationName);
            TimeOutPopup.showAtLocation(activity, Gravity.CENTER,0,0);*/


            final Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                   // TimeOutPopup.setAnimationStyle(R.style.animationName);
                    //TimeOutPopup.dismiss();
                    fadePopup.setAnimationStyle(R.style.animationName);
                    fadePopup.dismiss();
                }
            }, 30000);
        }

        @Override
        protected List<IEsptouchResult> doInBackground(String... params) {
            int taskResultCount = -1;
            synchronized (mLock) {
                // !!!NOTICE
                String apSsid = mWifiAdmin.getWifiConnectedSsidAscii(params[0]);
                String apBssid = params[1];
                String apPassword = params[2];
                String taskResultCountStr = params[3];
                taskResultCount = Integer.parseInt(taskResultCountStr);
                mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword,30000, EsptouchDemoActivity.this);
                mEsptouchTask.setEsptouchListener(myListener);
            }
            List<IEsptouchResult> resultList = mEsptouchTask.executeForResults(taskResultCount);
            return resultList;
        }

        @Override
        protected void onPostExecute(List<IEsptouchResult> result) {
           /* mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setEnabled(true);
            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(
                    "Confirm"); */
            IEsptouchResult firstResult = result.get(0);
            // check whether the task is cancelled and no results received
            if (!firstResult.isCancelled()) {
                int count = 0;
                // max results to be displayed, if it is more than maxDisplayCount,
                // just show the count of redundant ones
                final int maxDisplayCount = 5;
                // the task received some results including cancelled while
                // executing before receiving enough results
                if (firstResult.isSuc()) {
                    StringBuilder sb = new StringBuilder();
                    for (IEsptouchResult resultInList : result) {
                        sb.append("FlinkTouch success, bssid = "
                                + resultInList.getBssid()
                                + ",InetAddress = "
                                + resultInList.getInetAddress()
                                .getHostAddress() + "\n");
                        //OVDJE
                        String ip = resultInList.getInetAddress().getHostAddress();
                        /*getData("http://"+ip+"/confirm");
                        String podaci = aa;

                        Pattern pattern = Pattern.compile("\\d+");
                        Matcher matcher = pattern.matcher(podaci);
                        matcher.find();
                        String id = matcher.group();
                        pattern = Pattern.compile("\\w+");
                        matcher = pattern.matcher(podaci);
                        matcher.find(); matcher.find();
                        String name = matcher.group();*/

                        count++;
                        count1++;
                        SharedPreferences.Editor editor = prefs.edit();
                        String esp = "esp" + String.valueOf(count);
                        editor.putInt("count1", count1);
                        editor.putString(esp, "Flink " + resultInList.getBssid());
                        editor.putString(esp+"ip",ip);
                        editor.putString(esp+"Uuid",pass);
                        editor.commit();

                    }
                    if (count < result.size()) {
                        sb.append("\nthere's " + (result.size() - count)
                                + " more result(s) without showing\n");
                    }
                  //  mProgressDialog.setMessage(sb.toString());
                }
                else {
                    final PopupWindow fadePopup = fade();
                    fadePopup.setAnimationStyle(R.style.animationName);
                    ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.esptouchdialogfailed,null);
                    final PopupWindow TimeOutPopup = new PopupWindow(container, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                    TimeOutPopup.setAnimationStyle(R.style.animationName);
                    TimeOutPopup.showAtLocation(activity, Gravity.CENTER,0,0);
                    final Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TimeOutPopup.setAnimationStyle(R.style.animationName);
                            TimeOutPopup.dismiss();
                            fadePopup.setAnimationStyle(R.style.animationName);
                            fadePopup.dismiss();
                        }
                    }, 1500);
                }
            }
            if(count1 > 0){
                Intent intent = new Intent(getApplicationContext(), DevicesList.class);
                Bundle bandl = getIntent().getExtras();
                intent.putExtras(bandl);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
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
            try {
            JSONArray js = new JSONArray(result);
            JSONObject jo = null;
            data1 = new String[js.length()];
            for (int i = 0; i < js.length(); i++) {
                jo = js.getJSONObject(i);
                data1[i] = js.getString(i);
            }
            }
            catch (JSONException e) {
            e.printStackTrace();
            }
    }
 /*public void Next (View view) {

     Intent intent = new Intent(getApplicationContext(), DevicesList.class);
     Bundle bandl = getIntent().getExtras();
     intent.putExtras(bandl);
     startActivity(intent);
     overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
 }*/

    public void Back (View view){
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

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

    public PopupWindow fade (){
        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup container1 = (ViewGroup) layoutInflater.inflate(R.layout.fadepopup,null);
        final PopupWindow fadePopup = new PopupWindow(container1, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT ,false);
        fadePopup.showAtLocation(activity, Gravity.NO_GRAVITY,0,0);
        return fadePopup;
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
                ViewGroup container1 = (ViewGroup) layoutInflater.inflate(R.layout.fadepopup,null);
                final PopupWindow popupWindow = new PopupWindow(container, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                final View mDrawerLayout = (ConstraintLayout) findViewById(R.id.activity_add_device);
                final PopupWindow fadePopup = new PopupWindow(container1, mDrawerLayout.getWidth(),mDrawerLayout.getHeight() ,false);
                fadePopup.showAtLocation(mDrawerLayout, Gravity.NO_GRAVITY,mDrawerLayout.getWidth()/2,mDrawerLayout.getHeight()/2);
                fadePopup.setAnimationStyle(R.style.animationName);
                popupWindow.setAnimationStyle(R.style.animationName);
                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        popupWindow.showAtLocation(mDrawerLayout, Gravity.CENTER, 0, 0);
                    }
                }, 500);
                container.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        fadePopup.dismiss();
                        popupWindow.dismiss();
                        finish();
                        return false;
                    }
                });
                Button povezacuse = (Button) container.findViewById(R.id.povezacuse);
                povezacuse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fadePopup.dismiss();
                        popupWindow.dismiss();
                        finish();
                    }
                });
            }
        }, 200);
    }
}