package com.soul.flyingace;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

class Bullet {
    public Bitmap bmpBullet;
    int bulletX, bulletY;
    private int speed;
    private int bulletType;
    static final int BULLET_PLAYER = -1;
    static final int BULLET_DUCK = 1;//漂浮物的
    static final int BULLET_FLY = 2;
    boolean isDead;//
    static int num = 0;

    Bullet(Bitmap bmpBullet, int bulletX, int bulletY, int bulletType) {
        this.bmpBullet = bmpBullet;
        this.bulletX = bulletX;
        this.bulletY = bulletY;
        this.bulletType = bulletType;
        switch (bulletType) {//
            case BULLET_PLAYER:
                speed = 15;
                break;
            case BULLET_DUCK:
                speed = 3;
                break;
            case BULLET_FLY:
                speed = 4;
                break;
        }
    }

    void draw(Canvas canvas, Paint paint) {
        Matrix matrix = new Matrix();
        int angle = 0;
        matrix.postRotate(angle);
        int width = bmpBullet.getWidth();
        int height = bmpBullet.getHeight();
        Bitmap xuanBmp = Bitmap.createBitmap(bmpBullet, 0, 0, width, height, matrix, true);
        canvas.drawBitmap(xuanBmp, bulletX, bulletY, paint);
    }

    void logic() {
        switch (bulletType) {
            case BULLET_PLAYER:
                bulletY -= speed;
                if (bulletY < -50) {
                    isDead = true;
                }
                break;
            case BULLET_DUCK:
            case BULLET_FLY:
                bulletY += speed;
                if (bulletY > MySurfaceView.mScreenHeight) {
                    isDead = true;
                }
                break;
        }
    }
}
