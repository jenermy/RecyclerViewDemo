package com.example.listslidesmoothly;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wanlijun
 * @description  处理列表数据集
 * @time 2018/1/2 17:12
 */

public abstract class MyBaseAdapter<T> extends RecyclerView.Adapter {
    private List<T> dataSet = new ArrayList<>();
    public void updateData(List dataSet){
        this.dataSet.clear();
        appendData(dataSet);
    }
    public void appendData(List dataSet){
        if(dataSet != null && !dataSet.isEmpty()){
            this.dataSet.addAll(dataSet);
            notifyDataSetChanged();
        }
    }

    public List<T> getDataSet() {
        return dataSet;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
