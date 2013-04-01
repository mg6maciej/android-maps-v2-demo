package pl.mg6.newmaps.demo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import pl.mg6.newmaps.demo.util.GoogleMapHelper;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;

public class TileOverlayExampleActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tiles_example);

		GoogleMap map = GoogleMapHelper.getMap(this, R.id.tiles_map);

		TileProvider provider = new UrlTileProvider(256, 256) {

			private static final String URL_FORMAT = "http://mw1.google.com/mw-planetary/lunar/lunarmaps_v1/clem_bw/%3$d/%1$d/%2$d.jpg";

			@Override
			public URL getTileUrl(int x, int y, int zoom) {
				try {
					y = (1 << zoom) - y - 1;
					return new URL(String.format(Locale.US, URL_FORMAT, x, y, zoom));
				} catch (MalformedURLException e) {
					throw new RuntimeException();
				}
			}
		};
		map.addTileOverlay(new TileOverlayOptions().tileProvider(provider));
	}
}
