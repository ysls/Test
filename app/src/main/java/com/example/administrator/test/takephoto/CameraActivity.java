package com.example.administrator.test.takephoto;


import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import com.example.administrator.test.R;
import com.example.administrator.test.login.CodeBean;
import com.example.administrator.test.network.RetrofitServiceManager;
import com.example.administrator.test.util.SPUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.administrator.test.MyApplication.TOKEN;

public class CameraActivity extends Activity {
	private SurfaceView mySurfaceView;
	private SurfaceHolder myHolder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.e("onCreate: ","camera activity i am coming");
		// 无title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);

		mySurfaceView = (SurfaceView) findViewById(R.id.camera_surfaceview);
		myHolder = mySurfaceView.getHolder();
		myHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		final Camera camera = Camera.open();// 摄像头的初始化
		new Thread(new Runnable() {

			@Override
			public void run() {

				try {

					Thread.sleep(2000);
					try {
						camera.setPreviewDisplay(myHolder);
					} catch (IOException e) {
						e.printStackTrace();
					}

					camera.startPreview();

					// 自动对焦
					camera.autoFocus(myAutoFocus);

					// 对焦后拍照
					camera.takePicture(null, null, myPicCallback);


				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {

				}

			}
		}).start();

	}

	// 自动对焦回调函数(空实现)
	private AutoFocusCallback myAutoFocus = new AutoFocusCallback() {
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
		}
	};

	// 拍照成功回调函数
	private PictureCallback myPicCallback = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.e("onPictureTaken: ","=============================" );
			try {
				String path = Environment.getExternalStorageDirectory() + "/"+System.currentTimeMillis()+".jpg";
				data2file(data, path);
				File photo = new File(path);
				RequestBody requestFile =
						RequestBody.create(MediaType.parse("multipart/form-data"), photo);
				MultipartBody.Part body =
						MultipartBody.Part.createFormData("upload", photo.getName(), requestFile);
				RetrofitServiceManager.getService().fileupload(SPUtils.getInstance().getString(TOKEN),body)
						.subscribeOn(Schedulers.io())
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe(new Subscriber<CodeBean>() {
							@Override
							public void onCompleted() {

							}

							@Override
							public void onError(Throwable e) {
								e.printStackTrace();
							}

							@Override
							public void onNext(CodeBean codeBean) {
								Log.e("photo: ", codeBean.getFlag()+"");
							}
						});
				// TODO: 2018/5/3 上传图片
				//把图片上传到服务器
//				NewsUtils.upImage(path, getApplicationContext());
			} catch (Exception e) {
				e.printStackTrace();
			}
			camera.startPreview();
			camera.setPreviewCallback(null);
			camera.stopPreview();
			camera.release();
			CameraActivity.this.finish();
		}
	};

	private void data2file(byte[] w, String fileName) throws Exception {// 将二进制数据转换为文件的函数
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(fileName);
			out.write(w);
			out.close();
		} catch (Exception e) {
			if (out != null)
				out.close();
			e.printStackTrace();
		}
	}

}
