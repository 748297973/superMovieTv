package com.zmovie.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.zmovie.app.domain.OnlinePlayInfo;
import com.zmovie.app.domain.RecentUpdate;
import com.zmovie.app.http.ApiManager;
import com.zmovie.app.presenter.iview.IMoview;
import com.zmovie.app.presenter.iview.IRandom;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by huangyong on 2018/1/26.
 */

public class GetRandomRecpresenter extends BasePresenter<IRandom>{

    public GetRandomRecpresenter(Context context, IRandom iview) {
        super(context, iview);
    }


    public void getBtRecommend(String type){

        if (TextUtils.isEmpty(type)){
            return;
        }
        Subscription subscription = ApiManager
                .getRetrofitInstance()
                .getBtRandomRecomend(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<OnlinePlayInfo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(OnlinePlayInfo result) {
                        iview.loadRandomData(result);
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void release() {
        unSubcription();
    }


    public void getSeriRecommend(String type) {
        if (TextUtils.isEmpty(type)){
            return;
        }
        Subscription subscription = ApiManager
                .getRetrofitInstance()
                .getSeriRandomRecomend(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<OnlinePlayInfo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(OnlinePlayInfo result) {
                        iview.loadRandomData(result);
                    }
                });
        addSubscription(subscription);
    }
}
