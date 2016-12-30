package com.pln.decki.sijali.customfont;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.pln.decki.sijali.R;

/**
 * Created by cahya on 9/3/16.
 */
public class KOActivista extends TextView {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public KOActivista(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(context,attrs,defStyleAttr,defStyleRes);
        init(attrs);
    }

    public KOActivista(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        init(attrs);
    }

    public KOActivista(Context context, AttributeSet attrs){
        super(context, attrs);
        init(attrs);
    }

    public KOActivista(Context context){
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs){
        if(!isInEditMode()){
            Typeface myTypeface=Typeface.createFromAsset(getContext().getAssets(), "fonts/activista.ttf");
            setTypeface(myTypeface);
        }
    }
}
