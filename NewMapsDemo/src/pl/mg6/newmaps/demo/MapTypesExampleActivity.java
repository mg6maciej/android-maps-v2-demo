package pl.mg6.newmaps.demo;

import java.util.ArrayList;
import java.util.List;

import pl.mg6.newmaps.demo.util.GoogleMapHelper;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.CameraPosition;

public class MapTypesExampleActivity extends FragmentActivity {

	private GoogleMap satelliteMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_types_example);

		final List<GoogleMap> maps = new ArrayList<GoogleMap>();

		final OnCameraChangeListener cameraChangeListener = new OnCameraChangeListener() {
			@Override
			public void onCameraChange(CameraPosition position) {
				CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(position);
				for (GoogleMap map : maps) {
					map.animateCamera(cameraUpdate, 200, null);
				}
			}
		};

		OnTouchListener overlayTouchListener = new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				for (GoogleMap map : maps) {
					map.setOnCameraChangeListener(null);
				}
				GoogleMap touchedMap = (GoogleMap) v.getTag();
				touchedMap.setOnCameraChangeListener(cameraChangeListener);
				return false;
			}
		};

		int[] mapIds = { R.id.normal_map, R.id.terrain_map, R.id.satellite_map };
		int[] overlayIds = { R.id.normal_overlay, R.id.terrain_overlay, R.id.satellite_overlay };

		for (int i = 0; i < mapIds.length; i++) {
			GoogleMap map = GoogleMapHelper.getMap(this, mapIds[i]);
			maps.add(map);

			if (mapIds[i] == R.id.satellite_map) {
				satelliteMap = map;
			}

			View overlay = findViewById(overlayIds[i]);
			overlay.setTag(map);
			overlay.setOnTouchListener(overlayTouchListener);
		}
	}

	public void onSatelliteHybridSwitchClick(View view) {
		if (satelliteMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE) {
			satelliteMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		} else {
			satelliteMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		}
	}
}
