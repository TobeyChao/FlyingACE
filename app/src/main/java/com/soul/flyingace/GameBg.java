package com.soul.flyingace;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

class GameBg {
    private Bitmap bmpBackGround1;
    private Bitmap bmpBackGround2;

    private int bg1y, bg2y;

    GameBg(Bitmap bmpBackGround) {
        this.bmpBackGround1 = bmpBackGround;
        this.bmpBackGround2 = bmpBackGround;
        bg1y = -Math.abs(bmpBackGround1.getHeight() - MySurfaceView.mScreenHeight);
        bg2y = bg1y - bmpBackGround1.getHeight() + 1;
    }

    void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(bmpBackGround1, new Rect(0, 0, bmpBackGround1.getWidth(), bmpBackGround1.getHeight()),
                new Rect(0, bg1y, MySurfaceView.mScreenWidth, bg1y + bmpBackGround1.getHeight()), paint);
        canvas.drawBitmap(bmpBackGround2, new Rect(0, 0, bmpBackGround2.getWidth(), bmpBackGround2.getHeight()),
                new Rect(0, bg2y, MySurfaceView.mScreenWidth, bg2y + bmpBackGround2.getHeight()), paint);
    }

    void logic() {
        int speed = 5;
        bg1y += speed;
        bg2y += speed;
        if (bg1y > MySurfaceView.mScreenHeight) {
            bg1y = bg2y - bmpBackGround1.getHeight() + 1;
        }
        if (bg2y > MySurfaceView.mScreenHeight) {
            bg2y = bg1y - bmpBackGround1.getHeight() + 1;
        }
    }
}
