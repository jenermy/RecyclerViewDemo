package com.example.listslidesmoothly;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author wanlijun
 * @description 数据列表适配器，不包括加载更多项
 * @time 2018/1/3 9:45
 */

public class PureDataListAdapter extends MyBaseAdapter<String> {
    private Context mContext;
    public PureDataListAdapter(Context context){
        this.mContext = context;
    }
    private static final int ONE_VIEW_TYPE = 1;
    private static final int TWO_VIEW_TYPE = 2;
    private View oneView,twoView;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //数据列表不同的视图表现
        if(viewType == ONE_VIEW_TYPE){
            //以下判断oneView不为空或者设置Tag的做法是不对的，会报错java.lang.IllegalArgumentException: Scrapped or attached views may not be recycled
            //所以还是得不断重新创建view
            //本来想复用view来着，因为我的列表滑动的时候非常卡，所以我想是不是绘制视图比较耗时间，想根据普通的BaseAdapter一样复用视图，设置tag，利用视图缓存，结果机制不一样，运行直接就报错了，所以此思路不通，看来对RecyclerView还有很多不了解
//            if(oneView == null){
//                oneView = LayoutInflater.from(mContext).inflate(R.layout.list_item_one,parent,false);
//                return new OneViewHolder(oneView);
//            }else{
//                return (OneViewHolder)oneView.getTag();
//            }
            oneView = LayoutInflater.from(mContext).inflate(R.layout.list_item_one,parent,false);
            return new OneViewHolder(oneView);
        }else{
//            if(twoView == null){
//                twoView = LayoutInflater.from(mContext).inflate(R.layout.list_item_two,parent,false);
//                return new TwoViewHolder(twoView);
//            }else{
//                return (TwoViewHolder)twoView.getTag();
//            }
            twoView = LayoutInflater.from(mContext).inflate(R.layout.list_item_two,parent,false);
            return new TwoViewHolder(twoView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof OneViewHolder){
            OneViewHolder oneViewHolder = (OneViewHolder)holder;
            oneViewHolder.markItemOne.setText(getDataSet().get(position));
        }else if(holder instanceof TwoViewHolder){
            TwoViewHolder twoViewHolder = (TwoViewHolder)holder;
            twoViewHolder.markItemTwo.setText(getDataSet().get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return ONE_VIEW_TYPE;
        } else {
            return TWO_VIEW_TYPE;
        }
    }
}

