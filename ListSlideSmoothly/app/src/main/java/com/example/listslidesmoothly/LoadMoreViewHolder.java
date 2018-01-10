package com.example.listslidesmoothly;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author wanlijun
 * @description  当数据有很多页并且当前加载的不是最后一页的时候，当前页最后一项的视图
 * @time 2018/1/2 17:32
 */

public class LoadMoreViewHolder extends RecyclerView.ViewHolder {
    public LoadMoreViewHolder(View view){
        super(view);
//        view.setTag(this);
    }
}
