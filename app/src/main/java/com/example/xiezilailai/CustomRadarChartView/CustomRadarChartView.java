package com.example.xiezilailai.CustomRadarChartView;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 19459 on 2016/9/28.
 */

public class CustomRadarChartView extends View {

    /**
     *雷达图的默认大小，也就是最小尺寸
     */
    private int defaultSize=700;

    /**
     * 多边形的数目，默认为7
     */
    private int polygonsNum;

    /**
     * 多边形的层数，默认为4
     */
    private int layerNum;
    /**
     * 要显示的字
     */
    private List<String>texts=new ArrayList<>();
    /**
     * 包括字在内的半径
     */
    private float validRadius;
    /**
     * 字体画笔
     */
    private Paint textPaint;
    /**
     * 提示字体颜色
     */
    private int tipTextColor;

    /**
     * 雷达图中心位置距离view左上角的X值
     */
    private float centerX;
    /**
     * 雷达图中心位置距离view左上角的Y值
     */
    private float centerY;

    /**
     * 提示字最大的半径
     */
    private float maxTextRadius=0;

    /**
     * 绘制每层多边形的画笔
     */
    private List<Paint>polygonsPaints=new ArrayList<>();

    /**
     * 每个元素的值
     */
    private List<Float>values=new ArrayList<>();


    /**
     * 中间放射线的画笔
     */
    private Paint levelPaint;

    /**
     * 放射线颜色
     */
    private int levelLineColor;

    /**
     * 提示字体大小
     */
    private int tipTextSize;

    /**
     * 分割线颜色
     */
    private int splitLineColor;

    /**
     * 分割线画笔
     */
    private Paint splitLinePaint;

    private int finalWidth,finalHeight;
    private int widthMode;
    private int widthSize;
    private int heightMode;
    private int heightSize;


    public CustomRadarChartView(Context context) {
        this(context,null);
    }

    public CustomRadarChartView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomRadarChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /**
         * 初始化各个参数
         */
        TypedArray array=context.obtainStyledAttributes(attrs,R.styleable.CustomRadarChartView);
        polygonsNum=array.getInt(R.styleable.CustomRadarChartView_polygonsNum,7);
        layerNum=array.getInt(R.styleable.CustomRadarChartView_layerNum,4);
        tipTextColor=array.getColor(R.styleable.CustomRadarChartView_tipTextColor,Color.BLACK);
        levelLineColor=array.getColor(R.styleable.CustomRadarChartView_levelLineColor,Color.WHITE);
        tipTextSize=array.getDimensionPixelSize(R.styleable.CustomRadarChartView_tipTextSize,dp_px(16));
        splitLineColor=array.getColor(R.styleable.CustomRadarChartView_splitLineColor,Color.WHITE);
        array.recycle();

