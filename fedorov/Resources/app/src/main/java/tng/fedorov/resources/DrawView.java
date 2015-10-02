package tng.fedorov.resources;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by fedorov on 02.10.2015.
 */
public class DrawView extends View {

    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    Paint mPaint = new Paint();
    float x = getResources().getDimension(R.dimen.x);
    float y = getResources().getDimension(R.dimen.y);
    float radius = getResources().getDimension(R.dimen.radius);

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setAntiAlias(true);
        canvas.drawCircle(x,y,radius,mPaint);
    }
}
