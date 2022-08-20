package com.lexycon.hostcardemulation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText login_res;
    Button btn_login;
    RequestQueue requestQueue;
    StringRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_res = findViewById(R.id.login_res);
        btn_login = findViewById(R.id.btn_login);

        // 로그인
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });
    }

    public void send() {
        // RequestQueue 객체 생성
        requestQueue = Volley.newRequestQueue(this);

        request = new StringRequest(
                Request.Method.POST,
                "http://172.30.1.12:8083/pyoripass/guestlogin.do",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("resultValue", response);
                        Log.v("resultValue", response.length() + "");

                        if (response.length() > 0) {
                            try {
                                JSONObject json = new JSONObject(response);

                                String reservation_num = json.getString("reservation_num");
                                String guest_name = json.getString("guest_name");

                                Toast.makeText(getApplicationContext(), guest_name + "님 안녕하세요.", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String reservation_num = login_res.getText().toString();

                params.put("reservation_num", reservation_num);

                return params;
            }
        };
        requestQueue.add(request);
    }
}