package com.example.zb.bloodcrownviewpagertransformer.transformer;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * =============================================
 * <p>
 * 版权 ： 北京爱接力科技有限公司
 * <p>
 * 作者 ： iwen
 * <p>
 * 版本 ： 1.0
 * <p>
 * 创建日期 ：on 2017/8/25  1:37
 * <p>
 * 描述 ：
 * <p>
 * 修订历史 ：
 * <p>
 * =============================================
 */

public class AlpheScaleTransformer implements ViewPager.PageTransformer {

    private float minAlphe = 0.3f;
    private float minScale = 0.7f;

    @Override
    public void transformPage(View page, float position) {

        if (position < -1 || position > 1) {
            page.setAlpha(1);
            page.setTranslationX(0);
            page.setScaleX(1);
            page.setScaleY(1);
            return;
        }

        if (position >= -1 && position <= 0) {
            return;
        }

        if (position > 0 && position <= 1) {
            page.setAlpha(minAlphe + (1 - minAlphe) * (1 - Math.abs(position)));
            page.setScaleX(minScale + (1 - minScale) * (1 - Math.abs(position)));
            page.setScaleY(minScale + (1 - minScale) * (1 - Math.abs(position)));
            page.setPivotX(page.getWidth() / 2);
            page.setPivotY(page.getHeight() / 2);
            page.setTranslationX(page.getWidth() * -position);

        }

    }

}
