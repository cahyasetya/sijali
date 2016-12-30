package com.pln.decki.sijali.customfont;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by cahya on 9/3/16.
 */
public class HelloBasicButton extends Button {
    public HelloBasicButton(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        init();
    }

    public HelloBasicButton(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    public HelloBasicButton(Context context){
        super(context);
        init();
    }

    private void init(){
        if(!isInEditMode()){
            Typeface tf=Typeface.createFromAsset(getContext().getAssets(), "fonts/hellobasic.ttf");
            setTypeface(tf);
        }
    }
}
