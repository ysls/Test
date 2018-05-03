package com.example.administrator.test.login;


import com.example.administrator.test.base.BasePresenter;
import com.example.administrator.test.base.BaseView;

/**
 * Created by 梁遂 on 2017/9/30.
 */

public interface ILoginContract {
    interface ILoginView extends BaseView {
        void sendCodeSucceed();
        void loginSucceed(LoginBean dataBean);
    }
    interface ILoginPrestener extends BasePresenter {
        void getCode(String phoneNum);
        void login(String phoneNum, String code);
    }
}
