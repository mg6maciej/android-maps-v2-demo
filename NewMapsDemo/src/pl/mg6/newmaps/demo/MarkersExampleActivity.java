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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import pl.mg6.newmaps.demo.util.GoogleMapHelper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Align;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MarkersExampleActivity extends FragmentActivity {

	private Map<Marker, String> data = new HashMap<Marker, String>();
	private TextView infoTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.markers_example);

		infoTextView = (TextView) findViewById(R.id.info_textview);

		GoogleMap map = GoogleMapHelper.getMap(this, R.id.markers_map);

		setMarkerClickListener(map);
		setMarkerDragListener(map);
		setInfoWindowClickListener(map);

		addDefaultMarker(map);
		addPredefinedMarkers(map);
		addColorfulMarkersCircle(map);
		addCustomMarkers(map);
	}

	private void setMarkerClickListener(GoogleMap map) {
		map.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				updateInfo("click", marker);
				boolean isRed = "red".equals(marker.getTitle());
				if (isRed) {
					marker.showInfoWindow();
					return true;
				}
				return false;
			}
		});
	}

	private void setMarkerDragListener(GoogleMap map) {
		map.setOnMarkerDragListener(new OnMarkerDragListener() {
			@Override
			public void onMarkerDragStart(Marker marker) {
				updateInfo("drag start", marker);
			}

			@Override
			public void onMarkerDrag(Marker marker) {
				LatLng p = marker.getPosition();
				updateInfo(String.format(Locale.US, "drag %.1f %.1f", p.latitude, p.longitude), marker);
			}

			@Override
			public void onMarkerDragEnd(Marker marker) {
				updateInfo("drag end", marker);
			}
		});
	}

	private void setInfoWindowClickListener(GoogleMap map) {
		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			@Override
			public void onInfoWindowClick(Marker marker) {
				updateInfo("info window click", marker);
				if (marker.getSnippet() == null) {
					marker.hideInfoWindow();
					for (Marker m : data.keySet()) {
						m.setVisible(true);
					}
				} else {
					marker.setVisible(false);
				}
			}
		});
	}

	private void addDefaultMarker(GoogleMap map) {
		LatLng position = new LatLng(0.0, 0.0);
		String title = "red";
		MarkerOptions options = new MarkerOptions().position(position).title(title);

		Marker marker = map.addMarker(options);
		data.put(marker, "default (red)");
	}

	private void addPredefinedMarkers(GoogleMap map) {
		float[] hues = { BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_YELLOW, BitmapDescriptorFactory.HUE_GREEN,
				BitmapDescriptorFactory.HUE_CYAN, BitmapDescriptorFactory.HUE_BLUE, BitmapDescriptorFactory.HUE_MAGENTA, };
		String[] names = { "red", "yellow", "green", "cyan", "blue", "magenta", };
		for (int i = 0; i < hues.length; i++) {
			double scaled = 2.0 * i / (hues.length - 1) - 1.0; // -1 to 1

			LatLng position = new LatLng(7.0 * Math.abs(scaled) - 15.0, 10.0 * scaled);
			BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(hues[i]);
			String title = names[i];
			String snippet = String.format(Locale.US, "hue value: %.0f", hues[i]);
			MarkerOptions options = new MarkerOptions().position(position).icon(icon).title(title).snippet(snippet);

			Marker marker = map.addMarker(options);
			data.put(marker, "predefined (" + names[i] + ")");
		}
	}

	private void addColorfulMarkersCircle(GoogleMap map) {
		for (int degree = 9; degree < 360; degree += 18) {
			double rad = Math.toRadians(degree);

			LatLng position = new LatLng(30.0 * Math.cos(rad), 20.0 * Math.sin(rad));
			BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(degree);
			boolean draggable = true;
			MarkerOptions options = new MarkerOptions().position(position).icon(icon).draggable(draggable);

			Marker marker = map.addMarker(options);
			data.put(marker, "value (" + degree + ")");
		}
	}

	private void addCustomMarkers(GoogleMap map) {
		LatLng position = new LatLng(15.0, 0.0);

		BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.arrow_right);
		MarkerOptions options = new MarkerOptions().position(position).icon(icon).anchor(1.0f, 0.5f).title("right arrow").draggable(true);
		Marker marker = map.addMarker(options);
		data.put(marker, "right arrow");

		Bitmap bitmap = prepareBitmap();
		icon = BitmapDescriptorFactory.fromBitmap(bitmap);
		options = new MarkerOptions().position(position).icon(icon).anchor(0.0f, 0.5f).title("left arrow").draggable(true);
		marker = map.addMarker(options);
		data.put(marker, "left arrow");
	}

	private Bitmap prepareBitmap() {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_left);
		bitmap = bitmap.copy(Config.ARGB_8888, true);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(getResources().getDimension(R.dimen.text_size));
		String text = "mg6";
		Rect bounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), bounds);
		float x = bitmap.getWidth() / 2.0f;
		float y = (bitmap.getHeight() - bounds.height()) / 2.0f - bounds.top;
		canvas.drawText(text, x, y, paint);
		return bitmap;
	}

	private void updateInfo(String action, Marker marker) {
		String markerData = data.get(marker);
		infoTextView.setText("action: " + action + " on: " + markerData);
	}
}
