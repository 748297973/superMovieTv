package com.zmovie.app.presenter.iview;


import com.zmovie.app.domain.OnlinePlayInfo;
import com.zmovie.app.domain.RecentUpdate;

/**
 * Created by huangyong on 2018/1/26.
 */

public interface IRandom {
    void loadRandomData(OnlinePlayInfo info);
    void loadRError(String msg);
}
