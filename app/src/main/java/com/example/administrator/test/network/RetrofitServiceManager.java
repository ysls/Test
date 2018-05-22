package com.example.administrator.test.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.compat.BuildConfig;
import android.util.Log;

import com.example.administrator.test.MyApplication;
import com.example.administrator.test.util.NetWorkUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class RetrofitServiceManager {
    private static final int  DEFAULT_TIME_OUT = 5;//超时时间 5s
    private static final int DEFAULT_READ_TIME_OUT = 10;
    private Retrofit mRetrofit;
    private String BASE_URL = "http://101.132.151.168:8080/PhoneSafe/api/";
    private static long CacheSize = 4*1024*1024;
    private File cacheFile;
    private Cache cache;

    private RetrofitServiceManager(){

        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        if(BuildConfig.DEBUG){
            //显示日志
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }else {
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        //可以配置拦截器
        cacheFile = new File(MyApplication.getApplication().getCacheDir(),"cacheData");
        cache = new Cache(cacheFile,CacheSize);
        //配置okhttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cache(cache)
                .addInterceptor(new NetCacheInterceptor())
//                .addNetworkInterceptor(new NetCacheInterceptor())
                .addInterceptor(new ReceivedCookiesInterceptor(MyApplication.getApplication()))
                .addInterceptor(new AddCookiesInterceptor(MyApplication.getApplication(),"CN"))
                .addInterceptor(logInterceptor)
                .readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_READ_TIME_OUT,TimeUnit.SECONDS)
                .connectTimeout(DEFAULT_TIME_OUT,TimeUnit.SECONDS);

        //创建retrofit
        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }

    private static class singleHolder{
        private static final RetrofitServiceManager INSTANCE = new RetrofitServiceManager();
        private static final ApiService SERVICE = INSTANCE.create(ApiService.class);
    }

    public static RetrofitServiceManager getInstance(){
        return singleHolder.INSTANCE;
    }

    public static ApiService getService(){
        return singleHolder.SERVICE;
    }

    //设置url
    public void setBaseUrl(String url){
        this.BASE_URL = url;
    }


    public <T> T create(Class<T> service){
        return mRetrofit.create(service);
    }

    //在有网络的情况下直接获取网络上的数据，没有网络的情况下获取缓存的数据。
    public static class NetCacheInterceptor implements Interceptor{
        @Override
        public Response intercept(Chain chain) throws IOException {
            //获取到请求
            Request request = chain.request();
            //没有网的情况下强制使用缓存数据
            if (!NetWorkUtil.isConnected()){
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response originalResponse = chain.proceed(request);
            if (!NetWorkUtil.isConnected()){
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        //max-age=0，不进行缓存
                        .header("Cache-Control","max-age=" + 0)
                        .build();
            }else {
                int maxtime = 4*24*60*60;
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control","public,only-if-cached,maxStale"+maxtime)
                        .build();
            }
        }
    }

    //无论有无网络我们都去获取缓存的数据（我们会设置一个缓存时间，
    // 在某一段时间内（例如 60S）去获取缓存数据。超过 60S 我们就去网络重新请求数据）
    public static class CacheInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            //获取请求
            Request request = chain.request();
            Response response = chain.proceed(request);
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control","max-age=" + 60)
                    .build();
        }
    }


    //拦截器设置cookie
    public static class ReceivedCookiesInterceptor implements Interceptor {
        private Context context;
        SharedPreferences sharedPreferences;

        public ReceivedCookiesInterceptor(Context context) {
            super();
            this.context = context;
            sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            if (chain == null)
                Log.e("http", "Receivedchain == null");
            Response originalResponse = chain.proceed(chain.request());
            Log.e("http", "originalResponse" + originalResponse.toString());
            if (!originalResponse.headers("set-cookie").isEmpty()) {
                final StringBuffer cookieBuffer = new StringBuffer();
                Observable.from(originalResponse.headers("set-cookie"))
                        .map(new Func1<String, String>() {
                            @Override
                            public String call(String s) {
                                String[] cookieArray = s.split(";");
                                return cookieArray[0];
                            }
                        })
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String cookie) {
                                cookieBuffer.append(cookie).append(";");
                            }
                        });
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("cookie", cookieBuffer.toString());
                Log.e("http", "ReceivedCookiesInterceptor" + cookieBuffer.toString());
                editor.commit();
            }

            return originalResponse;
        }
    }


    //拦截器增加cookie
    public static class AddCookiesInterceptor implements Interceptor {
        private Context context;
        private String lang;

        public AddCookiesInterceptor(Context context, String lang) {
            super();
            this.context = context;
            this.lang = lang;

        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            if (chain == null)
                Log.e("http", "Addchain == null");
            final Request.Builder builder = chain.request().newBuilder();
            SharedPreferences sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
            Observable.just(sharedPreferences.getString("cookie", ""))
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String cookie) {
                            if (cookie.contains("lang=ch")) {
                                cookie = cookie.replace("lang=ch", "lang=" + lang);
                            }
                            if (cookie.contains("lang=en")) {
                                cookie = cookie.replace("lang=en", "lang=" + lang);
                            }
                            //添加cookie
                            Log.e("http", "AddCookiesInterceptor" + cookie);
                            builder.addHeader("cookie", cookie);
                        }
                    });
            SharedPreferences sharedPreferences2 = context.getSharedPreferences("jwt", Context.MODE_PRIVATE);
            Observable.just(sharedPreferences2.getString("jwt", ""))
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String jwt) {
                            //添加authorization
                            Log.e("http", "authorization" + jwt);
                            builder.addHeader("authorization", jwt);
                        }
                    });
            return chain.proceed(builder.build());
        }
    }
}
