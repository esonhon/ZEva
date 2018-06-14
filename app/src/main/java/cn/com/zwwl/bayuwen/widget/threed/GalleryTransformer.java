package cn.com.zwwl.bayuwen.widget.threed;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

public class GalleryTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View view, float position) {
        float scale = 0.5f;
        float scaleValue = 1 - Math.abs(position) * scale;
        Log.e("sssssssss",Math.abs(position) +"");
        view.setScaleX(scaleValue);
        view.setScaleY(scaleValue);
        view.setAlpha(scaleValue);
        view.setPivotX(view.getWidth() * (1 - position - (position > 0 ? 1 : -1) * 0.75f) * scale);
        view.setPivotY(view.getHeight() / 2);

    }
}