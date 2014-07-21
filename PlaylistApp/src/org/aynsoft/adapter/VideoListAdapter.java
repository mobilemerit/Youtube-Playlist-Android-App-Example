package org.aynsoft.adapter;

import java.util.List;
import java.util.Random;

import org.aynsoft.java.VideoDetail;
import org.aynsoft.lazy.ImageLoader;
import org.aynsoft.playList.R;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class VideoListAdapter extends BaseAdapter {

	public List<VideoDetail> itemsList;
	Activity a;
	LayoutInflater inflater;
	Paint p;
	Random color;
	ImageLoader loader;

	public VideoListAdapter(Activity activity, List<VideoDetail> movieInfoList) {
		this.a = activity;
		itemsList = movieInfoList;
		inflater = LayoutInflater.from(activity);
		p = new Paint();
		loader=new ImageLoader(a);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemsList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return itemsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.movie_listview_item,
					parent, false);
			holder.v = (View) convertView.findViewById(R.id.listindicator);
			holder.title = (TextView) convertView
					.findViewById(R.id.movie_title);
			holder.duration = (TextView) convertView
					.findViewById(R.id.movie_duration);
			holder.poster = (ImageView) convertView
					.findViewById(R.id.movie_poster);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			try {
				holder.title.setText(""
						+ itemsList.get(position).getTitle().trim());
				holder.duration.setText(this.convertTime(Integer
						.parseInt(itemsList.get(position).getDuration())));
				ImageView img=holder.poster;
				loader.DisplayImage(itemsList.get(position).getIcon_url(),
						img);
				// poster.setImageBitmap(loader.DisplayImage(url, imageView));
			} catch (Exception e) {
				e.printStackTrace();
			}

			/** Assigning the random color to the view */
			color = new Random();
			p.setARGB(255, color.nextInt(255), color.nextInt(255),
					color.nextInt(255));

			holder.v.setBackgroundColor(Color.rgb(color.nextInt(255),
					color.nextInt(255), color.nextInt(255)));
		}
		return convertView;
	}

	static class ViewHolder {
		TextView title, duration;
		ImageView poster;
		View v;
	}

	public String convertTime(int totalSecs) {
		int hours = totalSecs / 3600;
		int minutes = (totalSecs % 3600) / 60;
		int seconds = totalSecs % 60;
		return hours + "hh :" + minutes + "mm :" + seconds + "ss";
	}
}
