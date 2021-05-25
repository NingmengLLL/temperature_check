package com.jhlkdz.temperatremeasure.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class SecondView extends View {

    private static float[] tempThreshold = {800, 40, 30, 25, 20, 15, 10, -15};
    private static String[] tempToColor = {"#000000", "#FF0000", "#F08080", "#FFFF00", "#F0E68C", "#708090", "#B0C4DE", "#00FF00", "#000000"};

    private Paint shapePaint = new Paint();
    private Paint textPaint = new Paint();

    private List<Dot> leftDots = new ArrayList<>();
    private List<Dot> rightDots = new ArrayList<>();
    private List<Dot> middleDots = new ArrayList<>();
    private List<Path> primaryPaths = new ArrayList<>();
    private List<Path> otherPaths = new ArrayList<>();

    private float[][][] data;

    private int height;
    private int width;
    private int len;
    private int layerIndex;

    public SecondView(Context context){
        super(context);
    }

    public SecondView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SecondView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SecondView(Context context, float[][][] data, int layerIndex) {
        super(context);
        this.data = data;
        this.layerIndex = layerIndex;
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
        Dot leftDown = leftDots.get(0);
        Dot rightDown = getRightBottomDot();
        for(int i=0;i<width;i++){
            canvas.drawText((i+1)+"列",rightDown.getX()-(i+0.5f)*(rightDown.getX()-leftDown.getX())/width,leftDown.getY()+getWindowHeight()*0.025f,textPaint);
        }
        //几层
        Dot leftUp = leftDots.get(leftDots.size()-1);
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
        canvas.drawPath(getQuadrangle().getPath(), shapePaint);
        shapePaint.setColor(Color.parseColor("#DCDCDC"));
        shapePaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(getQuadrangle().getPath(), shapePaint);

        //绘制所有矩形框
        shapePaint.setColor(Color.BLACK);
        shapePaint.setStyle(Paint.Style.STROKE);
        for (Quadrangle r : getAllQuadr()) {
            canvas.drawPath(r.getPath(), shapePaint);
        }

        //绘制所有矩形颜色
        shapePaint.setStyle(Paint.Style.FILL);
        int quadrIndex = 0;
        List<Quadrangle> quadrangles = getAllQuadr();

        for (int i = len - 1; i >= 0; i--) {
            for (int j = 0; j < width; j++) {
                float val = data[layerIndex][j][i];
                Quadrangle quadrangle = quadrangles.get(quadrIndex);

                //颜色
                shapePaint.setColor(Color.parseColor(getColorByVal(val)));
                canvas.drawPath(quadrangle.getPath(), shapePaint);

                //数据
                Paint.FontMetrics metrics = textPaint.getFontMetrics();
                float top = metrics.top;//为基线到字体上边框的距离
                float bottom = metrics.bottom;//为基线到字体下边框的距离
                float baseLineY = quadrangle.getCenterY() - top / 2 - bottom / 2;//基线中间点的y轴计算公式
                canvas.drawText(String.valueOf(val), quadrangle.getCenterX(), baseLineY, textPaint);

                quadrIndex++;
            }
        }


    }

    private void initPrimaryDot() {

        float vOffset = getWindowHeight() * 0.2f / height;
        for (int i = 0; i <= height; i++) {
            middleDots.add(new Dot(getWindowWidth() * 0.25f, getWindowHeight() * 0.25f - i * vOffset));
            leftDots.add(new Dot(getWindowWidth() * 0.1f, getWindowHeight() * 0.45f - i * vOffset));
            rightDots.add(new Dot(getWindowWidth() * 0.8f, getWindowHeight() * 0.25f - i * vOffset));
        }
    }

    private void initPrimaryPath() {
        for (int i = 0; i <= height; i++) {
            Path path = new Path();

            path.moveTo(leftDots.get(i).getX(), leftDots.get(i).getY());
            path.lineTo(middleDots.get(i).getX(), middleDots.get(i).getY());
            path.lineTo(rightDots.get(i).getX(), rightDots.get(i).getY());
            primaryPaths.add(path);
        }
    }

    private void initOtherPath() {

        otherPaths.add(getPathFromList(leftDots));
        otherPaths.add(getPathFromList(rightDots));
        otherPaths.add(getPathFromList(middleDots));

        //边框
        Path path = new Path();
        int lastIndex = leftDots.size() - 1;
        Dot dot = getRightBottomDot();
        path.moveTo(rightDots.get(0).getX(), rightDots.get(0).getY());
        path.lineTo(dot.getX(), dot.getY());
        path.lineTo(leftDots.get(0).getX(), leftDots.get(0).getY());
        otherPaths.add(path);

        Path outerPath = new Path();
        outerPath.moveTo(dot.getX(), dot.getY());
        outerPath.lineTo(dot.getX(), dot.getY() + getWindowHeight() * 0.01f);
        outerPath.lineTo(leftDots.get(0).getX() - getWindowHeight() * 0.01f, leftDots.get(0).getY() + getWindowHeight() * 0.01f);
        outerPath.lineTo(leftDots.get(lastIndex).getX() - getWindowHeight() * 0.01f, leftDots.get(lastIndex).getY());
        outerPath.lineTo(middleDots.get(lastIndex).getX() - getWindowHeight() * 0.01f, middleDots.get(lastIndex).getY());
        outerPath.lineTo(middleDots.get(lastIndex).getX(), middleDots.get(lastIndex).getY());
        otherPaths.add(outerPath);

    }

    private static Path getPathFromList(List<Dot> list) {
        Path path = new Path();
        path.moveTo(list.get(0).getX(), list.get(0).getY());
        path.lineTo(list.get(list.size() - 1).getX(), list.get(list.size() - 1).getY());
        return path;
    }

    private Quadrangle getQuadrangle() {
        float vOffset = getWindowHeight() * 0.2f / height;
        Dot first = new Dot(leftDots.get(layerIndex).getX(), leftDots.get(layerIndex).getY());
        Dot second = new Dot(getRightBottomDot().getX(), getRightBottomDot().getY() - vOffset * layerIndex);
        Dot third = new Dot(rightDots.get(layerIndex).getX(), rightDots.get(layerIndex).getY());
        Dot fourth = new Dot(middleDots.get(layerIndex).getX(), middleDots.get(layerIndex).getY());
        return new Quadrangle(first, second, third, fourth);
    }

    private List<Quadrangle> getAllQuadr() {
        List<Quadrangle> list = new ArrayList<>();

        float H = getQuadrangle().getFirst().getY() - getQuadrangle().getFourth().getY();
        float h = H / len;
        float W = getQuadrangle().getSecond().getX() - getQuadrangle().getFirst().getX();

        float x1 = (getQuadrangle().getFourth().getX() - getQuadrangle().getFirst().getX()) / len / 5;
        float x2 = (getQuadrangle().getSecond().getX() - getQuadrangle().getThird().getX()) / len / 5;

        // 结尾处理方便循环使用
        float startX = getQuadrangle().getFirst().getX() + x1 - 5 * x1;
        float startY = getQuadrangle().getFirst().getY() - h * 0.2f + h;

        for (int i = 0; i < len; i++) {
            // 宽度缩小因子
            h = h * 0.99f;
            float W1 = W - x1 - x2 - 5 * i * x1 - 5 * i * x2;
            float w1 = W1 / width;
            float W2 = W - 4 * x1 - 4 * x2 - 5 * i * x1 - 5 * i * x2;
            float w2 = W2 / width;
            startX = startX + 5 * x1;
            startY = startY - h;
            for (int j = 0; j < width; j++) {
                Dot first = new Dot(startX + w1 * j + w1 * 0.2f, startY);
                Dot second = new Dot(startX + w1 * j + w1 * 0.8f, startY);
                Dot third = new Dot(startX + 3 * x1 + w2 * 0.8f + w2 * j, startY - h * 0.6f);
                Dot fourth = new Dot(startX + 3 * x1 + w2 * 0.2f + w2 * j, startY - h * 0.6f);
                Quadrangle quadrangle = new Quadrangle(first, second, third, fourth);
                list.add(quadrangle);
            }
        }

        return list;
    }

    private Dot getRightBottomDot() {
        return new Dot(getWindowWidth() * 0.9f, getWindowHeight() * 0.45f);
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
        int rate = (int) (5 * (float) screenWidth / 250); // 我自己测试这个倍数比较适合，当然你可以测试后再修改
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
        layerIndex = layerIndex + 1 > height - 1 ? layerIndex : layerIndex + 1;
    }

    public void previous() {
        layerIndex = layerIndex - 1 < 0 ? layerIndex : layerIndex - 1;
    }

    public int getLayerIndex() {
        return layerIndex;
    }

    public void setLayerIndex(int layerIndex) {
        this.layerIndex = layerIndex;
    }
}