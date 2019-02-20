package com.zmovie.app.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huangyong.playerlib.CustomIjkplayer;
import com.owen.tvrecyclerview.widget.SimpleOnItemListener;
import com.owen.tvrecyclerview.widget.TvRecyclerView;
import com.tencent.smtt.sdk.TbsVideo;
import com.zmovie.app.R;
import com.zmovie.app.adapter.OnlineRecAdapter;
import com.zmovie.app.adapter.PlayListAdapter;
import com.zmovie.app.data.GlobalMsg;
import com.zmovie.app.display.DisplayAdaptive;
import com.zmovie.app.domain.DescBean;
import com.zmovie.app.domain.OnlinePlayInfo;
import com.zmovie.app.domain.PlayUrlBean;
import com.zmovie.app.focus.FocusBorder;
import com.zmovie.app.presenter.GetRandomRecpresenter;
import com.zmovie.app.presenter.iview.IRandom;

import java.util.ArrayList;


/**
 *  intent.putExtra(GlobalMsg.KEY_POST_IMG, finalImgUrl);
 intent.putExtra(GlobalMsg.KEY_DOWN_URL,datas.getData().get(position).getDownLoadUrl());
 intent.putExtra(GlobalMsg.KEY_MOVIE_TITLE,datas.getData().get(position).getDownLoadName());
 intent.putExtra(GlobalMsg.KEY_MOVIE_DETAIL,datas.getData().get(position).getMvdesc());
 */
public class OnlineSeriDetailActivity extends Activity implements IRandom {

    private String title;
    private String downUrl;
    private String posterUrl;
    private String movDescription;
    private TextView tvDescription;
    private TvRecyclerView recyclerView;
    private TextView titleView;
    private String downItemTitle;
    private String[] downItemList;
    private String playUrl;
    private String playTitle;
    private CustomIjkplayer ijkplayer;
    private String descContent;
    private FocusBorder mFocusBorder;
    private PlayUrlBean playUrlBean;
    private TextView shortDesc;
    private View fullScreen;
    private View descView;
    private ChoseSerisDialog serisDialog;
    private GetRandomRecpresenter getRandomRecpresenter;
    private String mvType;
    private TvRecyclerView recommedList;
    private OnlineRecAdapter onlineMvAdapter;
    private OnlinePlayInfo info;
    SimpleOnItemListener listener =   new SimpleOnItemListener() {

        @Override
        public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
            float radius = DisplayAdaptive.getInstance().toLocalPx(10);
            onMoveFocusBorder(itemView, 1.1f, radius);
        }

        @Override
        public void onItemClick(TvRecyclerView parent, View itemView, int position) {
            if (info!=null&&info.getData().size()>0){
                if (position<info.getData().size()){
                    Intent intent = new Intent(OnlineSeriDetailActivity.this, OnlineSeriDetailActivity.class);
                    String imgUrl = info.getData().get(position).getDownimgurl();
                    intent.putExtra(GlobalMsg.KEY_POST_IMG, imgUrl);
                    intent.putExtra(GlobalMsg.KEY_MOVIE_TYPE,mvType);
                    intent.putExtra(GlobalMsg.KEY_DOWN_URL,info.getData().get(position).getDownLoadUrl());
                    intent.putExtra(GlobalMsg.KEY_MOVIE_TITLE, info.getData().get(position).getDownLoadName());
                    intent.putExtra(GlobalMsg.KEY_MOVIE_DOWN_ITEM_TITLE, info.getData().get(position).getDowndtitle());
                    intent.putExtra(GlobalMsg.KEY_MOVIE_DETAIL,info.getData().get(position).getMvdesc());
                    startActivity(intent);
                    finish();
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_mov_detail_layout);
        initView();
        initData();


    }

    private void initData() {
        Intent intent = getIntent();
        posterUrl = intent.getStringExtra(GlobalMsg.KEY_POST_IMG);
        playUrl = intent.getStringExtra(GlobalMsg.KEY_PLAY_URL);
        playTitle = intent.getStringExtra(GlobalMsg.KEY_PLAY_TITLE);
        downItemTitle = intent.getStringExtra(GlobalMsg.KEY_MOVIE_DOWN_ITEM_TITLE);
        mvType = intent.getStringExtra(GlobalMsg.KEY_MOVIE_TYPE);
        downItemList = downItemTitle.split(",");

        downUrl = intent.getStringExtra(GlobalMsg.KEY_DOWN_URL);
        title = intent.getStringExtra(GlobalMsg.KEY_MOVIE_TITLE);
        movDescription = intent.getStringExtra(GlobalMsg.KEY_MOVIE_DETAIL);

        getRandomRecpresenter = new GetRandomRecpresenter(this,this);
        getRandomRecpresenter.getSeriRecommend(mvType);

        Gson gson = new Gson();
        final DescBean descBean = gson.fromJson(movDescription, DescBean.class);
        titleView.setText(title);
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < descBean.getHeader().size(); i++) {
            builder.append(descBean.getHeader().get(i)+"\n");
        }
        if (!TextUtils.isEmpty(descBean.getDesc())){
            shortDesc.setText("简介："+descBean.getDesc());
        }else {
            shortDesc.setText(builder.toString());
        }



        descView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DescMovieDialog descDialog = new DescMovieDialog(OnlineSeriDetailActivity.this,1);
                descDialog.setDescData(builder.toString(),posterUrl);
                descDialog.show();
            }
        });
        tvDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DescMovieDialog descDialog = new DescMovieDialog(OnlineSeriDetailActivity.this,1);
                descDialog.setDescData(builder.toString(),posterUrl);
                descDialog.show();
            }
        });



        playUrlBean = gson.fromJson(downUrl, PlayUrlBean.class);
        ArrayList<String> playM3u8List = new ArrayList<>();
        ArrayList<String> playWebUrlList = new ArrayList<>();

        for (int i = 0; i < playUrlBean.getNormal().size(); i++) {
            playWebUrlList.add("第"+(i+1)+"集");
        }
        for (int i = 0; i < playUrlBean.getM3u8().size(); i++) {
            if (playUrlBean.getM3u8().size()==1){
                playM3u8List.add("在线观看");
            }else {
                if (playUrlBean.getM3u8().size()>10){
                    playM3u8List.add("第"+(i+1)+"集");
                    if (i==9){
                        break;
                    }
                }else {
                   playM3u8List.add("第"+(i+1)+"集");
                }

            }

        }

        final PlayListAdapter mAdapter = new PlayListAdapter(OnlineSeriDetailActivity.this, false);
        mAdapter.setDatas(playM3u8List);
