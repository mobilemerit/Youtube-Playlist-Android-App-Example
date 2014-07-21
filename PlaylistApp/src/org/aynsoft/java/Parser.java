package org.aynsoft.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;

public class Parser {
	/** --------------------- Movie Search TAG --------------------- */
	private static final String TAG_GROUP = "media:group";
	private static final String TAG_TITLE = "media:title";
	private static final String TAG_MEDIA_THUMB = "media:thumbnail";
	private static final String TAG_VIDEO_ID = "yt:videoid";
	private static final String TAG_DURATION = "yt:duration";

	/** ---------------------Movie Attribute tags--------------------- */
	private static final String ATR_THUMB_NAME = "yt:name";
	private static final String ATR_THUMB_URL = "url";
	private static final String ATR_DURATION = "seconds";
	/** ---------------------Movie Attribute tags--------------------- */
	private static final String VALUE_ATR_POSTER = "poster";
	private static final String VALUE_ATR_DEFAULT = "default";
	
	

	public NodeList getResponceNodeList(String service_url) {
		String searchResponce = getHttpResponse(URI.create(service_url));
		//Log.i("url",""+service_url);
		Log.i("responce",""+searchResponce);
		Document doc;
		NodeList items = null;
		if (searchResponce != null) {
			Log.i("Not null","executed");
			try {
				doc = this.getDomElement(searchResponce);
				items = doc.getElementsByTagName("entry");				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return items;
	}

	public VideoDetail getResult(NodeList searchresult, int position) {
		VideoDetail result = new VideoDetail();
		try {
			Element e = (Element) searchresult.item(position);
			NodeList list=e.getElementsByTagName(TAG_GROUP);
			Element e1=(Element)list.item(0);
			String title = this.getValue(e1, TAG_TITLE);
			String video_id = this.getValue(e1, TAG_VIDEO_ID);
			String image_url=this.getImageUrlAttributeValue(TAG_MEDIA_THUMB,
					ATR_THUMB_NAME,VALUE_ATR_POSTER, e1);
			if(image_url==null){
				image_url=this.getImageUrlAttributeValue(TAG_MEDIA_THUMB,
						ATR_THUMB_NAME,VALUE_ATR_DEFAULT, e1);
			}
			String duration=this.getAttributeValue(e1, TAG_DURATION, ATR_DURATION);			
			result.setTitle(title);
			result.setVideo_id(video_id);
			result.setDuration(duration);
			result.setIcon_url(image_url);
			
			Log.i("title",""+title);
			Log.i("id",""+video_id);
			Log.i("duration",""+duration);
			Log.i("image_url",""+image_url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public String getImageUrlAttributeValue(String tag,String attribute_key,String attribute_value,Element e){
		NodeList list=e.getElementsByTagName(tag);
		String url=null;
		for(int i=0;i<list.getLength();i++){
			Element element=(Element)list.item(i);
			if(element.getAttribute(attribute_key).equals(attribute_value)){
				url=element.getAttribute(ATR_THUMB_URL);
				return url;
			}
		}
		return url;
	}

	public String getAttributeValue(Element e,String tag,String attribute){
		String atr=null;
		NodeList list=e.getElementsByTagName(tag);
		Element element=(Element)list.item(0);
		atr=element.getAttribute(attribute);		
		return atr;
	}
	
	

	/** In app reused functions */

	private String getUrlContents(String theUrl) {
		
		StringBuilder content = new StringBuilder();
		try {
			URL url = new URL(theUrl);
			URLConnection urlConnection = url.openConnection();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(urlConnection.getInputStream()), 8);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				content.append(line + "");
			}
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content.toString();
		
		/*
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(theUrl);
		String responceString=null;
		try {		   
		    HttpResponse response = httpclient.execute(httppost);
		    responceString=EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) {
		    // TODO Auto-generated catch block
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		}
		return responceString;
		*/
	}
	public static String getHttpResponse(URI uri) {
	    Log.d("Parser", "Going to make a get request");
	    StringBuilder response = new StringBuilder();
	    try {
	        HttpGet get = new HttpGet();
	        get.setURI(uri);
	        DefaultHttpClient httpClient = new DefaultHttpClient();
	        HttpResponse httpResponse = httpClient.execute(get);
	        if (httpResponse.getStatusLine().getStatusCode() == 200) {
	            Log.d("demo", "HTTP Get succeeded");

	            HttpEntity messageEntity = httpResponse.getEntity();
	            InputStream is = messageEntity.getContent();
	            BufferedReader br = new BufferedReader(new InputStreamReader(is));
	            String line;
	            while ((line = br.readLine()) != null) {
	                response.append(line);
	            }
	        }
	    } catch (Exception e) {
	        Log.e("demo", e.getMessage());
	    }
	    Log.d("demo", "Done with HTTP getting");
	    return response.toString();
	}

	public Document getDomElement(String xml) {
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);

		} catch (ParserConfigurationException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		} catch (SAXException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		} catch (IOException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		}

		return doc;
	}

	public final String getElementValue(Node elem) {
		Node child;
		if (elem != null) {
			if (elem.hasChildNodes()) {
				for (child = elem.getFirstChild(); child != null; child = child
						.getNextSibling()) {
					if (child.getNodeType() == Node.TEXT_NODE
							|| (child.getNodeType() == Node.CDATA_SECTION_NODE)) {
						return child.getNodeValue();
					}
				}
			}
		}
		return "";
	}

	public String getValue(Element item, String str) {
		NodeList n = item.getElementsByTagName(str);
		return this.getElementValue(n.item(0));
	}

}
