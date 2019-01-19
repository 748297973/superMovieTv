package com.owen.tvrecyclerview.example.presenter.iview;


import com.owen.tvrecyclerview.example.domain.BtInfo;
import com.owen.tvrecyclerview.example.domain.RecentUpdate;

/**
 * Created by huangyong on 2018/1/26.
 */

public interface IMoview {
    void loadData(RecentUpdate info);
    void loadError(String msg);

    void loadMore(RecentUpdate result);

    void loadBtData(RecentUpdate result);

    void loadDetail(BtInfo result);
}
