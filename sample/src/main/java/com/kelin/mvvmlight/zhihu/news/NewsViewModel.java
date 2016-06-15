package com.kelin.mvvmlight.zhihu.news;

import android.app.Fragment;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.support.v4.util.Pair;
import android.widget.Toast;

import com.kelin.mvvmlight.BR;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.kelin.mvvmlight.messenger.Messenger;
import com.kelin.mvvmlight.zhihu.R;
import com.kelin.mvvmlight.zhihu.ZhiHuApp;
import com.kelin.mvvmlight.zhihu.retrofit.RetrofitProvider;
import com.trello.rxlifecycle.FragmentLifecycleProvider;

import java.util.Calendar;

import me.tatarka.bindingcollectionadapter.BaseItemViewSelector;
import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import rx.Notification;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * Created by kelin on 16-4-25.
 */
public class NewsViewModel implements ViewModel {
    public static final String TOKEN_TOP_NEWS_FINISH = "token_top_news_finish" + ZhiHuApp.sPackageName;

    //context
    private Fragment fragment;

    /**
     * model
     */
    private NewsService.News news;
    private TopNewsService.News topNews;

    /*
      data for presenter
     */

    // viewModel for RecyclerView
    public final ObservableList<NewItemViewModel> itemViewModel = new ObservableArrayList<>();
    // view layout for RecyclerView
    public final ItemViewSelector<NewItemViewModel> itemView = new BaseItemViewSelector<NewItemViewModel>() {
        @Override
        public void select(ItemView itemView, int position, NewItemViewModel itemViewModel) {
            itemView.set(BR.viewModel, itemViewModel.storiesBean.getExtraField() != null ? R.layout.listitem_news_header : R.layout.listitem_news);
        }

        @Override
        public int viewTypeCount() {
            return 2;
        }

    };
    //collection of view style,wrap to a class to manage conveniently!
    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isRefreshing = new ObservableBoolean(true);
        public final ObservableBoolean progressRefreshing = new ObservableBoolean(true);
    }

    /**
     * command
     */

    public final ReplyCommand onRefreshCommand = new ReplyCommand<>(() -> {
        Observable.just(Calendar.getInstance())
                .doOnNext(c -> c.add(Calendar.DAY_OF_MONTH, 1))
                .map(c -> NewsListHelper.DAY_FORMAT.format(c.getTime()))
                .subscribe(d -> loadTopNews(d));
    });
    /**
     * @param p count of listview items,is unused here!
     * @params,funciton when return true，the callback just can be invoked!
     */
    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand<>((p) -> {
        loadNewsList(news.getDate());
    });


    public NewsViewModel(Fragment fragment) {
        this.fragment = fragment;

        BehaviorSubject<Notification<NewsService.News>> subject = BehaviorSubject.create();
        subject.filter(Notification::isOnNext)
                .subscribe(n -> Toast.makeText(fragment.getActivity(), "load finish!", Toast.LENGTH_SHORT).show());

        Observable.just(Calendar.getInstance())
                .doOnNext(c -> c.add(Calendar.DAY_OF_MONTH, 1))
                .map(c -> NewsListHelper.DAY_FORMAT.format(c.getTime()))
                .subscribe(d -> loadTopNews(d));
    }


    private void loadNewsList(String date) {
        viewStyle.isRefreshing.set(true);

        Observable<Notification<NewsService.News>> newsListOb =
                RetrofitProvider.getInstance().create(NewsService.class)
                        .getNewsList(date)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(((FragmentLifecycleProvider) fragment).bindToLifecycle())
                        .materialize().share();

        newsListOb.filter(Notification::isOnNext)
                .map(n -> n.getValue())
                .filter(m -> !m.getStories().isEmpty())
                .doOnNext(m -> Observable.just(m.getDate()).map(d -> new NewsService.News.StoriesBean.ExtraField(true, d))
                        .map(d -> new NewsService.News.StoriesBean(d))
                        .subscribe(d -> itemViewModel.add(new NewItemViewModel(fragment.getActivity(), d))))
                .doOnNext(m -> news = m)
                .doAfterTerminate(()-> viewStyle.isRefreshing.set(false))
                .flatMap(m -> Observable.from(m.getStories()))
                .subscribe(i -> itemViewModel.add(new NewItemViewModel(fragment.getActivity(), i)));


        NewsListHelper.dealWithResponseError(newsListOb.filter(Notification::isOnError)
                .map(n -> n.getThrowable()));


    }

    private void loadTopNews(String date) {
        viewStyle.isRefreshing.set(true);

        Observable<TopNewsService.News> topNewsOb =
                RetrofitProvider.getInstance().create(TopNewsService.class)
                        .getTopNewsList()
                        .compose(((FragmentLifecycleProvider) fragment).bindToLifecycle());

        Observable<NewsService.News> newsListOb =
                RetrofitProvider.getInstance().create(NewsService.class)
                        .getNewsList(date)
                        .compose(((FragmentLifecycleProvider) fragment).bindToLifecycle());


        Observable<Notification<Pair<TopNewsService.News, NewsService.News>>> combineRequestOb = Observable.combineLatest(topNewsOb, newsListOb, Pair::new)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .materialize().share();


        combineRequestOb.filter(Notification::isOnNext)
                .map(n -> n.getValue())
                .map(p -> p.first)
                .filter(m -> !m.getTop_stories().isEmpty())
                .doOnNext(m -> Observable.just(NewsListHelper.isTomorrow(date)).filter(b -> b).subscribe(b -> itemViewModel.clear()))
                .subscribe(m -> Messenger.getDefault().send(m, TOKEN_TOP_NEWS_FINISH));

        combineRequestOb.filter(Notification::isOnNext)
                .map(n -> n.getValue())
                .map(p -> p.second).filter(m -> !m.getStories().isEmpty())
                .doOnNext(m -> news = m)
                .flatMap(m -> Observable.from(m.getStories()))
                .subscribe(i -> itemViewModel.add(new NewItemViewModel(fragment.getActivity(), i)));

        combineRequestOb.subscribe((n) -> {
            viewStyle.isRefreshing.set(false);
            viewStyle.progressRefreshing.set(false);
        });

        NewsListHelper.dealWithResponseError(combineRequestOb.filter(Notification::isOnError)
                .map(n -> n.getThrowable()));

    }

}
