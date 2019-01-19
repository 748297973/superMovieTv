package com.owen.tvrecyclerview.example.presenter;

import android.content.Context;

import com.owen.tvrecyclerview.example.domain.BtInfo;
import com.owen.tvrecyclerview.example.domain.RecentUpdate;
import com.owen.tvrecyclerview.example.http.ApiManager;
import com.owen.tvrecyclerview.example.presenter.iview.IMoview;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by huangyong on 2018/1/26.
 */

public class GetRecpresenter extends BasePresenter<IMoview>{
    private Context context;
    private IMoview iMoview;

    public GetRecpresenter(Context context, IMoview iview) {
        super(context, iview);
    }

    public void getRecentUpdate(int page, int pagesize){

        Subscription subscription = ApiManager
                .getRetrofitInstance()
                .getRecomend(page,pagesize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RecentUpdate>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(RecentUpdate result) {
                        iview.loadData(result);
                    }
                });
        addSubscription(subscription);
    }
    public void getBtRecommend(int page,int pagesize){

        Subscription subscription = ApiManager
                .getRetrofitInstance()
                .getBtRecomend(page,pagesize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RecentUpdate>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(RecentUpdate result) {
                        iview.loadBtData(result);
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void release() {
        unSubcription();
    }

    public void getMoreData(int page,int pagesize) {
        Subscription subscription = ApiManager
                .getRetrofitInstance()
                .getRecomend(page,pagesize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RecentUpdate>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(RecentUpdate result) {
                        iview.loadMore(result);
                    }
                });
        addSubscription(subscription);
    }
    public void getBtMoreData(int page,int pagesize) {
        Subscription subscription = ApiManager
                .getRetrofitInstance()
                .getBtRecomend(page,pagesize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RecentUpdate>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(RecentUpdate result) {
                    }
                });
        addSubscription(subscription);
    }

    public void getBtDetail(String title) {
        Subscription subscription = ApiManager
                .getRetrofitInstance()
                .getBtDetail(title)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BtInfo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(BtInfo result) {
                        iview.loadDetail(result);
                    }
                });
        addSubscription(subscription);
    }
}
