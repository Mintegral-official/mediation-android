package com.mintegral.outofmediation.network;

import android.app.Activity;
import android.os.Bundle;

import com.mintegral.mediation.network.NetCallback;
import com.mintegral.mediation.network.NetworkUtil;

import java.util.HashMap;

public class NetworkSampleActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestNetwork();
    }

    private void requestNetwork() {
        new NetworkUtil<String>(this, new NetCallback<String>() {
            @Override
            public void onPrepare() {

            }

            @Override
            public void onSuccess(String response) {

            }

            @Override
            public void onFail(Exception e) {
                new ExceptionHandlerImpl().handleException(NetworkSampleActivity.this, e);
            }
        }).execute(new HUCRequstImpl(new HashMap<String, String>()));
    }
}
