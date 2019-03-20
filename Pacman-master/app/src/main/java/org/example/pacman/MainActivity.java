package org.example.pacman;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity
{
    //reference to the main view
    GameView gameView;
    //reference to the game class.
    Game game;
    int speed = 5;
    int enemySpeed = 7;
    int blueSpeed = 5;
    private Timer pacTimer;
    private Timer realTimer;
    private Timer enemyTimer;
    private Timer blueCoinTimer;
    private int delay = 15;
    private TextView timeView;
    private TextView blueView;
    private Random randy = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //saying we want the game to run in one mode only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        gameView =  findViewById(R.id.gameView);
        TextView textView = findViewById(R.id.points);
        timeView = findViewById(R.id.time);
        blueView = findViewById(R.id.blueCoin);

        game = new Game(this,textView, timeView, blueView);
        game.setGameView(gameView);
        gameView.setGame(game);
        pacTimer = new Timer();
        realTimer = new Timer();
        enemyTimer = new Timer();
        blueCoinTimer = new Timer();

        game.newGame();

        realTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                RealTime();
            }
        }, 0, 1000);

        pacTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Move();
            }
        }, 0, delay);

        enemyTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                RandomDir();
            }
        }, 500, 500);

        blueCoinTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(game.getBlueCoins().size() < 1)
                {
                    SpawnBlueCoin();
                }
            }
        }, 0,1000);



        Button buttonRight = findViewById(R.id.moveRight);
        Button buttonLeft = findViewById(R.id.moveLeft);
        Button buttonUp = findViewById(R.id.moveUp);
        Button buttonDown = findViewById(R.id.moveDown);
        Button pause = findViewById(R.id.pauseApp);
        Button continueBtn = findViewById(R.id.continueApp);
        //listener of our pacman, when somebody clicks it
        buttonRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!game.getIsPaused())
                {
                    game.movePacmanRight();
                    game.setRunning(true);
                }

            }
        });
        buttonLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!game.getIsPaused())
                {
                    game.movePacmanLeft();
                    game.setRunning(true);
                }
            }
        });
        buttonUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!game.getIsPaused())
                {
                    game.movePacmanUp();
                    game.setRunning(true);
                }

            }
        });
        buttonDown.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!game.getIsPaused())
                {
                    game.movePacmanDown();
                    game.setRunning(true);
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                game.setRunning(false);
                game.setIsPaused(true);
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!game.getGameOver())
                {
                    game.setRunning(true);
                    game.setIsPaused(false);
                }

            }
        });

    }

    @Override
    protected void onStop()
    {
        super.onStop();
        pacTimer.cancel();
        realTimer.cancel();
        enemyTimer.cancel();
    }

    private void Move()
    {
        this.runOnUiThread(Move_Tick);
    }

    private void RealTime()
    {
        this.runOnUiThread(Time_Tick);
    }

    private void RandomDir()
    {
      this.runOnUiThread(Enemy_Tick);
    }

    private void SpawnBlueCoin(){this.runOnUiThread(BlueCoin_Tick);}

    private Runnable Time_Tick = new Runnable() {
        @Override
        public void run() {
            if(game.isRunning())
            {
                game.setTime(game.getTime() - 1);
                timeView.setText(getText(R.string.time) + " " + game.getTime());

                if (game.getTime() <= 0)
                {
                    game.GameOver(false);
                    game.setRunning(false);
                    game.setIsPaused(true);
                    game.setGameOver(true);
                }
            }


        }
    };

    private Runnable Enemy_Tick = new Runnable() {
        @Override
        public void run() {
            if (game.isRunning())
            {
                for (Enemy enemy : game.getEnemies())
                {
                    enemy.setDir(randy.nextInt(4));
                }

            }
        }
    };

    private Runnable BlueCoin_Tick = new Runnable()
    {
        @Override
        public void run()
        {
            if (game.isRunning())
            {
                game.initBlueCoin();
            }
        }
    };

    private Runnable Move_Tick = new Runnable()
    {
        @Override
        public void run()
        {
           for (Enemy enemy : game.getEnemies()) {
               switch (enemy.getDir()) {
                   case 0:
                       if (game.isRunning())
                       {
                           if (enemy.getEnemyY() + enemySpeed > 0)
                           {
                               enemy.setEnemyY(enemy.getEnemyY() - enemySpeed);
                               game.doCollisionCheck();
                               gameView.invalidate();
                           }
                       }
                       break;
                   case 1:
                       if (game.isRunning())
                       {
                               if (enemy.getEnemyX() + enemySpeed > 0)
                               {
                                   enemy.setEnemyX(enemy.getEnemyX() - enemySpeed);
                                   game.doCollisionCheck();
                                   gameView.invalidate();
                               }
                       }
                       break;
                   case 2:
                       if (game.isRunning())
                       {
                           if (enemy.getEnemyY() + enemySpeed + game.getEnemyBitMap().getWidth() < gameView.w)
                           {
                               enemy.setEnemyY(enemy.getEnemyY() + enemySpeed);
                               game.doCollisionCheck();
                               gameView.invalidate();
                           }
                       }
                       break;
                   case 3:
                       if (game.isRunning())
                       {
                           if (enemy.getEnemyX() + enemySpeed + game.getEnemyBitMap().getWidth() < gameView.w)
                           {
                               enemy.setEnemyX(enemy.getEnemyX() + enemySpeed);
                               game.doCollisionCheck();
                               gameView.invalidate();
                           }
                       }

                       break;
               }
           }
            switch (game.getCurDir())
            {
                case 0:
                    if(game.getPacy() + speed + blueSpeed > 0 && game.isRunning() && game.isBluePickup())
                    {
                        game.setPacy(game.getPacy() - speed - blueSpeed);
                        game.doCollisionCheck();
                        gameView.invalidate();
                    }else if (game.getPacy() + speed > 0 && game.isRunning())
                    {
                        game.setPacy(game.getPacy() - speed);
                        game.doCollisionCheck();
                        gameView.invalidate();
                    }
                    break;
                case 1:
                    if(game.getPacx() + speed + blueSpeed > 0 && game.isRunning() && game.isBluePickup())
                    {
                        game.setPacx(game.getPacx() - speed - blueSpeed);
                        game.doCollisionCheck();
                        gameView.invalidate();
                    }else if (game.getPacx() + speed > 0 && game.isRunning())
                    {
                        game.setPacx(game.getPacx() - speed);
                        game.doCollisionCheck();
                        gameView.invalidate();
                    }

                    break;
                case 2:
                    if(game.getPacy() + speed + blueSpeed + game.getPacBitmap().getHeight() < gameView.h && game.isRunning() && game.isBluePickup())
                    {
                        game.setPacy(game.getPacy() + speed + blueSpeed);
                        game.doCollisionCheck();
                        gameView.invalidate();
                    }else if (game.getPacy() + speed +game.getPacBitmap().getHeight()< gameView.h && game.isRunning()) {
                        game.setPacy(game.getPacy() + speed);
                        game.doCollisionCheck();
                        gameView.invalidate();
                    }

                    break;
                case 3:
                    if(game.getPacx() + speed + blueSpeed + game.getPacBitmap().getWidth() < gameView.w && game.isRunning() && game.isBluePickup())
                    {
                        game.setPacx(game.getPacx() + speed + blueSpeed);
                        game.doCollisionCheck();
                        gameView.invalidate();
                    }else if (game.getPacx() + speed + game.getPacBitmap().getWidth() < gameView.w && game.isRunning()) {
                        game.setPacx(game.getPacx() + speed);
                        game.doCollisionCheck();
                        gameView.invalidate();
                    }
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            Toast.makeText(this,"settings clicked",Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_newGame)
        {
            game.newGame();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
