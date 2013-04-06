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

import java.util.ArrayList;
import java.util.List;

import pl.mg6.newmaps.demo.util.GoogleMapHelper;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class AnimateCameraChainingExampleActivity extends FragmentActivity {

	private static final String TAG = AnimateCameraChainingExampleActivity.class.getSimpleName();

	private GoogleMap map;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_example);

		map = GoogleMapHelper.getMap(this, R.id.simple_map);
		map.addMarker(new MarkerOptions().position(new LatLng(20.0, 20.0)));

		map.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				final LatLng position = marker.getPosition();
				map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 14.0f), 1000, new CancelableCallback() {

					@Override
					public void onFinish() {
						Projection projection = map.getProjection();
						Point point = projection.toScreenLocation(position);
						point.x -= 100;
						point.y -= 100;
						LatLng offsetPosition = projection.fromScreenLocation(point);
						map.animateCamera(CameraUpdateFactory.newLatLng(offsetPosition), 300, null);
					}

					@Override
					public void onCancel() {
					}
				});
				return true;
			}
		});

		map.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng position) {
				List<CameraUpdate> updates = new ArrayList<CameraUpdate>();
				CameraPosition.Builder builder = CameraPosition.builder();
				builder.target(position);
				updates.add(CameraUpdateFactory.newCameraPosition(builder.build()));
				builder.target(new LatLng(position.latitude + 20, position.longitude));
				updates.add(CameraUpdateFactory.newCameraPosition(builder.build()));
				builder.bearing(90);
				updates.add(CameraUpdateFactory.newCameraPosition(builder.build()));
				builder.target(new LatLng(position.latitude + 20, position.longitude + 40));
				updates.add(CameraUpdateFactory.newCameraPosition(builder.build()));
				builder.bearing(180);
				updates.add(CameraUpdateFactory.newCameraPosition(builder.build()));
				builder.target(new LatLng(position.latitude, position.longitude + 40));
				updates.add(CameraUpdateFactory.newCameraPosition(builder.build()));
				builder.bearing(270);
				updates.add(CameraUpdateFactory.newCameraPosition(builder.build()));
				builder.target(position);
				updates.add(CameraUpdateFactory.newCameraPosition(builder.build()));

				loopAnimateCamera(updates);
			}
		});

		map.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng position) {
				map.stopAnimation();
			}
		});
	}

	private void loopAnimateCamera(final List<CameraUpdate> updates) {
		CameraUpdate update = updates.remove(0);
		updates.add(update);
		map.animateCamera(update, 1000, new CancelableCallback() {

			@Override
			public void onFinish() {
				loopAnimateCamera(updates);
			}

			@Override
			public void onCancel() {
				Log.i(TAG, "camera animation cancelled");
			}
		});
	}
}
