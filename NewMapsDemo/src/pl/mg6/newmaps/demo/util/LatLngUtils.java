package pl.mg6.newmaps.demo.util;

import com.google.android.gms.maps.model.LatLng;

public class LatLngUtils {

	private LatLngUtils() {
	}

	public static double distanceSquared(LatLng first, LatLng second) {
		double latDiff = Math.abs(first.latitude - second.latitude);
		double lngDiff = Math.abs(first.longitude - second.longitude);
		if (lngDiff > 180.0) {
			lngDiff = 360.0 - lngDiff;
		}
		return latDiff * latDiff + lngDiff * lngDiff;
	}
}
