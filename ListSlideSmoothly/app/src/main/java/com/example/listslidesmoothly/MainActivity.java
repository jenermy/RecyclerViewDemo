package com.example.listslidesmoothly;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.List;

//用Android Studio无法安装apk到小米手机上，解决办法：在开发者选项里面找到开启MIUI优化（在最下面），关闭MIUI优化并重启手机即可

public class MainActivity extends AppCompatActivity{
    private CustomVideoView mVideoBackgroundVV;
    private FrameLayout mPlaceHolderLayout;
    private HomeWatchReceiver mHomeWatchReceiver;
    private  int mCurrentVideoPosition = 0;
    private LinearLayout oneLayout,twoLayout,threeLayout;
    private RecyclerView recyclerList;
    private static final int ONE_VIEW_TYPE = 1;
    private static final int TWO_VIEW_TYPE = 2;
    private static final int NO_MORE_DATA = 3;
    private static final int LOAD_MORE_DATA = 4;
    private SmoothListAdapter adapter;
    private PureDataListAdapter pureDataListAdapter;
    private List<String> dataSet = new ArrayList<>();
    LinearLayoutManager layoutManager;
    private int totalItemCount = 0;
    private int lastVisibleItemPosition = 0;
    private boolean isLoading = false;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ViewSwitcher refreshPlaceHolder;
    private TextView refreshTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        String devicedId = manager.getDeviceId();
        initUI();
        initValue();
        mHomeWatchReceiver = new HomeWatchReceiver(mVideoBackgroundVV);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(Intent.ACTION_USER_UNLOCKED);
        registerReceiver(mHomeWatchReceiver,filter);
    }
    private void initUI(){
        mPlaceHolderLayout = (FrameLayout)findViewById(R.id.placeHolderLayout);
        mVideoBackgroundVV = (CustomVideoView)findViewById(R.id.videoBackgroundVV);
        mVideoBackgroundVV.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.video));
        mVideoBackgroundVV.start();
        mVideoBackgroundVV.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //视频播放完成
                mVideoBackgroundVV.start();
            }
        });
        mVideoBackgroundVV.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                //视频资源加载完成
                mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
                        //Android4.1之前的版本不支持
                        if(i == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START){
                            mPlaceHolderLayout.setVisibility(View.GONE);
                        }
                        return true;
                    }
                });
            }
        });

//        oneLayout = (LinearLayout)findViewById(R.id.oneLayout);
//        twoLayout = (LinearLayout)findViewById(R.id.twoLayout);
//        threeLayout = (LinearLayout)findViewById(R.id.threeLayout);
//        TextView textView1 = new TextView(MainActivity.this);
//        textView1.setText("我就是我，是颜色不一样的烟火");
//        ImageView imageView1 = new ImageView(MainActivity.this);
//        imageView1.setMaxHeight(20);
//        imageView1.setMaxWidth(20);
//        imageView1.setMinimumHeight(20);
//        imageView1.setMinimumWidth(20);
//        imageView1.setBackgroundResource(R.drawable.dog2);
//        oneLayout.addView(textView1);
//        oneLayout.addView(imageView1);
//        TextView textView2 = new TextView(MainActivity.this);
//        textView2.setText("莫斯科没有眼泪-twins");
//        twoLayout.addView(textView2);
//        ImageView imageView2 = new ImageView(MainActivity.this);
//        imageView2.setBackgroundResource(R.drawable.timg);
//        threeLayout.addView(imageView2);
        recyclerList = (RecyclerView)findViewById(R.id.recyclerList);
        layoutManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false);
        recyclerList.setLayoutManager(layoutManager);
        pureDataListAdapter = new PureDataListAdapter(MainActivity.this);
        for(int i=0;i<20;i++){
            dataSet.add("winter"+i);
        }
        pureDataListAdapter.updateData(dataSet);
        adapter = new SmoothListAdapter(pureDataListAdapter);
        recyclerList.setAdapter(adapter);
        //加载更多通过LinearLayoutManager获取RecyclerView是否滑动到底部来实现
        recyclerList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = layoutManager.getItemCount();
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                //当前数据没有在加载并且即将滑动到当前页最后一项时，开始加载另一页的数据
                if(!isLoading && totalItemCount <= lastVisibleItemPosition + 5){
                    //加载第二页数据，并且更新纯数据列表
                    loadData();
                    //当前数据加载没有完成的时候，不允许发生重复加载
                    isLoading = true;
                    //更新整个列表的数据，包括纯数据列表和加载更多
                    adapter.notifyDataSetChanged();
                }
            }
        });
        refreshTv = (TextView)findViewById(R.id.refreshTv);
        //ViewSwitcher只适用于两个相同大小控件之间切换，如果大小不相同，ViewSwitcher会占据两个控件中较大的控件大小
        //用ViewSwitcher的setDisplayedChild(0)可以切换显示的视图，并且切换过程带有动画效果
        refreshPlaceHolder = (ViewSwitcher)findViewById(R.id.refreshPlaceHolder);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary);
        swipeRefreshLayout.setProgressViewOffset(true,0,100);
        //通过SwipeRefreshLayout.OnRefreshListener接口来实现下拉刷新，具有通用性
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("wanlijun","onRefresh");
                refreshPlaceHolder.setVisibility(View.VISIBLE);
                refreshPlaceHolder.setDisplayedChild(0);
                for(int i=0;i<20;i++){
                    dataSet.add("winter"+i);
                }
                pureDataListAdapter.updateData(dataSet);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    private void initValue(){
    }
