package com.example.administrator.test.security;

import java.util.List;

public class NewsBean {


    private List<ArticleListBean> articleList;

    public List<ArticleListBean> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<ArticleListBean> articleList) {
        this.articleList = articleList;
    }

    public static class ArticleListBean {
        /**
         * date : 2018年05月10日14:15:05
         * id : 20063
         * title : 测试文章数据
         * url : http://101.132.151.168:8080/PhoneSafe/showArticle?tid=20063
         */

        private String date;
        private int id;
        private String title;
        private String url;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
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
