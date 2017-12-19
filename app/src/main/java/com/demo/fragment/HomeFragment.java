package com.demo.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.demo.R;
import com.demo.bean.HomeData;
import com.demo.config.DataFactory;
import com.demo.view.SmileView;

import org.adapter.recycler.EAdapter;
import org.adapter.recycler.EHolder;
import org.adapter.recycler.mode.ItemBind;
import org.annotation.ViewById;
import org.base.BaseFragment;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/11/27
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class HomeFragment extends BaseFragment {

    @ViewById(R.id.recyclerView)
    public RecyclerView recyclerView;

    @Override
    protected void setContentView() {
        setContentView(R.layout.fragment_home);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        EAdapter.Builder
                .load(DataFactory.getHomeData())
                .item(R.layout.item_smile)
                .bind(new ItemBind<HomeData>() {
                    @Override
                    public void convert(final EHolder holder, final HomeData itemData, int position, RecyclerView.Adapter adapter) {
                        final SmileView likeView = holder.getView(R.id.smileView1);
                        final SmileView disLikeView = holder.getView(R.id.smileView);

                        likeView.setFaceCount(itemData.like / 20);
                        disLikeView.setFaceCount(itemData.disLike / 20);
                        disLikeView.setIsGood(false);

                        likeView.setAnimationListener(new SmileView.AnimationListener() {
                            @Override
                            public void onEnd() {
                                holder.setViewVisibility(R.id.tvLike, View.GONE);
                                holder.setViewVisibility(R.id.tvDisLike, View.GONE);
                            }
                        });

                        disLikeView.setAnimationListener(new SmileView.AnimationListener() {
                            @Override
                            public void onEnd() {
                                holder.setViewVisibility(R.id.tvLike, View.GONE);
                                holder.setViewVisibility(R.id.tvDisLike, View.GONE);
                            }
                        });

                        holder.setImageResource(R.id.imageView, itemData.img)
                                .setOnClick(R.id.smileView1, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        holder.setViewVisibility(R.id.tvLike, View.VISIBLE);
                                        holder.setViewVisibility(R.id.tvDisLike, View.VISIBLE);
                                        likeView.startAnima();
                                        disLikeView.startAnima();
                                        holder.setText(R.id.tvLike, itemData.like + "%")
                                                .setText(R.id.tvDisLike, itemData.disLike + "%");
                                    }
                                })
                                .setOnClick(R.id.smileView, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        holder.setViewVisibility(R.id.tvLike, View.VISIBLE);
                                        holder.setViewVisibility(R.id.tvDisLike, View.VISIBLE);
                                        likeView.startAnima();
                                        disLikeView.startAnima();
                                        holder.setText(R.id.tvLike, itemData.like + "%")
                                                .setText(R.id.tvDisLike, itemData.disLike + "%");
                                    }
                                });
                    }
                })
                .into(recyclerView);
    }

}
