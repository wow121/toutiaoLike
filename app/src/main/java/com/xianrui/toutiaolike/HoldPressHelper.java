package com.xianrui.toutiaolike;

import android.view.MotionEvent;
import android.view.View;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by xianrui on 2017/10/24.
 */

public class HoldPressHelper {

    public static void addHoldPressListener(View v, OnHoldPressListener onHoldPressListener) {
        addHoldPressListener(v, onHoldPressListener, 100);
    }

    public static void addHoldPressListener(View v, final OnHoldPressListener onHoldPressListener, final long time) {
        v.setOnTouchListener(new View.OnTouchListener() {
            Subscription subscription = null;

            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        subscription = Observable.interval(0, time, TimeUnit.MILLISECONDS)
                                .subscribeOn(AndroidSchedulers.mainThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<Long>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(Long aLong) {
                                        onHoldPressListener.onHold(v);
                                    }
                                });
                        onHoldPressListener.onTouchDown(v);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (subscription != null) {
                            subscription.unsubscribe();
                        }
                        onHoldPressListener.onTouchUp(v);
                        break;
                }
                return true;
            }
        });
    }

    public interface OnHoldPressListener {
        void onTouchDown(View v);

        void onHold(View v);

        void onTouchUp(View v);
    }
}
