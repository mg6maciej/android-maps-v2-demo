/*
 * Copyright (C) 2013 Maciej Górski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.mg6.newmaps.demo;

import pl.mg6.newmaps.demo.util.GoogleMapHelper;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class ConfigurationExampleActivity extends FragmentActivity {

	private TextView infoTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configuration_example);

		GoogleMap map = GoogleMapHelper.getMap(this, R.id.confugured_map);
		final UiSettings settings = map.getUiSettings();

		map.setMyLocationEnabled(true);
		settings.setMyLocationButtonEnabled(false);

		infoTextView = (TextView) findViewById(R.id.info_textview);
		map.setOnCameraChangeListener(new OnCameraChangeListener() {
			@Override
			public void onCameraChange(CameraPosition position) {
				updatePositionInfo(position);
			}
		});
		updatePositionInfo(map.getCameraPosition());

		CheckBox controlsCheckBox = (CheckBox) findViewById(R.id.controls_checkbox);
		controlsCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				settings.setCompassEnabled(isChecked);
				settings.setMyLocationButtonEnabled(isChecked);
				settings.setZoomControlsEnabled(isChecked);
			}
		});
		CheckBox gesturesCheckBox = (CheckBox) findViewById(R.id.gestures_checkbox);
		gesturesCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				settings.setAllGesturesEnabled(isChecked);
				// or
				settings.setRotateGesturesEnabled(isChecked);
				settings.setScrollGesturesEnabled(isChecked);
				settings.setTiltGesturesEnabled(isChecked);
				settings.setZoomGesturesEnabled(isChecked);
			}
		});
	}

	private void updatePositionInfo(CameraPosition pos) {
		String format = "latitude: %.2f longitude: %.2f zoom: %.2f bearing: %.2f tilt: %.2f";
		LatLng t = pos.target;
		String text = String.format(format, t.latitude, t.longitude, pos.zoom, pos.bearing, pos.tilt);
		infoTextView.setText(text);
	}
}
