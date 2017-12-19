package org.permission;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import org.utils.StatusBarUtils;

import java.util.List;

/**
 * description：
 * <p>
 * Created by TIAN FENG on 2017/12/14.
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
class PermissionActivity extends Activity {
    final static int REQUESR_CODE = 65;

    public static void start(Activity context, String... permissions) {
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra("PermissionActivity_permission", permissions);
        context.startActivity(intent);
        context.overridePendingTransition(0, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.statusBarTranslucent(this);
        String[] permission = getIntent().getStringArrayExtra("PermissionActivity_permission");
        requestPermissions(permission, REQUESR_CODE);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != REQUESR_CODE) return;

        // 再次获取没有授予的权限
        List<String> deniedPermissions = PermissionHelper.getDeniedPermissions(this, permissions);

        if (deniedPermissions.size() == 0) {
            // 用户都同意授予了
            PermissionUtils.sCustom.onNext(true);
        } else {
            // 你申请的权限中 有用户不同意的
            PermissionUtils.sCustom.onNext(false);
        }
        finish();
        overridePendingTransition(0, 0);
    }
}
