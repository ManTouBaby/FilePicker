package com.mt.filelibrary.base;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:MtBaby
 * @date:2020/05/07 11:07
 * @desc:
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<SmartVH> {

    protected OnItemClickListener mOnItemClickListener;
    protected OnItemLongClickListener mOnItemLongClickListener;
    protected OnItemChildViewClickListener mOnItemChildViewClickListener;

    protected List<T> mDates;
    private int mResLayout;

    public BaseAdapter(int mResLayout) {
        this(new ArrayList<>(), mResLayout);
    }

    public BaseAdapter(List<T> mDates, int mResLayout) {
        this.mDates = mDates;
        this.mResLayout = mResLayout;
    }


    @Override
    public void onBindViewHolder(SmartVH holder, int position) {
        T data = mDates.get(position);
        holder.getItemView().setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, data, position);
            }
        });
        holder.getItemView().setOnLongClickListener(v -> {
            if (mOnItemLongClickListener != null) {
                mOnItemLongClickListener.onItemLongClick(v, data, position);
            }
            return true;
        });
        onBindView(holder, data, position);
    }

    public List<T> getDates() {
        return mDates;
    }

    @Override
    public SmartVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mResLayout, null);
        return new SmartVH(view);
    }

    public void setDates(List<T> mDates) {
        this.mDates = mDates;
        notifyDataSetChanged();
    }

    public void addData(T data) {
        int size = this.mDates.size();
        this.mDates.add(data);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        this.mDates.remove(position);
        notifyItemRemoved(mDates.size());
    }

    public void addChildViewClick(View view, T data, int position) {
        view.setOnClickListener(v -> {
            if (mOnItemChildViewClickListener != null) {
                mOnItemChildViewClickListener.onItemChildViewClick(view, data, position);
            }
        });

    }

    public void setOnItemChildViewClickListener(OnItemChildViewClickListener mOnItemChildViewClickListener) {
        this.mOnItemChildViewClickListener = mOnItemChildViewClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }


    protected abstract void onBindView(SmartVH holder, T data, int position);

    @Override
    public int getItemCount() {
        return mDates == null ? 0 : mDates.size();
    }
}
