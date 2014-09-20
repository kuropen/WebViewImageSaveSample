package org.kuropen.webviewimagesavesample;

import java.io.File;
import java.net.URL;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import android.widget.Toast;

/**
 * WebViewを表示させるFragment
 */
public class WebViewFragment extends Fragment implements OnLongClickListener {

	private WebView mWebView;

	@SuppressLint("SetJavaScriptEnabled") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);

		mWebView = (WebView) rootView.findViewById(R.id.mainWebView);
		
		WebSettings w = mWebView.getSettings();
		w.setJavaScriptEnabled(true);
		w.setLoadsImagesAutomatically(true);
		w.setUseWideViewPort(true);
		w.setJavaScriptCanOpenWindowsAutomatically(true);

		w.setSaveFormData(true);
		w.setLoadWithOverviewMode(true);
		// w.setPageCacheCapacity();

		// WebView inside Browser doesn't want initial focus to be set.
		w.setNeedInitialFocus(false);
		// Browser does not support multiple windows
		w.setSupportMultipleWindows(false);

		// HTML5 API flags
		w.setAppCacheEnabled(true);
		w.setDatabaseEnabled(true);
		w.setDomStorageEnabled(true);
		w.setGeolocationEnabled(true);

		// HTML5 configuration parameters.
		w.setAppCacheEnabled(true);
		w.setDatabaseEnabled(true);
		w.setDomStorageEnabled(true);
		w.setGeolocationEnabled(true);

		w.setCacheMode(WebSettings.LOAD_DEFAULT);

		// スクロールバー非表示
		mWebView.setVerticalScrollbarOverlay(true);
		mWebView.setHorizontalScrollBarEnabled(false);
		
		mWebView.setOnLongClickListener(this);

		mWebView.loadUrl("http://divnil.com/wallpaper/iphone6plus/");
		
		return rootView;
	}

	@Override
	public boolean onLongClick(View v) {
		WebView webView = (WebView) v;
		HitTestResult hr = webView.getHitTestResult();
		
		//画像が長押しされたら保存する
		int type = hr.getType();
		if (type == HitTestResult.IMAGE_TYPE || type == HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
			//この両者の場合、Extraはいずれも画像のURLとなる
			String imageUrl = hr.getExtra();
			
			// 本来であれば、ここでダウンロードの確認を表示してあげる必要がある
			
			// 保存先ディレクトリ
			File file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), getFilenameFromURL(imageUrl));
			
			DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
			DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imageUrl));
			request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
			request.setDestinationUri(Uri.fromFile(file));
			request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
			request.setTitle("WebViewImageSaveSample");
			request.setTitle(getFilenameFromURL(imageUrl) + "をダウンロードしています。");
			downloadManager.enqueue(request);
			
			Toast.makeText(getActivity(), "ダウンロードを開始します…", Toast.LENGTH_LONG).show();
		}
		
		return false;
	}
	
	protected String getFilenameFromURL(URL url) {
		return getFilenameFromURL(url.getFile());
	}
	
	protected String getFilenameFromURL(String url) {
		String[] p = url.split("/");
		String s = p[p.length - 1];
		if (s.indexOf("?") > -1) {
			return s.substring(0, s.indexOf("?"));
		}
		return s;
	}

}
