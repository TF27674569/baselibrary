package org.adapter.recycler;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.adapter.recycler.listener.ItemClickListener;
import org.adapter.recycler.listener.ItemLongClickListener;
import org.adapter.recycler.mode.EImageLoader;
import org.adapter.recycler.mode.ItemBind;
import org.adapter.recycler.mode.MultiTypeSupport;

import java.util.List;

/**
 * description：万能适配器 链式调用
 * <p/>
 * Created by TIAN FENG on 2017/9/30
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class EAdapter extends RecyclerView.Adapter<EHolder> {

    /**
     * 设置图片的加载规则 建议在application中
     */
    public static void setImageLoader(EImageLoader imageLoader) {
        EHolder.setImageLoader(imageLoader);
    }

    private Builder.Params P;

    private EAdapter(Builder.Params P) {
        this.P = P;
    }

    private EAdapter apply() {
        //默认为listview 样式
        P.recyclerView.setLayoutManager(P.manager == null ? new LinearLayoutManager(P.recyclerView.getContext()) : P.manager);
        P.recyclerView.setAdapter(this);
        return this;
    }

    @Override
    public EHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /**
         * 三目运算 为了适配多布局
         */
        View itemView = LayoutInflater.from(parent.getContext()).inflate(P.typeSupport != null ? viewType : P.layoutId, parent, false);
        return new EHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EHolder holder, final int position) {
        if (P.bind != null) {
            // 数据绑定规则回调
            P.bind.convert(holder, P.datas.get(position), position,EAdapter.this);

            // 设置Item点击事件
            if (P.itemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        P.itemClickListener.onItemClick(position);
                    }
                });
            }

            // 设置Item长按事件
            if (P.itemLongClickListener != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return P.itemLongClickListener.onItemLongClick(position,v);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return P.datas == null ? 0 : P.datas.size();
    }

    @Override
    public int getItemViewType(int position) {
        // 多布局支持
        if (P.typeSupport != null) {
            try {
                return P.typeSupport.getLayoutId(P.datas.get(position), position);
            } catch (Throwable e) {
                throw new IllegalArgumentException("请保证函数typeSupport与函数load泛型参数一致.");
            }
        }
        return super.getItemViewType(position);
    }

    /**
     * 构造器
     */
    public static class Builder {
        // 参数类
        private Params P;

        private Builder(List datas) {
            P = new Params(datas);
        }

        /**
         * 入口 赋值加载数据
         *
         * @param datas 加载数据
         * @return
         */
        public static Builder load(List datas) {
            return new Builder(datas);
        }

        /**
         * recycle布局 在有显示规则typeSupport下此项设置无用
         *
         * @param layoutId 布局id
         * @return
         */
        public Builder item(int layoutId) {
            P.layoutId = layoutId;
            return this;
        }

        /**
         * 多布局适配
         *
         * @param typeSupport 适配规则
         * @return
         */
        public <T> Builder typeSupport(MultiTypeSupport<T> typeSupport) {
            P.typeSupport = typeSupport;
            return this;
        }


        /**
         * recycle  LayoutManager
         *
         * @param manager
         * @return
         */
        public Builder manager(RecyclerView.LayoutManager manager) {
            P.manager = manager;
            return this;
        }


        /**
         * recycle  LayoutManager
         *
         * @param bind 数据绑定实现
         * @return
         */
        public <T> Builder bind(ItemBind<T> bind) {
            P.bind = bind;
            return this;
        }

        /**
         * item 点击
         *
         * @param listener
         * @return
         */
        public Builder itemClick(ItemClickListener listener) {
            P.itemClickListener = listener;
            return this;
        }

        /**
         * item 长按
         *
         * @param listener
         * @return
         */
        public Builder itemLongClick(ItemLongClickListener listener) {
            P.itemLongClickListener = listener;
            return this;
        }

        /**
         * 加载到控件
         *
         * @param recyclerView
         * @return
         */
        public EAdapter into(RecyclerView recyclerView) {
            P.recyclerView = recyclerView;
            return new EAdapter(P).apply();
        }


        class Params<T> {
            // 加载数据
            List<T> datas;
            // 单布局id
            int layoutId;
            // 显示LayoutManager
            RecyclerView.LayoutManager manager;
            // recycle控件
            RecyclerView recyclerView;
            // 绑定规则
            ItemBind<T> bind;
            // 条目点击
            ItemClickListener itemClickListener;
            // 条目长按
            ItemLongClickListener itemLongClickListener;
            // 多布局适配规则
            MultiTypeSupport<T> typeSupport;
            // 构造器
            Params(List<T> datas) {
                this.datas = datas;
            }
        }
    }
}
