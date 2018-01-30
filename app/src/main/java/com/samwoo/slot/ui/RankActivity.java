package com.samwoo.slot.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.samwoo.slot.R;
import com.samwoo.slot.adapter.RecyclerAdapter;
import com.samwoo.slot.base.BaseActivity;
import com.samwoo.slot.database.DatabaseManager;
import com.samwoo.slot.database.Rank;
import com.samwoo.slot.utils.LogUtils;
import com.samwoo.slot.utils.ToastUtils;
import com.samwoo.slot.widget.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/1/27.
 */

public class RankActivity extends BaseActivity {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private List<Rank> mList = new ArrayList<Rank>();
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        ButterKnife.bind(this);
        initRecyclerView();
        initSwipeRefresh();
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        mList = DatabaseManager.getInstance().queryAllRank();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapter(this, mList);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, R.drawable.recycleview_divider, false));
    }

    /**
     * 初始化SwipeRefreshView
     */
    private void initSwipeRefresh() {
        refreshLayout.setColorSchemeColors(Color.BLUE, Color.RED);
        refreshLayout.setOnRefreshListener(onRefreshListener);
    }

    /**
     * 设置swipeRefreshView 刷新监听
     */
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            ToastUtils.showToast(getApplicationContext(), "正在刷新...");
            adapter.notifyDataSetChanged();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setRefreshing(false);
                    ToastUtils.showToast(getApplicationContext(), "刷新完成！");
                }
            }, 2000);

        }
    };

    /**
     * 删除历史记录
     *
     * @param view
     */
    public void deletAll(View view) {
        int itemCount = mList.size() - 1；
        refreshLayout.setRefreshing(true);
        DatabaseManager.getInstance().deleteAllRank();
        adapter.notifyItemRangeRemoved(0, itemCount);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
                ToastUtils.showToastById(getApplicationContext(), R.string.deleteMsg);
            }
        }, 2000);
    }
}
