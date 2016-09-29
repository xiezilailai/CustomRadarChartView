# CustomRadarChartView
自定义雷达图(模仿掌盟能力值分析效果 )

**GitHub地址：**[自定义雷达图(模仿掌盟能力值分析效果 ) ](https://github.com/xiezilailai/CustomRadarChartView)

**微博：**[蝎子莱莱的微博](http://weibo.com/xiezilailai)

先贴几张图，通过我的 CustomRadarChartView,你可以这样

![这里写图片描述](http://img.blog.csdn.net/20160929165940520)

也可以这样

![这里写图片描述](http://img.blog.csdn.net/20160929170020739)

还可以这样

![这里写图片描述](http://img.blog.csdn.net/20160929170044032)

所有的颜色、多边形边数和层数都可以由你自己定制，每个顶点上的字也是根据你传进去的string array自动生成的，不需要你摆弄TextView。

来张动态的吧
![这里写图片描述](http://img.blog.csdn.net/20160929171945903)

该控件默认层数为四层、默认边数为7
你可以在xml中使用如下属性
```
        <attr name="polygonsNum" format="integer"/>//边数
        <attr name="layerNum" format="integer"/>//层数
        <attr name="splitLineColor" format="color"/>//分割线颜色
        <attr name="levelLineColor" format="color"/>//放射线颜色
        <attr name="tipTextColor" format="color"/>//文字颜色
        <attr name="tipTextSize" format="dimension"/>//文字大小
```
也可以在代码中使用下列方法
```

    /**
     * 设置提示字画笔
     * @param textPaint
     */
    public void setTipTextPaint(Paint textPaint){}

    /**
     * 设置提示字颜色
     * @param color
     */
    public void setTipTextColor(int color){}

    /**
     * 设置提示字大小
     * @param size
     */
    public void setTipTextSize(int size){ }

    /**
     * 设置分割线颜色
     * @param color
     */
    public void setSplitLineColor(int color){}

    /**
     * 设置分割线画笔
     * @param paint
     */
    public void setSplitLinePaint(Paint paint){}


    /**
     * 设置放射线画笔
     * @param paint
     */
    public void setLevelPaint(Paint paint){}

    /**
     * 设置分割线颜色
     * @param color
     */
    public void setLevelLineColor(int color){}


    /**
     * 设置提示字
     * @param texts
     */
    public void setTipText(List<String>texts){ }


    /**
     * 设置每一层的颜色
     * @param colors
     */
    public void setLayer(List<Integer>colors){ }

    /**
     * 获取多边形的边数
     * @return
     */
    public int getPolygonsNum() { }

    /**
     * 设置多边形的边数
     * @param polygonsNum
     */
    public void setPolygonsNum(int polygonsNum) { }

    /**
     * 获取层数
     * @return
     */
    public int getLayerNum() { }

    /**
     * 设置层数
 * @param layerNum
     */
    public void setLayerNum(int layerNum) { }
    /**
     * 获取当前位置对应的值 0-1
     * @param position
     * @return
     */
    public float getLevel(int position){}

    /**
     * 设置对应位置的值
     * @param position
     * @param value
     */
    public void setValue(int position,float value){ }

```
如果你设置了多层颜色，一定要记得将足够数目的color或者paint传进去，否则可能会带来错误。更多使用你可以参考我的demo，如果有问题欢迎和我联系，谢谢！
