package com.hoiyen.iats.library;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.paolorotolo.appintro.AppIntroBaseFragment;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.hoiyen.iats.R;

public class IntroFragment extends AppIntroBaseFragment {

    public static IntroFragment newInstance(CharSequence title, CharSequence description,
                                               @DrawableRes int imageDrawable,
                                               @ColorInt int bgColor) {
        return newInstance(title, description, imageDrawable, bgColor, 0, 0);
    }

    public static IntroFragment newInstance(CharSequence title, CharSequence description,
                                               @DrawableRes int imageDrawable, @ColorInt int bgColor,
                                               @ColorInt int titleColor, @ColorInt int descColor) {
        IntroFragment slide = new IntroFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title.toString());
        args.putString(ARG_TITLE_TYPEFACE, null);
        args.putString(ARG_DESC, description.toString());
        args.putString(ARG_DESC_TYPEFACE, null);
        args.putInt(ARG_DRAWABLE, imageDrawable);
        args.putInt(ARG_BG_COLOR, bgColor);
        args.putInt(ARG_TITLE_COLOR, titleColor);
        args.putInt(ARG_DESC_COLOR, descColor);
        slide.setArguments(args);

        return slide;
    }

    public static AppIntroFragment newInstance(CharSequence title, String titleTypeface,
                                               CharSequence description, String descTypeface,
                                               @DrawableRes int imageDrawable,
                                               @ColorInt int bgColor) {
        return newInstance(title, titleTypeface, description, descTypeface, imageDrawable, bgColor,
                0, 0);
    }

    public static AppIntroFragment newInstance(CharSequence title, String titleTypeface,
                                               CharSequence description, String descTypeface,
                                               @DrawableRes int imageDrawable, @ColorInt int bgColor,
                                               @ColorInt int titleColor, @ColorInt int descColor) {
        AppIntroFragment slide = new AppIntroFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title.toString());
        args.putString(ARG_TITLE_TYPEFACE, titleTypeface);
        args.putString(ARG_DESC, description.toString());
        args.putString(ARG_DESC_TYPEFACE, descTypeface);
        args.putInt(ARG_DRAWABLE, imageDrawable);
        args.putInt(ARG_BG_COLOR, bgColor);
        args.putInt(ARG_TITLE_COLOR, titleColor);
        args.putInt(ARG_DESC_COLOR, descColor);
        slide.setArguments(args);

        return slide;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View v = super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(getLayoutId(), container, false);

        //LinearLayout mainLayout = (LinearLayout) v.findViewById(R.id.main);

        //mainLayout.setBackgroundColor(getResources().getColor(R.color.buttonBorder));

        return v;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.library_intro;
    }
}
