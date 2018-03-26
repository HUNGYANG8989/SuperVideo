package com.chenayi.supervideo;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Chenwy on 2018/3/26.
 */

public class VideoListAdapter extends BaseQuickAdapter<Url, BaseViewHolder> {
    public VideoListAdapter(List<Url> data) {
        super(R.layout.item_video_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Url item) {
        helper.setText(R.id.tv_name, item.name);
    }
}
