package com.example.listslidesmoothly;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * @author wanlijun
 * @description  数据列表其中一列
 * @time 2018/1/2 15:50
 */

public class OneViewHolder extends RecyclerView.ViewHolder {
    public TextView markItemOne;
    public OneViewHolder(View view){
        super(view);
        markItemOne = (TextView)view.findViewById(R.id.markItemOne);
//        view.setTag(this);
    }
}
