package com.flapps.mobile.android.flapps.utils;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

public class ActiveScrollView extends ScrollView {

	Context cx;
 
	public interface IKeyboardChanged {
        void onKeyboardShown();
        void onKeyboardHidden();
    }

    private ArrayList<IKeyboardChanged> keyboardListener = new ArrayList<IKeyboardChanged>();

    public ActiveScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        cx = context;
    }

    public ActiveScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        cx = context;
    }

    public ActiveScrollView(Context context) {
        super(context);
        cx = context;
    }

    public void addKeyboardStateChangedListener(IKeyboardChanged listener) {
        keyboardListener.add(listener);
    }

    public void removeKeyboardStateChangedListener(IKeyboardChanged listener) {
        keyboardListener.remove(listener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int proposedheight = MeasureSpec.getSize(heightMeasureSpec);
        final int actualHeight = getHeight();

        if (actualHeight > proposedheight) {
            notifyKeyboardShown();
        } else if (actualHeight < proposedheight) {
            notifyKeyboardHidden();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void notifyKeyboardHidden() {
        for (IKeyboardChanged listener : keyboardListener) {
            listener.onKeyboardHidden();
        }
    }

    private void notifyKeyboardShown() {
        for (IKeyboardChanged listener : keyboardListener) {
            listener.onKeyboardShown();
        }

        final ScrollView mScrollView = (ScrollView) this;
        final EditText et = (EditText) findViewWithTag("top_form_element");
        this.post(new Runnable() { 
            public void run() { 
                mScrollView.scrollTo(0, et.getTop());
           } 
        });
   }

}