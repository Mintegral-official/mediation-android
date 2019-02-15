package com.mintegral.outofmediation;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.mintegral.outofmediation.HttpUrlConnectionRequstImpl;
import com.mintegral.mediation.network.NetCallback;
import com.mintegral.mediation.network.NetworkUtil;

import java.util.HashMap;

public class NetworkSampleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HashMap<String, String> map = new HashMap<>();
        map.put("query",  "ATM机");
        map.put("tag",    "银行");
        map.put("region", "北京市");
        map.put("output", "json");
        map.put("ak",     "6UNZnLPjePZGToNG5YuimKpSUqDjOBIX");

        new NetworkUtil<String>(this, new NetCallback<String>() {
            @Override
            public void onPrepare() {
                new AlertDialog.Builder(NetworkSampleActivity.this)
                        .setMessage("Loading...")
                        .show();
            }

            @Override
            public void onSuccess(String response) {
                TextView textView = findViewById(R.id.main_text);
                textView.setText(response);
            }

            @Override
            public void onFail() {
                Log.e("response", "fail");
            }
        }).execute(new HttpUrlConnectionRequstImpl(map));
    }
}
