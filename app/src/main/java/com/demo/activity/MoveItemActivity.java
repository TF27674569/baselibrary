package com.demo.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.R;
import com.demo.base.NavigationTitle;
import com.demo.bean.FoodModel;
import com.demo.config.DataFactory;

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
 * Created by TIAN FENG on 2017/12/6.
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class MoveItemActivity extends BaseActivity {

    // recyclerView 一行显示的个数
    private static final int RECYCLER_LINE_COUNT = 3;

    @ViewById(R.id.recyclerView1)
    public RecyclerView recyclerView1;
    @ViewById(R.id.recyclerView2)
    public RecyclerView recyclerView2;


    private List<FoodModel> mData1 = new ArrayList<>(), mData2 = new ArrayList<>();


    private EAdapter mAdapter1, mAdapter2;


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_move_item);
    }

    @Override
    protected void initView() {
        new NavigationTitle.Builder(this)
                .setTitle("列表")
                .setBackClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .immersed()
                .build();

        recyclerView2.post(new Runnable() {
            @Override
            public void run() {
                itemWidth = recyclerView2.getMeasuredWidth() / RECYCLER_LINE_COUNT;
            }
        });
    }


    int itemWidth;

    @Override
    protected void initData() {
        List<FoodModel> datas = DataFactory.factoryFoods();
        for (int i = 0; i < datas.size(); i++) {
            if (i < datas.size() / 2) {
                mData1.add(datas.get(i));
            } else {
                mData2.add(datas.get(i));
            }
        }

        bindRecycler1();
        bindRecycler2();
    }


    // 每个Item的宽高
    private ViewGroup.LayoutParams mItemLayoutParams;

    int[] recycler1PointFInWindow = new int[2];
    int[] recycler2PointFInWindow = new int[2];

    boolean isRecycler1 = false;

    private void bindRecycler1() {
        // 获取recyclerView1在Window的位置
        recyclerView1.getLocationInWindow(recycler1PointFInWindow);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, RECYCLER_LINE_COUNT);
        mAdapter1 = EAdapter.Builder.load(mData1)
                .item(R.layout.item_move)
                .manager(gridLayoutManager)
                .bind(new ItemBind<FoodModel>() {
                    @Override
                    public void convert(EHolder holder, final FoodModel itemData, final int position, RecyclerView.Adapter adapter) {
                        // 拿到Item的参数，添加到decortorView中需要
                        mItemLayoutParams = holder.itemView.getLayoutParams();
                        holder.setImageResource(R.id.ivMove, itemData.getPath())
                                .setText(R.id.tvMove, itemData.getName());
                        // 长按事件
                        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                // 计算终点位置
                                PointF endPoint = endPointF(false);
                                // 是否点击的1
                                isRecycler1 = true;
                                // 获取起始位置
                                int[] pos = new int[2];
                                v.getLocationInWindow(pos);
                                mData1.remove(position);

                                // recycler2增加的数据
                                mData2.add(itemData);

                                // 开始移动
                                moveAnima(new PointF(pos[0], pos[1]), endPoint, itemData);
                                return true;
                            }
                        });
                    }
                })
                .into(recyclerView1);
    }


    private void bindRecycler2() {
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, RECYCLER_LINE_COUNT);
        mAdapter2 = EAdapter.Builder.load(mData2)
                .item(R.layout.item_move)
                .manager(gridLayoutManager)
                .bind(new ItemBind<FoodModel>() {
                    @Override
                    public void convert(final EHolder holder, final FoodModel itemData, final int position, RecyclerView.Adapter adapter) {
                        // 拿到Item的参数，添加到decortorView中需要
                        holder.setImageResource(R.id.ivMove, itemData.getPath())
                                .setText(R.id.tvMove, itemData.getName());

                        // 长按事件
                        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                PointF endPoint = endPointF(true);
                                isRecycler1 = false;
                                mData2.remove(position);
                                mData1.add(itemData);
                                int[] pos = new int[2];
                                v.getLocationInWindow(pos);
                                moveAnima(new PointF(pos[0], pos[1]), endPoint, itemData);
                                return true;
                            }
                        });
                    }
                })
                .into(recyclerView2);
    }

    /**
     * 计算结束点
     *
     * @param isRecycler1
     * @return
     */
    private PointF endPointF(boolean isRecycler1) {
        recyclerView1.getLocationInWindow(recycler1PointFInWindow);
        recyclerView2.getLocationInWindow(recycler2PointFInWindow);
        PointF pointF = new PointF();
        int position = isRecycler1 ? mData1.size() - 1 : mData2.size() - 1;
        // 第几行
        int line = position / RECYCLER_LINE_COUNT;
        // 第几个
        int location = position % RECYCLER_LINE_COUNT;

        // 判断是否是此行的最后一个
        if (location != RECYCLER_LINE_COUNT - 1) {
            // recycler1的位置 = 最后一个Item  X的位置 + 一个Item宽
            float x = (location + 1) * itemWidth;
            // y = line * itemHeight;
            float y = (line) * mItemLayoutParams.height + (isRecycler1 ? recycler1PointFInWindow[1] : recycler2PointFInWindow[1]);
            pointF.set(x, y);
        } else {
            if (isRecycler1) {
                pointF.set(recycler1PointFInWindow[0], (line + 1) * mItemLayoutParams.height + recycler1PointFInWindow[1]);
            } else {
                pointF.set(recycler2PointFInWindow[0], (line + 1) * mItemLayoutParams.height + recycler2PointFInWindow[1]);
            }
        }
        return pointF;
    }


    private void moveAnima(final PointF startPon, PointF endPoint, FoodModel itemData) {

        // 拿到Item
        final View view = View.inflate(this, R.layout.item_move, null);
        // 绑定数据
        ImageView imageView = (ImageView) view.findViewById(R.id.ivMove);
        imageView.setImageResource(itemData.getPath());
        TextView textView = (TextView) view.findViewById(R.id.tvMove);
        textView.setText(itemData.getName());

        // 设置参数
        view.setLayoutParams(mItemLayoutParams);

        // 拿到所有布局的decortorView
        final ViewGroup rootView = (ViewGroup) getWindow().getDecorView();
        // 添加
        rootView.addView(view);

        // 设置位置
        view.setX(startPon.x);
        view.setY(startPon.y);

        // 动画
        ValueAnimator animator = ValueAnimator.ofObject(new PointFEvaluator(), startPon, endPoint);

        // 持续时间插值器
        animator.setDuration(1000).setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointf = (PointF) animation.getAnimatedValue();
                // 移动view
                view.setX(pointf.x);
                view.setY(pointf.y);
            }
        });

        // 监听结束
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // 移出view
                rootView.removeView(view);

                // 更新数据
                mAdapter2.notifyDataSetChanged();
                mAdapter1.notifyDataSetChanged();
            }
        });

        animator.start();
    }

    // 线性移动估值器
    private class PointFEvaluator implements TypeEvaluator<PointF> {

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            float x = startValue.x + (endValue.x - startValue.x) * fraction;
            float y = startValue.y + (endValue.y - startValue.y) * fraction;
            return new PointF(x, y);
        }
    }
}
