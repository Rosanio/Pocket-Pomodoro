package com.example.guest.pomodoro;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;

/**
 * Created by Guest on 4/21/16.
 */
public class Ship {

    PointF a;
    PointF b;
    PointF c;
    PointF centre;

    float facingAngle = 270;

    private float length;
    private float width;

    private float speed = 200;

    private float horizontalVelocity;
    private float verticalVelocity;

    private float rotationSpeed = 200;

    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;
    public final int THRUSTING = 3;

    private int shipMoving = STOPPED;

    public Ship(Context context, int screenX, int screenY) {
        length = screenX / 5;
        width = screenY / 5;

        a = new PointF();
        b = new PointF();
        c = new PointF();
        centre = new PointF();

        centre.x = screenX / 2;
        centre.y = screenY / 2;

        a.x = centre.x;
        a.y = centre.y - length / 2;

        b.x = centre.x - width / 2;
        b.y = centre.y + length / 2;

        c.x = centre.x + width / 2;
        c.y = centre.y + length / 2;
    }

    public PointF getCentre() {
        return centre;
    }

    public PointF getA() {
        return a;
    }

    public PointF getB() {
        return b;
    }

    public PointF getC() {
        return c;
    }

    float getFacingAngle() {
        return facingAngle;
    }

    public void setMovementState(int state) {
        shipMoving = state;
    }

    public void update(long fps) {
        float previousFA = facingAngle;
        if(shipMoving == LEFT) {
            facingAngle = facingAngle - rotationSpeed/fps;
            if(facingAngle < 1) {
                facingAngle = 360;
            }
        }
        if(shipMoving == RIGHT) {
            facingAngle = facingAngle + rotationSpeed/fps;
            if(facingAngle > 360) {
                facingAngle = 1;
            }
        }
        if(shipMoving == THRUSTING) {
            horizontalVelocity = (float)(Math.cos(Math.toRadians(facingAngle)));
            verticalVelocity = (float) (Math.sin(Math.toRadians(facingAngle)));

            centre.x = centre.x + horizontalVelocity * speed / fps;
            centre.y = centre.y + verticalVelocity * speed / fps;

            a.x = a.x + horizontalVelocity * speed/fps;
            a.y = a.y + verticalVelocity * speed/fps;

            b.x = b.x + horizontalVelocity * speed/fps;
            b.y = b.y + verticalVelocity * speed/fps;

            c.x = c.x + horizontalVelocity * speed/fps;
            c.y = c.y + verticalVelocity * speed/fps;;
        }

        float tempX = 0;
        float tempY = 0;

        a.x = a.x - centre.x;
        a.y = a.y - centre.y;

        tempX = (float)(a.x * Math.cos(Math.toRadians(facingAngle - previousFA)) - a.y * Math.sin(Math.toRadians(facingAngle - previousFA)));
        tempY = (float) (a.x * Math.sin(Math.toRadians(facingAngle - previousFA)) + a.y * Math.cos(Math.toRadians(facingAngle - previousFA)));

        a.x = tempX + centre.x;
        a.y = tempY + centre.y;

        b.x = b.x - centre.x;
        b.y = b.y - centre.y;

        tempX = (float)(b.x * Math.cos(Math.toRadians(facingAngle - previousFA)) - b.y * Math.sin(Math.toRadians(facingAngle - previousFA)));
        tempY = (float) (b.x * Math.sin(Math.toRadians(facingAngle - previousFA)) + b.y * Math.cos(Math.toRadians(facingAngle - previousFA)));

        b.x = tempX + centre.x;
        b.y = tempY + centre.y;

        c.x = c.x - centre.x;
        c.y = c.y - centre.y;

        tempX = (float)(c.x * Math.cos(Math.toRadians(facingAngle - previousFA)) - c.y * Math.sin(Math.toRadians(facingAngle - previousFA)));
        tempY = (float) (c.x * Math.sin(Math.toRadians(facingAngle - previousFA)) + c.y * Math.cos(Math.toRadians(facingAngle - previousFA)));

        c.x = tempX + centre.x;
        c.y = tempY + centre.y;

    }
}