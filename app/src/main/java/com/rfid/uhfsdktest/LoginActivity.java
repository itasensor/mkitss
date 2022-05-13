package com.rfid.uhfsdktest;



import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;


public class LoginActivity extends AppCompatActivity {
    private Button loginbtn;




    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        TextView username =(TextView) findViewById(R.id.username);
        TextView password =(TextView) findViewById(R.id.password);

        loginbtn = (Button) findViewById(R.id.loginbtn);

        //admin and admin

        loginbtn.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("admin") && password.getText().toString().equals("admin")) {
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
}