package com.kelin.mvvmlight.zhihu.newsdetail;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by kelin on 16-4-29.
 */
public interface NewsDetailService {
    @GET("/api/4/news/{id}")
    public Observable<NewsDetail> getNewsDetail(@Path("id") long id);

    public class NewsDetail{

        /**
         * body : T
         * image_source : Angel Abril Ruiz / CC BY
         * title : 卖衣服的新手段：把耐用品变成「不停买新的」
         * image : http://p4.zhimg.com/30/59/30594279d368534c6c2f91b2c00c7806.jpg
         * share_url : http://daily.zhihu.com/story/3892357
         * js : []
         * ga_prefix : 050615
         * images : ["http://p3.zhimg.com/69/d0/69d0ab1bde1988bd475bc7e0a25b713e.jpg"]
         * type : 0
         * id : 3892357
         * css : ["http://news-at.zhihu.com/css/news_qa.auto.css?v=4b3e3"]
         */

        private String body;
        private String image_source;
        private String title;
        private String image;
        private String share_url;
        private String ga_prefix;
        private int type;
        private int id;
        private List<?> js;
        private List<String> images;
        private List<String> css;
        private String cssStr;

        public String getCssStr() {
            return cssStr;
        }

        public void setCssStr(String cssStr) {
            this.cssStr = cssStr;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getImage_source() {
            return image_source;
        }

        public void setImage_source(String image_source) {
            this.image_source = image_source;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<?> getJs() {
            return js;
        }

        public void setJs(List<?> js) {
            this.js = js;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public List<String> getCss() {
            return css;
        }

        public void setCss(List<String> css) {
            this.css = css;
        }
    }
}
