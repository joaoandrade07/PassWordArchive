package com.joaoandrade.passwordarchive.Controller;

import android.os.SystemClock;
import android.view.View;

public class SingleClickListener implements View.OnClickListener{

    protected int defaultInterval;
    protected long lastTimeClicked = 0;

    public SingleClickListener(){
        this(1000);
    }

    public SingleClickListener(int minInterval){
        this.defaultInterval = minInterval;
    }

    @Override
    public void onClick(View view) {
        if(SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval){
            return;
        }
        lastTimeClicked = SystemClock.elapsedRealtime();
        performClick(view);
    }

    public void performClick(View view) {

    }
}
