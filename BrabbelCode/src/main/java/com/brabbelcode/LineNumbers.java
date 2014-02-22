package com.brabbelcode;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class LineNumbers extends EditText {

    protected Paint nPaintNumbers;
    protected int nPaddingDP = 6;
    protected int nPadding, nLinePadding;
    protected float nScale;
    protected Rect nDrawingRect, nLineBounds;
    public static int TEXT_SIZE = 14;
    protected Point nMaxSize;

    public LineNumbers(Context context, AttributeSet attrs) {
        super(context, attrs);

        nPaintNumbers = new Paint();
        nPaintNumbers.setTypeface(Typeface.MONOSPACE);
        nPaintNumbers.setTextSize(12);
        nPaintNumbers.setAntiAlias(true);

        nScale = context.getResources().getDisplayMetrics().density;
        nPadding = (int) (nPaddingDP*nScale);

        nDrawingRect = new Rect();
        nLineBounds = new Rect();

        sets();
    }

    public void onDraw (Canvas canvas) {
        int count, lineX, baseline;

        count = getLineCount();
        int padding = (int) (Math.floor(Math.log10(count)))+2;
        padding = (int) ((padding*nPaintNumbers.getTextSize())+nPadding+(TEXT_SIZE *nScale));

        if(nLinePadding != padding){
            nLinePadding = padding;
            setPadding(nLinePadding, nPadding, nPadding, nPadding);
        }

        getDrawingRect(nDrawingRect);
        lineX = (int) (nDrawingRect.left+nLinePadding-(TEXT_SIZE*nScale));

        int min = 0;
        int max = count;
        getLineBounds(0, nLineBounds);

        int startBottom = nLineBounds.bottom;
        int startTop = nLineBounds.top;

        getLineBounds(count -1, nLineBounds);

        int endBottom = nLineBounds.bottom;
        int endTop = nLineBounds.top;

        if(count > 1 && endBottom > startBottom && endTop > startTop) {
            min = Math.max(min, ((nDrawingRect.top-startBottom) * (count - 1)) / (endBottom - startBottom));
            max = Math.min(max, ((nDrawingRect.bottom - startTop) * (count - 1)) / (endTop - startTop) + 1);
        }
        for (int i = min; i < max; i++) {
            baseline = getLineBounds(i, nLineBounds);
            if((nMaxSize != null) && (nMaxSize.x < nLineBounds.right)) {
                nMaxSize.x = nLineBounds.right;
            }
            canvas.drawText("" + (i + 1), nDrawingRect.left + nPadding, baseline, nPaintNumbers);
            canvas.drawLine(lineX, nDrawingRect.top, lineX, nDrawingRect.bottom, nPaintNumbers);
        }
        getLineBounds(count - 1, nLineBounds);
        if(nMaxSize != null){
            nMaxSize.y = nLineBounds.bottom;
            nMaxSize.x = Math.max(nMaxSize.x + nPadding - nDrawingRect.width(), 0);
            nMaxSize.y = Math.max(nMaxSize.y + nPadding - nDrawingRect.height(), 0);
        }
        super.onDraw(canvas);
    }

    public void sets(){
        setHorizontallyScrolling(true);
        setTextSize(TEXT_SIZE);
        nPaintNumbers.setTextSize(this.getTextSize());
    }
}
