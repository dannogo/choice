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
 * Created by oleh on 4/16/16.
 */
public class RightBasedMessage extends RelativeLayout {

    private Paint paint;
    private Path path;
    private Point pointleftTop;
    private Point pointRightTop;
    private Point pointRightBottom;
    private Point pointLeftBottom;
    private float corner;
    private Context context;
    private int color = R.color.chat_my_message_field_color;

    public RightBasedMessage(Context context, AttributeSet attrs) {
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
        int width = getWidth();
        int height = getHeight();

        pointRightTop = new Point(width-(int)corner, (int)corner/2);
        pointRightBottom = new Point(width-(int)corner, height);
        pointLeftBottom = new Point(0, height);
        pointleftTop = new Point(0, 0);
        path.reset();

        path.moveTo(corner, 0);
        path.lineTo(width, 0);
        path.quadTo(pointRightTop.x, pointRightTop.y, width-corner, corner+corner/2);
        path.lineTo(width-corner, height-corner);
        path.quadTo(pointRightBottom.x, pointRightBottom.y, width - corner*2, height);
        path.lineTo(corner, height);
        path.quadTo(pointLeftBottom.x, pointLeftBottom.y, 0, height-corner);
        path.lineTo(0, corner);
        path.quadTo(pointleftTop.x, pointleftTop.y, corner, 0);

        paint.setColor(ContextCompat.getColor(context, color));
        canvas.drawPath(path, paint);

        path.close();
    }
}
