package com.kelin.mvvmlight.zhihu.utils;

import android.graphics.Color;
import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;

/**
 * Created by kelin on 16-4-28.
 */
public class AlphaForegroundColorSpan extends ForegroundColorSpan {
    private float mAlpha;

    public AlphaForegroundColorSpan(int color) {
        super(color);
    }

    public AlphaForegroundColorSpan(Parcel src) {
        super(src);
        mAlpha = src.readFloat();
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeFloat(mAlpha);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(getAlphaColor());
    }

    public void setAlpha(float alpha) {
        mAlpha = alpha;
    }

    public float getAlpha() {
        return mAlpha;
    }

    private int getAlphaColor() {
        int foregroundColor = getForegroundColor();
        return Color.argb((int) (mAlpha), Color.red(foregroundColor), Color.green(foregroundColor), Color.blue(foregroundColor));
    }
}
