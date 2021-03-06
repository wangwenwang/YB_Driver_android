package com.example.administrator.ybdriver.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.example.administrator.ybdriver.R;
import com.example.administrator.ybdriver.adapter.GuideViewpageAdapter;
import com.example.administrator.ybdriver.canstants.Constants;
import com.example.administrator.ybdriver.ui.base.BaseActivity;
import com.example.administrator.ybdriver.utils.SharedPreferencesUtils;
import com.example.administrator.ybdriver.utils.UIUtils;

import java.util.ArrayList;




public class GuideActivity extends BaseActivity {
    private ViewPager guideViewpager;
    private LinearLayout guidepoints;
    private ImageView orangecircle;
    private Button guideButton;
    private  int distance;//小灰点的间距
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initview();
        initviewpager();
    }
    @Override
    public void initWindow() {
        //重写为空，针对满屏页面取消沉浸式状态栏
    }
    private void initviewpager() {
        //构建viewpager数据源
        ArrayList<ImageView>imageViews=new ArrayList<>();
        ImageView imageView1=new ImageView(this);
        imageView1.setBackgroundResource(R.drawable.guide_1);
        imageViews.add(imageView1);
        ImageView imageView2=new ImageView(this);
        imageView2.setBackgroundResource(R.drawable.guide_2);
        imageViews.add(imageView2);
        ImageView imageView3=new ImageView(this);
        imageView3.setBackgroundResource(R.drawable.guide_3);
        imageViews.add(imageView3);
        //初始化加载小灰点
        for (int i=0;i<imageViews.size();i++){
            ImageView graypoint=new ImageView(this);
            graypoint.setBackgroundResource(R.drawable.circle_gray);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(UIUtils.dip2px(10),UIUtils.dip2px(10));
            if (i!=0){
                params.leftMargin=UIUtils.dip2px(10);
            }
            graypoint.setLayoutParams(params);
            guidepoints.addView(graypoint);
        }
        //创建适配器
        final GuideViewpageAdapter guideViewpageAdapter=new GuideViewpageAdapter(this,imageViews);
        //绑定适配器
        guideViewpager.setAdapter(guideViewpageAdapter);
        //加载小灰点完成后测量两小灰点的距离
        orangecircle.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                guidepoints.getChildAt(0).getLeft();
                   guidepoints.getChildAt(1).getLeft();
                distance=guidepoints.getChildAt(1).getLeft()-guidepoints.getChildAt(0).getLeft();
            }
        });

        //设置监听器
        guideViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    float leftmagin=distance*(position+positionOffset);
                RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) orangecircle.getLayoutParams();
                params.leftMargin=Math.round(leftmagin);
                orangecircle.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                if (position==guideViewpageAdapter.getCount()-1){
                    guideButton.setVisibility(View.VISIBLE);
                    guideButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(GuideActivity.this,LoginActivity.class);
                            startActivity(intent);
                            SharedPreferencesUtils.WriteSharedPreferences(Constants.BasicInfo, Constants.IsUsedApp, true);
                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initview() {
        guideViewpager= (ViewPager) findViewById(R.id.viewPager_guideActivity);
        guidepoints= (LinearLayout) findViewById(R.id.points_guideActivity );
        orangecircle= (ImageView) findViewById(R.id.circle_orange);
        guideButton= (Button) findViewById(R.id.button_guideActivity);
    }


}
