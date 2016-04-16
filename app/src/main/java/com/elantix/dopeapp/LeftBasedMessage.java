package com.elantix.dopeapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by oleh on 4/14/16.
 */
public class LeftBasedMessage extends RelativeLayout {

    private Paint paint;
    private Path path;
    private Point pointleftTop;
    private Point pointRightTop;
    private Point pointRightBottom;
    private Point pointLeftBottom;
    private float corner;
    private Context context;
    private int color = R.color.start_login_toolbar_fancy_button_color;

    public LeftBasedMessage(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        corner = getResources().getDimension(R.dimen.message_container_corner);
        paint = new Paint();
        path = new Path();
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.FILL);
        this.setWillNotDraw(false);
    }

    public void setMessageBackground(int color){
        this.color = color;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        pointleftTop = new Point((int)corner, (int)corner/2);
        pointRightTop = new Point(getWidth(), 0);
        pointRightBottom = new Point(getWidth(), getHeight());
        pointLeftBottom = new Point((int)corner, getHeight());
        path.reset();

        path.lineTo(getWidth() - corner, 0);
        path.quadTo(pointRightTop.x, pointRightTop.y, getWidth(), corner);
        path.lineTo(getWidth(), getHeight() - corner);
        path.quadTo(pointRightBottom.x, pointRightBottom.y, getWidth() - corner, getHeight());
        path.lineTo(corner + corner, getHeight());
        path.quadTo(pointLeftBottom.x, pointLeftBottom.y, corner, getHeight() - corner);
//        path.lineTo(corner, corner);
        path.lineTo(corner, corner+corner/2);
        path.quadTo(pointleftTop.x, pointleftTop.y, 0, 0);

        paint.setColor(ContextCompat.getColor(context, color));
        canvas.drawPath(path, paint);

        path.close();
    }
}
