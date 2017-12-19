package com.demo.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.demo.R;
import com.demo.base.NavigationTitle;
import com.demo.bean.FoodModel;
import com.demo.config.DataFactory;
import com.demo.recycler.CardConfig;
import com.demo.recycler.zuimei.BaseLinearLayoutManager;

import org.adapter.recycler.EAdapter;
import org.adapter.recycler.EHolder;
import org.adapter.recycler.mode.ItemBind;
import org.annotation.ViewById;
import org.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * description：
 * <p>
 * Created by TIAN FENG on 2017/12/7.
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class RecyclerViewActivity extends BaseActivity {

    @ViewById(R.id.recyclerView)
    public RecyclerView recyclerView;

    private List<FoodModel> mDatas = new ArrayList<>();

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_recycler);
    }

    @Override
    protected void initView() {
        new NavigationTitle.Builder(this)
                .setTitle("recycler")
                .setBackClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .immersed()
                .build();
    }

    @Override
    protected void initData() {

        mDatas.addAll(DataFactory.factoryFoods());
        mDatas.addAll(DataFactory.factoryFoods());
        mDatas.addAll(DataFactory.factoryFoods());

        CardConfig.initConfig(this);

        EAdapter.Builder.load(mDatas)
                .item(R.layout.item_move)
                .manager(new BaseLinearLayoutManager(recyclerView))
                .bind(new ItemBind<FoodModel>() {
                    @Override
                    public void convert(final EHolder holder, final FoodModel itemData, int position, RecyclerView.Adapter adapter) {
                        holder.setImageResource(R.id.ivMove, itemData.getPath())
                                .setText(R.id.tvMove, itemData.getName());
                    }
                })
                .into(recyclerView);

    }
}
