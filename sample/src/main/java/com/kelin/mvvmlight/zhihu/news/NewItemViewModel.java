package com.kelin.mvvmlight.zhihu.news;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.kelin.mvvmlight.zhihu.newsdetail.NewsDetailActivity;

/**
 * Created by kelin on 16-4-26.
 */
public class NewItemViewModel implements ViewModel {
    //context
    private Context context;

    //model
    public NewsService.News.StoriesBean storiesBean;

    //field to presenter
    public final ObservableField<String> title = new ObservableField<>();
    public final ObservableField<String> imageUrl = new ObservableField<>();
    public final ObservableField<String> date = new ObservableField<>();
    public ViewStyle viewStyle = new ViewStyle();

    //Use class viewStyle to wrap field which is binding to style of view
    public static class ViewStyle {
        public final ObservableInt titleTextColor = new ObservableInt();
    }


    //command
    public ReplyCommand itemClickCommand = new ReplyCommand(() -> {
        this.viewStyle.titleTextColor.set(context.getResources().getColor(android.R.color.darker_gray));
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra(NewsDetailActivity.EXTRA_KEY_NEWS_ID, storiesBean.getId());
        context.startActivity(intent);
    });

    public NewItemViewModel(Context context, NewsService.News.StoriesBean storiesBean) {
        this.context = context;
        this.storiesBean = storiesBean;
        this.viewStyle.titleTextColor.set(context.getResources().getColor(android.R.color.black));
        if (storiesBean.getExtraField() != null) {
            date.set(NewsListHelper.changeDateFormat(storiesBean.getExtraField().getDate(), NewsListHelper.DAY_FORMAT, NewsListHelper.DAY_UI_FORMAT));
        } else {
            title.set(storiesBean.getTitle());
            imageUrl.set(storiesBean.getImages().get(0));
        }
    }

}
