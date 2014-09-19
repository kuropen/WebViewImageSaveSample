package org.kuropen.webviewimagesavesample;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.widget.Toast;

/**
 * 非同期ダウンロードタスク
 */
public class ImageDownloadTask extends AsyncTask<String, Void, FileInfo> {
	
	private Context mContext;
	
	public ImageDownloadTask (Context context) {
		mContext = context;
	}
	
	@Override
	protected FileInfo doInBackground(String... params) {
		try {
			URL url = new URL(params[0]);
			
			String fileName = getFilenameFromURL(url);
			URLConnection conn = url.openConnection();
			
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(true);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			
			FileInfo info = new FileInfo();
			
			int resp = httpConn.getResponseCode();
			if (resp != HttpURLConnection.HTTP_OK) {
				//HTTPエラー
				throw new IOException("HTTP Status Code " + resp + " returned");
			}
			info.contentType = httpConn.getContentType();
			InputStream in = httpConn.getInputStream();
			File targetFile = new File(params[1] + "/" + fileName);
			FileOutputStream outStream = new FileOutputStream(targetFile);
			DataInputStream dataInStream = new DataInputStream(in);
			DataOutputStream dataOutStream = new DataOutputStream(
					new BufferedOutputStream(outStream));
			
			// Read Data
			byte[] b = new byte[4096];
			int readByte = 0;

			while (-1 != (readByte = dataInStream.read(b))) {
				dataOutStream.write(b, 0, readByte);
			}

			dataInStream.close();
			dataOutStream.close();
			
			
			info.file = targetFile;
			return info;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute (FileInfo result) {
		if (result != null) {
			File file = result.file;
			ContentValues values = new ContentValues();
			ContentResolver contentResolver = mContext.getContentResolver();
			values.put(Images.Media.CONTENT_TYPE, result.contentType);
			values.put(Images.Media.DATE_MODIFIED, System.currentTimeMillis() / 1000);
			values.put(Images.Media.SIZE, file.length());
			values.put(Images.Media.TITLE, file.getName());
			values.put(Images.Media.DATA, file.getPath());
			contentResolver.insert(Media.EXTERNAL_CONTENT_URI, values);
			Toast.makeText(mContext, "ファイルの保存に成功しました。", Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(mContext, "ファイルの保存に失敗しました。", Toast.LENGTH_LONG).show();
		}
	}
	
	protected String getFilenameFromURL(URL url) {
		String[] p = url.getFile().split("/");
		String s = p[p.length - 1];
		if (s.indexOf("?") > -1) {
			return s.substring(0, s.indexOf("?"));
		}
		return s;
	}

}
