package com.main.tools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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

	public void onClicked(View view) {
		// String text = view.getId() == R.id.JsonReader ? "Background" :
		// "Foreground";
		if (view.getId() == R.id.XMLReader) {
			Intent i = new Intent(getApplicationContext(),
					com.XMLReader.MainActivity.class);
			startActivity(i);
		} else if (view.getId() == R.id.ImageDownloader) {
			Intent i = new Intent(getApplicationContext(),
					com.Downloader.MainActivity.class);
			startActivity(i);
		} else if (view.getId() == R.id.JsonReader) {
			Intent i = new Intent(getApplicationContext(),
					com.JsonReader.MainActivity.class);
			startActivity(i);
		} else if (view.getId() == R.id.QRCode) {
			Intent i = new Intent(getApplicationContext(),
					com.QRCode.CameraTestActivity.class);
			startActivity(i);
		}
		// Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
}
