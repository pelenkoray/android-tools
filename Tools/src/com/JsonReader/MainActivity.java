package com.JsonReader;


import com.main.tools.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	private String url1 = "http://api.openweathermap.org/data/2.5/weather?q=";
	private EditText location, country, temperature, humidity, pressure;
	private HandleJSON obj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jsonreader);
		location = (EditText) findViewById(R.id.editLocation);
		country = (EditText) findViewById(R.id.editCountry);
		temperature = (EditText) findViewById(R.id.editTemp);
		humidity = (EditText) findViewById(R.id.editHumidity);
		pressure = (EditText) findViewById(R.id.editPressure);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items
		// to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void open(View view) {
		String url = location.getText().toString();
		String finalUrl = url1 + url;
		country.setText(finalUrl);
		obj = new HandleJSON(finalUrl);
		obj.fetchJSON();

		while (obj.parsingComplete)
			;
		country.setText(obj.getCountry());
		temperature.setText(String.valueOf((int) (Float.parseFloat(obj
				.getTemperature()) - 272.15)) + " C");
		humidity.setText(obj.getHumidity());
		pressure.setText(obj.getPressure());

	}
}
