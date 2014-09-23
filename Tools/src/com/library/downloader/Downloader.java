package com.library.downloader;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.constants.Constant;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

public class Downloader extends AsyncTask<String, Integer, DownloaderStatus> {
	ProgressBar mProgressBar;
	ProgressDialog mProgressDialog;
	Handler mHandler;

	HttpURLConnection mConn;

	double downloaded = 0;
	boolean isDownloading = true;

	public static DownloaderStatus DownloadResult;

	private static final int MAX_BUFFER_SIZE = 256;

	public Downloader(ProgressDialog dialog, Handler handler) {
		this.mProgressDialog = dialog;
		this.mProgressBar = null;
		this.mHandler = handler;
	}

	public Downloader(ProgressBar bar, Handler handler) {
		this.mProgressDialog = null;
		this.mProgressBar = bar;
		this.mHandler = handler;
	}

	@Override
	protected void onPreExecute() {
		if (mProgressDialog != null) {
			mProgressDialog.setProgress(0);
			mProgressDialog.setMax(100);
			mProgressDialog.show();
		}

		if (mProgressBar != null) {
			mProgressBar.setProgress(0);
			mProgressBar.setMax(100);
		}
	}

	@Override
	protected DownloaderStatus doInBackground(String... fileUrl) {
		// CacheClean
		// cacheCleaner();

		try {
			mConn = (HttpURLConnection) new URL(fileUrl[0]).openConnection();
			double fileSize = mConn.getContentLength();
			ByteArrayOutputStream out = new ByteArrayOutputStream(
					(int) fileSize);
			mConn.connect();

			Log.e("File Size", String.valueOf(fileSize));

			InputStream stream = mConn.getInputStream();

			while (isDownloading) {
				byte buffer[];

				if (fileSize - downloaded >= MAX_BUFFER_SIZE)
					buffer = new byte[MAX_BUFFER_SIZE];
				else
					buffer = new byte[(int) (fileSize - downloaded)];

				int read = stream.read(buffer);

				if (read == -1) {
					if (mProgressDialog != null)
						mProgressDialog.setProgress(100);

					if (mProgressBar != null)
						mProgressBar.setProgress(100);
					break;
				}

				out.write(buffer, 0, read);
				downloaded += read;

				// Log.e("Downloaded", String.valueOf(downloaded) + "/" +
				// String.valueOf(fileSize));

				if (mProgressDialog != null)
					mProgressDialog
							.setProgress((int) ((downloaded / fileSize) * 100));

				if (mProgressBar != null)
					mProgressBar
							.setProgress((int) ((downloaded / fileSize) * 100));
			}

			if (isDownloading)
				isDownloading = false;

			try {
				File root = Environment.getExternalStorageDirectory();
				try {
					File dir = new File(root.getPath() + "/" + Constant.dir);
					if (!dir.exists())
						dir.mkdirs();
				} catch (Exception e) {
					e.printStackTrace();
					return DownloaderStatus.FileSaveError;
				}

				File file = new File(root, Constant.dir + "/" + Constant.url);

				if (file.exists())
					file.delete();

				String localFilePath = root.getPath() + "/" + Constant.dir
						+ "/" + Constant.url;

				FileOutputStream fos = new FileOutputStream(localFilePath,
						false);
				OutputStream os = new BufferedOutputStream(fos);

				out.writeTo(os);

				fos.close();
			} catch (Exception e) {
				e.printStackTrace();

				return DownloaderStatus.FileSaveError;
			}

			return DownloaderStatus.Success;
		} catch (Exception e) {
			e.printStackTrace();
			return DownloaderStatus.DownloadError;
		}
	}

	@Override
	protected void onCancelled() {
		mConn.disconnect();

		super.onCancelled();
	}

	@Override
	protected void onPostExecute(DownloaderStatus result) {
		DownloadResult = result;

		if (mProgressDialog != null) {
			if (mProgressDialog.isShowing())
				mProgressDialog.dismiss();
		}

		if (DownloadResult == DownloaderStatus.Success)
			mHandler.sendEmptyMessage(0);
		else if (DownloadResult == DownloaderStatus.DownloadError)
			mHandler.sendEmptyMessage(-1);
		else
			mHandler.sendEmptyMessage(-2);
	}

	public void closeConnection() {
		if (mConn != null) {
			mConn.disconnect();
			mConn = null;
		}
	}

	public void setProgressDialog(ProgressDialog prg) {
		this.mProgressDialog = prg;
	}

	/*
	 * private void cacheCleaner() { try { File root =
	 * Environment.getExternalStorageDirectory(); File downloadFolder = new
	 * File(root, Constant.url);
	 * 
	 * if (downloadFolder.isDirectory()) { String[] files =
	 * downloadFolder.list();
	 * 
	 * if (files == null) return;
	 * 
	 * if (files.length == 0) return;
	 * 
	 * for (String file : files) { new File(downloadFolder, file).delete(); } }
	 * } catch (Exception e) { e.printStackTrace(); } }
	 */
}