        initPaint();

    }

    /**
     * 初始化画笔
     */
    private void initPaint() {

        //初始化字体画笔
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(tipTextColor);
        textPaint.setTextSize(tipTextSize);


        /**
         * 根据当前的字体计算最大的字体半径
         */
        for(int i=0;i<polygonsNum;i++){
            Rect rect=new Rect();
            textPaint.getTextBounds(getText(i),0, getText(i).length(), rect);
            int h=rect.height();
            int w=rect.width();
            int tmp= (int) Math.sqrt(h*h+w*w);
            if(tmp>maxTextRadius){
                maxTextRadius=tmp;
            }
        }



        Paint one_paint,two_paint,three_paint,four_paint;


        //初始化最外层多边形画笔
        one_paint = new Paint();
        one_paint.setAntiAlias(true);
        one_paint.setColor(getResources().getColor(R.color.blue_1));
        one_paint.setStyle(Paint.Style.FILL);//设置实心

        //初始化第二层多边形画笔
        two_paint = new Paint();
        two_paint.setAntiAlias(true);
        two_paint.setColor(getResources().getColor(R.color.blue_2));
        two_paint.setStyle(Paint.Style.FILL);//设置实心

        //初始化第三层多边形画笔
        three_paint = new Paint();
        three_paint.setAntiAlias(true);
        three_paint.setColor(getResources().getColor(R.color.blue_3));
        three_paint.setStyle(Paint.Style.FILL);//设置实心

        //初始化最内层多边形画笔
        four_paint = new Paint();
        four_paint.setAntiAlias(true);
        four_paint.setColor(getResources().getColor(R.color.blue_4));
        four_paint.setStyle(Paint.Style.FILL);//设置实心

        polygonsPaints.add(four_paint);
        polygonsPaints.add(three_paint);
        polygonsPaints.add(two_paint);
        polygonsPaints.add(one_paint);

        /**
         * 初始化放射线画笔
         */
        levelPaint=new Paint();
        levelPaint.setAntiAlias(true);
        levelPaint.setColor(levelLineColor);
        levelPaint.setStrokeWidth(8);
        levelPaint.setStyle(Paint.Style.STROKE);//设置空心

        /**
         * 初始化放射线的值，0-1，默认为0.8
         */
        for(int i=0;i<polygonsNum;i++){
            values.add(0.8f);
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
         widthMode = MeasureSpec.getMode(widthMeasureSpec);
         widthSize = MeasureSpec.getSize(widthMeasureSpec);
         heightMode = MeasureSpec.getMode(heightMeasureSpec);
         heightSize = MeasureSpec.getSize(heightMeasureSpec);

        /**
         * 如果用户设置的宽度和高度小于默认值，那么就取默认值
         */

        if(widthMode==MeasureSpec.EXACTLY){
            finalWidth=widthSize;
        }else{
            finalWidth=Math.min(widthSize,defaultSize);
        }

        if(heightMode==MeasureSpec.EXACTLY){
            finalHeight=heightSize;
        }else{
            finalHeight=Math.min(heightSize,defaultSize);
        }

        /**
         * 计算中心点的x值和y值
         *
         * 因为最后的图形大体上是个正方形，所以有效值是二者的最小值
         */
        centerX=finalWidth/2;
        centerY=finalHeight/2;
        validRadius=Math.min(centerX,centerY);

        setMeasuredDimension(finalWidth,finalHeight);
    }


    /**
     * 设置提示字画笔
     * @param textPaint
     */
    public void setTipTextPaint(Paint textPaint){
        this.textPaint=textPaint;
        invalidate();
    }

    /**
     * 设置提示字颜色
     * @param color
     */
    public void setTipTextColor(int color){
        textPaint.setColor(color);
        invalidate();
    }

    /**
     * 设置提示字大小
     * @param size
     */
    public void setTipTextSize(int size){
        textPaint.setTextSize(dp_px(size));
        invalidate();
    }

    /**
     * 设置分割线颜色
     * @param color
     */
    public void setSplitLineColor(int color){
       splitLinePaint.setColor(color);
        invalidate();
    }

    /**
     * 设置分割线画笔
     * @param paint
     */
    public void setSplitLinePaint(Paint paint){
        splitLinePaint=paint;
        invalidate();
    }


    /**
     * 设置放射线画笔
     * @param paint
     */
    public void setLevelPaint(Paint paint){
        levelPaint=paint;
        invalidate();
    }

    /**
     * 设置分割线颜色
     * @param color
     */
    public void setLevelLineColor(int color){

        levelPaint.setColor(color);
        invalidate();
    }


    /**
     * 设置提示字
     * @param texts
     */
    public void setTipText(List<String>texts){
        this.texts.clear();
        this.texts.addAll(texts);
        /**
         * 重新计算文字最大半径
         */
        for(int i=0;i<this.texts.size();i++){
            Rect rect=new Rect();
            textPaint.getTextBounds(getText(i),0, getText(i).length(), rect);
            int h=rect.height();
            int w=rect.width();
            int tmp= (int) Math.sqrt(h*h+w*w);
            if(tmp>maxTextRadius){
                maxTextRadius=tmp;
            }
        }
        invalidate();
    }


    /**
     * 设置每一层的颜色
     * @param colors
     */
    public void setLayer(List<Integer>colors){

        polygonsPaints.clear();
        for (int color:colors){
            Paint paint=new Paint();
            paint.setAntiAlias(true);
            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL);//设置实心
            polygonsPaints.add(paint);
        }

        invalidate();
    }

    /**
     * 获取多边形的边数
     * @return
     */
    public int getPolygonsNum() {
        return polygonsNum;
    }

    /**
     * 设置多边形的边数
     * @param polygonsNum
     */
    public void setPolygonsNum(int polygonsNum) {
        this.polygonsNum = polygonsNum;
        invalidate();
    }

    /**
     * 获取层数
     * @return
     */
    public int getLayerNum() {
        return layerNum;
    }

    /**
     * 设置层数
     * @param layerNum
     */
    public void setLayerNum(int layerNum) {
        this.layerNum = layerNum;
        invalidate();
    }
    /**
     * 获取当前位置对应的值 0-1
     * @param position
     * @return
     */
    public float getLevel(int position){
        try {
            return values.get(position);

        }catch (Exception e){
            if(e instanceof IndexOutOfBoundsException)
                return 0.8f;
            else return 0f;
        }
    }

    /**
     * 设置对应位置的值
     * @param position
     * @param value
     */
    public void setValue(int position,float value){
        values.set(position,value);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {


        drawPolygons(canvas);

        drawWhiteLine(canvas);

        drawLevel(canvas);

        drawText(canvas);


    }

    /**
     * 绘制多边形
     * @param canvas
     */

    private void drawPolygons(Canvas canvas) {
        for(int i=layerNum;i>=1;i--){

            Path path=new Path();
            float angle=360.0f/polygonsNum;
            float radius=(validRadius-maxTextRadius)/layerNum*i;
            float top=centerY-radius;
            path.moveTo(centerX,top);
            for(int j=1;j<polygonsNum;j++){
                path.lineTo((float) (centerX+radius*Math.sin(Math.toRadians(angle*j))),(float) (radius-radius*Math.cos(Math.toRadians(angle*j))+top));
            }
            path.close();
            canvas.drawPath(path,getPolygonsPaint(i-1));
        }
    }
    /**
     * 绘制放射线
     * @param canvas
     */
    private void drawLevel(Canvas canvas) {
        float radius=(validRadius-maxTextRadius);
        float top=centerY-radius;

        Path path=new Path();
        path.moveTo(centerX,getTranslateY(top,0));
        float angle=360.0f/polygonsNum;

        for(int i=1;i<polygonsNum;i++){
            path.lineTo( getTranslateX(centerX+radius*Math.sin(Math.toRadians(angle*i)),i)
                    ,getTranslateY(radius-radius*Math.cos(Math.toRadians(angle*i))+top,i));
        }

        path.close();
        canvas.drawPath(path,levelPaint);

    }
    public Paint getPolygonsPaint(int position){
        try {
            return polygonsPaints.get(position);

        }catch (Exception e){
            Paint paint=new Paint();
            paint.setAntiAlias(true);
            paint.setColor(getResources().getColor(R.color.blue_1));
            paint.setStyle(Paint.Style.FILL);//设置实心
            return paint;
        }
    }
    /**
     * 回执文字
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        float angle=360/polygonsNum;
        float radius=(validRadius-maxTextRadius);
        float top=centerY-radius;
        for(int i=0;i<polygonsNum;i++){
            int quadrant=getQuadrant(i);
            float x= (float) (centerX+radius*Math.sin(Math.toRadians(angle*i)));
            float y= (float) (radius-radius*Math.cos(Math.toRadians(angle*i))+top);
            Rect rect=new Rect();
            String text=getText(i);
            textPaint.getTextBounds(text,0,text.length(),rect);
            switch (quadrant){
                case 1:
                    canvas.drawText(text,x,y-rect.height()/2,textPaint);
                    break;
                case 2:
                    canvas.drawText(text,x,y+rect.height(),textPaint);

                    break;
                case 3:
                    canvas.drawText(text,x-rect.width(),y+rect.height(),textPaint);

                    break;
                case 4:
                    canvas.drawText(text,x-rect.width(),y,textPaint);

                    break;
                case 5:
                    canvas.drawText(text,x,centerY+rect.height()/2,textPaint);

                    break;
                case 6:
                    canvas.drawText(text,centerX-rect.width()/2,y+rect.height(),textPaint);

                    break;
                case 7:

                    canvas.drawText(text,x-rect.width(),centerY+rect.height()/2,textPaint);

                    break;
                case 8:
                    canvas.drawText(text,x-rect.width()/2,y-rect.height()/2,textPaint);

                    break;



            }





        }



    }

    /**
     * 根据位置获取文字
     * @param i 位置
     * @return
     */
    private String getText(int i) {
        try {
            return texts.get(i);

        }catch (Exception e){
            return "默认";
        }
    }

    /**
     * 根据位置获取当前点所在象限
     *
     * 第一、二、三、四象限分别为1,2,3,4
     *
     * x轴正向为5，y轴负向为6，x轴负向为7，y轴正向为8
     *
     *
     * @param position 位置
     * @return
     */
    private int getQuadrant(int position){
        int angle= (360*position/polygonsNum);
        if(angle==0)return 8;
        else if(angle==90)return 5;
        else if(angle==180)return 6;
        else if(angle==270)return 7;
        else if(angle>0&&angle<90)return 1;
        else if(angle>90&&angle<180)return 2;
        else if(angle>180&&angle<270)return 3;
        else return 4;
    }



    private float getTranslateX(double x,int position){
        return (float) ((x-centerX)*getLevel(position)+centerX);
    }
    private float getTranslateY(double y,int position){
        return (float) (centerY-(centerY-y)*getLevel(position));
    }


    /**
     * 绘制分割线
     * @param canvas
     */
    private void drawWhiteLine(Canvas canvas) {
        splitLinePaint=new Paint();
        //初始化中心线画笔
        splitLinePaint.setAntiAlias(true);
        splitLinePaint.setColor(splitLineColor);

        canvas.save();
        for(int i=0;i<polygonsNum;i++){
            float radius=validRadius-maxTextRadius;
            double angle=360.0f/polygonsNum;
            canvas.drawLine(centerX,centerY,(float)( centerX+radius*Math.sin(Math.toRadians(angle*i))),(float)(centerY-radius*Math.cos(Math.toRadians(angle*i))),splitLinePaint);

        }
        canvas.restore();
    }


    /**
     * dp转px
     *
     * @param values
     * @return
     */
    public int dp_px(int values) {

        float density = getResources().getDisplayMetrics().density;
        return (int) (values * density + 0.5f);
    }

}
