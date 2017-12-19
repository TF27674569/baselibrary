package com.demo.activity;

import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.R;
import com.demo.base.NavigationTitle;
import com.demo.bean.FoodModel;
import com.demo.config.DataFactory;
import com.demo.view.bezier.ShoppingView;

import org.adapter.recycler.EAdapter;
import org.adapter.recycler.EHolder;
import org.adapter.recycler.mode.ItemBind;
import org.annotation.Event;
import org.annotation.ViewById;
import org.base.BaseActivity;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/12/4
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class BezierShopActivity extends BaseActivity {

    @ViewById(R.id.shopMenu)
    public RecyclerView shopMenu;
    @ViewById(R.id.tv_good_fitting_num)
    public TextView tvGoodFittingNum;
    //￥0.0元
    @ViewById(R.id.tvMoney)
    public TextView tvMoney;

    private int mMaxMoney = 0;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_shop);
    }

    @Override
    protected void initView() {
        // title
        new NavigationTitle.Builder(this)
                .setTitle("购物车")
                .setBackClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .immersed()
                .build();

        tvGoodFittingNum.setText("0");
    }

    @Override
    protected void initData() {
        EAdapter.Builder.load(DataFactory.factoryFoods())
                .item(R.layout.item_food)
                .bind(new ItemBind<FoodModel>() {
                    @Override
                    public void convert(final EHolder holder, final FoodModel itemData, int position, RecyclerView.Adapter adapter) {
                        holder.setImageResource(R.id.iv_goods_fits_picture, itemData.getPath())
                                .setText(R.id.tv_goods_fits_name, itemData.getName())
                                .setText(R.id.tv_goods_fits_price, "￥:" + itemData.getMoney() + "元")
                                .setOnClick(R.id.iv_goods_fits_add, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        addAction(v);
                                        TextView textView = holder.getView(R.id.tv_goods_fits_num);
                                        int count = Integer.valueOf(textView.getText().toString());

                                        count++;

                                        // 条目选中个数
                                        textView.setText((count) + "");
                                        // 总共选取的个数
                                        int tvGoodFittingNumCount = Integer.valueOf(tvGoodFittingNum.getText().toString());
                                        // 总共选取的个数+1
                                        tvGoodFittingNumCount++;
                                        // 设置显示的个数
                                        tvGoodFittingNum.setText(tvGoodFittingNumCount + "");
                                        // 计算金额
                                        mMaxMoney += itemData.getMoney();
                                        // 计算需要多少钱
                                        tvMoney.setText("￥：" + mMaxMoney + ".0元");
                                    }
                                })
                                .setOnClick(R.id.iv_goods_fits_reduce, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        TextView textView = holder.getView(R.id.tv_goods_fits_num);
                                        int count = Integer.valueOf(textView.getText().toString());
                                        if (count <= 0) {
                                            // 条目选中个数
                                            textView.setText("0");
                                            Toast.makeText(BezierShopActivity.this, "选取个数不能小于0", Toast.LENGTH_SHORT).show();
                                        } else {
                                            count--;
                                            textView.setText((count) + "");
                                            // 总共选取的个数
                                            int tvGoodFittingNumCount = Integer.valueOf(tvGoodFittingNum.getText().toString());

                                            // 总共选取的个数+1
                                            tvGoodFittingNumCount--;
                                            // 设置显示的个数
                                            tvGoodFittingNum.setText(tvGoodFittingNumCount + "");
                                            // 计算金额
                                            mMaxMoney -= itemData.getMoney();
                                            // 计算需要多少钱
                                            tvMoney.setText("￥：" + mMaxMoney + ".0元");
                                        }

                                    }
                                });
                    }
                })
                .into(shopMenu);
    }

    public void addAction(View view) {
        ShoppingView nxHooldeView = new ShoppingView(this);
        int position[] = new int[2];
        view.getLocationInWindow(position);
        nxHooldeView.setStartPosition(new Point(position[0], position[1]));
        ViewGroup rootView = (ViewGroup) getWindow().getDecorView();
        rootView.addView(nxHooldeView);
        int endPosition[] = new int[2];
        tvGoodFittingNum.getLocationInWindow(endPosition);
        nxHooldeView.setEndPosition(new Point(endPosition[0], endPosition[1]));
        nxHooldeView.startBeizerAnimation();
    }

    @Event(R.id.btnSettlement)
    public void btnSettlementClick() {
        Toast.makeText(this, "总共需要支付" + mMaxMoney + "元", Toast.LENGTH_SHORT).show();
    }
}
