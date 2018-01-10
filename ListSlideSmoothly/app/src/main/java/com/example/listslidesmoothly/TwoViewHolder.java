package com.example.listslidesmoothly;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * @author wanlijun
 * @description 数据列表其中一列
 * @time 2018/1/2 15:52
 */

public class TwoViewHolder extends RecyclerView.ViewHolder {
    public TextView markItemTwo;
    public TwoViewHolder(View view){
        super(view);
        markItemTwo =  (TextView)view.findViewById(R.id.markItemTwo);
//        view.setTag(this);
    }
}
