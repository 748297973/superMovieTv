package com.zmovie.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.huangyong.playerlib.Params;
import com.owen.tab.TvTabLayout;
import com.zmovie.app.focus.FocusBorder;
import com.zmovie.app.fragment.BaseFragment;
import com.zmovie.app.fragment.BtTabFragment;
import com.zmovie.app.fragment.DownloadTabFragment;
import com.zmovie.app.fragment.OnlineMovieFragment;
import com.zmovie.app.fragment.OnlineSerisFragment;
import com.zmovie.app.fragment.OnlineTabFragment;
import com.zmovie.app.view.OnScrollDownListener;

public class HomeRootActivity extends FragmentActivity implements BaseFragment.FocusBorderHelper{

    private TvTabLayout mTabLayout;
    private FrameLayout contents;
    private FocusBorder mFocusBorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_root);


        mTabLayout = findViewById(R.id.home_tab_layout);
        contents= findViewById(R.id.home_tab_container);


        // 移动框
        if(null == mFocusBorder) {
//            mFocusBorder = new FocusBorder.Builder().asDrawable().borderResId(R.drawable.focus).build(this);
            mFocusBorder = new FocusBorder.Builder()
                    .asColor()
                    .borderWidth(TypedValue.COMPLEX_UNIT_DIP, 4)
                    .borderColor(getResources().getColor(R.color.item_pressed_color))
                    .shadowWidth(TypedValue.COMPLEX_UNIT_DIP, 18)
                    .borderRadius(0)
                    .build(this);
        }
        mTabLayout.addOnTabSelectedListener(new HomeTabSelectedListener());

        mTabLayout.addTab(
                mTabLayout.newTab()
                        .setText("看电影")
//                        .setIcon(R.drawable.ic_staggered)
        ,true);
        mTabLayout.addTab(
                mTabLayout.newTab()
                        .setText("看电视剧")
//                        .setIcon(R.drawable.ic_staggered)
                );

        mTabLayout.addTab(
                mTabLayout.newTab()
                        .setText("BT影库")
//                        .setIcon(R.drawable.ic_staggered)
        );
        mTabLayout.addTab(
                mTabLayout.newTab()
                        .setText("下载中心")
//                        .setIcon(R.drawable.ic_grid)
                );

    }

    @Override
    public FocusBorder getFocusBorder() {
        return mFocusBorder;
    }

    public class HomeTabSelectedListener implements TvTabLayout.OnTabSelectedListener{



        public HomeTabSelectedListener() {
        }

        private Fragment mFragment;


        @Override
        public void onTabSelected(TvTabLayout.Tab tab) {
            final int position = tab.getPosition();
            mFragment = (Fragment) getSupportFragmentManager().findFragmentByTag(position + "");
            FragmentTransaction mFt = getSupportFragmentManager().beginTransaction();

            if (mFragment == null) {
                switch (position) {

                    case 0:
                        mFragment = OnlineMovieFragment.getInstance();
                        break;
                    case  1:
                        mFragment =OnlineSerisFragment.getInstance();
                        break;
                    case 2:
                        mFragment = OnlineTabFragment.getInstance();
                        break;
                    case 3:
                        mFragment = DownloadTabFragment.getInstance();
                        break;

                }
                mFt.add(R.id.home_tab_container, mFragment, String.valueOf(position));
            } else {
                mFt.show(mFragment);
            }
            mFt.commit();



        }

        @Override
        public void onTabUnselected(TvTabLayout.Tab tab) {
            if (mFragment != null) {
                FragmentTransaction mFt = getSupportFragmentManager().beginTransaction();
                mFt.hide(mFragment);
                mFt.commit();
            }
        }

        @Override
        public void onTabReselected(TvTabLayout.Tab tab) {

        }
    }

    /**
     * 再按一次退出
     */
    private long mExitTime = 0;

    @Override
    public void onBackPressed() {

        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(this, "再按一次退出噢", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}