//


        serisDialog = new ChoseSerisDialog(OnlineSeriDetailActivity.this,playUrlBean.getM3u8().size(), new ChoseSerisDialog.OnItemClicked() {
            @Override
            public void clicked(int postion, String s) {
                Bundle bundle = new Bundle();
                bundle.putInt("screenMode",102);
                TbsVideo.openVideo(OnlineSeriDetailActivity.this,playUrlBean.getM3u8().get(postion).getUrl(),bundle);

                serisDialog.dismiss();
            }
        });


        recyclerView.setAdapter(mAdapter);
        recyclerView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    mFocusBorder.setVisible(false);
                }
            }
        });
    }


    private void initView() {
        // 移动框
        if(null == mFocusBorder) {
            mFocusBorder = new FocusBorder.Builder()
                    .asColor()
                    .borderWidth(TypedValue.COMPLEX_UNIT_DIP, 2)
                    .borderColor(getResources().getColor(R.color.item_activated_color))
                    .shadowWidth(TypedValue.COMPLEX_UNIT_DIP, 18)
                    .borderRadius(0)
                    .build(this);
        }
        ijkplayer = findViewById(R.id.video_view);
        descView = findViewById(R.id.show_desc);
        //全屏按钮
        fullScreen = findViewById(R.id.fullscreen_view);

        fullScreen.requestFocus();
        fullScreen.setNextFocusLeftId(R.id.fullscreen_view);

        shortDesc = findViewById(R.id.desc_short);
        shortDesc.setNextFocusLeftId(R.id.desc_short);


        recyclerView = findViewById(R.id.downlist);
        recommedList = findViewById(R.id.recommend);
        recommedList.setOnItemListener(listener);
//        recyclerView2 = findViewById(R.id.downlist2);
        recommedList.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mFocusBorder!=null){
                    mFocusBorder.setVisible(hasFocus);
                }

            }
        });
        recyclerView.setNextFocusUpId(R.id.fullscreen_view);

        recyclerView.setSpacingWithMargins(12, 20);
        recommedList.setSpacingWithMargins(12,40);



        titleView = findViewById(R.id.detai_title);

        recyclerView.setOnItemListener(new SimpleOnItemListener() {

            @Override
            public void onItemSelected(TvRecyclerView parent, View itemView, int position) {

            }
            @Override
            public void onItemClick(final TvRecyclerView parent, final View itemView, int position) {

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < parent.getChildCount(); i++) {
                            ((TextView)parent.getChildAt(i).findViewById(R.id.title)).setTextColor(Color.WHITE);
                        }
                        TextView textView = itemView.findViewById(R.id.title);
                        textView.setTextColor(getResources().getColor(R.color.green_bright));
                    }
                });

                if (position==9){
                    serisDialog.show();
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("screenMode",102);
                    TbsVideo.openVideo(OnlineSeriDetailActivity.this,playUrlBean.getM3u8().get(position).getUrl(),bundle);

                }
            }
        });



        tvDescription = findViewById(R.id.desc_short);

    }
    protected void onMoveFocusBorder(View focusedView, float scale, float roundRadius) {
        if(null != mFocusBorder) {
            mFocusBorder.onFocus(focusedView, FocusBorder.OptionsFactory.get(scale, scale, roundRadius));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ijkplayer.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        ijkplayer.pause();
    }


    @Override
    protected void onDestroy() {
        ijkplayer.release();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void loadRandomData(OnlinePlayInfo info) {
        this.info = info;
        onlineMvAdapter = new OnlineRecAdapter(this);
        //recommedList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        onlineMvAdapter.setDatas(info.getData());
        recommedList.setAdapter(onlineMvAdapter);
    }

    @Override
    public void loadRError(String msg) {

    }
}
