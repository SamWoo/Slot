package com.samwoo.slot.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.samwoo.slot.R;
import com.samwoo.slot.database.Rank;
import com.samwoo.slot.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/1/27.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    public static final String TAG = "RecyclerAdapter";
    private List<Rank> mList = new ArrayList<>();
    private Context context;

    public RecyclerAdapter(Context context, List<Rank> rankList) {
        this.context = context;
        this.mList = rankList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_recyclerview_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Rank item = mList.get(position);
        LogUtils.e("Slot", item.getWinner() + "/" + item.getTime() + "/" + item.getScore());
        holder.name.setText(item.getWinner());
        holder.score.setText(item.getScore()+"");
        holder.time.setText(item.getTime());
        Glide.with(context).load(R.drawable.icon).into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView name;
        @BindView(R.id.tv_score)
        TextView score;
        @BindView(R.id.tv_time)
        TextView time;
        @BindView(R.id.img_avatar)
        ImageView avatar;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