//    public static void setmCurrentVideoPosition(int position){
//        mCurrentVideoPosition = position;
//    }

    private void loadData(){
        List<String> stringList = new ArrayList<>();
        for(int i=20;i<30;i++){
            stringList.add("winter"+i);
        }
        pureDataListAdapter.appendData(stringList);
        //设置为false表明所有数据加载完，到底啦
        adapter.setHasMoreData(false);
    }
    @Override
    protected void onStart() {
        Log.i("wanlijun","onStart");
        mVideoBackgroundVV.start();
        //seekTo是异步方法
        mVideoBackgroundVV.seekTo(mCurrentVideoPosition);
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i("wanlijun","onStop");
        mCurrentVideoPosition = mVideoBackgroundVV.getCurrentPosition();
        Log.i("wanlijun",mCurrentVideoPosition+"");
        mVideoBackgroundVV.stopPlayback();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i("wanlijun","onDestroy");
        unregisterReceiver(mHomeWatchReceiver);
        super.onDestroy();
    }

    //最外层的适配器，包括纯数据列表的适配器和加载更多以及已经加载到底的两个ITEM
    public class SmoothListAdapter extends MyBaseAdapter<String>{
        private PureDataListAdapter mAdapter;  //纯数据列表适配器
        private boolean hasMoreData = true;  //数据是否加载完成
        public SmoothListAdapter(PureDataListAdapter adapter){
            this.mAdapter = adapter;
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        public void setHasMoreData(boolean hasMoreData) {
            this.hasMoreData = hasMoreData;
        }

        @Override
        public int getItemCount() {
            //每一页包括纯数据列表和最后一行的加载更多或者已加载完成
            return mAdapter.getItemCount() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if(position == getItemCount() - 1){
                //每一页最后一行ITEM类型
                if(hasMoreData){
                    return LOAD_MORE_DATA;
                }else{
                    return NO_MORE_DATA;
                }
            }else {
                //纯数据列表的视图类型
               return mAdapter.getItemViewType(position);
            }
//            return super.getItemViewType(position);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.i("wanlijun","viewType="+viewType);
            //同一个列表加载不同类型的视图
            View view = null;
            if(viewType == NO_MORE_DATA){
                view = LayoutInflater.from(MainActivity.this).inflate(R.layout.load_over,parent,false);
                return  new NoMoreDataViewHolder(view);
            }else if(viewType == LOAD_MORE_DATA){
                view = LayoutInflater.from(MainActivity.this).inflate(R.layout.load_more,parent,false);
                return new LoadMoreViewHolder(view);
            }else{
                //纯数据列表视图
               return mAdapter.onCreateViewHolder(parent,viewType);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Log.i("wanlijun","position="+position);
            //列表数据绑定
            if(holder instanceof LoadMoreViewHolder){
                Log.i("wanlijun","LoadMoreViewHolder");
                //当前视图为加载更多时，加载数据
            }else if(holder instanceof  NoMoreDataViewHolder){

            }else{
                //纯数据列表的数据绑定
                mAdapter.onBindViewHolder(holder,position);
            }
        }
    }
}
