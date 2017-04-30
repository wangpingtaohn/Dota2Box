package com.wpt.dota2box.ui.news.wallpaper;

import java.io.IOException;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.os.Handler;

public class SetWallpaperManager extends ContextWrapper{

	public final static int SET_WALL_PAPER_SUCCESS = 0;
	public final static int SET_WALL_PAPER_FAILURE = 1;

	private static SetWallpaperManager mInstance = null;
	private Handler mSetWallpaperCallback;
	
	public static SetWallpaperManager getInstance(Context mContext, Handler mSetWallpaperCallback){
		if(mInstance == null){
			mInstance = new SetWallpaperManager(mContext, mSetWallpaperCallback);
		}
		return mInstance;
	}
	
	private SetWallpaperManager(Context base, Handler mSetWallpaperCallback) {
		super(base);
		this.mSetWallpaperCallback = mSetWallpaperCallback;
	}

	@Override
	public void setWallpaper(Bitmap bitmap) throws IOException {
		try{
			super.setWallpaper(bitmap);
			mSetWallpaperCallback.obtainMessage(SET_WALL_PAPER_SUCCESS).sendToTarget();
		}catch(Exception e){
			e.printStackTrace();
			mSetWallpaperCallback.obtainMessage(SET_WALL_PAPER_FAILURE).sendToTarget();
		}
	}
	
}
