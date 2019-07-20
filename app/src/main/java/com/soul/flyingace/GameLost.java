package com.soul.flyingace;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;


class GameLost {

    private Bitmap bmpGameOver;
    private Bitmap bmpButton;
    private int goX, goY, goStopY;
    private int btnX, btnY, btnStopX, btnStopY;
    private int state;

    GameLost(Bitmap bmpGameOver, Bitmap reStart) {
        this.bmpGameOver = bmpGameOver;
        this.bmpButton = reStart;
        goX = MySurfaceView.mScreenWidth / 2 - bmpGameOver.getWidth() / 2;
        goStopY = MySurfaceView.mScreenHeight / 2 - bmpGameOver.getHeight() * 6 / 5;
        goY = -200;
        btnStopX = MySurfaceView.mScreenWidth + 100;
        btnX = MySurfaceView.mScreenWidth / 2 - bmpButton.getWidth() / 2;
        btnStopY = MySurfaceView.mScreenHeight - bmpButton.getHeight() * 3 / 2;
        btnY = MySurfaceView.mScreenHeight + 50;
        state = 0;
    }

    void draw(Canvas canvas, Paint paint) {
        switch (state) {
            case 0:
                goY += 20;
                if (goY >= goStopY) {
                    state = 1;
                }
                canvas.drawBitmap(bmpGameOver, goX, goY, paint);
                break;
            case 1:
                state = 2;
                break;
            case 2:
                btnY -= 20;
                if (btnY <= btnStopY) {
                    state = 3;
                }
                canvas.drawBitmap(bmpGameOver, goX, goY, paint);
                canvas.drawBitmap(bmpButton, btnX, btnY, paint);
                break;
            case 3:
                state = 4;
            case 4:
                canvas.drawBitmap(bmpGameOver, goX, goY, paint);
                canvas.drawBitmap(bmpButton, btnX, btnY, paint);
                break;
            case 5:
                btnX += 20;
                if (btnX >= btnStopX) {
                    MySurfaceView.gameState = MySurfaceView.GAME_MENU;//
                }
                canvas.drawBitmap(bmpGameOver, goX, goY, paint);
                canvas.drawBitmap(bmpButton, btnX, btnY, paint);
                break;
            default:
                break;
        }
    }

    void onTouchEvent(MotionEvent event) {
        float pointX = event.getX();
        float pointY = event.getY();
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (pointX > btnX && pointX < btnX + bmpButton.getWidth()) {
                if (pointY > btnY && pointY < btnY + bmpButton.getHeight()) {
                    state = 5;
                }
            }
        }
    }
}
