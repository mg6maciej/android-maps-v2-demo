package pl.mg6.newmaps.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pl.mg6.newmaps.demo.util.GoogleMapHelper;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class ShapesExampleActivity extends FragmentActivity {

	private static final double ONE_THOUSAND_KM = 1000000.0;

	private Circle circle;

	private Polygon polygon;

	private Polyline polyline;

	private GroundOverlay groundOverlay;

	private Handler handler = new Handler();
	private long startTime = SystemClock.uptimeMillis();
	private Runnable rotateThingsRunnable = new Runnable() {

		@Override
		public void run() {
			long currentTime = SystemClock.uptimeMillis();
			double diff = (currentTime - startTime) / 2222.2222;
			List<LatLng> points = new ArrayList<LatLng>();
			points.add(new LatLng(30.0, 30.0));
			double lat = 30.0 * Math.sin(diff);
			double lng = -100.0 * Math.cos(diff);
			points.add(new LatLng(30.0 + lat, 30.0 + lng));
			polyline.setPoints(points);

			groundOverlay.setBearing(groundOverlay.getBearing() + 1.0f);

			handler.postDelayed(this, 50);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shapes_example);

		GoogleMap map = GoogleMapHelper.getMap(this, R.id.shapes_map);

		map.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng position) {
				LatLng center = circle.getCenter();
				float[] results = new float[1];
				Location.distanceBetween(center.latitude, center.longitude, position.latitude, position.longitude, results);
				if (circle.getRadius() > results[0]) {
					circle.setRadius(results[0]);
				}
				Log.i("latlng", String.format(Locale.US, "options.add(new LatLng(%.4f, %.4f));", position.latitude, position.longitude));
			}
		});

		addCircle(map);
		addPolygon(map);
		addPolyline(map);
		addGroundOverlay(map);
	}

	private void addCircle(GoogleMap map) {
		LatLng center = new LatLng(-20.0, 0.0);
		double radius = ONE_THOUSAND_KM;
		int fillColor = 0x6600ff99;
		int strokeColor = 0xaa00ff99;
		float strokeWidth = getResources().getDimension(R.dimen.circle_stroke_width);
		CircleOptions options = new CircleOptions().center(center).radius(radius).fillColor(fillColor).strokeColor(strokeColor).strokeWidth(strokeWidth)
				.zIndex(1.0f);

		circle = map.addCircle(options);
	}

	private void addPolygon(GoogleMap map) {
		int fillColor = 0x44ffffff;
		int strokeColor = 0x88ffffff;
		float strokeWidth = getResources().getDimension(R.dimen.polygon_stroke_width);
		PolygonOptions options = new PolygonOptions().fillColor(fillColor).strokeColor(strokeColor).strokeWidth(strokeWidth).zIndex(0.0f);

		options.add(new LatLng(33.8070, -10.7504));
		options.add(new LatLng(58.7526, -13.0387));
		options.add(new LatLng(65.6205, 8.7599));
		options.add(new LatLng(71.6195, 19.9833));
		options.add(new LatLng(72.0245, 34.4512));
		options.add(new LatLng(68.3408, 44.1491));
		options.add(new LatLng(48.6029, 45.2666));
		options.add(new LatLng(43.4643, 34.3700));
		options.add(new LatLng(39.4653, 25.6528));
		options.add(new LatLng(34.0572, 27.6142));
		options.add(new LatLng(35.2226, 13.8846));
		options.add(new LatLng(38.7890, 11.9232));
		options.add(new LatLng(36.1078, -5.7291));

		List<LatLng> hole = new ArrayList<LatLng>();
		hole.add(new LatLng(54.3062, 13.1940));
		hole.add(new LatLng(55.5966, 19.3098));
		hole.add(new LatLng(54.8401, 24.0624));
		hole.add(new LatLng(52.3033, 24.9097));
		hole.add(new LatLng(50.2762, 25.2413));
		hole.add(new LatLng(48.5262, 23.2150));
		hole.add(new LatLng(48.5750, 18.5729));
		hole.add(new LatLng(50.7447, 13.5256));
		options.addHole(hole);

		polygon = map.addPolygon(options);
	}

	private void addPolyline(GoogleMap map) {
		int color = 0xaa0000ff;
		float width = getResources().getDimension(R.dimen.polyline_width);
		PolylineOptions options = new PolylineOptions().color(color).width(width).zIndex(2.0f);

		polyline = map.addPolyline(options);
	}

	private void addGroundOverlay(GoogleMap map) {
		LatLng position = new LatLng(46.7540, 2.7522);
		int width = 1000000;
		BitmapDescriptor image = BitmapDescriptorFactory.fromResource(R.drawable.star);
		GroundOverlayOptions options = new GroundOverlayOptions().position(position, width).image(image).zIndex(3.0f);

		groundOverlay = map.addGroundOverlay(options);

		position = new LatLng(27.6035, 18.1508);
		width = 1500000;
		options = new GroundOverlayOptions().position(position, width).image(image).bearing(180.0f).transparency(0.5f).zIndex(3.0f);

		map.addGroundOverlay(options);
	}

	@Override
	protected void onResume() {
		super.onResume();
		handler.post(rotateThingsRunnable);
	}

	@Override
	protected void onPause() {
		super.onPause();
		handler.removeCallbacks(rotateThingsRunnable);
	}

	public void onExpandCircleClick(View view) {
		if (circle.getRadius() < 7 * ONE_THOUSAND_KM) {
			circle.setRadius(circle.getRadius() + ONE_THOUSAND_KM);
		}
	}

	public void onRemoveHoleClick(View view) {
		polygon.setHoles(new ArrayList<List<LatLng>>());
		view.setEnabled(false);
	}

	public void onGeodesicClick(View view) {
		polyline.setGeodesic(!polyline.isGeodesic());
	}
}
