package org.skin;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.ArrayMap;

import org.skin.attr.SkinView;
import org.skin.callback.ISkinChangeListener;
import org.skin.utils.SkinPerUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Description :
 * Created : TIAN FENG
 * Date : 2017/4/6
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class SkinManager {
    // 单例对象
    private static SkinManager mInstance;
    // List<SkinView>存储对象
    private Map<ISkinChangeListener,List<SkinView>> mSkinViews;
    // 上下文
    private Context mContext;
    // 皮肤资源
    private SkinResource mSkinResource;

    // 初始化
    static {
        mInstance = new SkinManager();
    }

    /**
     * 初始化
     *
     * 需要判断用户是否加载过皮肤，如果加载过皮肤，就用加载过的皮肤
     */
    public void init(Context context){
        this.mContext = context;
        // 读取皮肤状态(拿路径)防止皮肤被任意删除，做一些措施
        String skinPath = SkinPerUtils.getInstance(mContext).getSkinPath();
        // 拿到这个文件
        File file = new File(skinPath);
        // 判断这个文件是否存在
        if (!file.exists()){
            // 不存在,路径存空
            SkinPerUtils.getInstance(mContext).clearSkinInfo();
            return;
        }
        // 最好做一下  能不能获取到包名
        String packageName = context.getPackageManager().getPackageArchiveInfo(
                skinPath, PackageManager.GET_ACTIVITIES).packageName;

        if(TextUtils.isEmpty(packageName)){
            SkinPerUtils.getInstance(mContext).clearSkinInfo();
            return;
        }
        // TODO 最好校验签名


        // 做一些初始化的工作
        mSkinResource = new SkinResource(mContext,skinPath);

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private SkinManager(){
        mSkinViews = new ArrayMap<>();
    }

    /**
     * 获取实例对象
     */
    public static SkinManager getInstance() {
        return mInstance;
    }

    /**
     * 获取skinViews
     */
    public List<SkinView> getSkinViews(ISkinChangeListener skinChangeListener) {
        return mSkinViews.get(skinChangeListener);
    }

    /**
     * 注册添加
     */
    public void registerSkinView(ISkinChangeListener skinChangeListener,List<SkinView> skinViews) {
        mSkinViews.put(skinChangeListener,skinViews);
    }

    /**
     * 恢复默认
     */
    public void reStoreDefault(){
        // 当前运行的皮肤路径
        String currentPath = mContext.getPackageResourcePath();
        // 开始执行
        skin(currentPath);
    }

    /**
     * 换肤方法
     * @param skinPath 皮肤路径
     */
    public void skin(String skinPath){
        // 获取当前加载的皮肤路径
        String loadSkinPath = SkinPerUtils.getInstance(mContext).getSkinPath();

        // 如果加载的皮肤一样直接返回
        if (skinPath.equals(loadSkinPath)){
            // 不执行后面方法
            return ;
        }

        // 可以加载
        // 保存换肤状态
        SkinPerUtils.getInstance(mContext).saveSkinPath(skinPath);

        // 初始化皮肤资源
        mSkinResource = new SkinResource(mContext,skinPath);
        // 遍历拿到key
        Set<ISkinChangeListener> keys = mSkinViews.keySet();
        // 遍历
        for (ISkinChangeListener listener:keys){
            // 拿到value值
            List<SkinView> skinViews = mSkinViews.get(listener);
            // 遍历改变皮肤
            for (SkinView skinView:skinViews){
                // 调用skinview加载皮肤
                skinView.skin();
                // 回调调用者
                listener.changeSkin(mSkinResource);
            }
        }
    }

    /**
     * 获取当前资源管理
     */
    public SkinResource getSkinResources() {
        return mSkinResource;
    }

    /**
     * 销毁执行方式
     */
    public void onDestroy(ISkinChangeListener skinChangeListener){
        // 移除activity的引用 避免内存泄露
        mSkinViews.remove(skinChangeListener);
    }
}
