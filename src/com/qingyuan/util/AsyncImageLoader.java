package com.qingyuan.util;

import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class AsyncImageLoader extends AsyncTask<Integer, Void, Drawable> {

	 private static String TAG = "qingyuan";
     private HashMap<String, SoftReference<Drawable>> imageCache;
     public AsyncImageLoader() {
             imageCache = new HashMap<String, SoftReference<Drawable>>();
         }
      
     @SuppressLint("HandlerLeak")
	public Drawable loadDrawable(final String imageUrl,final String Tag, final ImageCallback imageCallback) {
             if (imageCache.containsKey(imageUrl)) {
            	 
                 SoftReference<Drawable> softReference = imageCache.get(imageUrl);
                 Drawable drawable = softReference.get();
                 if (drawable != null) {
                	 Log.i(TAG,"资源相同");
                     return drawable;
                 }
                 if(drawable == null){
                	 Log.i(TAG,"资源已经被回收");
                 }
             }
             final Handler handler = new Handler() {
                 public void handleMessage(Message message) {
                     imageCallback.imageLoaded((Drawable) message.obj, imageUrl,Tag);
                 }
             };
             new Thread() {
                 @Override
                 public void run() {
                     Drawable drawable = loadImageFromUrl(imageUrl);
                     imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
                     Message message = handler.obtainMessage(0, drawable);
                     handler.sendMessage(message);
                 }
             }.start();
             return null;
         }
       
    public static Drawable loadImageFromUrl(String url) {
    	/**
    	 * 加载网络图片
   	    */

       try {
/**这里会出现异步加载异常，待修改	*/		
            return Drawable.createFromStream(new URL(url).openStream(), "src");
//        
        	}
    catch (Exception e) {
            //Toast.makeText(Activity.getApplication(),"加载中，请稍等", Toast.LENGTH_SHORT);
    	return  null;
        }
    	/**
    	 * 加载内存卡图片
    	 */
/*    	    Options options=new Options();
    	    options.inSampleSize=2;
    	    Bitmap bitmap=BitmapFactory.decodeFile(url, options);
    	    Drawable drawable=new BitmapDrawable(bitmap);*/
            //return drawable;
        }
//    protected Drawable loadImageFromUrl(String imageUrl)  
//    {  
//        try {  
//            return Drawable.createFromStream(new URL(imageUrl).openStream(),"debug");  
//        } catch (Exception e) {  
//            // TODO: handle exception  
//            throw new RuntimeException(e);  
//        }  
//    }  
      
    public interface ImageCallback {
             public void imageLoaded(Drawable imageDrawable, String imageUrl,String Tag);
             
         }

	@Override
	protected Drawable doInBackground(Integer... arg0) {
		// TODO Auto-generated method stub
		return null;
	}


    
}