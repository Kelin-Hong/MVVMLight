package com.kelin.mvvmlight.zhihu.newsdetail;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by kelin on 16-4-29.
 */
public interface NewsDetailCssService {
    @GET
    Observable<String> getNewsDetailCss(@Url String url);
}
