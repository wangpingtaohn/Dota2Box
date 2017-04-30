package com.wpt.dota2box.adapter;

import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wpt.dota2box.R;
import com.wpt.dota2box.model.HeroSkill;

public class HeroDetailSkillAdapter extends BaseAdapter {

	private Context mContext;
	
	private List<HeroSkill> datas;
	
	public HeroDetailSkillAdapter(Context context,List<HeroSkill> list) {
		mContext = context;
		datas = list;
	}
	
	@Override
	public int getCount() {
		if (datas != null && datas.size() > 0) {
			return datas.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView = View.inflate(mContext, R.layout.hero_detail_skill_item,
					null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.hero_detail_skill_item_avatar);
			holder.textView1 = (TextView) convertView
					.findViewById(R.id.hero_detail_skill_item_textView1);
			holder.textView2 = (TextView) convertView
					.findViewById(R.id.hero_detail_skill_item_textView2);
			convertView.setTag(holder);
		}
		HeroSkill heroSkill = datas.get(position);
		if (heroSkill != null) {
			int imageId = mContext.getResources().getIdentifier(heroSkill.getImage(), "drawable", mContext.getApplicationInfo().packageName);
			holder.imageView.setImageResource(imageId);
			String mofaStr = heroSkill.getMofa() == null?"":heroSkill.getMofa() + "\n" ;
			String shanghaiStr = heroSkill.getShanghai() == null?"":heroSkill.getShanghai();
			String textStr = "名称:" + heroSkill.getName() + "\n" + mofaStr + shanghaiStr;
			holder.textView1.setText(textStr);
			holder.textView2.setText(heroSkill.getText());
		}
		
		return convertView;
	}

	private class ViewHolder {
		ImageView imageView;
		TextView textView1;
		TextView textView2;
	}
}
