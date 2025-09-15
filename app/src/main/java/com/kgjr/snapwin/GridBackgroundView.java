package com.kgjr.snapwin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class GridBackgroundView extends View {

    private Paint paint;
    private int cellSize = 20;

    public GridBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(0xFFCCCCCC);
        paint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        // draw vertical lines
        for (int x = 0; x < width; x += cellSize) {
            canvas.drawLine(x, 0, x, height, paint);
        }

        // draw horizontal lines
        for (int y = 0; y < height; y += cellSize) {
            canvas.drawLine(0, y, width, y, paint);
        }
    }
}