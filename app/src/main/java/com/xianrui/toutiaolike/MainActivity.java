package com.xianrui.toutiaolike;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    SprayView mSprayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mSprayView = findViewById(R.id.spray_view);
//        findViewById(R.id.do_animation).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSprayView.makeBody();
//                mSprayView.startScroller();

        HoldPressHelper.addHoldPressListener(findViewById(R.id.do_animation), new HoldPressHelper.OnHoldPressListener() {
            @Override
            public void onTouchDown(View v) {

            }

            @Override
            public void onHold(View v) {
                mSprayView.makeBody();
                mSprayView.startScroller();
            }

            @Override
            public void onTouchUp(View v) {

            }
        }, 10);

    }
//        });
//}


}
