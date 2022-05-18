package com.rfid.uhfsdktest;



import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class LoginActivity extends AppCompatActivity  {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button loginbtn;
    String jwt = "";
    String responseMessage = "";
    int responseCode = 0;
    SharedPreferences myPrefs;
    SharedPreferences.Editor editor;
    String username = "";
    String password = "";
    ProgressDialog p;
    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        TextView username =(TextView) findViewById(R.id.username);
        TextView password =(TextView) findViewById(R.id.password);
        String prefID = "";
        SharedPreferences myPrefs = getSharedPreferences(prefID, Context.MODE_PRIVATE);
        myPrefs.getString("jwt","default");
       // SharedPreferences.Editor editor = myPrefs.edit();

        loginbtn = (Button) findViewById(R.id.loginbtn);

        //admin and admin

        loginbtn.setOnClickListener(new View.OnClickListener() {
            //@RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("") && password.getText().toString().equals("")) {
                    //correct
                    String nameValue = username.getText().toString();


                   try
                   {
                       decoded("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYyN2M5NWEzNmU3YjczMDAyOWZjYTU5ZiIsImFkbWluIjpmYWxzZSwicm9sZSI6ImF1dGhlbnRpY2F0ZWQiLCJ1c2VybmFtZSI6InJhdWIwMSIsImNvbmZpcm0iOnRydWUsImVtYWlsIjoicmF1YjAxQHlvcG1haWwuY29tIiwiaWF0IjoxNjUyNDA1NzgwLCJleHAiOjE2NTI0OTIxODB9.sgJIIDLLw4b4WvC64lyE9MB5cGEYJksuRNIYU0SJUS8");
                   } catch (Exception e)
                    {
                       e.printStackTrace();
                    }


                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("Name", nameValue);
                    intent.putExtra("Role", nameValue);
                    startActivity(intent);
                    //Toast.makeText(LoginActivity.this,"LOGIN SUCCESSFUL",Toast.LENGTH_SHORT).show();
                } else

                    //incorrect
                    Toast.makeText(LoginActivity.this, "LOGIN FAILED !!!", Toast.LENGTH_SHORT).show();
            }

        });
               // login();

            //}
       // });
    }
    private void login()
    {
       runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new LoginSync().execute();
            }
       });
    }

     private class LoginSync extends AsyncTask<String, String, String> {

         @Override
         protected void onPreExecute() {
             super.onPreExecute();
             p = new ProgressDialog(LoginActivity.this);
             p.setMessage("Please wait...It is downloading");
             p.setIndeterminate(false);
             p.setCancelable(false);
             p.show();
         }

         @Override
         protected String doInBackground(String... args) {
             try {
                 URL url = new URL("https://api-dev.sirim-dsr.xfero.xyz/auth/local");
                 Log.d("SIRIMRFID", url.toString());
                 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                 conn.setDoOutput(true);
                 conn.setRequestMethod("POST");
                 conn.setRequestProperty("Content-Type", "application/json");
                 JSONObject obj = new JSONObject();

                 obj.put("identifier", "raub01");
                 obj.put("password", "Raub01123#");
                 OutputStream os = new BufferedOutputStream(conn.getOutputStream());
                 os.write(obj.toString().getBytes());
                 os.flush();

                 if (conn.getResponseCode() != 200) {
                     Log.d("LOGIN", "Failed : HTTP error code : "
                             + conn.getResponseCode());
                     int responseCode = conn.getResponseCode();
                     BufferedReader br = new BufferedReader(new InputStreamReader(
                             (conn.getErrorStream())));
                     String output;
                     String message = "";
                     while ((output = br.readLine()) != null) {
                         String msg = "";
                         for (int i = 51; i < output.length(); i++) {
                             char letter = output.charAt(i);
                             if (letter != '"') {
                                 msg = msg + letter;
                             } else {
                                 break;
                             }
                         }
                         message = msg;
                     }
                     responseMessage = message;
                     return "failed";
                 } else {
                     BufferedReader br = new BufferedReader(new InputStreamReader(
                             (conn.getInputStream())));

                     String output;
                     String localJwt = "";

                     while ((output = br.readLine()) != null) {
                         // Get JWT
                         String jwt = "";
                         for (int i = 8; i < output.length(); i++) {
                             char letter = output.charAt(i);
                             if (letter != '"') {
                                 jwt = jwt + letter;
                             } else {
                                 break;
                             }
                         }
                         localJwt = jwt;
                     }
                     jwt = localJwt;
                     editor.putString("jwt", jwt);
                     editor.apply();
                     Log.d("LOGIN", String.valueOf(username));
                     Log.d("LOGIN", "Success!");
                 }
                 conn.disconnect();
                 return "success";
             } catch (MalformedURLException e) {
                  Log.e(TAG, e.toString());
             } catch (IOException e) {
                  Log.e(TAG, e.toString());
             } catch (JSONException e) {
                 Log.e(TAG, e.toString());
             }
             return "failed";
         }


         protected void onPostExecute(String result) {
             Log.d("SIRIMRFID", "onPostExecute: Getting result");
             Log.d("SIRIMRFID", result);

             // if (pDialog.isShowing()) pDialog.dismiss();

             if (result == "success") {
             }
         }
     }
    public void decoded(String JWTEncoded) throws Exception {
        try {
            String[] split = JWTEncoded.split("\\.");
            Log.d("JWT_DECODED", "Header: " + getJson(split[0]));
            Log.d("JWT_DECODED", "Body: " + getJson(split[1]));
        } catch (UnsupportedEncodingException e) {
            //Error
        }
    }

    private String getJson(String strEncoded) throws UnsupportedEncodingException{
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}