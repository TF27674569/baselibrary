package org.skin.support;

import android.content.Context;
import android.util.AttributeSet;

import org.skin.attr.SkinAttr;
import org.skin.attr.SkinType;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 * Created : TIAN FENG
 * Date : 2017/4/6
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class SkinSupport {
    /**
     * 获取Skin的属性
     */
    public static List<SkinAttr> getSkinAttrs(Context context, AttributeSet attrs) {
        // 创建一个属性集合
        List<SkinAttr> skinAttrs = new ArrayList<>();
        // 拿到当前view设置的属性个数 eg：textColor textSize
        int attrLength = attrs.getAttributeCount();
        // 遍历当前属性 获取名称和对应id值
        for (int index=0;index<attrLength;index++){
            // 获取名称         eg:textColor
            String attrName = attrs.getAttributeName(index);
            // 属性对应的值     eg:#ffffff or @int(id)
            String attrValue = attrs.getAttributeValue(index);
            // 根据名称获取我们SkinType里面对应的Type
            SkinType skinType = getSkinType(attrName);
            // 判断有没有对应的属性
            if (skinType!=null){
                // 获取资源值的名称
                String resName = getResName(context,attrValue);
                // 创建一个SkinAttr
                SkinAttr skinAttr = new SkinAttr(skinType,resName);
                // 添加到集合
                skinAttrs.add(skinAttr);
            }
        }
        return skinAttrs;
    }


    /**
     * 获取资源值得名称
     */
    private static String getResName(Context context, String attrValue) {
        // 如果以@开头 @int
        if (attrValue.startsWith("@")){
            // 截取
            attrValue = attrValue.substring(1);
            // 获取id的值
            int resId = Integer.parseInt(attrValue);
            // 根据Id得到名称
            return context.getResources().getResourceEntryName(resId);
        }
        // 还有颜色#ffffff 我们不能更改 没有对应的资源id名称
        return null;
    }


    /**
     * 根据名称获取SkinType
     */
    private static SkinType getSkinType(String attrName) {
        // 拿到SkinType中的所有类型
        SkinType[] skinTypes = SkinType.values();
        // 遍历
        for (SkinType skinTpye:skinTypes) {
            // 拿到资源名称（skinType中含有的）
            String resName = skinTpye.getResName();
            // 判断 SkinType中是否含有此属性
            if (attrName.equals(resName)){
                // 如果相同，返回对应类型
                return skinTpye;
            }
        }

        return null;
    }
}
