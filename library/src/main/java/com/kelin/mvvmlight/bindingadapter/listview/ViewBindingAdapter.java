package com.kelin.mvvmlight.bindingadapter.listview;

import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kelin.mvvmlight.command.ReplyCommand;

import java.util.concurrent.TimeUnit;

import rx.subjects.PublishSubject;

/**
 * Created by kelin on 16-3-24.
 */
public final class ViewBindingAdapter {

    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"onScrollChangeCommand", "onScrollStateChangedCommand"}, requireAll = false)
    public static void onScrollChangeCommand(final ListView listView,
                                             final ReplyCommand<ListViewScrollDataWrapper> onScrollChangeCommand,
                                             final ReplyCommand<Integer> onScrollStateChangedCommand) {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int scrollState;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.scrollState = scrollState;
                if (onScrollStateChangedCommand != null) {
                    onScrollChangeCommand.equals(scrollState);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (onScrollChangeCommand != null) {
                    onScrollChangeCommand.execute(new ListViewScrollDataWrapper(scrollState, firstVisibleItem, visibleItemCount, totalItemCount));
                }
            }
        });

    }


    @BindingAdapter(value = {"onItemClickCommand"}, requireAll = false)
    public static void onItemClickCommand(final ListView listView, final ReplyCommand<Integer> onItemClickCommand) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onItemClickCommand != null) {
                    onItemClickCommand.execute(position);
                }
            }
        });
    }


    @BindingAdapter({"onLoadMoreCommand"})
    public static void onLoadMoreCommand(final ListView listView, final ReplyCommand<Integer> onLoadMoreCommand) {
        listView.setOnScrollListener(new OnScrollListener(listView, onLoadMoreCommand));

    }

    public static class OnScrollListener implements AbsListView.OnScrollListener {
        private PublishSubject<Integer> methodInvoke = PublishSubject.create();
        private ReplyCommand<Integer> onLoadMoreCommand;
        private ListView listView;

        public OnScrollListener(ListView listView, ReplyCommand<Integer> onLoadMoreCommand) {
            this.onLoadMoreCommand = onLoadMoreCommand;
            this.listView = listView;
            methodInvoke.throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(c -> this.onLoadMoreCommand.execute(c));
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem + visibleItemCount >= totalItemCount
                    && totalItemCount != 0
                    && totalItemCount != listView.getHeaderViewsCount()
                    + listView.getFooterViewsCount()) {
                if (onLoadMoreCommand != null) {
                    methodInvoke.onNext(totalItemCount);
                }
            }
        }
    }

    public static class ListViewScrollDataWrapper {
        public int firstVisibleItem;
        public int visibleItemCount;
        public int totalItemCount;
        public int scrollState;

        public ListViewScrollDataWrapper(int scrollState, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            this.firstVisibleItem = firstVisibleItem;
            this.visibleItemCount = visibleItemCount;
            this.totalItemCount = totalItemCount;
            this.scrollState = scrollState;
        }
    }
}
