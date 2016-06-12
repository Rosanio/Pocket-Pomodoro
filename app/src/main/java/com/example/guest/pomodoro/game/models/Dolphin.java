package com.example.guest.pomodoro.game.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;

import com.example.guest.pomodoro.R;

import java.util.Random;


public class Dolphin {
    private float x, y, screenX, screenY, width, height, dolphinSpeed, startX, startY;
    private RectF rect;
    private RectF hitbox;
    public boolean isVisible;
    public boolean isDead;
    public boolean spearThrown;
    public final int FALL_SPEED = 250;
    public Harpoon killHarpoon;
    private int frameCount;
    private int currentFrame;
    private long lastFrameChangeTime;
    private int frameLength;
    private Rect frameToDraw;
    private Random randomNumberGenerator;
    public int life;

    private Bitmap bitmap;


    public Dolphin(Context context, float screenX, float screenY) {
        this.screenX = screenX;
        this.screenY = screenY;
        this.width = screenX/5;
        this.height = screenY/5;
        this.rect = new RectF();
        this.dolphinSpeed = screenY/10;
        this.hitbox = new RectF();
        frameCount = 2;
        currentFrame = 0;
        lastFrameChangeTime = 0;
        frameLength = 700;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.dolphin);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) width*frameCount, (int) height, false);
        frameToDraw = new Rect(0, 0, (int) width, (int) height);
        randomNumberGenerator = new Random();
        life = 2;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getDolphinSpeed() {
        return dolphinSpeed;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public RectF getHitbox() {
        return hitbox;
    }

    public Rect getFrameToDraw() {
        return frameToDraw;
    }

    public void setFrameLength(int length) {
        frameLength = length;
    }

    public RectF getRect() {
        return rect;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public void setHitbox(RectF hitbox) {
        this.hitbox = hitbox;
    }

    public float getWidth() {
        return width;
    }

    public boolean takeAim(float playerY, float playerHeight) {

        int randomNumber = -1;

        if (!spearThrown) {
            if ((playerY + playerHeight > y && playerY + playerHeight < y + height) || (playerY > y && playerY < y + height)) {

                randomNumber = randomNumberGenerator.nextInt(10);

            } else {
                randomNumber = randomNumberGenerator.nextInt(100);
            }
            if (randomNumber == 0) {
                spearThrown = true;
                return true;
            }
        }

        return false;
    }


    public float getHeight() {
        return height;
    }

    public void generate(float startY) {
        this.startY = startY;
        this.startX = screenX;
        x = startX;
        y = startY;

        if (y < screenY/5) {
            y = screenY/5;
        }
        if (y + height > 9*screenY/10) {
            y = 9*screenY/10 - height;
        }

        isVisible = true;
        isDead = false;
        killHarpoon = null;
    }

    public void getCurrentFrame() {
        long time = System.currentTimeMillis();
        if (time > lastFrameChangeTime + frameLength) {
            lastFrameChangeTime = time;
            currentFrame++;
            if (currentFrame > frameCount-1) {
                currentFrame = 0;
            }
        }
        frameToDraw.left = currentFrame * (int) width;
        frameToDraw.right = frameToDraw.left + (int) width;
    }

    public void update(long fps, float scrollSpeed) {
        if(!isDead) {
            if(fps > 0) {
                x = x - dolphinSpeed/fps;
            }
        } else {
            if (y < screenY-height) {
                y = y + FALL_SPEED/fps;
            }
        }
        x = x - scrollSpeed/fps;
        rect.left = x;
        rect.right = x + width;
        rect.top = y;
        rect.bottom = y+height;

        hitbox.top = y + height/18;
        hitbox.bottom = y + height-height/6;
        hitbox.left = x + width/15;
        hitbox.right = x + width - width/15;

        if(rect.right < 0) {
            isVisible = false;
            killHarpoon = null;
            isDead = false;
        }

    }
}
