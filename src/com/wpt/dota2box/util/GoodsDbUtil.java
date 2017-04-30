package com.wpt.dota2box.util;

import java.util.ArrayList;
import java.util.List;

import com.wpt.dota2box.model.Goods;
import com.wpt.dota2box.model.GoodsHeCheng;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GoodsDbUtil {

	public List<List<String>> getGoodsList(int tabIndex) {

		List<List<String>> list = new ArrayList<List<String>>();
		String path = DBManager.DB_PATH + "/" + DBManager.DB_NAME;
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(path,
				null);
		int index = 1;
		int count = 5;
		if (tabIndex != 0) {
			index = 5;
			count = 10;
		}
		for (int i = index; i <= count; i++) {
			if (i == 5 && tabIndex == 0) {
				i = 11;
			}
			List<String> typeList = new ArrayList<String>();
			Cursor cur = database.rawQuery(
					"SELECT image FROM items WHERE type = " + i + " ORDER BY ord", null);
			if (cur != null) {
				if (cur.moveToFirst()) {
					do {
						String image = cur.getString(cur
								.getColumnIndex("image"));
						typeList.add(image);
					} while (cur.moveToNext());
				}
				cur.close();
			}
			list.add(typeList);
		}
		return list;
	}

	public Goods getGoodsInfo(String image) {

		String path = DBManager.DB_PATH + "/" + DBManager.DB_NAME;
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(path,
				null);
		Cursor cur = database.rawQuery(
				"SELECT fujia,jiage,mingcheng,shiyong,shuoming,itemsId FROM wuping WHERE touxiang = "
						+ "\"" + image + "\"", null);
		Goods goods = null;
		if (cur != null) {
			goods = new Goods();
			if (cur.moveToFirst()) {
				do {
					String fujia = cur.getString(cur.getColumnIndex("fujia"));
					String jiage = cur.getString(cur.getColumnIndex("jiage"));
					String mingcheng = cur.getString(cur.getColumnIndex("mingcheng"));
					String shiyong = cur.getString(cur.getColumnIndex("shiyong"));
					String shuoming = cur.getString(cur.getColumnIndex("shuoming"));
					String itemsId = cur.getString(cur.getColumnIndex("itemsId"));
					goods.setJiage(jiage);
					goods.setQuanMing(mingcheng);
					goods.setShiyong(shiyong);
					goods.setShuoming(shuoming);
					goods.setFujia(fujia);
					goods.setItemsId(itemsId);
				} while (cur.moveToNext());
			}
			cur.close();
		}
		return goods;
	}
	public List<GoodsHeCheng> getGoodsHeCheng(String itemsId) {
		
		List<GoodsHeCheng> list = null;
		String path = DBManager.DB_PATH + "/" + DBManager.DB_NAME;
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(path,
				null);
		Cursor cur = database.rawQuery(
				"SELECT ritemsId,touxiang,mingcheng FROM hecheng WHERE itemsId = "
						+ "\"" + itemsId + "\"", null);
		if (cur != null) {
			list = new ArrayList<GoodsHeCheng>();
			if (cur.moveToFirst()) {
				do {
					GoodsHeCheng ghc = new GoodsHeCheng();
					String ritemsId = cur.getString(cur.getColumnIndex("ritemsId"));
					String touxiang = cur.getString(cur.getColumnIndex("touxiang"));
					String mingcheng = cur.getString(cur.getColumnIndex("mingcheng"));
					ghc.setMingcheng(mingcheng);
					ghc.setTouxiang(touxiang);
					ghc.setRitemsId(ritemsId);
					list.add(ghc);
				} while (cur.moveToNext());
			}
			cur.close();
		}
		return list;
	}
}
