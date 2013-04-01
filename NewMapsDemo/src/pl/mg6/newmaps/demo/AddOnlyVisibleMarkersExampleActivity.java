package pl.mg6.newmaps.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pl.mg6.newmaps.demo.util.GoogleMapHelper;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class AddOnlyVisibleMarkersExampleActivity extends FragmentActivity {

	private static final String TAG = AddOnlyVisibleMarkersExampleActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.many_markers_example);

		final GoogleMap map = GoogleMapHelper.getMap(this, R.id.many_markers_map);

		final List<LatLng> positions = new ArrayList<LatLng>();

		Random r = new Random();

		long start = SystemClock.uptimeMillis();
		for (int i = 0; i < ManyMarkersExampleActivity.MARKERS_COUNT; i++) {
			LatLng position = new LatLng((r.nextDouble() - 0.5) * 170.0, (r.nextDouble() - 0.5) * 360.0);
			positions.add(position);
		}
		long end = SystemClock.uptimeMillis();
		Log.w(TAG, "generate time: " + (end - start));

		map.setOnCameraChangeListener(new OnCameraChangeListener() {

			@Override
			public void onCameraChange(CameraPosition cameraPosition) {
				long start = SystemClock.uptimeMillis();
				int count = 0;
				Projection projection = map.getProjection();
				LatLngBounds bounds = projection.getVisibleRegion().latLngBounds;
				for (int i = positions.size() - 1; i >= 0; i--) {
					LatLng position = positions.get(i);
					if (bounds.contains(position)) {
						map.addMarker(new MarkerOptions().position(position));
						positions.remove(i);
						count++;
					}
				}
				long end = SystemClock.uptimeMillis();
				Log.w(TAG, "addMarkers time: " + (end - start) + ", added: " + count);
			}
		});
	}
}
