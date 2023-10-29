package com.amazing.android.autopompomme.search;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import java.util.List;

public class BoundingBoxImageView extends AppCompatImageView {

    private List<Rect> boxes;
    private Paint paint;

    public BoundingBoxImageView(Context context) {
        super(context);
        init();
    }

    public BoundingBoxImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2.0f);
    }

    public void setBoxes(List<Rect> boxes) {
        this.boxes = boxes;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (boxes != null) {
            for (Rect box : boxes) {
                canvas.drawRect(box, paint);
            }
        }
    }
}
