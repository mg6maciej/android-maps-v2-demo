package pl.mg6.newmaps.demo;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import pl.mg6.newmaps.demo.util.GoogleMapHelper;
import pl.mg6.newmaps.demo.util.LatLngUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AddMarkersInBackgroundExampleActivity extends FragmentActivity {

	private static final String TAG = AddMarkersInBackgroundExampleActivity.class.getSimpleName();

	private GoogleMap map;

	private LatLngDistance[] positions = new LatLngDistance[ManyMarkersExampleActivity.MARKERS_COUNT];
	private int count = ManyMarkersExampleActivity.MARKERS_COUNT;

	private Handler handler = new Handler();
	private Runnable batchAddMarkersRunnable = new Runnable() {

		private static final int ADD_IN_BATCH = 10;
		private static final int DELAY = 1;

		private LatLng previousCameraPosition;

		@Override
		public void run() {
			if (count == 0) {
				return;
			}
			// sortPositionsIfNeeded();
			long start = SystemClock.uptimeMillis();
			for (int i = 0; i < ADD_IN_BATCH; i++) {
				count--;
				LatLngDistance position = positions[count];
				positions[count] = null;
				map.addMarker(new MarkerOptions().position(position.latLng));
			}
			long end = SystemClock.uptimeMillis();
			Log.w(TAG, "addMarkers time: " + (end - start));
			handler.postDelayed(this, DELAY);
		}

		private void sortPositionsIfNeeded() {
			final LatLng cameraPosition = map.getCameraPosition().target;
			if (previousCameraPosition == null || LatLngUtils.distanceSquared(previousCameraPosition, cameraPosition) > 400.0) {
				previousCameraPosition = cameraPosition;
				long start = SystemClock.uptimeMillis();
				for (LatLngDistance p : positions) {
					p.distance = LatLngUtils.distanceSquared(cameraPosition, p.latLng);
				}
				Arrays.sort(positions, new Comparator<LatLngDistance>() {
					public int compare(LatLngDistance lhs, LatLngDistance rhs) {
						return Double.compare(rhs.distance, lhs.distance);
					}
				});
				long end = SystemClock.uptimeMillis();
				Log.w(TAG, "sort time: " + (end - start));
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.many_markers_example);

		map = GoogleMapHelper.getMap(this, R.id.many_markers_map);

		Random r = new Random();

		long start = SystemClock.uptimeMillis();
		for (int i = 0; i < ManyMarkersExampleActivity.MARKERS_COUNT; i++) {
			LatLng position = new LatLng((r.nextDouble() - 0.5) * 170.0, (r.nextDouble() - 0.5) * 360.0);
			positions[i] = new LatLngDistance(position);
		}
		long end = SystemClock.uptimeMillis();
		Log.w(TAG, "generate time: " + (end - start));
	}

	@Override
	protected void onResume() {
		super.onResume();
		handler.post(batchAddMarkersRunnable);
	}

	@Override
	protected void onPause() {
		super.onPause();
		handler.removeCallbacks(batchAddMarkersRunnable);
	}

	private static class LatLngDistance {

		private LatLng latLng;
		private double distance;

		private LatLngDistance(LatLng latLng) {
			this.latLng = latLng;
		}
	}
}
