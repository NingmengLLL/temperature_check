package com.jhlkdz.temperatremeasure.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class FirstView extends View {

    private static float[] tempThreshold = {800, 40, 30, 25, 20, 15, 10, -15};
    private static String[] tempToColor = {"#000000", "#FF0000", "#F08080", "#FFFF00", "#F0E68C", "#708090", "#B0C4DE", "#00FF00", "#000000"};

    private Paint shapePaint = new Paint();
    private Paint textPaint = new Paint();

    private List<Dot> leftDots = new ArrayList<>();
    private List<Dot> rightDots = new ArrayList<>();
    private List<Dot> topDots = new ArrayList<>();
    private List<Path> primaryPaths = new ArrayList<>();
    private List<Path> otherPaths = new ArrayList<>();

    private float[][][] data;

    private int height;
    private int width;
    private int len;
    private int rowIndex;

    public FirstView(Context context){
        super(context);
    }

    public FirstView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FirstView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FirstView(Context context, float[][][] data, int rowIndex) {
        super(context);
        this.data = data;
        this.rowIndex = rowIndex;
        height = data.length;
        width = data[0].length;
        len = data[0][0].length;

        initPrimaryDot();
        initPrimaryPath();
        initOtherPath();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int heightSize = (int) (getWindowHeight() * 0.5);
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode);

        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);
    }

    @Override
    public void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        shapePaint.setAntiAlias(true);
        shapePaint.setColor(Color.BLACK);
        shapePaint.setStyle(Paint.Style.STROKE);
        shapePaint.setStrokeWidth(1);

        //绘制主路径
        for (Path p : primaryPaths) {
            canvas.drawPath(p, shapePaint);
        }

        //绘制边缘
        for (Path p : otherPaths) {
            canvas.drawPath(p, shapePaint);
        }

        //设置textPaint
        int adjustFontSize = adjustFontSize(getWindowWidth(), getWindowHeight());
        textPaint.setColor(Color.BLACK);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(adjustFontSize);
        //这里我设置为center
        textPaint.setTextAlign(Paint.Align.CENTER);

        //几列
        Dot leftDown = leftDots.get(leftDots.size()-1);
        Dot rightDown = rightDots.get(rightDots.size()-1);
        for(int i=0;i<width;i++){
            canvas.drawText((i+1)+"列",rightDown.getX()-(i+0.5f)*(rightDown.getX()-leftDown.getX())/width,leftDown.getY()+getWindowHeight()*0.025f,textPaint);
        }
        //几层
        Dot leftUp = topDots.get(topDots.size()-1);
        for(int i=0;i<height;i++){
            canvas.drawText((i+1)+"层",leftUp.getX()-getWindowHeight()*0.025f,leftUp.getY()+(i+0.5f)*(leftDown.getY()-leftUp.getY())/height,textPaint);
        }
        //几行
        Dot rightUp = rightDots.get(0);
        for(int i=0;i<len;i++){
            canvas.drawText((i+1)+"行",rightDown.getX()+getWindowHeight()*0.02f-(i+0.5f)*(rightDown.getX()-rightUp.getX())/len,rightDown.getY()-(i+0.5f)*(rightDown.getY()-rightUp.getY())/len,textPaint);
        }

        //绘制数据背景
        shapePaint.setColor(Color.parseColor("#696969"));
        shapePaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(getRect(), shapePaint);
        shapePaint.setColor(Color.parseColor("#DCDCDC"));
        shapePaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(getRect(), shapePaint);

        //绘制所有矩形框
        shapePaint.setColor(Color.BLACK);
        shapePaint.setStyle(Paint.Style.STROKE);
        for (RectF r : getAllRects()) {
            canvas.drawRect(r, shapePaint);
        }

        //绘制所有矩形颜色以及文字
        shapePaint.setStyle(Paint.Style.FILL);
        int rectIndex = 0;
        List<RectF> recList = getAllRects();

        for (int i = height - 1; i >= 0; i--) {
            for (int j = 0; j < width; j++) {
                float val = data[i][j][rowIndex];
                RectF r = recList.get(rectIndex);

                //颜色
                shapePaint.setColor(Color.parseColor(getColorByVal(val)));
                canvas.drawRect(r, shapePaint);

                //数据
                Paint.FontMetrics metrics = textPaint.getFontMetrics();
                float top = metrics.top;//为基线到字体上边框的距离
                float bottom = metrics.bottom;//为基线到字体下边框的距离
                float baseLineY = r.centerY() - top / 2 - bottom / 2;//基线中间点的y轴计算公式
                canvas.drawText(String.valueOf(val), r.centerX(), baseLineY, textPaint);

                rectIndex++;
            }
        }


    }

    private void initPrimaryDot() {
        float leftHOffset = getWindowWidth() * 0.15f / len;
        float leftVOffset = getWindowHeight() * 0.2f / len;
        float rightHOffset = getWindowWidth() * 0.1f / len;

        for (int i = 0; i <= len; i++) {
            leftDots.add(new Dot(getWindowWidth() * 0.25f - i * leftHOffset, getWindowHeight() * 0.25f + i * leftVOffset));
            rightDots.add(new Dot(getWindowWidth() * 0.8f + i * rightHOffset, getWindowHeight() * 0.25f + i * leftVOffset));
            topDots.add(new Dot(getWindowWidth() * 0.25f - i * leftHOffset, getWindowHeight() * 0.05f + i * leftVOffset));
        }
    }

    private void initPrimaryPath() {
        for (int i = 0; i <= len; i++) {
            Path path = new Path();
            path.moveTo(topDots.get(i).getX(), topDots.get(i).getY());
            path.lineTo(leftDots.get(i).getX(), leftDots.get(i).getY());
            path.lineTo(rightDots.get(i).getX(), rightDots.get(i).getY());
            primaryPaths.add(path);
        }
    }

    private void initOtherPath() {

        otherPaths.add(getPathFromList(leftDots));
        otherPaths.add(getPathFromList(rightDots));
        otherPaths.add(getPathFromList(topDots));

        //边框
        Path path = new Path();
        int lastIndex = leftDots.size() - 1;
        path.moveTo(rightDots.get(0).getX(), rightDots.get(0).getY());
        path.lineTo(rightDots.get(0).getX(), topDots.get(0).getY());
        path.lineTo(topDots.get(0).getX() - getWindowHeight() * 0.01f, topDots.get(0).getY());
        path.lineTo(topDots.get(lastIndex).getX() - getWindowHeight() * 0.01f, topDots.get(lastIndex).getY());
        path.lineTo(leftDots.get(lastIndex).getX() - getWindowHeight() * 0.01f, leftDots.get(lastIndex).getY() + getWindowHeight() * 0.01f);
        path.lineTo(rightDots.get(lastIndex).getX(), rightDots.get(lastIndex).getY() + getWindowHeight() * 0.01f);
        path.lineTo(rightDots.get(lastIndex).getX(), rightDots.get(lastIndex).getY());
        otherPaths.add(path);


    }

    private static Path getPathFromList(List<Dot> list) {
        Path path = new Path();
        path.moveTo(list.get(0).getX(), list.get(0).getY());
        path.lineTo(list.get(list.size() - 1).getX(), list.get(list.size() - 1).getY());
        return path;
    }

    private RectF getRect() {
        return new RectF(topDots.get(rowIndex + 1).getX(), topDots.get(rowIndex + 1).getY(),
                rightDots.get(rowIndex + 1).getX(), rightDots.get(rowIndex + 1).getY());
    }

    private List<RectF> getAllRects() {
        List<RectF> list = new ArrayList<>();

        float H = getRect().bottom - getRect().top;
        float W = getRect().right - getRect().left;
        float h = H / height;
        float w = W / width;
        float startX = getRect().left;
        float startY = getRect().top;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                list.add(new RectF(startX + (j + 0.2f) * w, startY + (i + 0.2f) * h,
                        startX + (j + 0.8f) * w, startY + (i + 0.8f) * h));
            }
        }
        return list;
    }

    private String getColorByVal(Float val) {
        for (int i = 0; i < tempThreshold.length; i++) {
            if (val >= tempThreshold[i]) {
                return tempToColor[i];
            }
        }
        return tempToColor[tempToColor.length - 1];
    }

    // 获取字体大小
    private static int adjustFontSize(int screenWidth, int screenHeight) {
        screenWidth = screenWidth < screenHeight ? screenWidth : screenHeight;
        /**
         * 1. 在视图的 onsizechanged里获取视图宽度，一般情况下默认宽度是320，所以计算一个缩放比率 rate = (float)
         * w/320 w是实际宽度 2.然后在设置字体尺寸时 paint.setTextSize((int)(8*rate));
         * 8是在分辨率宽为320 下需要设置的字体大小 实际字体大小 = 默认字体大小 x rate
         */
        int rate = (int) (5 * (float) screenWidth / 220); // 我自己测试这个倍数比较适合，当然你可以测试后再修改
        return rate < 15 ? 15 : rate; // 字体太小也不好看的
    }

    private int getWindowWidth() {
        WindowManager wManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point.x;
    }

    private int getWindowHeight() {
        WindowManager wManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point.y;
    }

    public void next() {
        rowIndex = rowIndex + 1 > len - 1 ? rowIndex : rowIndex + 1;
    }

    public void previous() {
        rowIndex = rowIndex - 1 < 0 ? rowIndex : rowIndex - 1;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }
}
