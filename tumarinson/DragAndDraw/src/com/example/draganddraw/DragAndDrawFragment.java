package com.example.draganddraw;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class DragAndDrawFragment extends Fragment {
	
	private static int curColor=0;
	private static int curStaff=0;
	private static int[] colorArray;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_drag_and_draw, parent,
				false);
		
		colorArray = getActivity().getResources().getIntArray(
				R.array.staffcolors);
		
		Spinner spin_staff = (Spinner) v.findViewById(R.id.staff_spinner);
		Spinner spin_color = (Spinner) v.findViewById(R.id.color_spinner);

		ArrayAdapter<CharSequence> adapter_staff = ArrayAdapter.createFromResource(getActivity(),
		         R.array.staff, android.R.layout.simple_spinner_item);
		adapter_staff.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin_staff.setAdapter(adapter_staff);
		 
		ArrayAdapter<CharSequence> adapter_color = ArrayAdapter.createFromResource(getActivity(),
		         R.array.colors, android.R.layout.simple_spinner_item);
		adapter_color.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin_color.setAdapter(adapter_color);
		 
		spin_staff.setOnItemSelectedListener(MySpinnerListener);
		spin_color.setOnItemSelectedListener(MySpinnerListener);
		   
		return v;
	}
	
	OnItemSelectedListener MySpinnerListener=new OnItemSelectedListener(){
		@Override
		public void onItemSelected(AdapterView<?> parView, View view,
				int pos, long id) {
			switch(parView.getId()) {
			case R.id.staff_spinner:
				curStaff=pos;
				break;
			case R.id.color_spinner:
				curColor=pos;
				break;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}
	};
	
	public static class DrawingView extends View {
		
		public static final String TAG = "DrawingView";
		
		private final static int PEN_SIZE=10;
		
		private Path curPath;
		private RectF mCurRectOrRect;
		
		private ArrayList<ExtendedFigure> FiguresList = new ArrayList<ExtendedFigure>();
		
		private Paint mLinePaint;
		private Paint mBoxCircPaint;

		// Used when creating the view in code
		public DrawingView(Context context) {
			this(context, null);
		}

		// Used when inflating the view from XML
		public DrawingView(Context context, AttributeSet attrs) {
			super(context, attrs);
			
			mBoxCircPaint = new Paint();
			
			mLinePaint = new Paint();
			mLinePaint.setStyle(Paint.Style.STROKE);
			mLinePaint.setStrokeJoin(Paint.Join.ROUND);
			mLinePaint.setStrokeCap(Paint.Cap.ROUND);
			mLinePaint.setStrokeWidth(10);
			mLinePaint.setPathEffect(new CornerPathEffect(10));
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			for (ExtendedFigure figure : FiguresList) {
				switch(figure.getType()) {
				case ExtendedFigure.LINE:
					mLinePaint.setColor(figure.getColor());
					canvas.drawPath((Path)figure.getFigure(), mLinePaint);
					break;
				case ExtendedFigure.RECT:
					mBoxCircPaint.setColor(figure.getColor());
					canvas.drawRect((RectF)figure.getFigure(), mBoxCircPaint);
					break;
				case ExtendedFigure.CIRC:
					mBoxCircPaint.setColor(figure.getColor());
					canvas.drawOval((RectF)figure.getFigure(), mBoxCircPaint);
					break;
				}				
			}
		}
		
		public boolean onTouchEvent(MotionEvent event) {
			PointF curr = new PointF(event.getX(), event.getY());
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				switch(curStaff) {
				case 0:
					curPath=new Path();
					curPath.reset();
					curPath.moveTo(curr.x, curr.y);
					FiguresList.add(new ExtendedFigure(curPath,
							ExtendedFigure.LINE, colorArray[curColor]));
					break;
				case 1:
					mCurRectOrRect = new RectF(curr.x, curr.y, curr.x, curr.y);
					FiguresList.add(new ExtendedFigure(mCurRectOrRect,
							ExtendedFigure.RECT, colorArray[curColor]));
					break;
				case 2:
					mCurRectOrRect = new RectF(curr.x, curr.y, curr.x, curr.y);
					FiguresList.add(new ExtendedFigure(mCurRectOrRect,
							ExtendedFigure.CIRC, colorArray[curColor]));
					break;
				}	
				break;
			case MotionEvent.ACTION_MOVE:
				switch(curStaff) {
				case 0:
					curPath.lineTo(curr.x, curr.y);
					invalidate();
					break;
				case 1:
				case 2:
					if (mCurRectOrRect != null) {
						mCurRectOrRect.right=curr.x;
						mCurRectOrRect.bottom=curr.y;
						invalidate();
					}
					break;
				}
				break;
			case MotionEvent.ACTION_UP:
				switch(curStaff) {
				case 0:
					curPath = null;
					break;
				case 1:
				case 2:
					mCurRectOrRect = null;
					break;
				}
				break;
			case MotionEvent.ACTION_CANCEL:
				switch(curStaff) {
				case 0:
					curPath = null;
					break;
				case 1:	
				case 2:
					mCurRectOrRect = null;
					break;
				}
				break;
			}
			return true;
		}	
	}
	
	/** 
	 * Class for collect different geometric figures plus color parameter 
	 * */
	static class ExtendedFigure {
		final static int LINE = 0;
		final static int RECT = 1;
		final static int CIRC = 2;
		
		Object figure;
		int type;
		int color;
		
		ExtendedFigure(Object figure, int type, int color) {
			this.figure=figure;
			this.type=type;
			this.color=color;		
		}
		public Object getFigure() {
			return figure;
		}
		public int getType() {
			return type;
		}
		public int getColor() {
			return color;
		}
	}

}
