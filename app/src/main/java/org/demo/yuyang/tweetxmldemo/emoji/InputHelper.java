package org.demo.yuyang.tweetxmldemo.emoji;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.widget.EditText;

import org.demo.yuyang.tweetxmldemo.R;

/**
 * Created on 3/28/16.
 */
public class InputHelper {
    public static void backspace(EditText editText) {
        if (editText == null) {
            return;
        }
        KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL,
                0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
        editText.dispatchKeyEvent(event);
    }

    public static int getEmojiResId(String name) {
        Integer res = DisplayRules.getMapAll().get(name);
        if (res != null) {
            return res;
        } else {
            return -1;
        }
    }

    public static Spannable displayEmoji(Resources res, CharSequence s) {
        String str = s.toString();
        Spannable spannable;
        if (s instanceof Spannable) {
            spannable = (Spannable) s;
        } else {
            spannable = new SpannableString(str);
        }

        if (!str.contains(":") && !str.contains("[")) {
            return spannable;
        }

        for (int i = 0; i < str.length(); i++) {
            // [微笑]
            int index1 = str.indexOf("[", i);
            int length1 = str.indexOf("]", index1 + 1);

            // :bowtie:
            int index2 = str.indexOf(":", i);
            int length2 = str.indexOf(":", index2 + 1);

            int bound = (int) res.getDimension(R.dimen.space_20);

            try {
                if (index1 > 0) {
                    // public String substring(int start, int end)
                    // Returns a string containing the given subsequence of this string.
                    // The returned string shares this string's backing array.
                    //
                    // Parameters:
                    // start - the start offset.
                    // end - the end+1 offset.
                    String emojiStr = str.substring(index1, length1 + "]".length());
                    int resId = getEmojiResId(emojiStr);

                    if (resId > 0) {
                        Drawable drawable = res.getDrawable(resId);

                        drawable.setBounds(0, 20, bound, bound + 20);

                        ImageSpan span = new ImageSpan(drawable,
                                ImageSpan.ALIGN_BASELINE);
                        spannable.setSpan(span, index1, length1 + "]".length(),
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                }

                if (index2 > 0) {
                    String emojiStr2 = str.substring(index2, length2 + ":".length());
                    int resId2 = getEmojiResId(emojiStr2);

                    if (resId2 > 0) {
                        Drawable emojiDrawable = res.getDrawable(resId2);
                        emojiDrawable.setBounds(0, 0, bound, bound);

                        ImageSpan imageSpan = new ImageSpan(emojiDrawable, str);
                        spannable.setSpan(imageSpan, index2,
                                length2 + ":".length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            } catch (Exception e) {
            }
        }

        return spannable;
    }

    public static void input2OSC(EditText editText, Emojicon emojicon) {
        if (editText == null || emojicon == null) {
            return;
        }

        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();

        if (start < 0) {
            editText.append(displayEmoji(editText.getResources(),
                    emojicon.getRemote()));
        } else {
            Spannable str = displayEmoji(editText.getResources(),
                    emojicon.getRemote());

            // public abstract Editable replace(int st, int en,
            // java.lang.CharSequence source,
            // int start, int end)
            //
            // Replaces the specified range (st…en) of text in this
            // Editable with a copy of the slice start…end from source.
            editText.getText().replace(Math.min(start, end),
                    Math.max(start, end), str, 0, str.length());
        }
    }
}
