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
package pl.mg6.newmaps.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	private static final ExampleStartInfo[] data = { new ExampleStartInfo(SimpleExampleActivity.class, "Simple"),
			new ExampleStartInfo(ConfigurationExampleActivity.class, "Configuration"), new ExampleStartInfo(MapTypesExampleActivity.class, "Map Types"),
			new ExampleStartInfo(MarkersExampleActivity.class, "Markers"), new ExampleStartInfo(InfoWindowExampleActivity.class, "Info Window"),
			new ExampleStartInfo(ShapesExampleActivity.class, "Shapes"), new ExampleStartInfo(TileOverlayExampleActivity.class, "Tile Overlay"),
			new ExampleStartInfo(ManyMarkersExampleActivity.class, "Too Many Markers"),
			new ExampleStartInfo(AddOnlyVisibleMarkersExampleActivity.class, "Add Only Visible Markers"),
			new ExampleStartInfo(AddMarkersInBackgroundExampleActivity.class, "Add Markers In Background"),
			new ExampleStartInfo(RetainInstanceExampleActivity.class, "Retain Instance (Don't)"),
			new ExampleStartInfo(AnimateCameraChainingExampleActivity.class, "Animate Camera Chaining"), new ExampleStartInfo(AboutActivity.class, "About"), };

	private boolean available;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ArrayAdapter<ExampleStartInfo> adapter = new ArrayAdapter<ExampleStartInfo>(this, android.R.layout.simple_list_item_1, data);

		ListView listView = (ListView) findViewById(R.id.main_listview);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				startExample(position);
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		available = GooglePlayServicesErrorDialogFragment.showDialogIfNotAvailable(this);
	}

	private void startExample(int position) {
		Class<? extends Activity> cls = data[position].activityClass;
		if (available || AboutActivity.class.equals(cls)) {
			Intent intent = new Intent(this, cls);
			startActivity(intent);
		} else {
			Toast.makeText(getApplication(), "Not available", Toast.LENGTH_SHORT).show();
		}
	}

	private static class ExampleStartInfo {

		private final Class<? extends Activity> activityClass;
		private final String displayName;

		public ExampleStartInfo(Class<? extends Activity> activityClass, String displayName) {
			this.activityClass = activityClass;
			this.displayName = displayName;
		}

		@Override
		public String toString() {
			return displayName;
		}
	}
}
