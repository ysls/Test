package com.example.administrator.test.network;


import com.example.administrator.test.backup.DownBean;
import com.example.administrator.test.login.CodeBean;
import com.example.administrator.test.login.LoginBean;
import com.example.administrator.test.model.PhoneCodeBean;
import com.example.administrator.test.security.NewsBean;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

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

    /**
     * 获取文章
     * @return
     */
    @GET("GetArticle")
    Observable<NewsBean> getArticle();

    /**
     * 上传地址
     * @param token
     * @param position
     * @return
     */
    @POST("SetPosition")
    @FormUrlEncoded
    Observable<CodeBean> setPosition(@Field("token") String token,@Field("position") String position);

    /**
     * 上传安全手机号
     * @param token
     * @param safe_num
     * @return
     */
    @POST("SetSafeNum")
    @FormUrlEncoded
    Observable<CodeBean> setSafeNum(@Field("token") String token,@Field("safe_num") String safe_num);

    /**
     * 获取安全手机号
     * @param token
     * @return
     */
    @POST("GetSafeNum")
    @FormUrlEncoded
    Observable<PhoneCodeBean> getSafeNum(@Field("token") String token);

    /**
     * 上传图片
     * @param token
     * @param photo
     * @return
     */
    @Multipart
    @POST("fileupload")
    Observable<ResponseBody> fileupload(@Query("token") String token, @Part MultipartBody.Part photo);

    /**
     * 备份联系人
     * @param token
     * @param mlist
     * @return
     */
    @POST("mlist")
    @FormUrlEncoded
    Observable<CodeBean> upContacts(@Field("token") String token,@Field("mlist") String mlist);

    @POST("mlist")
    @FormUrlEncoded
    Observable<DownBean> downContacts(@Field("token") String token);


}
