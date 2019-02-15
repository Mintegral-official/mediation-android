package com.mintegral.outofmediation;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button rewardBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initView(){
        rewardBtn = findViewById(R.id.main_reward);
    }

    private void initListener(){
        if(rewardBtn != null){
            rewardBtn.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_reward:
                Intent intent = new Intent(MainActivity.this, RewardActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
