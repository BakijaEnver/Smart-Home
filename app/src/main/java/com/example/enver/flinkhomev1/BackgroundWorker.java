package com.example.enver.flinkhomev1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import static android.content.Context.MODE_PRIVATE;

public class BackgroundWorker extends AsyncTask<String,Void,String>  {

    Context context;
    PopupWindow fadePopup;
    AlertDialog alertDialog;
    BackgroundWorker (Context ctx) {
        context = ctx;
    }
    BackgroundWorker (Context ctx, PopupWindow fadepopup) {
        context = ctx;
        fadePopup = fadepopup;
    }
    String type;
    String trebamiusername,aa,data1[];
    String devName="",selectedTemp,selectedSwing,selectedFan,selectedMode,selectedState,roomName="";
    ArrayList<String> data = new ArrayList<>();

    @Override
    protected String doInBackground(String... params) {
        type = params[0];
        String login_url = "http://159.203.107.114/flinkphp/login.php";
        String register_url = "http://159.203.107.114/flinkphp/register.php";
        String name_url = "http://159.203.107.114/flinkphp/name.php";
        String rooms_commit = "http://159.203.107.114/flinkphp/roomscommit.php";
        String last_room = "http://159.203.107.114/flinkphp/Lastroom.php";
        String last_device = "http://159.203.107.114/flinkphp/Lastdevice.php";
        String devices_commit = "http://159.203.107.114/flinkphp/devicescommit.php";
        String command_commit = "http://159.203.107.114/flinkphp/commandcommit.php";
        String forgot_password = "http://159.203.107.114/flinkphp/forgotpassword.php";
        String share_home = "http://159.203.107.114/flinkphp/sharehome.php";
        if(type.equals("login")) {
            try {
                String Username = params[1];
                String Password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("Username","UTF-8")+"="+URLEncoder.encode(Username,"UTF-8")+"&"
                        +URLEncoder.encode("Password","UTF-8")+"="+URLEncoder.encode(Password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("register"))
        {
            try {
                String Firstname = params[1];
                String Lastname = params[2];
                String Email = params[3];
                String Username = params[4];
                String Password = params[5];
                setusername(Username);
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("Firstname","UTF-8")+"="+URLEncoder.encode(Firstname,"UTF-8")+"&"
                        +URLEncoder.encode("Lastname","UTF-8")+"="+URLEncoder.encode(Lastname,"UTF-8")+"&"
                        +URLEncoder.encode("Email","UTF-8")+"="+URLEncoder.encode(Email,"UTF-8")+"&"
                        +URLEncoder.encode("Username","UTF-8")+"="+URLEncoder.encode(Username,"UTF-8")+"&"
                        +URLEncoder.encode("Password","UTF-8")+"="+URLEncoder.encode(Password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (type.equals("name") || type.equals("surname") || type.equals("password")){
            try{
                String field = params[0];
                String Username = params[1];
                String Name = params[2];
                String NewPas = params[3];
                URL url = new URL(name_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data =URLEncoder.encode("field","UTF-8")+"="+URLEncoder.encode(field,"UTF-8")
                        +"&"+URLEncoder.encode("Username","UTF-8")+"="+URLEncoder.encode(Username,"UTF-8")+"&"
                        +URLEncoder.encode("Name","UTF-8")+"="+URLEncoder.encode(Name,"UTF-8")+"&"+
                        URLEncoder.encode("NewPas","UTF-8")+"="+URLEncoder.encode(NewPas,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (type.equals("roomscommit")){
            try {
                String Username = params[1];
                String Numrooms = params[2];
                String Roomname = params[3];
                URL url = new URL(rooms_commit);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("Username","UTF-8")+"="+URLEncoder.encode(Username,"UTF-8")+"&"+
                        URLEncoder.encode("Numrooms","UTF-8")+"="+URLEncoder.encode(Numrooms,"UTF-8")+"&"+
                        URLEncoder.encode("Roomname","UTF-8")+"="+URLEncoder.encode(Roomname,"UTF-8");
                System.out.println(post_data);
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (type.equals("devicescommit")){
            try {
                String Username = params[1];
                String Devicename = params[2];
                String Roomname = params[3];
                String IP = params[4];
                String Uuid = params[5];
                String Chipid = params[6];
                URL url = new URL(devices_commit);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("Username","UTF-8")+"="+URLEncoder.encode(Username,"UTF-8")+"&"+
                        URLEncoder.encode("Devicename","UTF-8")+"="+URLEncoder.encode(Devicename,"UTF-8")+"&"+
                        URLEncoder.encode("Roomname","UTF-8")+"="+URLEncoder.encode(Roomname,"UTF-8")+"&"+
                        URLEncoder.encode("IP","UTF-8")+"="+URLEncoder.encode(IP,"UTF-8")+"&"+
                        URLEncoder.encode("Uuid","UTF-8")+"="+URLEncoder.encode(Uuid,"UTF-8")+"&"+
                        URLEncoder.encode("Chipid","UTF-8")+"="+URLEncoder.encode(Chipid,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (type.equals("commandcommit")){
            try {
                String Username = params[1];
                String Devicename = params[2];
                String Roomname = params[3];
                String Commandname = params[4];
                URL url = new URL(command_commit);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("Username","UTF-8")+"="+URLEncoder.encode(Username,"UTF-8")+"&"+
                        URLEncoder.encode("Devicename","UTF-8")+"="+URLEncoder.encode(Devicename,"UTF-8")+"&"+
                        URLEncoder.encode("Roomname","UTF-8")+"="+URLEncoder.encode(Roomname,"UTF-8")+"&"+
                        URLEncoder.encode("Commandname","UTF-8")+"="+URLEncoder.encode(Commandname,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (type.equals("forgotpassword")){
            try {
                String Email = params[1];
                URL url = new URL(forgot_password);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("Email","UTF-8")+"="+URLEncoder.encode(Email,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (type.equals("sharehome")){
            try {
                String Username = params[1];
                String Email = params[2];
                URL url = new URL(share_home);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("Username","UTF-8")+"="+URLEncoder.encode(Username,"UTF-8")+"&"+
                        URLEncoder.encode("Email","UTF-8")+"="+URLEncoder.encode(Email,"UTF-8");
                System.out.println(post_data);
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (type.equals("lastroom")){
            try {
                String Username = params[1];
                String Roomname = params[2];
                URL url = new URL(last_room);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("Username","UTF-8")+"="+URLEncoder.encode(Username,"UTF-8")+"&"+
                        URLEncoder.encode("Roomname","UTF-8")+"="+URLEncoder.encode(Roomname,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (type.equals("lastdev")){
            try {
                String Username = params[1];
                String Devicename = params[2];
                String LastState = params[3];
                String LastMode = params[4];
                String LastFan = params[5];
                String LastSwing = params[6];
                String LastTemp = params[7];
                String Roomname = params[8];
                URL url = new URL(last_device);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("Username","UTF-8")+"="+URLEncoder.encode(Username,"UTF-8")+"&"+
                        URLEncoder.encode("Devicename","UTF-8")+"="+URLEncoder.encode(Devicename,"UTF-8")+"&"+
                        URLEncoder.encode("LastState","UTF-8")+"="+URLEncoder.encode(LastState,"UTF-8")+"&"+
                        URLEncoder.encode("LastMode","UTF-8")+"="+URLEncoder.encode(LastMode,"UTF-8")+"&"+
                        URLEncoder.encode("LastFan","UTF-8")+"="+URLEncoder.encode(LastFan,"UTF-8")+"&"+
                        URLEncoder.encode("LastSwing","UTF-8")+"="+URLEncoder.encode(LastSwing,"UTF-8")+"&"+
                        URLEncoder.encode("LastTemp","UTF-8")+"="+URLEncoder.encode(LastTemp,"UTF-8")+"&"+
                        URLEncoder.encode("Roomname","UTF-8")+"="+URLEncoder.encode(Roomname,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.equals("successno")){
            final Dialog dialog = new Dialog(context);
            dialog.getWindow().getAttributes().windowAnimations = R.style.animationName;
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_demo);
            dialog.show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    alertDialog.cancel();
                    Intent launchNextActivity;
                    launchNextActivity = new Intent(context,HomePanel.class);
                    launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(launchNextActivity);
                }
            }, 1500);
        }
        else if (type.equals("login")){
            String s = Character.toString(result.charAt(0));
            if(s.equals("s")){
                final Dialog dialog = new Dialog(context);
                dialog.getWindow().getAttributes().windowAnimations = R.style.animationName;
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_demo);
                dialog.show();
                final String roomname = result.substring(7,result.length());
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Bundle b = new Bundle();
                        b.putString("key",roomname );
                        b.putBoolean("new",false);
                        Intent intent = new Intent(context, Room.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtras(b);
                        context.startActivity(intent);
                    }
                }, 1500);
            }
            else if (s.equals("q")){
                Integer ln = result.length();
                selectedTemp = result.substring(ln-2,ln);
                selectedSwing = result.substring(ln-4,ln-2);
                selectedFan = result.substring(ln-6,ln-4);
                if(!selectedFan.equals("10")) {
                    selectedFan = result.substring(ln-5,ln-4);
                    selectedMode = result.substring(ln-6,ln-5);
                    selectedState = result.substring(ln-7,ln-6);
                    if(!selectedState.equals("1")) {
                        selectedState = "32";
                        Integer i = ln-8;
                        while(result.charAt(i)!='q') i--;
                        devName = result.substring(i+1,ln-8);
                        Integer j = i; i--;
                        while(result.charAt(i)!='q') i--;
                        roomName = result.substring(i+1,j);
                    }
                    else {
                        Integer i = ln-7;
                        while(result.charAt(i)!='q')  i--;
                        devName = result.substring(i+1,ln-7);
                        Integer j = i; i--;
                        while(result.charAt(i)!='q')  i--;
                        roomName = result.substring(i+1,j);
                    }
                }
                else{
                    selectedMode = result.substring(ln-7,ln-6);
                    selectedState = result.substring(ln-8,ln-7);
                    if(!selectedState.equals("1")) {
                        selectedState = "32";
                        Integer i = ln-10;
                        while(result.charAt(i)!='q') i--;
                        devName = result.substring(i+1,ln-10);
                        Integer j = i; i--;
                        while(result.charAt(i)!='q') i--;
                        roomName = result.substring(i+1,j);
                    }
                    else {
                        Integer i = ln-8;
                        while(result.charAt(i)!='q') i--;
                        devName = result.substring(i+1,ln-8);
                        Integer j = i; i--;
                        while(result.charAt(i)!='q') i--;
                        roomName = result.substring(i+1,j);

                    }
                }
                final Dialog dialog = new Dialog(context);
                dialog.getWindow().getAttributes().windowAnimations = R.style.animationName;
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_demo);
                dialog.show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences prefs = context.getSharedPreferences("X", MODE_PRIVATE);
                        Bundle b = new Bundle();
                        b.putString("devicename",devName);
                        b.putBoolean("izlogina",true);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("room",roomName);
                        editor.putString("SelectedMode"+devName,selectedMode);
                        editor.putString("SelectedFan"+devName,selectedFan);
                        editor.putString("SelectedSwing"+devName,selectedSwing);
                        editor.putString("SelectedState"+devName,selectedState);
                        editor.putString("SelectedTemp"+devName,selectedTemp);
                        editor.commit();
                        Intent intent = new Intent(context, Device.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtras(b);
                        context.startActivity(intent);
                    }
                }, 1500);
            }
            else{
                String text ="";
                if(result.equals("Wrong password"))
                {
                    text = context.getString(R.string.wrong_password);
                }
                else if(result.equals("Wrong username"))
                {
                    text = context.getString(R.string.wrong_username);
                }
                else if(result.equals("Account not approved. Please check your mail."))
                {
                    text = context.getString(R.string.account_not_approved);
                }
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                View toastView = toast.getView();
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(14);
                toastMessage.setShadowLayer(0, 0, 0, 0);
                toastMessage.setTextColor(context.getResources().getColor(R.color.colorPrimaryWhite));
                toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.warning_icon, 0, 0, 0);
                toastMessage.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
                toast.setGravity(Gravity.BOTTOM,0,20);
                toastMessage.setCompoundDrawablePadding(15);
                toastView.setBackground(context.getResources().getDrawable(R.drawable.toast_back));
                toast.show();
            }
        }
        else if (type.equals("roomscommit") || type.equals("sharehome") || type.equals("lastroom") || type.equals("forgotpassword") || type.equals("lastdev")){
        }
        else if (type.equals("devicescommit") || type.equals("commandcommit")){
        }
        else if (result.equals("Account successfully created. A verification mail has been sent to activate your account.")){
            final Dialog dialog = new Dialog(context);
            dialog.getWindow().getAttributes().windowAnimations = R.style.animationName;
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_demo_register);
            dialog.show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    alertDialog.cancel();
                    Intent launchNextActivity=null;
                    Bundle b = new Bundle();
                    b.putString("username", getusername());
                    launchNextActivity = new Intent(context,MainActivity.class);
                    launchNextActivity.putExtras(b);
                    launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(launchNextActivity);
                }
            }, 1500);
        }
        else if (type.equals("name") || type.equals("surname") || type.equals("password")){
            if(result.equals("Password changed.")){
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       Toast toast = Toast.makeText(context, R.string.password_changed_success, Toast.LENGTH_LONG);
                        View toastView = toast.getView();
                        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                        toastMessage.setTextSize(14);
                        toastMessage.setShadowLayer(0, 0, 0, 0);
                        toastMessage.setTextColor(context.getResources().getColor(R.color.colorPrimaryWhite));
                        toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.success_icon_2, 0, 0, 0);
                        toastMessage.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
                        toastMessage.setCompoundDrawablePadding(15);
                        toastView.setBackground(context.getResources().getDrawable(R.drawable.toast_back_green));
                        toast.setGravity(Gravity.BOTTOM,0,20);
                        toast.show();
                        Intent launchNextActivity;
                        launchNextActivity = new Intent(context,MainActivity.class);
                        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(launchNextActivity);
                    }
                }, 1500);
            }
            else if(result.equals("Password incorrect.")){
                        Toast toast = Toast.makeText(context, R.string.password_incorect, Toast.LENGTH_LONG);
                        View toastView = toast.getView();
                        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                        toastMessage.setTextSize(14);
                        toastMessage.setShadowLayer(0, 0, 0, 0);
                        toastMessage.setTextColor(context.getResources().getColor(R.color.colorPrimaryWhite));
                        toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.warning_icon, 0, 0, 0);
                        toastMessage.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
                        toast.setGravity(Gravity.BOTTOM,0,20);
                        toastMessage.setCompoundDrawablePadding(15);
                        toastView.setBackground(context.getResources().getDrawable(R.drawable.toast_back));
                        toast.show();
            }
            else {
                String text ="";
                if(result.equals("Firstname changed."))
                {
                    text = context.getString(R.string.firstname_changed);
                }
                else if(result.equals("Lastname changed."))
                {
                    text = context.getString(R.string.lastname_changed);
                }
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                View toastView = toast.getView();
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(14);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setShadowLayer(0, 0, 0, 0);
                toastMessage.setTextColor(context.getResources().getColor(R.color.colorPrimaryWhite));
                toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.success_icon_2, 0, 0, 0);
                toast.setGravity(Gravity.FILL_HORIZONTAL, 10, 20);
                toast.setGravity(Gravity.BOTTOM,0,20);
                toastMessage.setCompoundDrawablePadding(15);
                toastView.setBackground(context.getResources().getDrawable(R.drawable.toast_back_green));
                toast.show();
            }
        }
        else {
            fadePopup.dismiss();
            String text ="";
            if(result.equals("Username is taken."))
            {
                text = context.getString(R.string.username_exist);
            }
            else if(result.equals("Entered email already has an account."))
            {
                text = context.getString(R.string.email_exist);
            }
            Toast toast = Toast.makeText(context, result, Toast.LENGTH_LONG);
            View toastView = toast.getView();
            TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
            toastMessage.setTextSize(14);
            toastMessage.setShadowLayer(0, 0, 0, 0);
            toastMessage.setTextColor(context.getResources().getColor(R.color.colorPrimaryWhite));
            toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.warning_icon, 0, 0, 0);
            toastMessage.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
            toast.setGravity(Gravity.BOTTOM,0,20);
            toastMessage.setCompoundDrawablePadding(15);
            toastView.setBackground(context.getResources().getDrawable(R.drawable.toast_back));
            toast.show();
        }
    }

    public String getusername(){
        return trebamiusername;
    }

    protected void setusername(String a){
        trebamiusername = a;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
