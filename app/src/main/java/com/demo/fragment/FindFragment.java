package com.demo.fragment;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.demo.R;
import com.demo.bean.DiscoverListResult;
import com.demo.config.UrlConfig;
import com.demo.decortor.CacheDecortor;
import com.demo.view.banner.DotIndicatorView;
import com.demo.view.banner.bannerView.BannerAdapter;
import com.demo.view.banner.bannerView.BannerView;

import org.adapter.recycler.EAdapter;
import org.adapter.recycler.EHolder;
import org.adapter.recycler.mode.ItemBind;
import org.adapter.recycler.view.WrapRecyclerView;
import org.annotation.ViewById;
import org.base.BaseFragment;
import org.http.HttpUtils;
import org.http.mode.callback.Callback;
import org.log.L;

import java.util.List;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/11/27
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class FindFragment extends BaseFragment {
    @ViewById(R.id.rvFind)
    public WrapRecyclerView rvFind;
    @ViewById(R.id.swipeContainer)
    public SwipeRefreshLayout swipeContainer;
    @ViewById(R.id.loadView)
    public ImageView loadView;
    @ViewById(R.id.frameLayout)
    public View frameLayout;

    @Override
    protected void setContentView() {
        setContentView(R.layout.fragment_find);
    }

    @Override
    protected void initView() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                loadNetData();
            }
        });
    }

    @Override
    protected void initData() {
        loadNetData();
        addLoadView();
    }


    private void addLoadView() {
        AnimationDrawable animationDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.anim_loading);
        loadView.setBackground(animationDrawable);
        animationDrawable.start();
    }

    private void loadNetData() {
        HttpUtils.with(context)
                .url(UrlConfig.HOME_FIND)
                .addParams("iid", 6152551759L)
                .addParams("aid", 7)
                .addDecortor(new CacheDecortor())
                .get(new Callback.CommonCallback<DiscoverListResult>() {
                    @Override
                    public void onSuccess(String url, DiscoverListResult result) {
                        // 先显示列表
                        showListData(result.getData().getCategories().getCategory_list());
                        addBannerView(result.getData().getRotate_banner().getBanners());
                    }

                    @Override
                    public void onError(String url, Throwable e) {
                        L.e(e);
                    }

                    @Override
                    public void onFinal(String url) {
                        swipeContainer.setRefreshing(false);
                        frameLayout.setVisibility(View.GONE);
                    }
                });
    }

    private void showListData(List<DiscoverListResult.DataBean.CategoriesBean.CategoryListBean> category_list) {

        EAdapter.Builder.load(category_list)
                .item(R.layout.item_channel_list)
                .bind(new ItemBind<DiscoverListResult.DataBean.CategoriesBean.CategoryListBean>() {
                    @Override
                    public void convert(EHolder holder, DiscoverListResult.DataBean.CategoriesBean.CategoryListBean item, int position, RecyclerView.Adapter adapter) {
                        // 显示数据
                        String str = item.getSubscribe_count() + " 订阅 | " +
                                "总帖数 <font color='#FF678D'>" + item.getTotal_updates() + "</font>";
                        holder.setText(R.id.channel_text, item.getName())
                                .setText(R.id.channel_topic, item.getIntro())
                                .setText(R.id.channel_update_info, Html.fromHtml(str))
                                .setImageByUrl(R.id.channel_icon, item.getIcon_url());

                        // 是否是最新
                        if (item.isIs_recommend()) {
                            holder.setViewVisibility(R.id.recommend_label, View.VISIBLE);
                        } else {
                            holder.setViewVisibility(R.id.recommend_label, View.GONE);
                        }
                    }
                })
                .into(rvFind);
    }

    private void addBannerView(final List<DiscoverListResult.DataBean.RotateBannerBean.BannersBean> banners) {
        // 后台没有轮播那就不添加
        if (banners.size() <= 0) {
            return;
        }

        BannerView bannerView = (BannerView) LayoutInflater.from(context)
                .inflate(R.layout.layout_banner_view, rvFind, false);

        bannerView.setAdapter(new BannerAdapter() {
            @Override
            public View getView(int position, View convertView) {
                if (convertView == null) {
                    convertView = new ImageView(context);
                }
                ((ImageView) convertView).setScaleType(ImageView.ScaleType.CENTER_CROP);

                Glide.with(context).load(banners.get(position).getBanner_url().getUrl_list()
                        .get(0).getUrl()).into((ImageView) convertView);
                return convertView;
            }

            @Override
            public View getIndicatorView(int height) {
                // 不断的往点的指示器添加圆点
                DotIndicatorView indicatorView = new DotIndicatorView(getContext());
                // 设置大小
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(height / 2, height / 2);
                // 设置左右间距
                params.setMargins(5, 5, 15, 5);
                indicatorView.setLayoutParams(params);
                return indicatorView;
            }

            @Override
            public void setHightLight(int position, View view) {
                DotIndicatorView indicatorView = (DotIndicatorView) view;
                Drawable drawable = new ColorDrawable(Color.RED);
                indicatorView.setImageDrawable(drawable);
            }

            @Override
            public void setDefaultLight(int position, View view) {
                DotIndicatorView indicatorView = (DotIndicatorView) view;
                Drawable drawable = new ColorDrawable(Color.WHITE);
                indicatorView.setImageDrawable(drawable);
            }

            @Override
            public int getCount() {
                return banners.size();
            }

            @Override
            public String getDescription(int position) {
                return banners.get(position).getBanner_url().getTitle();
            }
        });

        // 开启滚动
        bannerView.startScroll();

        rvFind.addHeaderView(bannerView);
    }
}
