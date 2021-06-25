package com.abatra.android.wheelie.core.widget;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.abatra.android.wheelie.core.res.text.Text;

public final class Toaster {

    public static void toastLongInCenter(Context context, Text message) {
        toastWithGravity(context, message, Toast.LENGTH_LONG, Gravity.CENTER);
    }

    private static void toastWithGravity(Context context, Text message, int duration, int gravity) {
        Toast toast = makeToast(context, message, duration);
        toast.setGravity(gravity, 0, 0);
        toast.show();
    }

    private static Toast makeToast(Context context, Text message, int duration) {
        return Toast.makeText(context, message.getString(context), duration);
    }

    public static void toastLong(Context context, Text message) {
        toast(context, message, Toast.LENGTH_LONG);
    }

    public static void toastLongAtTop(Context context, Text message) {
        toastWithGravity(context, message, Toast.LENGTH_LONG, Gravity.TOP);
    }

    private static void toast(Context context, Text text, int duration) {
        makeToast(context, text, duration).show();
    }

    public static void toastShortAtTop(Context context, Text message) {
        toastWithGravity(context, message, Toast.LENGTH_SHORT, Gravity.TOP);
    }

    public static void toastShortInCenter(Context context, Text message) {
        toastWithGravity(context, message, Toast.LENGTH_SHORT, Gravity.CENTER);
    }

    public static void toastShort(Context context, Text message) {
        toast(context, message, Toast.LENGTH_SHORT);
    }
}
