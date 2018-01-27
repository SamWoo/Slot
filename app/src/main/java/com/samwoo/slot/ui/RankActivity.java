package com.samwoo.slot.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

    private List<Rank> mList = new ArrayList<Rank>();
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        ButterKnife.bind(this);
        initRecyclerView();
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        mList = DatabaseManager.getInstance().queryAllRank();
        for (int i = 0; i < mList.size(); i++) {
            LogUtils.e("Slot", mList.get(i).getWinner() + "/" + mList.get(i).getTime() + "/" + mList.get(i).getScore());
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapter(this, mList);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, R.drawable.recycleview_divider, false));
    }

    /**
     * 删除历史记录
     *
     * @param view
     */
    public void deletAll(View view) {
        DatabaseManager.getInstance().deleteAllRank();
        adapter.notifyDataSetChanged();
        ToastUtils.showToastById(this, R.string.deleteMsg);
    }
}
