package com.soul.flyingace;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

class Boom {
    private Bitmap bmpBoom;
    private int boomX, boomY;
    private int cureentFrameIndex;
    private int totleFrame;
    private int frameW, frameH;
    boolean playEnd;

    Boom(Bitmap bmpBoom, int x, int y, int totleFrame) {
        this.bmpBoom = bmpBoom;
        this.boomX = x;
        this.boomY = y;
        this.totleFrame = totleFrame;
        frameW = bmpBoom.getWidth() / totleFrame;
        frameH = bmpBoom.getHeight();
    }

    void draw(Canvas canvas, Paint paint) {
        canvas.save();
        canvas.clipRect(boomX, boomY, boomX + frameW, boomY + frameH);
        canvas.drawBitmap(bmpBoom, boomX - cureentFrameIndex * frameW, boomY, paint);
        canvas.restore();
    }

    void logic() {
        if (cureentFrameIndex < totleFrame) {
            cureentFrameIndex++;
        } else {
            playEnd = true;
        }
    }
}
