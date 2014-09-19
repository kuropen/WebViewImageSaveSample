package org.kuropen.webviewimagesavesample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * WebViewを表示させるFragment
 */
public class WebViewFragment extends Fragment {
	
	private WebView mWebView;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        
        mWebView = (WebView) rootView.findViewById(R.id.mainWebView);
        mWebView.loadUrl("http://divnil.com/wallpaper/iphone6plus/");
        
        return rootView;
    }
	
}
