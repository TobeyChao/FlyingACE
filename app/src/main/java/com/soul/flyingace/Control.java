package com.soul.flyingace;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

class Control {
    private Paint paint;

    private float smallCenterX, smallCenterY, smallCenterR;
    private float BigCenterX, BigCenterY, BigCenterR;

    Control(float centerX, float centerY, float sR, float bR) {
        smallCenterX = BigCenterX = centerX;
        BigCenterY = smallCenterY = centerY;
        smallCenterR = sR;
        BigCenterR = bR;
        paint = new Paint(Color.RED);
    }

    void reSet() {
        smallCenterX = BigCenterX;
        smallCenterY = BigCenterY;
    }

    void myDraw(Canvas canvas) {

        paint.setAlpha(0x77);
        canvas.drawCircle(BigCenterX, BigCenterY, BigCenterR, paint);
        canvas.drawCircle(smallCenterX, smallCenterY, smallCenterR, paint);
    }

    void onTouchEvent(MotionEvent event, Player player) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            smallCenterX = BigCenterX;
            smallCenterY = BigCenterY;
            player.setDirect(0);
        } else {
            player.setDirect(0);
            int pointX = (int) event.getX();
            int pointY = (int) event.getY();
            double angle = getRad(BigCenterX, BigCenterY, pointX, pointY);//

            if (Math.sqrt(Math.pow((BigCenterX - (int) event.getX()), 2) + Math.pow((BigCenterY - (int) event.getY()), 2)) <= BigCenterR) {
                smallCenterX = pointX;
                smallCenterY = pointY;
            } else {
                //最边上
                setSmallCircleXY(BigCenterX, BigCenterY, BigCenterR, angle);
            }
            angle = angle / Math.PI * 180;
            if (angle >= -150 && angle <= -30) player.setDirect(1);
            else if (angle >= 30 && angle <= 150) player.setDirect(3);
            if (Math.abs(angle) >= 120) player.setDirect(2);
            else if (Math.abs(angle) <= 60) player.setDirect(4);
        }
    }

    private void setSmallCircleXY(float centerX, float centerY, float R, double rad) {
        smallCenterX = (float) (R * Math.cos(rad)) + centerX;
        smallCenterY = (float) (R * Math.sin(rad)) + centerY;
    }

    private double getRad(float px1, float py1, float px2, float py2) {
        float x = px2 - px1;
        float y = py1 - py2;
        float Hypotenuse = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        float cosAngle = x / Hypotenuse;
        float rad = (float) Math.acos(cosAngle);
        if (py2 < py1) {
            rad = -rad;
        }
        return rad;
    }
}
