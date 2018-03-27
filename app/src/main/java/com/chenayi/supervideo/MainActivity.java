package com.chenayi.supervideo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends AppCompatActivity {
    private final int PAGE_COUNT = 10;

    private RecyclerView mRV;
    private VideoListAdapter mAdapter;
    private SwipeRefreshLayout refreshLayout;
    private BmobQuery<Url> bmobQuery;
    private LoadingDialog loadingDialog;
    private int curPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRV = findViewById(R.id.rv);
        refreshLayout = findViewById(R.id.refresh_layout);

        loadingDialog = LoadingDialog.newInstance();
        loadingDialog.show(getSupportFragmentManager());
        bmobQuery = new BmobQuery<>();

        mAdapter = new VideoListAdapter(new ArrayList<Url>());
        mAdapter.setEnableLoadMore(true);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mRV.setLayoutManager(new LinearLayoutManager(this));
        mRV.setAdapter(mAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                curPage = 0;
                requestDatas();
            }
        });

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                curPage += 1;
                requestDatas();
            }
        }, mRV);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("title", mAdapter.getItem(position).name);
                intent.putExtra("url", mAdapter.getItem(position).url);
                startActivity(intent);
            }
        });


        requestDatas();
    }

    private void requestDatas() {
        bmobQuery
                .setSkip(curPage * PAGE_COUNT)
                .setLimit(PAGE_COUNT)//每页10条
                .findObjects(new FindListener<Url>() {
                    @Override
                    public void done(List<Url> list, BmobException e) {
                        loadingDialog.dismiss();
                        refreshLayout.setRefreshing(false);

                        if (e != null) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (curPage <= 0) {
                            mAdapter.setNewData(list);
                            mAdapter.disableLoadMoreIfNotFullPage();
                            if (list.size() < PAGE_COUNT) {
                                mAdapter.loadMoreEnd(true);
                            }
                        } else {
                            mAdapter.addData(list);
                            if (list.size() < PAGE_COUNT) {
                                mAdapter.loadMoreEnd(true);
                            } else {
                                mAdapter.loadMoreComplete();
                            }
                        }
                    }
                });
    }
}
