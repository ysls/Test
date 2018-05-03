package com.example.administrator.test.network;


import com.example.administrator.test.login.CodeBean;
import com.example.administrator.test.login.LoginBean;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by 梁遂 on 2017/9/26.
 * retrofit 接口
 */

public interface ApiService {

    /**
     * 获取验证码
     * @param phoneNum
     * @return
     */
    @POST("GetVerify")
    @FormUrlEncoded
    Observable<CodeBean> getCode(@Field("user_num") String phoneNum);

    /**
     * 登录
     * @param phoneNum
     * @param password
     * @return
     */
    @POST("login")
    @FormUrlEncoded
    Observable<LoginBean> login(@Field("user.user_num") String phoneNum, @Field("user.user_psw") String password);


}
