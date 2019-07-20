package com.soul.flyingace;

import java.util.Random;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder mSurfaceHolder;
    private Paint paint;
    private boolean flag;
    private Canvas canvas;
    //屏幕大小
    public static int mScreenWidth;
    public static int mScreenHeight;
    //游戏菜单
    public static final int GAME_MENU = 0;
    //游戏中
    public static final int GAMEING = 1;
    //游戏失败
    public static final int GAME_LOST = 2;

    public static int gameState = GAME_MENU;

    private Resources res = this.getResources();
    //爆炸
    private Bitmap bmpBoom;
    private Bitmap bmpEnemyFly;
    Bitmap bmpEnemyDuck;
    private Bitmap bmpPlayer;
    //子弹
    public static Bitmap bmpBullet;
    public static Bitmap bmpEnemyBullet;

    private GameMenu gameMenu;
    private GameBg backGround;
    private GameLost gamelost;
    private Player player;

    private Vector<Enemy> vcEnemy;
    private int count;

    private int enemyArray[][] = {{1, 2, 1}, {1, 1}, {1, 3, 1, 2}, {1, 2}, {2, 3}, {3, 1, 3}, {2, 2}, {1, 2}, {2, 2}, {1, 3, 1, 1}, {2, 1},
            {1, 3}, {2, 1}, {1, 3, 1, 1}, {3, 3, 3, 3}};
    private int enemyArrayIndex;//当前取出一维数组的下标
    private Random random;

    //敌人子弹
    private Vector<Bullet> vcBullet;
    private int countEnemyBullet;

    //玩家子弹
    private Vector<Bullet> vcBulletPlayer;
    private int countPlayerBullet;

    //爆炸
    private Vector<Boom> vcBoom;
    //控制器
    private Control control;

    SoundPlayer soundPlayer;

    public MySurfaceView(Context context) {
        super(context);
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
        paint = new Paint(Color.RED);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
        soundPlayer = new SoundPlayer(context);
        soundPlayer.initGameSound();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mScreenWidth = this.getWidth();
        mScreenHeight = this.getHeight();

        Init();
        flag = true;
        Thread th = new Thread(this);
        th.start();
    }

    private void Init() {
        if (gameState == GAME_MENU) {
            //加载资源
            Bitmap bmpBackGround = BitmapFactory.decodeResource(res, R.drawable.background);
            bmpBoom = BitmapFactory.decodeResource(res, R.drawable.boom);
            Bitmap bmpButton = BitmapFactory.decodeResource(res, R.drawable.button);
            Bitmap bmpButtonPress = BitmapFactory.decodeResource(res, R.drawable.button_press);
            bmpEnemyDuck = BitmapFactory.decodeResource(res, R.drawable.enemy_duck);
            bmpEnemyFly = BitmapFactory.decodeResource(res, R.drawable.enemy_fly);
            Bitmap bmpGameOver = BitmapFactory.decodeResource(res, R.drawable.gameover);
            Bitmap bmpGameReStart = BitmapFactory.decodeResource(res, R.drawable.restart);
            bmpPlayer = BitmapFactory.decodeResource(res, R.drawable.player);
            Bitmap bmpPlayerHp = BitmapFactory.decodeResource(res, R.drawable.hp);
            Bitmap bmpMenu = BitmapFactory.decodeResource(res, R.drawable.menu);
            bmpBullet = BitmapFactory.decodeResource(res, R.drawable.bullet);
            bmpEnemyBullet = BitmapFactory.decodeResource(res, R.drawable.bullet_enemy);
            //爆炸
            vcBoom = new Vector<>();//爆炸效果容器实例
            //子弹
            vcBullet = new Vector<>();
            vcBulletPlayer = new Vector<>();
            //菜单
            gameMenu = new GameMenu(bmpMenu, bmpButton, bmpButtonPress);
            //背景
            backGround = new GameBg(bmpBackGround);
            gamelost = new GameLost(bmpGameOver, bmpGameReStart);
            //玩家
            player = new Player(bmpPlayer, bmpPlayerHp);
            //敌人
            vcEnemy = new Vector<>();

            random = new Random();
            control = new Control(mScreenWidth - 350, mScreenHeight - 450, 80, 220);
        }
        control.reSet();
        Enemy.reset();
        Bullet.num = 0;
    }

    public void myDraw() {
        try {
            canvas = mSurfaceHolder.lockCanvas();
            if (canvas != null) {
                canvas.drawColor(Color.BLACK);
                switch (gameState) {
                    case GAME_MENU:
                        gameMenu.draw(canvas, paint);
                        break;
                    case GAMEING:
                        // 绘制背景
                        backGround.draw(canvas, paint);
                        // 绘制飞机
                        player.draw(canvas, paint);
                        // 绘制敌人
                        for (int i = 0; i < vcEnemy.size(); i++) {
                            vcEnemy.elementAt(i).draw(canvas, paint);
                        }
                        // 绘制敌人子弹
                        for (int i = 0; i < vcBullet.size(); i++) {
                            vcBullet.elementAt(i).draw(canvas, paint);
                        }
                        // 绘制主角子弹
                        for (int i = 0; i < vcBulletPlayer.size(); i++) {
                            vcBulletPlayer.elementAt(i).draw(canvas, paint);
                        }
                        // 绘制爆炸
                        for (int i = 0; i < vcBoom.size(); i++) {
                            vcBoom.elementAt(i).draw(canvas, paint);
                        }
                        // 绘制控制器
                        control.myDraw(canvas);
                        break;
                    case GAME_LOST:
                        // 绘制游戏背景
                        backGround.draw(canvas, paint);
                        // 绘制游戏结束界面
                        gamelost.draw(canvas, paint);

                        if (gameState == GAME_MENU) {
                            Init();
                        }
                        break;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (canvas != null)
                mSurfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (gameState) {
            case GAME_MENU:
                gameMenu.onTouchEvent(event);
                break;
            case GAMEING:
                control.onTouchEvent(event, player);
                break;
            case GAME_LOST:
                gamelost.onTouchEvent(event);
                if (gameState == GAME_MENU) {
                    Init();
                }
                break;
        }
        return true;
    }

    private void logic() {
        if (gameState == GAMEING) {
            backGround.logic();
            player.logic();
            for (int i = 0; i < vcEnemy.size(); i++) {
                Enemy enemy = vcEnemy.elementAt(i);
                if (enemy.isDead) {
                    vcEnemy.removeElementAt(i);
                } else {
                    enemy.logic();
                }
            }
            count++;
            if (count % Enemy.createEnemyTime == 0) {
                for (int i = 0; i < enemyArray[enemyArrayIndex].length; i++) {
                    if (enemyArray[enemyArrayIndex][i] == 1) {//章鱼怪
                        int x = random.nextInt(mScreenWidth - 100) + 50;
                        vcEnemy.addElement(new Enemy(bmpEnemyFly, 1, x, -50));
                    } else if (enemyArray[enemyArrayIndex][i] == 2) {//漂浮物左
                        int y = random.nextInt(20);
                        vcEnemy.addElement(new Enemy(bmpEnemyDuck, 2, -50, y));
                    } else if (enemyArray[enemyArrayIndex][i] == 3) {//漂浮物右
                        int y = random.nextInt(20);
                        vcEnemy.addElement(new Enemy(bmpEnemyDuck, 3, mScreenWidth + 50, y));
                    }
                }
                enemyArrayIndex = enemyArrayIndex + 1;//15组出现效果....一轮过去提升难度
                if (enemyArrayIndex >= 15) {
                    enemyArrayIndex = 0;
                    if (Enemy.createBulletTime > 5
                            && Enemy.createBulletTime >= Enemy.createEnemyTime)
                        Enemy.createBulletTime -= 5;
                    else if (Enemy.createEnemyTime > 5
                            && Enemy.createBulletTime <= Enemy.createEnemyTime)
                        Enemy.createEnemyTime -= 5;
                }
            }

            for (int i = 0; i < vcEnemy.size(); i++) {
                if (player.isCollsionWith(vcEnemy.elementAt(i))) {
                    player.setPlayerHp(player.getPlayerHp() - 1);
                    if (player.getPlayerHp() <= -1) {
                        gameState = GAME_LOST;
                    }
                }
            }

            countEnemyBullet++;
            if (countEnemyBullet % Enemy.createBulletTime == 0) {
                for (int i = 0; i < vcEnemy.size(); i++) {
                    Enemy en = vcEnemy.elementAt(i);
                    int bulletType = 0;
                    switch (en.type) {//不同类型敌人不同的子弹运行轨迹
                        case Enemy.TYPE_FLY://章鱼怪
                            bulletType = Bullet.BULLET_FLY;
                            break;
                        case Enemy.TYPE_DUCKL://漂浮物
                        case Enemy.TYPE_DUCKR:
                            bulletType = Bullet.BULLET_DUCK;
                            break;
                    }
                    vcBullet.add(new Bullet(bmpEnemyBullet, en.x + 10, en.y + 20, bulletType));
                }
            }
            //清除敌人子弹
            for (int i = 0; i < vcBullet.size(); i++) {
                Bullet b = vcBullet.elementAt(i);
                if (b.isDead) {
                    vcBullet.removeElement(b);
                } else {
                    b.logic();
                }
            }
            //被敌人子弹攻击
            for (int i = 0; i < vcBullet.size(); i++) {
                if (player.isCollsionWith(vcBullet.elementAt(i))) {
                    player.setPlayerHp(player.getPlayerHp() - 1);
                    if (player.getPlayerHp() <= -1) {
                        gameState = GAME_LOST;
                    }
                }
            }
            //攻击敌人
            for (int i = 0; i < vcBulletPlayer.size(); i++) {
                Bullet blPlayer = vcBulletPlayer.elementAt(i);
                for (int j = 0; j < vcEnemy.size(); j++) {
                    //添加爆炸效果
                    if (vcEnemy.elementAt(j).isCollsionWith(blPlayer)) {
                        vcBoom.add(new Boom(bmpBoom, vcEnemy.elementAt(j).x, vcEnemy.elementAt(j).y, 7));
                        soundPlayer.playSound(2, 0);
                        //章鱼怪
                        if (vcEnemy.elementAt(j).type == 1) {
                            player.addPlayerGoals(20);
                        }
                        vcEnemy.removeElementAt(j);
                        vcBulletPlayer.remove(blPlayer);
                    }
                }
            }

            //
            countPlayerBullet++;
            if (countPlayerBullet % 20 == 0) {
                soundPlayer.playSound(1, 0);
                switch (player.bulletKind) {//玩家武器选择
                    case 0://单发子弹
                        vcBulletPlayer.add(new Bullet(bmpBullet, player.x + 15, player.y - 20, Bullet.BULLET_PLAYER));
                        break;
                    case 1:
                        vcBulletPlayer.add(new Bullet(bmpBullet, player.x + 10, player.y - 20, Bullet.BULLET_PLAYER));
                        vcBulletPlayer.add(new Bullet(bmpBullet, player.x + 20, player.y - 20, Bullet.BULLET_PLAYER));
                        break;
                    case 2:
                        vcBulletPlayer.add(new Bullet(bmpBullet, player.x + 10, player.y - 20, Bullet.BULLET_PLAYER));
                        vcBulletPlayer.add(new Bullet(bmpBullet, player.x + 20, player.y - 20, Bullet.BULLET_PLAYER));
                        break;
                    case 3:
                        vcBulletPlayer.add(new Bullet(bmpBullet, player.x + 8, player.y - 20, Bullet.BULLET_PLAYER));
                        vcBulletPlayer.add(new Bullet(bmpBullet, player.x + 15, player.y - 25, Bullet.BULLET_PLAYER));
                        vcBulletPlayer.add(new Bullet(bmpBullet, player.x + 23, player.y - 20, Bullet.BULLET_PLAYER));
                        break;
                    case 4:
                        vcBulletPlayer.add(new Bullet(bmpBullet, player.x + 8, player.y - 20, Bullet.BULLET_PLAYER));
                        vcBulletPlayer.add(new Bullet(bmpBullet, player.x + 15, player.y - 25, Bullet.BULLET_PLAYER));
                        vcBulletPlayer.add(new Bullet(bmpBullet, player.x + 23, player.y - 20, Bullet.BULLET_PLAYER));
                        break;
                    case 5:
                        vcBulletPlayer.add(new Bullet(bmpBullet, player.x + 4, player.y - 20, Bullet.BULLET_PLAYER));
                        vcBulletPlayer.add(new Bullet(bmpBullet, player.x + 11, player.y - 20, Bullet.BULLET_PLAYER));
                        vcBulletPlayer.add(new Bullet(bmpBullet, player.x + 18, player.y - 20, Bullet.BULLET_PLAYER));
                        vcBulletPlayer.add(new Bullet(bmpBullet, player.x + 25, player.y - 20, Bullet.BULLET_PLAYER));
                        break;
                    case 6:
                        vcBulletPlayer.add(new Bullet(bmpBullet, player.x + 3, player.y - 20, Bullet.BULLET_PLAYER));
                        vcBulletPlayer.add(new Bullet(bmpBullet, player.x + 9, player.y - 22, Bullet.BULLET_PLAYER));
                        vcBulletPlayer.add(new Bullet(bmpBullet, player.x + 15, player.y - 25, Bullet.BULLET_PLAYER));
                        vcBulletPlayer.add(new Bullet(bmpBullet, player.x + 21, player.y - 22, Bullet.BULLET_PLAYER));
                        vcBulletPlayer.add(new Bullet(bmpBullet, player.x + 27, player.y - 20, Bullet.BULLET_PLAYER));

                    default:
                        break;
                }
            }
            //处理主角子弹
            for (int i = 0; i < vcBulletPlayer.size(); i++) {
                Bullet b = vcBulletPlayer.elementAt(i);
                if (b.isDead) {
                    vcBulletPlayer.removeElement(b);
                } else {
                    b.logic();
                }
            }
            //处理爆炸
            for (int i = 0; i < vcBoom.size(); i++) {
                Boom boom = vcBoom.elementAt(i);
                if (boom.playEnd) {
                    vcBoom.removeElementAt(i);
                } else {
                    vcBoom.elementAt(i).logic();
                }
            }
        }
    }

    @Override
    public void run() {

        while (flag) {
            long start = System.currentTimeMillis();
            myDraw();
            logic();
            long end = System.currentTimeMillis();
            try {
                if (end - start < 25) {
                    Thread.sleep(25 - (end - start));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        flag = false;
    }
}

