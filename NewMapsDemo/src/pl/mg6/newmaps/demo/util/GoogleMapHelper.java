package pl.mg6.newmaps.demo.util;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public class GoogleMapHelper {

	private GoogleMapHelper() {
	}

	public static GoogleMap getMap(FragmentActivity activity, int id) {
		FragmentManager fm = activity.getSupportFragmentManager();
		SupportMapFragment f = (SupportMapFragment) fm.findFragmentById(id);
		return f.getMap();
	}
}
