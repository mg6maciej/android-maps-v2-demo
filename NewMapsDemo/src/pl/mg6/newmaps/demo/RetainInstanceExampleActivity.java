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

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

// http://code.google.com/p/gmaps-api-issues/issues/detail?id=5103
public class RetainInstanceExampleActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_example);

		if (savedInstanceState == null) {

			FragmentManager fm = getSupportFragmentManager();
			SupportMapFragment f = (SupportMapFragment) fm.findFragmentById(R.id.simple_map);

			// NEVER do it: this leaks first activity
			f.setRetainInstance(true);

			GoogleMap map = f.getMap();
			map.addMarker(new MarkerOptions().position(new LatLng(0.0, 0.0)));
		}
	}
}
