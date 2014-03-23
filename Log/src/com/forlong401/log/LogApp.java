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
package com.forlong401.log;

import android.app.Application;

import com.forlong401.log.transaction.log.manager.LogManager;

/**
 * Log application.
 * 
 * @author Li Cong
 * @date 2014-3-23
 */
public class LogApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		LogManager.getManager(getApplicationContext()).registerCrashHandler();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		LogManager.getManager(getApplicationContext()).unregisterCrashHandler();
	}
}