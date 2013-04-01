package pl.mg6.newmaps.demo;

import java.util.Random;

import pl.mg6.newmaps.demo.util.GoogleMapHelper;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class ManyMarkersExampleActivity extends FragmentActivity {

	private static final String TAG = ManyMarkersExampleActivity.class.getSimpleName();

	public static final int MARKERS_COUNT = 25000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.many_markers_example);

		GoogleMap map = GoogleMapHelper.getMap(this, R.id.many_markers_map);

		Random r = new Random();

		long start = SystemClock.uptimeMillis();
		for (int i = 0; i < MARKERS_COUNT; i++) {
			LatLng position = new LatLng((r.nextDouble() - 0.5) * 170.0, (r.nextDouble() - 0.5) * 360.0);
			map.addMarker(new MarkerOptions().position(position));
		}
		long end = SystemClock.uptimeMillis();
		Log.w(TAG, "addMarkers time: " + (end - start));
	}
}
