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
    private RecyclerView mRV;
    private VideoListAdapter mAdapter;
    private SwipeRefreshLayout refreshLayout;
    private BmobQuery<Url> bmobQuery;
    private LoadingDialog loadingDialog;

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
        mRV.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mRV.setAdapter(mAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bmobQuery.findObjects(new FindListener<Url>() {
                    @Override
                    public void done(List<Url> list, BmobException e) {
                        refreshLayout.setRefreshing(false);
                        if (e == null) {
                            mAdapter.setNewData(list);
                        } else {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

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


        bmobQuery.findObjects(new FindListener<Url>() {
            @Override
            public void done(List<Url> list, BmobException e) {
                loadingDialog.dismiss();
                if (e == null) {
                    mAdapter.setNewData(list);
                } else {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
