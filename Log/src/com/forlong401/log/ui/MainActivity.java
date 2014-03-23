/*
 * Copyright (C) 2014 Li Cong, forlong401@163.com http://www.360qihoo.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.forlong401.log.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.forlong401.log.R;
import com.forlong401.log.transaction.log.manager.LogManager;
import com.forlong401.log.transaction.utils.LogUtils;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		LogManager.getManager(getApplicationContext()).registerActivity(this);
		LogManager.getManager(getApplicationContext()).log(TAG, "onCreate",
				LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);
		String crashNullException = null;
		crashNullException.charAt(1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		LogManager.getManager(getApplicationContext()).unregisterActivity(this);
	}

}
