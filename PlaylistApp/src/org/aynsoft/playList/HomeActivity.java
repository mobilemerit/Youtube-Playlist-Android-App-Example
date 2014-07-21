package org.aynsoft.playList;

import java.util.ArrayList;
import java.util.List;

import org.aynsoft.adapter.VideoListAdapter;
import org.aynsoft.java.Parser;
import org.aynsoft.java.VideoDetail;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;


public class HomeActivity extends Activity implements  OnItemClickListener{

	private ListView videoList;
	private VideoListAdapter adapter;
	private List<VideoDetail > list;
	public static final String ID_Extra="ID";
	public static final String PROCESS_DIALOG_MSG="Loading...";
	
	private static final String url="http://gdata.youtube.com/feeds/api/playlists/" +
				"PL4E272D49F6209D23"+//Your playlist ID
				"?v=2&max-results=50";//Number of max counts you want.
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);		
		list=new ArrayList<VideoDetail>();
		adapter=new VideoListAdapter(HomeActivity.this, list);		
		setContentView(R.layout.listview);
		
		
		videoList=(ListView)this.findViewById(R.id.movie_list_view);
		videoList.setAdapter(adapter);
		videoList.setOnItemClickListener(this);		

		//StartLoading
		startLoading(url);
	}	
	
	
	public void startLoading(String url){
		new LoadMoviesAsync().execute(url);
	}
	
	class LoadMoviesAsync extends AsyncTask<String,VideoDetail,Void>{
		ProgressDialog dialog;
		@Override
		protected void onPreExecute() {
			dialog=new ProgressDialog(HomeActivity.this);
			dialog.setMessage(PROCESS_DIALOG_MSG);
			dialog.show();
			super.onPreExecute();
		}		
		@Override
		protected Void doInBackground(String... params) {
			String url=params[0];			
			Parser parser=new Parser();
			NodeList movieContentLst=parser.getResponceNodeList(url);
			//Log.i("HomeActivity",""+movieContentLst.getLength());
			if(movieContentLst!=null){
				for(int i=0;i<movieContentLst.getLength();i++){
					publishProgress(parser.getResult(movieContentLst, i));
				}
			}
			
			return null;
		}
		
		@Override
		protected void onProgressUpdate(VideoDetail... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			addItem(values);
			adapter.notifyDataSetChanged();
		}	
		public void addItem(VideoDetail... items){
			for(VideoDetail item: items){
				list.add(item);
			}	
		}
		@Override
		protected void onPostExecute(Void result) {
			if(dialog.isShowing()){
				dialog.dismiss();
			}		
			Toast.makeText(getBaseContext(), ""+list.size(),Toast.LENGTH_SHORT).show();
			super.onPostExecute(result);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {				
		Intent i=new Intent(HomeActivity.this,YouTubePlayerActivity.class);
		i.putExtra(ID_Extra,""+list.get(arg2).getVideo_id());
		startActivity(i);			
	}
	
}
