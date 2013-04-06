/*
 * Copyright (C) 2013 Maciej Górski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
