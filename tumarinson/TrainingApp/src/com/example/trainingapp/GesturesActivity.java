package com.example.trainingapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

public class GesturesActivity extends Activity implements OnTouchListener,
	GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

	private GestureDetectorCompat mDetector; 
	private TextView tv;
	private ImageView iv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gestures);
		//setContentView(new MyView(this));

		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		tv = (TextView) findViewById(R.id.tvGest);
		iv = (ImageView) findViewById(R.id.imageView1);
		
		findViewById(R.id.gestBack).setOnTouchListener(this);
		
		mDetector = new GestureDetectorCompat(this,this);
        mDetector.setOnDoubleTapListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/*class MyView extends View {
		Paint p;
		// координаты для рисования квадрата
		float x = 100;
		float y = 100;
		int side = 100;

		// переменные для перетаскивания
		boolean drag = false;
		float dragX = 0;
		float dragY = 0;

		public MyView(Context context) {
			super(context);
			p = new Paint();
			p.setColor(Color.GREEN);
		}

		protected void onDraw(Canvas canvas) {
			// рисуем квадрат
			canvas.drawRect(x, y, x + side, y + side, p);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			// координаты Touch-события
			float evX = event.getX();
			float evY = event.getY();

			switch (event.getAction()) {
			// касание началось
			case MotionEvent.ACTION_DOWN:
				// если касание было начато в пределах квадрата
				if (evX >= x && evX <= x + side && evY >= y && evY <= y + side) {
					// включаем режим перетаскивания
					drag = true;
					// разница между левым верхним углом квадрата и точкой
					// касания
					dragX = evX - x;
					dragY = evY - y;
				}
				break;
			// тащим
			case MotionEvent.ACTION_MOVE:
				// если режим перетаскивания включен
				if (drag) {
					// определеяем новые координаты для рисования
					x = evX - dragX;
					y = evY - dragY;
					// перерисовываем экран
					invalidate();
				}
				break;
			// касание завершено
			case MotionEvent.ACTION_UP:
				// выключаем режим перетаскивания
				drag = false;
				break;
			}
			return true;
		}
	}*/

	private void touchActions(MotionEvent event, String str) {
		tv.setText(str+": " +"\n"+ event.toString()
				+"\n\n"+event.getX()+" "+event.getY()
				+"\n"+event.getRawX()+" "+event.getRawY()); 
		iv.setX(event.getX());
		iv.setY(event.getY());
	
	}
	
	/**
	 * The first way for process touch 
	 * */
	/*@Override
	public boolean onTouchEvent(MotionEvent event){ 
	        
	    int action = MotionEventCompat.getActionMasked(event);
	        
	    switch(action) {
	        case (MotionEvent.ACTION_DOWN) :
	        	touchActions(event, "Down");
	            return true;
	        case (MotionEvent.ACTION_MOVE) :
	        	touchActions(event, "Move");
	            return true;
	        case (MotionEvent.ACTION_UP) :
	        	touchActions(event, "Up");
	            return true;
	        case (MotionEvent.ACTION_CANCEL) :
	        	touchActions(event, "Cancel");
	            return true;
	        case (MotionEvent.ACTION_OUTSIDE) :
	        	touchActions(event, "Outside");
	            return true;      
	        default : 
	            return super.onTouchEvent(event);
	    }      
	}*/
	
	/**
	 * The second way for process touch 
	 * */
	/*@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: // нажатие
			touchActions(event, "Down");
			break;
		case MotionEvent.ACTION_MOVE: // движение
			touchActions(event, "Move");
			break;
		case MotionEvent.ACTION_UP: // отпускание
			touchActions(event, "Up");
			break;
		case MotionEvent.ACTION_CANCEL:
			touchActions(event, "Cancel");
		}
		return true;
	}*/
	

	/**
	 * The third way for process touch 
	 * */
	
	/* @Override 
	    public boolean onTouchEvent(MotionEvent event){ 
	        this.mDetector.onTouchEvent(event);
	        // Be sure to call the superclass implementation
	        return super.onTouchEvent(event);
	    }*/
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return this.mDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        //return super.onTouchEvent(event);
	}
	
	@Override
	public boolean onDown(MotionEvent event) {
		touchActions(event, "onDown");
		return true;
	}

	@Override
	public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX,
			float velocityY) {
		tv.setText("onFling: " +"\n"+ event1.toString()+"\n\n"+event2.toString()+"\n\n"
				+ velocityX+" " + velocityY);
		iv.setX(event2.getX());
		iv.setY(event2.getY());
        return true;
	}

	@Override
	public void onLongPress(MotionEvent event) {
		tv.setText("onLongPress: " +"\n"+ event.toString()); 
		iv.setScaleX(1);
		iv.setScaleY(1);
		iv.setAlpha((float)1);
	}

	@Override
	public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX,
			float distanceY) {
		tv.setText("onScroll: " +"\n"+ event1.toString()+"\n\n"+event2.toString()
				+"\n\n"+distanceX+" "+distanceY
				+"\n\n"+event2.getRawX()+" "+event2.getRawY());
		
		iv.setX(event2.getX());
		iv.setY(event2.getY());
        return true;
	}

	@Override
	public void onShowPress(MotionEvent event) {
		tv.setText("onShowPress: " +"\n"+ event.toString());
		iv.setAlpha((float)0.5);
	}

	@Override
	public boolean onSingleTapUp(MotionEvent event) {
		touchActions(event, "onSingleTapUp");
		iv.setAlpha((float)1);
		return true;
	}


	/** Double touch events */
	@Override
	public boolean onDoubleTap(MotionEvent event) {
		tv.setText("onDoubleTap: " +"\n"+ event.toString());
        return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent event) {
		tv.setText("onDoubleTapEvent: " +"\n"+ event.toString()); 
		iv.setScaleX(2);
		iv.setScaleY(2);
        return true;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent event) {
		touchActions(event, "onSingleTapConfirmed");
        return true;
	}
}
