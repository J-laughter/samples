package com.laughter.network.activity;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.laughter.R;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

public class WvMainActivity extends AppCompatActivity {

    private WebView webView;
    private QMUITipDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wv_activity_main);

        initView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        webView = findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        loadingDialog = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        loadingDialog.setCancelable(true);
        loadingDialog.show();
        webView.loadUrl(getIntent().getStringExtra("link"));
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100){
                    loadingDialog.dismiss();
                }
            }
        });
    }


}
