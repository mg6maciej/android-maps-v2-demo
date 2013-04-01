package pl.mg6.newmaps.demo;

import pl.mg6.newmaps.demo.util.GoogleMapHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class InfoWindowExampleActivity extends FragmentActivity {

	private static final LatLng GNIEZNO_LOCATION = new LatLng(52.5349253, 17.5826575);
	private static final LatLng CRACOW_LOCATION = new LatLng(50.06465, 19.9449799);
	private static final LatLng WARSAW_LOCATION = new LatLng(52.2296756, 21.0122287);
	private Marker gnieznoMarker;
	private Marker cracowMarker;
	private Marker warsawMarker;

	private Handler handler = new Handler();
	private long startTime = SystemClock.uptimeMillis() / 1000;

	private Runnable refreshInfoWindow = new Runnable() {

		@Override
		public void run() {
			if (cracowMarker.isInfoWindowShown()) {
				cracowMarker.showInfoWindow();
			}
			if (warsawMarker.isInfoWindowShown()) {
				warsawMarker.showInfoWindow();
			}
			handler.postDelayed(this, 2222);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_window_example);

		GoogleMap map = GoogleMapHelper.getMap(this, R.id.info_window_map);

		setInfoWindowAdapter(map);
		setInfoWindowClickListener(map);

		addGnieznoMarker(map);
		addCracowMarker(map);
		addWarsawMarker(map);

		if (savedInstanceState == null) {
			moveCameraToShowMarkers(map);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		handler.post(refreshInfoWindow);
	}

	@Override
	protected void onPause() {
		super.onPause();
		handler.removeCallbacks(refreshInfoWindow);
	}

	private void setInfoWindowAdapter(GoogleMap map) {
		map.setInfoWindowAdapter(new InfoWindowAdapter() {

			@Override
			public View getInfoWindow(Marker marker) {
				if (warsawMarker.equals(marker)) {
					return createView(R.layout.warsaw_info_window, 1711324, 3309);
				}
				return null;
			}

			@Override
			public View getInfoContents(Marker marker) {
				if (cracowMarker.equals(marker)) {
					return createView(R.layout.cracow_info_contents, 759137, 2232);
				}
				return null;
			}

			private View createView(int layoutId, int population, int density) {
				View view = getLayoutInflater().inflate(layoutId, null);
				TextView snippet = (TextView) view.findViewById(R.id.snippet);
				long currentTime = SystemClock.uptimeMillis() / 1000;
				int bornCount = (int) (currentTime - startTime);
				snippet.setText("Populacja: " + (population + bornCount) + "\nGêstoœæ: " + density + " os./km^2");
				return view;
			}
		});
	}

	private void setInfoWindowClickListener(GoogleMap map) {
		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {
				if (gnieznoMarker.equals(marker)) {
					gnieznoMarker.setSnippet("Pierwsza stolica Polski\ndo ok. 1039r.");
					gnieznoMarker.showInfoWindow();
				}
				if (warsawMarker.equals(marker)) {
					cracowMarker.showInfoWindow();
				}
				if (cracowMarker.equals(marker)) {
					cracowMarker.hideInfoWindow();
				}
			}
		});
	}

	private void addGnieznoMarker(GoogleMap map) {
		MarkerOptions options = new MarkerOptions().position(GNIEZNO_LOCATION).title("Gniezno").snippet("Pierwsza stolica Polski");
		gnieznoMarker = map.addMarker(options);
	}

	private void addCracowMarker(GoogleMap map) {
		BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
		MarkerOptions options = new MarkerOptions().position(CRACOW_LOCATION).icon(icon);
		cracowMarker = map.addMarker(options);
	}

	private void addWarsawMarker(GoogleMap map) {
		BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
		MarkerOptions options = new MarkerOptions().position(WARSAW_LOCATION).icon(icon);
		warsawMarker = map.addMarker(options);
	}

	private void moveCameraToShowMarkers(GoogleMap map) {
		LatLngBounds bounds = LatLngBounds.builder().include(GNIEZNO_LOCATION).include(CRACOW_LOCATION).include(WARSAW_LOCATION).build();
		int width = getResources().getDisplayMetrics().widthPixels;
		int height = getResources().getDisplayMetrics().heightPixels;
		int padding = getResources().getDimensionPixelSize(R.dimen.padding);
		map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));
	}
}
