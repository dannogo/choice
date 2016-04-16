package com.elantix.dopeapp;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by oleh on 4/14/16.
 */
public class FragmentSplashFourth extends Fragment {
    private View mFragmentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_splash_fourth, container, false);

        TextView shadedText = (TextView) mFragmentView.findViewById(R.id.splash_4_shaded_text);
//        Shader textShader=new LinearGradient(shadedText.getWidth()/2, 0, shadedText.getWidth()/2, shadedText.getHeight(),
//                new int[]{R.color.white_text_color, R.color.transparent_white},
//                new float[]{0, 1}, Shader.TileMode.CLAMP);
//        shadedText.getPaint().setShader(textShader);

        return mFragmentView;
    }
}
