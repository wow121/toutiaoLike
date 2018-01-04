package com.xianrui.toutiaolike;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Scroller;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by xianrui on 2018/1/2.
 */

public class SprayView extends View {
    //水平速度随机范围
    private static final int HORIZONTAL_SPEED_RANGE = 600;

    //垂直速度随机范围
    private static final int VERTICAL_SPEED_RANGE = 1500;

    //重力加速度
    private static final float GRAVITY = -980f;

    //动画执行时间
    private static final int ANIMATION_DURATION = 3000;

    private static Random mRandom = new Random();

    //通过Scroller更新View
    Scroller mScroller;

    List<SprayBody2> mBodyList;

    Paint mPaint;

    List<Emoji> mEmojiList;

    //测试bitmap
    static Bitmap mBitmap;

    public SprayView(Context context) {
        this(context, null);
    }

    public SprayView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SprayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        mScroller = new Scroller(getContext());

        mBodyList = new ArrayList<>();

        mPaint = new Paint();
        mPaint.setTextSize(80);
        mPaint.setTextAlign(Paint.Align.CENTER);

        mEmojiList = (List<Emoji>) EmojiManager.getAll();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);

    }

    public void makeBody() {
        int index = mRandom.nextInt(mEmojiList.size() - 1);
        mBodyList.add(new SprayBody2(mEmojiList.get(index), getWidth() / 2, getHeight()));
    }

    public void startScroller() {
        mScroller.startScroll(0, 0, 100, 100, ANIMATION_DURATION);
        invalidate();
    }


    @Override
    public void computeScroll() {
        if (!mScroller.computeScrollOffset()) {
            if (mBodyList.size() > 0) {
                mBodyList.clear();
            }
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (SprayBody2 sprayBody : mBodyList) {
            sprayBody.draw(canvas, mPaint);
        }
    }

    private static class SprayBody2 {

        private int horizontal_speed = mRandom.nextInt(HORIZONTAL_SPEED_RANGE) - HORIZONTAL_SPEED_RANGE / 2;
        private int vertical_speed = mRandom.nextInt(VERTICAL_SPEED_RANGE);

        Emoji mEmoji;
        int mX;
        int mY;
        long mStartTime;
        long mDrawTime;

        SprayBody2(Emoji emoji, int x, int y) {
            this.mEmoji = emoji;
            this.mX = x;
            this.mY = y;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
        }

        int getX() {
            return (int) (mX - ((float) mDrawTime / 1000 * horizontal_speed));
        }

        int getY() {
            return (int) (mY - ((Math.pow(((float) mDrawTime / 1000), 2) * GRAVITY / 2) + ((float) mDrawTime / 1000) * vertical_speed));
        }

        int getAlpha() {
            float progress = ((float) (ANIMATION_DURATION - mDrawTime) / ANIMATION_DURATION);
            if (progress < 0.50f) {
                progress = (progress / 0.50f);
            } else {
                progress = 1;
            }
            int alpha = (int) (progress * 255);
            if (alpha < 0) {
                alpha = 0;
            }
            return alpha;
        }

        int getAngle() {
            int angle = (int) (((float) (ANIMATION_DURATION - mDrawTime) / ANIMATION_DURATION) * 360);
            if (angle < 0) {
                angle = 0;
            }
            return angle;
        }


        void draw(Canvas canvas, Paint paint) {
            mDrawTime = AnimationUtils.currentAnimationTimeMillis() - mStartTime;
            paint.setAlpha(getAlpha());
            canvas.rotate(getAngle(), getX(), getY());
            canvas.drawText(mEmoji.getUnicode(), getX(), getY(), paint);
            canvas.rotate(-getAngle(), getX(), getY());
        }
    }

}
