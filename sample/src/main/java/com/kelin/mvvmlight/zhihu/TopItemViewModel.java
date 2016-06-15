package com.kelin.mvvmlight.zhihu;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.kelin.mvvmlight.zhihu.news.TopNewsService;
import com.kelin.mvvmlight.zhihu.newsdetail.NewsDetailActivity;

/**
 * Created by kelin on 16-4-26.
 */
public class TopItemViewModel implements ViewModel {
    //context
    private Context context;

    //model
    public TopNewsService.News.TopStoriesBean topStoriesBean;

    //field to presenter
    public final ObservableField<String> title = new ObservableField<>();
    public final ObservableField<String> imageUrl = new ObservableField<>();

    public final ReplyCommand topItemClickCommand = new ReplyCommand(() -> {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra(NewsDetailActivity.EXTRA_KEY_NEWS_ID, topStoriesBean.getId());
        context.startActivity(intent);
    });

    public TopItemViewModel(Context context, TopNewsService.News.TopStoriesBean topStoriesBean) {
        this.context = context;
        this.topStoriesBean = topStoriesBean;
        title.set(topStoriesBean.getTitle());
        imageUrl.set(topStoriesBean.getImage());
    }
}
