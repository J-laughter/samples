package com.laughter.network.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.laughter.R;
import com.laughter.network.HttpCallbackListener;
import com.laughter.network.adapter.ArticleAdapter;
import com.laughter.network.model.Article;
import com.laughter.network.util.HttpUtil;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HttpMainActivity extends AppCompatActivity implements Callback{

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private ArticleAdapter adapter;
    private List<Article> articleList;

    private QMUITipDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.http_activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("第一行代码");
        setSupportActionBar(toolbar);

        loadingDialog = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();

        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this ,DividerItemDecoration.VERTICAL));
        articleList = new ArrayList<>();
        adapter = new ArticleAdapter(articleList, this);
        mRecyclerView.setAdapter(adapter);

        loadingDialog.show();
        String address = "http://www.wanandroid.com/article/list/0/json";
        HttpUtil.sendOkHttpRequest(address, this);
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {

    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        articleList.clear();
        try {
            String jsonData = null;
            if (response.body() != null){
                jsonData = response.body().string();
            }
            JSONObject mJsonObject = new JSONObject(jsonData);
            JSONObject data = mJsonObject.getJSONObject("data");
            JSONArray mJsonArray = data.getJSONArray("datas");
            for (int i = 0; i < mJsonArray.length(); i++){
                JSONObject item = mJsonArray.getJSONObject(i);
                String title = item.getString("title");
                String author = item.getString("author");
                JSONArray tags = item.getJSONArray("tags");
                String tag = null;
                if (tags.length() > 0){
                    tag = tags.getJSONObject(0).getString("name");
                }
                String date = item.getString("niceDate");
                String link = item.getString("link");
                articleList.add(new Article(title, author, tag, date, link));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            initData();
        }
    }

    private void initData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                loadingDialog.dismiss();
            }
        });
    }
}
