package com.wpt.frame.widget;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;


public class MyGallery extends Gallery{

private float gTouchStartX; 
	
    private float gTouchStartY; 
    
	public MyGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override 
    public boolean onInterceptTouchEvent(MotionEvent ev) { 
        int action = ev.getAction(); 
        switch(action){ 
        case MotionEvent.ACTION_DOWN: 
            gTouchStartX = ev.getX(); 
            gTouchStartY = ev.getY(); 
            super.onTouchEvent(ev); 
            break; 
        case MotionEvent.ACTION_MOVE: 
            final float touchDistancesX = Math.abs(ev.getX()-gTouchStartX); 
            final float touchDistancesY = Math.abs(ev.getY()-gTouchStartY); 
            if(touchDistancesY *2 >= touchDistancesX){ 
                return false; 
            }else{ 
                return true; 
            } 
        case MotionEvent.ACTION_CANCEL: 
            break; 
        case MotionEvent.ACTION_UP: 
            break; 
        } 
        return false; 
    }     
	
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, 
            float velocityY) { 
        if(e2.getX() > e1.getX()){ 
            onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null); 
        }else{ 
            onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null); 
        } 
        return false; 
    } 

}
