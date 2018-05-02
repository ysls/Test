package com.example.administrator.test.security;

import java.util.List;

public class NewsBean {

    /**
     * articleList : [{"content":"<p style=\"margin-t","id":20044,"title":"看完这篇文章 你还敢不重视智能手机的安全么?","url":"http://101.132.151.168:8080/PhoneSafe/showArticle?tid=20044"}]
     * showArt : null
     * tid : 0
     */

    private Object showArt;
    private int tid;
    private List<ArticleListBean> articleList;

    public Object getShowArt() {
        return showArt;
    }

    public void setShowArt(Object showArt) {
        this.showArt = showArt;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public List<ArticleListBean> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<ArticleListBean> articleList) {
        this.articleList = articleList;
    }

    public static class ArticleListBean {
        /**
         * content : <p style="margin-t
         * id : 20044
         * title : 看完这篇文章 你还敢不重视智能手机的安全么?
         * url : http://101.132.151.168:8080/PhoneSafe/showArticle?tid=20044
         */

        private String content;
        private int id;
        private String title;
        private String url;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
