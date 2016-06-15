package com.kelin.mvvmlight.zhihu.news;

import android.util.Log;

import com.kelin.mvvmlight.zhihu.retrofit.ApiException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import rx.Observable;
import rx.subjects.ReplaySubject;

/**
 * Created by kelin on 16-4-26.
 */
public class NewsListHelper {
    public static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat DAY_UI_FORMAT = new SimpleDateFormat("yyyy年MM月dd日");

    public static void dealWithResponseError(Observable<Throwable> throwableObservable) {

        ReplaySubject<Throwable> throwableReplaySubject = ReplaySubject.create();

        throwableObservable.subscribe(throwableReplaySubject);

        throwableReplaySubject
                .repeat(5)
                .scan((n, c) -> n.getCause())
                .takeUntil(n -> n.getCause() == null)
                .filter(n -> n instanceof ApiException)
                .cast(ApiException.class)
                .subscribe(e -> Log.v("error", e.msg));


    }

    public static boolean isToday(String date) {
        return new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()).equals(date);
    }

    public static boolean isTomorrow(String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return new SimpleDateFormat("yyyyMMdd").format(calendar.getTime()).equals(date);
    }

    public static String changeDateFormat(String oldDate, SimpleDateFormat oldFormat, SimpleDateFormat newFormat) {
        Date date;
        try {
            date = oldFormat.parse(oldDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return newFormat.format(date);
    }

}
