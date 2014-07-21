package org.aynsoft.playList;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class YouTubePlayerActivity extends Activity {
	
	private  String VIDEO = "CfCBWUkWzXM";
	private static String VIDEO_URL="https://www.youtube.com/watch?v";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		VIDEO=getIntent().getStringExtra(HomeActivity.ID_Extra);
		try{
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + VIDEO)); 
			intent.putExtra("VIDEO_ID",VIDEO); 
			startActivity(intent);
			this.finish();
			
		}catch(Exception e){
			Intent i=new Intent(Intent.ACTION_VIEW,Uri.parse(VIDEO_URL+"="+VIDEO));
			startActivity(i);		
			this.finish();
		}	
	}
}
