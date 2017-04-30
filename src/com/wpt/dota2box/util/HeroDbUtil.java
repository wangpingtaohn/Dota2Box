package com.wpt.dota2box.util;

import java.util.ArrayList;
import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HeroDbUtil {

	
	public List<String> getHeroList(int tabIndex) {
		
		List<String> list = new ArrayList<String>();
		String path = DBManager.DB_PATH + "/" + DBManager.DB_NAME;
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(path, null);
		Cursor cur = database.rawQuery("SELECT heroid,type FROM heroid WHERE type = " + tabIndex, null);
		if (cur != null) {
            if (cur.moveToFirst()) {
                do {
                    String heroId = cur.getString(cur.getColumnIndex("heroid"));
                    list.add(heroId);
                } while (cur.moveToNext());
            }
            cur.close();
        } 
		return list;
	}
}
