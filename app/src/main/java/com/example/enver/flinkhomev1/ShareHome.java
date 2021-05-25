package com.example.enver.flinkhomev1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.StrictMode;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShareHome extends AppCompatActivity {
    Button search;
    ImageButton imgLeft;
    EditText shareemail;
    String usern, aa, data1[], email;
    CountDownTimer co;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_home);
        if (isOnline() == false) {
            notOnline();
        } else {
            search = (Button) findViewById(R.id.searchemail);
            shareemail = (EditText) findViewById(R.id.shareemail);
            imgLeft = (ImageButton) findViewById(R.id.imgLeft);
            imgLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
            });
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (shareemail.getText().toString().isEmpty()) {
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.custom_toast_layout));
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.please_enter_an_email, Toast.LENGTH_SHORT);
                        View toastView = toast.getView();
                        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                        toastMessage.setTextSize(14);
                        toastMessage.setShadowLayer(0, 0, 0, 0);
                        toastMessage.setTextColor(getResources().getColor(R.color.colorPrimaryWhite));
                        toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.warning_icon, 0, 0, 0);
                        toastMessage.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                        toast.setGravity(Gravity.BOTTOM, 0, 20);
                        toastMessage.setCompoundDrawablePadding(15);
                        toastView.setBackground(getResources().getDrawable(R.drawable.toast_back));
                        toast.show();
                    } else {
                        LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                        ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.faderegister, null);
                        final PopupWindow fadePopup = new PopupWindow(container, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);
                        fadePopup.setAnimationStyle(R.style.animationName);
                        final View mDrawerLayout = (ConstraintLayout) findViewById(R.id.activity_share_home);
                        fadePopup.showAtLocation(mDrawerLayout, Gravity.NO_GRAVITY, 0, 0);
                        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

                        preferences = getSharedPreferences("X", MODE_PRIVATE);
                        usern = preferences.getString("usr", "");
                        email = shareemail.getText().toString();
                        new TestAsync().execute();
                        co = new CountDownTimer(10000, 1000) {
                            @Override
                            public void onTick(long l) {
                            }

                            @Override
                            public void onFinish() {
                                fadePopup.dismiss();
                                shareemail.setText("");
                                if(aa.length()==2) {
                                    Toast.makeText(ShareHome.this, "You entered an email which does not belong to an account. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Intent intent = new Intent(getApplicationContext(), ShareHome2.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("RoomsDevices", aa);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            }
                        };


                    }
                }
            });
        }
    }

    public void getData(String urln) {
        InputStream is = null;
        String line = null;
        String result = null;
        try {
            URL url = new URL(urln);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            is = new BufferedInputStream(con.getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                final View mDrawerLayout = (ConstraintLayout) findViewById(R.id.activity_share_home);
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }


    class TestAsync extends AsyncTask<Void, Boolean, Boolean> {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute() {
        }

        protected Boolean doInBackground(Void... arg0) {
            try {
                getData("http://159.203.107.114/flinkphp/sharehome1.php?Username=" + usern + "&Email=" + email);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        protected void onPostExecute(Boolean a) {
            if(a) {
                co.onFinish();
            }else{
                Toast.makeText(ShareHome.this, "We could not fetch the data. Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        }

    }

}