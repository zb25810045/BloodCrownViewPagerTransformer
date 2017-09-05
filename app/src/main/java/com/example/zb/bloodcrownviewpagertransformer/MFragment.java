package com.example.zb.bloodcrownviewpagertransformer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * =============================================
 * <p>
 * 版权 ： 北京爱接力科技有限公司
 * <p>
 * 作者 ： iwen
 * <p>
 * 版本 ： 1.0
 * <p>
 * 创建日期 ：on 2017/8/25  1:07
 * <p>
 * 描述 ：
 * <p>
 * 修订历史 ：
 * <p>
 * =============================================
 */

public class MFragment extends Fragment {

    public static final String TAG_COLOR = "color";
    public static final String TAG_CONTENT = "content";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        int color = arguments.getInt(TAG_COLOR);
        String content = arguments.getString(TAG_CONTENT);

        View rootView = inflater.inflate(R.layout.fragment_m, container, false);
        TextView viewContent = (TextView) rootView.findViewById(R.id.id_content);

        rootView.setBackgroundColor(color);
        viewContent.setText(content);

        return rootView;
    }
}
