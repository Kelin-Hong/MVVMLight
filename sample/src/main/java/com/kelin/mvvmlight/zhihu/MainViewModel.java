package com.kelin.mvvmlight.zhihu;

import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.messenger.Messenger;
import com.kelin.mvvmlight.zhihu.news.NewsViewModel;
import com.kelin.mvvmlight.zhihu.news.TopNewsService;

import java.util.concurrent.TimeUnit;

import me.tatarka.bindingcollectionadapter.ItemView;
import rx.Observable;

/**
 * Created by kelin on 16-4-28.
 */
public class MainViewModel implements ViewModel {
    // Token to Messenger append package name to be unique
    public static final String TOKEN_UPDATE_INDICATOR = "token_update_indicator" + ZhiHuApp.sPackageName;

    //context
    private Context context;

    // viewModel for recycler header viewPager
    public final ItemView topItemView = ItemView.of(com.kelin.mvvmlight.zhihu.BR.viewModel, R.layout.viewpager_item_top_news);
    public final ObservableList<TopItemViewModel> topItemViewModel = new ObservableArrayList<>();


    public MainViewModel(Activity activity) {
        context=activity;
        Messenger.getDefault().register(activity, NewsViewModel.TOKEN_TOP_NEWS_FINISH, TopNewsService.News.class, (news) -> {
            Observable.just(news)
                    .doOnNext(m -> topItemViewModel.clear())
                    .flatMap(n -> Observable.from(n.getTop_stories()))
                    .doOnNext(m -> topItemViewModel.add(new TopItemViewModel(context,m)))
                    .toList()
                    .subscribe((l) -> Messenger.getDefault().sendNoMsgToTargetWithToken(TOKEN_UPDATE_INDICATOR, activity));
        });

    }
}
