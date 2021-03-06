package org.example.pacman;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class GameView extends View {

	Game game;
    int h,w; //used for storing our height and width of the view

	public void setGame(Game game)
	{
		this.game = game;
	}


	/* The next 3 constructors are needed for the Android view system,
	when we have a custom view.
	 */
	public GameView(Context context) {
		super(context);

	}

	public GameView(Context context, AttributeSet attrs) {
		super(context,attrs);
	}


	public GameView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context,attrs,defStyleAttr);
	}

	//In the onDraw we put all our code that should be
	//drawn whenever we update the screen.
	@Override
	protected void onDraw(Canvas canvas) {
		//Here we get the height and weight
		h = canvas.getHeight();
		w = canvas.getWidth();
		if (!game.CoinsInit())
		{
			game.initCoins();
			game.setCoinsInit(true);
		}
		if (!game.EnemiesInit())
		{
			game.initEnemies();
			game.setEnemiesInit(true);
		}

		//update the size for the canvas to the game.
		game.setSize(h,w);
		Log.d("GAMEVIEW","h = "+h+", w = "+w);
		//Making a new paint object
		Paint paint = new Paint();
		canvas.drawColor(Color.WHITE); //clear entire canvas to white color

		//draw the pacman
		canvas.drawBitmap(game.getPacBitmap(), game.getPacx(),game.getPacy(), paint);

		for (GoldCoin coin : game.getCoins())
		{
			if (!coin.isTaken())
			{
			canvas.drawBitmap(game.getCoinBitMap(), coin.getCoinx(), coin.getCoiny(), paint);
			}
		}

		for (Enemy enemy : game.getEnemies())
		{
			if (enemy.isAlive())
			{
				canvas.drawBitmap(game.getEnemyBitMap(), enemy.getEnemyX(), enemy.getEnemyY(), paint);
			}
		}

		for(BlueCoin coin : game.getBlueCoins())
		{
			if (!coin.isTaken())
			{
				canvas.drawBitmap(game.getBlueCoinBitmap(), coin.getBluex(), coin.getBluey(), paint);
			}
		}

		if(game.getGameOver())
		{
			game.newGame();
		}
		super.onDraw(canvas);
	}

}
