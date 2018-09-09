package com.example.aliothman.dicref.utils;

import android.content.Context;

import com.kaopiz.kprogresshud.KProgressHUD;

public class DialogUtils {

    private static KProgressHUD createProgressDialog(Context context, String label, String labelDetails, boolean isCancelable) {
        KProgressHUD kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
        if (label != null) {
            kProgressHUD.setLabel(label);
        }
        if (labelDetails != null) {
            kProgressHUD.setDetailsLabel(labelDetails);
        }
        kProgressHUD.setCancellable(isCancelable)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        return kProgressHUD;
    }

    public static KProgressHUD createNotCancelableProgressDialog(Context context, String label) {
        return createProgressDialog(context, label, null, false);
    }

}
