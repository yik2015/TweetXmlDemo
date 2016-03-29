package org.demo.yuyang.tweetxmldemo.util;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import org.demo.yuyang.tweetxmldemo.AppContext;

/**
 * Created on 3/29/16.
 */
public class TypefaceUtils {

    private static Typeface getTypeface() {
        Context context = AppContext.getInstance();

        Typeface font = Typeface.createFromAsset(context.getAssets(),
                "fontawesome-webfont.ttf");
        return font;
    }

    public static void setTypeface(TextView tv, int textId) {
        setTypeface(tv, AppContext.getInstance().getString(textId));
    }

    public static void setTypeface(TextView tv, String text) {
        if (StringUtils.isEmpty(text)) {
            return;
        }
        tv.setText(text);
        setTypeface(tv);
    }

    public static void setTypeface(TextView textView) {
        textView.setTypeface(getTypeface());
    }

    public static void setTypeFaceWithText(TextView tv, int faRes,
                                           String text) {
        String lastText = AppContext.getInstance().getResources().
                getString(faRes) + " " + text;

        setTypeface(tv, lastText);
    }
}
