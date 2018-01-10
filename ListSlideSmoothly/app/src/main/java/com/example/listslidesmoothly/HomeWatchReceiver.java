package com.example.listslidesmoothly;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.VideoView;
import android.widget.WrapperListAdapter;

/**
 * @author wanlijun
 * @description 监听home键，home键的事件不直接传递到应用，而是分发给系统，所以onKeyDOwn()监听不到home键，需用广播监听
 * @time 2017/12/29 8:54
 */

//需要动态注册广播
public class HomeWatchReceiver extends BroadcastReceiver {
    private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
    private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
    private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    private static final String SYSTEM_DIALOG_REASON_LOCK = "lock";
    private static final String SYSTEM_DIALOG_REASON_ASSIST = "assist";
    private CustomVideoView mVideoView;
    public HomeWatchReceiver(CustomVideoView videoView){
        super();
        this.mVideoView = videoView;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i("wanlijun","action="+action);
        if(action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)){
            String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
            if(reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)){
                //短按home键
                Log.i("wanlijun","home");
            }else if(reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)){
                //最近任务列表
                Log.i("wanlijun","recentapps");
            }else if(reason.equals(SYSTEM_DIALOG_REASON_LOCK)){
                //测试HTC，锁屏接收不到这个广播
                Log.i("wanlijun","lock");
            }else if(reason.equals(SYSTEM_DIALOG_REASON_ASSIST)){
                //长按home键
                Log.i("wanlijun","assist");
            }
        }else if(action.equals(Intent.ACTION_SCREEN_ON)){
            //亮屏
            Log.i("wanlijun","ACTION_SCREEN_ON");
        }else if(action.equals(Intent.ACTION_SCREEN_OFF)){
            //灭屏
            mVideoView.surfaceDestroyed(mVideoView.getHolder());
            Log.i("wanlijun","ACTION_SCREEN_OFF");
        }else if(action.equals(Intent.ACTION_USER_PRESENT)){
            //用户解锁
            Log.i("wanlijun","ACTION_USER_PRESENT");
            mVideoView.surfaceCreated(mVideoView.getHolder());
//            mVideoView.surfaceChanged(mVideoView.getHolder());
            mVideoView.start();
        }else if(action.equals(Intent.ACTION_USER_UNLOCKED)){
            Log.i("wanlijun","ACTION_USER_UNLOCKED");
        }
    }
}
