package com.example.listslidesmoothly;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.VideoView;

/**
 * @author wanlijun
 * @description 自定义VideoView用来设置视频背景或者动态背景
 * VideoView继承自SurfaceView，SurfaceView在按home键时会执行surfaceDestroyed操作把SurfaceView销毁，再重新打开APP会执行surfaceCreated和surfaceChanged操作自动创建新的SurfaceView
 * 可惜了，系统锁屏并不会执行surfaceDestroyed操作
 * @time 2017/12/28 15:15
 */

public class CustomVideoView extends VideoView implements SurfaceHolder.Callback{
    private SurfaceHolder mSurfaceHolder;
    public CustomVideoView(Context context) {
        super(context);
        init();
    }
    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context,attrs);
        init();
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
        init();
    }
    private void init(){
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //使用默认的计算方式会导致视频的宽度没有铺满全屏，只占据了部分屏幕
        //所以需要重新计算宽高，实现全屏播放
        int width = getDefaultSize(0,widthMeasureSpec);
        int height = getDefaultSize(0,heightMeasureSpec);
        setMeasuredDimension(width,height);
    }
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.i("wanlijun","surfaceCreated");
    }

    //手机锁屏的时候可以手动调用surfaceDestroyed
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.i("wanlijun","surfaceDestroyed");
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.i("wanlijun","surfaceChanged");
    }
}
