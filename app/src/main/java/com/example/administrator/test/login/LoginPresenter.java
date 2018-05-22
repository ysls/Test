package com.example.administrator.test.login;

import android.app.Activity;
import android.util.Log;

import com.example.administrator.test.network.RetrofitServiceManager;
import com.example.administrator.test.util.ToastUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;



public class LoginPresenter implements ILoginContract.ILoginPrestener {

    private ILoginContract.ILoginView mLoginView;
    private Activity mContext;

    private List<Subscription> subscriptionList = new ArrayList<>();

    public LoginPresenter(Activity mContext, ILoginContract.ILoginView mLoginView) {
        this.mContext = mContext;
        this.mLoginView = mLoginView;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        Iterator<Subscription> iterator = subscriptionList.iterator();
        while (iterator.hasNext()){
            Subscription subscription = iterator.next();
            if (subscription != null && !subscription.isUnsubscribed()){
                subscription.unsubscribe();
            }
        }
    }

    @Override
    public void getCode(String phoneNum) {

        mLoginView.showDialog("发送中...");
        Subscription subscription = RetrofitServiceManager.getService().getCode(phoneNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CodeBean>() {
                    @Override
                    public void onCompleted() {
                        mLoginView.hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLoginView.hideDialog();
                        Log.e("onError: ", e.getMessage());
                        ToastUtils.showShort("数据加载失败");
                    }

                    @Override
                    public void onNext(CodeBean codeBean) {
                            if (codeBean.getFlag() == 1){
                                ToastUtils.showShort("发送成功...");
                                mLoginView.sendCodeSucceed();
                        } else {
                            ToastUtils.showShort(codeBean.getMsg());
                        }
                    }
                });
        subscriptionList.add(subscription);
    }

    @Override
    public void login(final String phoneNum, String code) {

        mLoginView.showDialog("登录中...");

        Subscription subscription = RetrofitServiceManager.getService().login(phoneNum, code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginBean>() {
                    @Override
                    public void onCompleted() {
                        mLoginView.hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLoginView.hideDialog();
                        Log.e("onError: ", e.getMessage());
                        ToastUtils.showShort("登录失败...");
                    }

                    @Override
                    public void onNext(LoginBean loginBean) {
                        Log.i("TOKEN", loginBean.getToken());
                        if (loginBean.getFlag() == 1) {
                            mLoginView.loginSucceed(loginBean);
                        } else {
                            ToastUtils.showShort(loginBean.getMsg());
                        }
                    }
                });
        subscriptionList.add(subscription);
    }
}
