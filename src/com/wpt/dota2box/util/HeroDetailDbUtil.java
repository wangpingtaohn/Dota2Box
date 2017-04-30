package com.wpt.dota2box.util;

import java.util.ArrayList;
import java.util.List;

import com.wpt.dota2box.model.Hero;
import com.wpt.dota2box.model.HeroSkill;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HeroDetailDbUtil {

	public Hero getHeroDetail(String heroId) {

		String path = DBManager.DB_PATH + "/" + DBManager.DB_NAME;
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(path,
				null);
		Cursor cur = database
				.rawQuery(
						"SELECT quanming, jiancheng, tishi, liliang, zhili,zhushuxing, minjie, gushi FROM herosbean WHERE heroId = "
								+ "\"" + heroId + "\"", null);
		Hero hero = null;
		if (cur != null) {
			if (cur.moveToFirst()) {
				hero = new Hero();
				do {
					String quanming = cur.getString(cur
							.getColumnIndex("quanming"));
					String jiancheng = cur.getString(cur
							.getColumnIndex("jiancheng"));
					String tishi = cur.getString(cur.getColumnIndex("tishi"));
					String zhushuxing = cur.getString(cur
							.getColumnIndex("zhushuxing"));
					String zhili = cur.getString(cur.getColumnIndex("zhili"));
					String liliang = cur.getString(cur
							.getColumnIndex("liliang"));
					String minjie = cur.getString(cur.getColumnIndex("minjie"));
					String gushi = cur.getString(cur.getColumnIndex("gushi"));
					String chengzhang = "";
					if (liliang != null && !"".equals(liliang)
							&& liliang.contains("+")) {
						int index = liliang.indexOf("+");
						chengzhang = liliang.substring(index + 1);
						liliang = liliang.substring(0, index);
					}
					if (minjie != null && !"".equals(minjie)
							&& minjie.contains("+")) {
						int index = minjie.indexOf("+");
						chengzhang = minjie.substring(index + 1);
						minjie = minjie.substring(0, index);
					}
					if (zhili != null && !"".equals(zhili)
							&& zhili.contains("+")) {
						int index = zhili.indexOf("+");
						chengzhang = zhili.substring(index + 1);
						zhili = zhili.substring(0, index);
					}
					hero.setMinJie(minjie);
					hero.setQuanMing(quanming);
					hero.setJianCheng(jiancheng);
					hero.setTishi(tishi);
					hero.setZhuShuXing(zhushuxing);
					hero.setZhiLi(zhili);
					hero.setLiLiang(liliang);
					hero.setChengZhang(chengzhang);
					hero.setGushi(gushi);

				} while (cur.moveToNext());
			}
			cur.close();
		}
		return hero;
	}
	public List<HeroSkill> getHeroSkillList(String heroId) {
		
		List<HeroSkill> list = new ArrayList<HeroSkill>();
		String path = DBManager.DB_PATH + "/" + DBManager.DB_NAME;
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(path,
				null);
		Cursor cur = database
				.rawQuery(
						"SELECT image, jineng_name, mofa, jineng_shanghai, text FROM jineng WHERE hero=" + "\"" + heroId + "\""  + "ORDER BY ord", null);
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					HeroSkill heroSkill = new HeroSkill();
					String image = cur.getString(cur
							.getColumnIndex("image"));
					String jineng_name = cur.getString(cur
							.getColumnIndex("jineng_name"));
					String mofa = cur.getString(cur.getColumnIndex("mofa"));
					String shanghai = cur.getString(cur.getColumnIndex("jineng_shanghai"));
					String text = cur.getString(cur.getColumnIndex("text"));
					heroSkill.setImage(image);
					heroSkill.setName(jineng_name);
					heroSkill.setMofa(mofa);
					heroSkill.setShanghai(shanghai);
					heroSkill.setText(text);
					list.add(heroSkill);
				} while (cur.moveToNext());
			}
			cur.close();
		}
		return list;
	}
}
