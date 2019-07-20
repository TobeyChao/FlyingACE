package com.soul.flyingace;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

class GameMenu {
    private Bitmap bmpMenu;
    private Bitmap[] bmpButton = new Bitmap[2];
    private int btnX, btnY;
    private Boolean isPress;
    private int btnState;

    GameMenu(Bitmap bmpMenu, Bitmap bmpButton, Bitmap bmpButtonPress) {
        this.bmpMenu = bmpMenu;
        this.bmpButton[0] = bmpButton;
        this.bmpButton[1] = bmpButtonPress;
        //开始游戏的按钮居中
        btnX = MySurfaceView.mScreenWidth / 2 - bmpButton.getWidth() / 2;
        btnY = MySurfaceView.mScreenHeight / 2 + bmpButton.getHeight();
        btnState = 0;
        isPress = false;
    }

    void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(bmpMenu, new Rect(0, 0, bmpMenu.getWidth(), bmpMenu.getHeight()),
                new Rect(0, 0, MySurfaceView.mScreenWidth, bmpMenu.getHeight()), paint);
        if (isPress) {
            canvas.drawBitmap(bmpButton[btnState], btnX, btnY, paint);
            btnY += 8;
            if (btnY > MySurfaceView.mScreenHeight - bmpButton[0].getHeight()) {
                isPress = false;
                MySurfaceView.gameState = MySurfaceView.GAMEING;
                paint.setColor(Color.WHITE);
            }
        } else {
            canvas.drawBitmap(bmpButton[btnState], btnX, btnY, paint);
        }
        btnState = (btnState + 1) % 2;
    }

    void onTouchEvent(MotionEvent event) {
        if (isPress) return;
        float pointX = event.getX();
        float pointY = event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            if (pointX > btnX && pointX < btnX + bmpButton[0].getWidth()) {
                isPress = pointY > btnY && pointY < btnY + bmpButton[0].getHeight();
            } else {
                isPress = false;
            }
        }
    }
}
