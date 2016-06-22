package com.epicodus.pocketpomodoro.game.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;

import com.epicodus.pocketpomodoro.R;
/**
 * Created by Guest on 5/18/16.
 */
public class Swordfish {
        private float x, y, screenX, screenY, width, height, swordfishSpeed, startX, startY;
        private RectF rect;
        private RectF hitbox;
        public boolean isVisible;
        public boolean isDead;
        public final int FALL_SPEED = 250;
        public Harpoon killHarpoon;
        private int frameCount;
        private int currentFrame;
        private long lastFrameChangeTime;
        private int frameLength;
        private Rect frameToDraw;

        private Bitmap bitmap;


        public Swordfish(Context context, float screenX, float screenY) {
            this.screenX = screenX;
            this.screenY = screenY;
            this.width = screenX/8;
            this.height = screenY/8;
            this.rect = new RectF();
            this.swordfishSpeed = screenY/4;
            this.hitbox = new RectF();
            frameCount = 2;
            currentFrame = 0;
            lastFrameChangeTime = 0;
            frameLength = 700;
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.swordfish);
            bitmap = Bitmap.createScaledBitmap(bitmap, (int) width*frameCount, (int) height, false);
            frameToDraw = new Rect(0, 0, (int) width, (int) height);
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

        public void setHitbox(RectF hitbox) {
            this.hitbox = hitbox;
        }

    public boolean isVisible() {
            return isVisible;
        }

        public void setVisible(boolean visible) {
            isVisible = visible;
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

            rect.left = x;
            rect.right = x + width;
            rect.top = y;
            rect.bottom = y+height;
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
                    x = x - swordfishSpeed/fps;
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
            if(rect.right < 0) {
                isVisible = false;
                killHarpoon = null;
                isDead = false;
            }

            hitbox.top = y + height/5;
            hitbox.bottom = y + height-height/5;
            hitbox.left = x + width/4;
            hitbox.right = x + width - width/12;
        }



        public float getHeight() {
            return height;
        }
    }

