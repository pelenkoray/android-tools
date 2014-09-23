package com.Downloader;

import java.io.File;

import com.library.downloader.Downloader;
import com.library.downloader.DownloaderStatus;
import com.main.tools.R;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.constants.Constant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ProgressDialog simpleWaitDialog;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imagedownloader);
		buttonConfig();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * created by koray button configurations
	 */

	public void buttonConfig() {
		ImageButton btnSend = (ImageButton) findViewById(R.id.buttonSend);

		handler = new Handler() {
			@Override
			public void dispatchMessage(Message msg) {
				Context context = getApplicationContext();
				int duration = Toast.LENGTH_SHORT;
				CharSequence text = "";
				Toast toast;
				if (msg.what == 0) {
					text = "Image Downloaded!";
					toast = Toast.makeText(context, text, duration);
					toast.show();
					try {
						File root = Environment.getExternalStorageDirectory();
						String localFilePath = root.getPath() + "/"
								+ Constant.dir + "/" + Constant.url;
						File imgFile = new File(localFilePath);
						if (imgFile.exists()) {

							Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
									.getAbsolutePath());

							ImageView myImage = (ImageView) findViewById(R.id.imageView);
							myImage.setImageBitmap(myBitmap);

						}
					} catch (Exception e) {
						text = "File cant be openned!";
						toast = Toast.makeText(context, text, duration);
						toast.show();
					}
				} else if (msg.what == -1) {
					text = "Download Error!";
					toast = Toast.makeText(context, text, duration);
					toast.show();

				} else {
					text = "Save Error!";
					toast = Toast.makeText(context, text, duration);
					toast.show();
				}
			}
		};

		btnSend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				EditText txtUrl = (EditText) findViewById(R.id.textUrl);
				String url = txtUrl.getText().toString();
				// simpleWaitDialog = ProgressDialog.show(MainActivity.this,
				// "Wait", "Downloading Image");

				simpleWaitDialog = new ProgressDialog(MainActivity.this);
				simpleWaitDialog
						.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				simpleWaitDialog.setMessage("Downloading Image");
				simpleWaitDialog.setTitle("Wait");
				simpleWaitDialog.show();

				if (url.equals("")) {
					url = "http://upload.wikimedia.org/wikipedia/tr/4/48/Kaptan_Resim_1.jpg";
				} else if (!url.contains("http://")) {
					url = "http://" + url;
				}
				String[] urlArr = url.split("/");
				Constant.url = urlArr[urlArr.length - 1];
				AsyncTask<String, Integer, DownloaderStatus> downloadFile = null;
				if (Build.VERSION.SDK_INT >= 11) {

					downloadFile = new Downloader(simpleWaitDialog, handler)
							.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
									url);
				} else {
					downloadFile = new Downloader(simpleWaitDialog, handler)
							.execute(url);
				}
			}
		});
	}
}
