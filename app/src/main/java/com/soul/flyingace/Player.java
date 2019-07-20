package com.soul.flyingace;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class Player {
    //主角的血量与血量位图
    private int playerHp =3;//默认3血
    private Bitmap bmpPlayerHp;
    public int x, y,x_v,y_v;//主角的坐标以
    private Bitmap bmpPlayer;//及位图
    private int speed = 20;//主角移动速度
    private int goals=0;//主角分数
    public int bulletKind=0;//主角子弹种类，根据当前分数升级武器
    private boolean isUp, isDown, isLeft, isRight;//主角移动标识[按键所用]
    private boolean isL,isR;//触屏专用
    private int noCollisionCount = 0;//计时器//碰撞后处于无敌时间
    private int noCollisionTime = 60;//无敌时间
    private boolean isCollision;//是否碰撞的标识位
    //传感器类
    private SensorManager sm;//申明一个传感器管理器
    private Sensor sensor;//声明一个传感器
    private SensorEventListener mySensorListener;//声明一个传感器监听器
    //主角的构造函数
    @SuppressLint("InlinedApi")
    public Player(Bitmap bmpPlayer, Bitmap bmpPlayerHp) {
        this.bmpPlayer = bmpPlayer;
        this.bmpPlayerHp = bmpPlayerHp;
        x = MySurfaceView.mScreenWidth / 2 - bmpPlayer.getWidth() / 2;
        y = MySurfaceView.mScreenHeight - bmpPlayer.getHeight();
        x_v=y_v=0;
        goals=0;
        isL=isR=false;
        bulletKind=0;
    }
    //主角的绘图函数
    public void draw(Canvas canvas, Paint paint) {
        //绘制主角
        //当处于无敌时间时，让主角闪烁
        if (isCollision) {
            //每2次游戏循环，绘制一次主角
            if (noCollisionCount % 2 == 0) {
                canvas.drawBitmap(bmpPlayer, x, y, paint);
            }
        } else {
            canvas.drawBitmap(bmpPlayer, x, y, paint);
        }
        //绘制主角血量
        for (int i = 0; i < playerHp; i++) {
            canvas.drawBitmap(bmpPlayerHp, i * bmpPlayerHp.getWidth(),MySurfaceView.mScreenHeight - bmpPlayerHp.getHeight(), paint);
        }
        paint.setTextSize(48);
        canvas.drawText("$:"+String.valueOf(goals),playerHp*bmpPlayerHp.getWidth(),MySurfaceView.mScreenHeight - bmpPlayerHp.getHeight()+15,paint);
        canvas.drawLine(playerHp*bmpPlayerHp.getWidth(),MySurfaceView.mScreenHeight - bmpPlayerHp.getHeight()+25, playerHp*bmpPlayerHp.getWidth()+15,MySurfaceView.mScreenHeight - bmpPlayerHp.getHeight()+25, paint);
    }

    //主角的逻辑
    public void logic() {
        if(goals<100){//根据分数设定武器类型
            bulletKind=0;
        }else if(goals<200){
            bulletKind=1;
        }else if(goals<300){
            bulletKind=2;
        }else if(goals<400){
            bulletKind=3;
        }else if(goals<500){
            bulletKind=4;
        }else if(goals<600){
            bulletKind=5;
        }else if(goals<700){
            bulletKind=6;
        }else if(goals<800){
            bulletKind=7;
        }else if(goals<900){
            bulletKind=8;
        }else if(goals<1000){
            bulletKind=9;
        }else if(goals<1100){
            bulletKind=10;
        }else if(goals<1200){
            bulletKind=11;
        }else if(goals<1300){
            bulletKind=12;
        }else if(goals<1400){
            bulletKind=13;
        }
        //处理主角移动
        if (isLeft) {
            x -= speed;
        }
        if (isRight) {
            x += speed;
        }
        if (isUp) {
            y -= speed;
        }
        if (isDown) {
            y += speed;
        }
        if(Math.abs(x_v)>5)x_v=x_v/Math.abs(x_v)*5;
        x+=x_v;
        //判断屏幕X边界
        if (x + bmpPlayer.getWidth() >= MySurfaceView.mScreenWidth) {
            x = MySurfaceView.mScreenWidth - bmpPlayer.getWidth();
            x_v=0;
            isL=isR=false;
        } else if (x <= 0) {
            x = 0;
            x_v=0;
            isL=isR=false;
        }
        //判断屏幕Y边界
        if (y + bmpPlayer.getHeight() >= MySurfaceView.mScreenHeight) {
            y = MySurfaceView.mScreenHeight - bmpPlayer.getHeight();
        } else if (y <= 0) {
            y = 0;
        }

        //处理无敌状态
        if (isCollision) {
            //计时器开始计时
            noCollisionCount++;
            if (noCollisionCount >= noCollisionTime) {
                //无敌时间过后，接触无敌状态及初始化计数器
                isCollision = false;
                noCollisionCount = 0;
            }
        }
    }
    //将主角设为无敌态，时间time
    public void setPlayerNoCollision(int time){
        isCollision=true;
    }
    //设置主角血量
    public void setPlayerHp(int hp) {
        this.playerHp = hp;
    }
    //获取主角血量
    public int getPlayerHp() {
        return playerHp;
    }
    //增加主角分数
    public void addPlayerGoals(int num){
        goals+=num;
    }
    //获取主角分数
    public int getPlayerGoals(){
        return goals;
    }
    //判断碰撞(主角与敌机)
    public boolean isCollsionWith(Enemy en) {
        //是否处于无敌时间
        if (isCollision == false) {
            int x2 = en.x;
            int y2 = en.y;
            int w2 = en.frameW;
            int h2 = en.frameH;
            if (x >= x2 && x >= x2 + w2) {
                return false;
            } else if (x <= x2 && x + bmpPlayer.getWidth() <= x2) {
                return false;
            } else if (y >= y2 && y >= y2 + h2) {
                return false;
            } else if (y <= y2 && y + bmpPlayer.getHeight() <= y2) {
                return false;
            }
            //碰撞即进入无敌状态
            isCollision = true;
            return true;
            //处于无敌状态，无视碰撞
        } else {
            return false;
        }
    }
    //判断碰撞(主角与敌机子弹)
    public boolean isCollsionWith(Bullet bullet) {
        //是否处于无敌时间
        if (isCollision == false) {
            int x2 = bullet.bulletX;
            int y2 = bullet.bulletY;
            int w2 = bullet.bmpBullet.getWidth();
            int h2 = bullet.bmpBullet.getHeight();
            if (x >= x2 && x >= x2 + w2) {
                return false;
            } else if (x <= x2 && x + bmpPlayer.getWidth() <= x2) {
                return false;
            } else if (y >= y2 && y >= y2 + h2) {
                return false;
            } else if (y <= y2 && y + bmpPlayer.getHeight() <= y2) {
                return false;
            }
            //碰撞即进入无敌状态
            isCollision = true;
            return true;
            //处于无敌状态，无视碰撞
        } else {
            return false;
        }
    }
    //设置运动方向[0不动，1up,2,left,3,down,4,right]
    public void setDirect(int dir){
        switch(dir){
            case 0:isLeft=isRight=isUp=isDown=false;break;
            case 1:isUp=true;break;
            case 2:isLeft=true;break;
            case 3:isDown=true;break;
            case 4:isRight=true;break;
            default:break;
        }
    }
}
