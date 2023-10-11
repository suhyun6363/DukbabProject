package kr.ac.duksung.dukbab.Home;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URISyntaxException;
/*
public class INIP2PWebView extends WebViewClient {
    private  WebView webView;
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith("http://") || url.startsWith("HTTP://") || url.startsWith("https://") || url.startsWith("HTTPS://") || url.startsWith("javascript:")) {
            // url load 시 필요한 조건이 있을 경우, 추가
        } else {
            // intent 스키마일 경우 처리
            Intent intent = null;
            try {
                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME); //IntentURI처리
                Uri uri = Uri.parse(intent.getDataString());
                webView.getContext().startActivity(new Intent(Intent.ACTION_VIEW, uri));
                return true;
            } catch (URISyntaxException ex) {
                Log.d("<INIPAYMOBILE>", "URI syntax error : " + url + ":" + ex.getMessage());
                return false;
            } catch (ActivityNotFoundException e) {
                if (intent == null)
                    return false;
                if (handleNotFoundPaymentScheme(webView.getContext(), intent.getScheme()))
                    return true;

                String packageName = intent.getPackage();
                if (packageName != null) {

/*
   market URL
market://search?q="+packageNm => packageNm을 검색어로 마켓 검색 페이지 이동
market://search?q=pname:"+packageNm => packageNm을 패키지로 갖는 앱 검색 페이지 이동
market://details?id="+packageNm => packageNm 에 해당하는 앱 상세 페이지로 이동
*/
/*
                    webView.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                    return true;
                }
                return false;
            }
        }
        return false;
    }
}
*/