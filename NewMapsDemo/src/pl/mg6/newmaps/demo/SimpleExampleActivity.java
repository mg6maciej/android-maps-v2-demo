package pl.mg6.newmaps.demo;

import java.util.Locale;

import pl.mg6.newmaps.demo.util.GoogleMapHelper;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.LatLng;

public class SimpleExampleActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_example);

		GoogleMap map = GoogleMapHelper.getMap(this, R.id.simple_map);

		map.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng position) {
				handleOnClick("Click", position);
			}
		});

		map.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng position) {
				handleOnClick("LongClick", position);
			}
		});
	}

	private void handleOnClick(String id, LatLng position) {
		String text = String.format(Locale.US, "%s: %.2f, %.2f", id, position.latitude, position.longitude);
		Toast.makeText(SimpleExampleActivity.this, text, Toast.LENGTH_SHORT).show();
	}
}
