package com.pln.decki.sijali.customfont;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by cahya on 9/4/16.
 */
public class HelloBasicTextView extends TextView {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HelloBasicTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public HelloBasicTextView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init();
    }

    public HelloBasicTextView(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }


    public HelloBasicTextView(Context context){
        super(context);
        init();
    }

    private void init(){
        if(!isInEditMode()){
            Typeface tf=Typeface.createFromAsset(getContext().getAssets(), "fonts/hellobasic.ttf");
            setTypeface(tf);
            this.setMovementMethod(new ScrollingMovementMethod());
        }
    }
}
