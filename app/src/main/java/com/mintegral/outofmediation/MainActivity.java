package com.mintegral.outofmediation;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button rewardBtn,interstitialBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initView(){
        rewardBtn = findViewById(R.id.main_reward);
        interstitialBtn = findViewById(R.id.main_interstitial);
    }

    private void initListener(){
        setViewListener(rewardBtn);
        setViewListener(interstitialBtn);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_reward:

                Intent rewardIntent = new Intent(MainActivity.this, RewardActivity.class);
                startActivity(rewardIntent);
                break;
            case R.id.main_interstitial:
                Intent interstitialIntent = new Intent(MainActivity.this, InterstitialActivity.class);
                startActivity(interstitialIntent);
                break;
            default:
                break;
        }
    }

    private void setViewListener(View view){
        if(view != null){
            view.setOnClickListener(this);
        }
    }
}
