package com.example.guest.pomodoro.game.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import com.example.guest.pomodoro.R;


public class Spike {
    private float x;
    private float y;
    private float screenX;
    private float screenY;
    private float width;
    private float height;
    private float spikeSpeed;
    private float startX;
    private float endX;
    private float angle;
    private Bitmap bitmap;
    private RectF rect;
    private RectF hitbox;
    public boolean isShot;
    public boolean isVisible;
    public final int FALL_SPEED = 250;
    public Pufferfish thrower;

    public Spike(Context context, float screenX, float screenY) {
        this.screenX = screenX;
        this.screenY = screenY;
        this.width = screenX / 30;
        this.height = screenY / 58;
        this.rect = new RectF();
        this.hitbox = new RectF();
        spikeSpeed = screenX/4;
        isVisible = false;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.spine);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, false);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getStartX() {
        return startX;
    }

    public RectF getRect() {
        return rect;
    }

    public RectF getHitbox() {
        return hitbox;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void shoot(float startX, float startY) {
        this.startX = startX;
        x = startX + thrower.getWidth()/2;
        y = startY + thrower.getHeight()/2;
        isShot = true;
        isVisible = true;

    }

    public void update(long fps, float scrollSpeed) {

        x = (float) (x - spikeSpeed*Math.cos(Math.toRadians(angle)) / fps);
        y = (float) (y - spikeSpeed*Math.sin(Math.toRadians(angle)) / fps);
        x = x - scrollSpeed / fps;
        rect.left = x;
        rect.right = x + width;
        rect.top = y;
        rect.bottom = y + height;

        hitbox.left = x + 13*width/16;
        hitbox.top = y + height/8;
        hitbox.right = x + width;
        hitbox.bottom = y + 13*height/16;
    }
}
