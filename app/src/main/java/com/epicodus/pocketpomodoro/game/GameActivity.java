/*
    todo:
    POLISH

    make points better
    treasure chests
    ideas for upgrades:
      better weapons
      weapon does more damage
    move speed is slow at first, increases as you move
    Make dolphins not overlap
    scroll faster as player touches right side of screen
*/

package com.epicodus.pocketpomodoro.game;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;

import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;

import com.epicodus.pocketpomodoro.models.Card;
import com.epicodus.pocketpomodoro.models.Deck;
import com.epicodus.pocketpomodoro.ui.StudyActivity;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/*
This is the entry point to the game.
It will handle the lifecycle of the game by calling
methods of view when prompted to so by the OS.
*/
public class GameActivity extends Activity {

    /*
    view will be the view of the game
    It will also hold the logic of the game
    and respond to screen touches as well
    */
    GameView view;
    private Timer timer;
    private TimerTask task;
    private long startTime;
    private long currentTime;
    private Deck mDeck;
    private ArrayList<Card> mCards;
    private int harpoonUpgrade;
    private int oxygenUpgrade;
    private int speedUpgrade;
    private int lungsUpgrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Get a Display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();
        // Load the resolution into a Point object
        Point size = new Point();
        display.getSize(size);

        mDeck = Parcels.unwrap(getIntent().getParcelableExtra("deck"));
        mCards = Parcels.unwrap(getIntent().getParcelableExtra("remainingCards"));
        harpoonUpgrade = getIntent().getIntExtra("harpoonUpgrade", 0);
        oxygenUpgrade = getIntent().getIntExtra("oxygenUpgrade", 0);
        speedUpgrade = getIntent().getIntExtra("speedUpgrade", 0);
        lungsUpgrade = getIntent().getIntExtra("lungsUpgrade", 0);

        // Initialize gameView and set it as the view
        view = new GameView(this, size.x, size.y, harpoonUpgrade, oxygenUpgrade, speedUpgrade, lungsUpgrade);
        setContentView(view);

        startTime = System.currentTimeMillis();

        task = new TimerTask() {
            @Override
            public void run() {
                currentTime = System.currentTimeMillis();
                if(currentTime - startTime > 30000) {
                    int[] upgrades = view.getUpgradeLevels();
                    Log.d("oxygenUpgrade", upgrades[1]+"");
                    Intent intent = new Intent(GameActivity.this, StudyActivity.class);
                    intent.putExtra("deck", Parcels.wrap(mDeck));
                    intent.putExtra("remainingCards", Parcels.wrap(mCards));
                    intent.putExtra("harpoonUpgrade", upgrades[0]);
                    intent.putExtra("oxygenUpgrade", upgrades[1]);
                    intent.putExtra("speedUpgrade", upgrades[2]);
                    intent.putExtra("lungsUpgrade", upgrades[3]);
                    startActivity(intent);
                    finish();
                }
            }
        };

        timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, 1000);

    }

    // This method executes when the player starts the game
    @Override
    protected void onResume() {
        super.onResume();

        // Tell the gameView resume method to execute
        view.resume();
    }

    // This method executes when the player quits the game
    @Override
    protected void onPause() {
        super.onPause();

        // Tell the gameView pause method to execute
        view.pause();
        timer.cancel();
    }
}
