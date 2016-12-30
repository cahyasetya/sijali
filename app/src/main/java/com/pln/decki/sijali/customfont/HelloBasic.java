package com.pln.decki.sijali.customfont;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by cahya on 9/3/16.
 */
public class HelloBasic extends EditText implements TextWatcher{
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HelloBasic(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public HelloBasic(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init();
    }

    public HelloBasic(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    public HelloBasic(Context context){
        super(context);
        init();
    }

    private void init(){
        if(!isInEditMode()){
            Typeface tf=Typeface.createFromAsset(getContext().getAssets(), "fonts/hellobasic.ttf");
            setTypeface(tf);
            setAllCaps(false);
        }
        this.addTextChangedListener(this);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void afterTextChanged(Editable s){
        this.setError(null);
    }
}
