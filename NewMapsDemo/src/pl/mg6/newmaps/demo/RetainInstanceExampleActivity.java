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
