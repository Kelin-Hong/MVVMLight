package com.kelin.mvvmlight.zhihu.news;

import java.io.Serializable;
import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by kelin on 16-4-26.
 */
public interface NewsService {
    @GET("/api/4/news/before/{date}")
    public Observable<News> getNewsList(@Path("date") String date);

    public class News {

        private String date;

        private List<StoriesBean> stories;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<StoriesBean> getStories() {
            return stories;
        }

        public void setStories(List<StoriesBean> stories) {
            this.stories = stories;
        }


        public static class StoriesBean implements Serializable{
            private ExtraField extraField;
            private String title;
            private String ga_prefix;
            private boolean multipic;
            private int type;
            private long id;
            private List<String> images;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getGa_prefix() {
                return ga_prefix;
            }

            public void setGa_prefix(String ga_prefix) {
                this.ga_prefix = ga_prefix;
            }

            public boolean isMultipic() {
                return multipic;
            }

            public void setMultipic(boolean multipic) {
                this.multipic = multipic;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public List<String> getImages() {
                return images;
            }

            public void setImages(List<String> images) {
                this.images = images;
            }

            public ExtraField getExtraField() {
                return extraField;
            }

            public void setExtraField(ExtraField extraField) {
                this.extraField = extraField;
            }

            public static class ExtraField  implements Serializable {
                private boolean isHeader;
                private String date;

                public ExtraField(boolean isHeader, String date) {
                    this.isHeader = isHeader;
                    this.date = date;
                }

                public boolean isHeader() {
                    return isHeader;
                }

                public void setHeader(boolean header) {
                    isHeader = header;
                }

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }
            }

            public StoriesBean(ExtraField extraField) {
                this.extraField = extraField;
            }
        }


    }
}
