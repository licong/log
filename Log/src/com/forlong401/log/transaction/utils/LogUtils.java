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
package com.forlong401.log.transaction.utils;

import java.io.File;

import android.content.Context;
import android.text.TextUtils;

/**
 * Log utility.
 * 
 * @author Li Cong
 * @date 2014-3-23
 */
public class LogUtils {
	public static boolean DEBUG = true;
	public static boolean CRASH_SAVE_2_FILE = true;
	public static boolean CRASH_UPLOAD_2_NETWORK = false;

	public static final int LOG_TYPE_2_LOGCAT = 0x01;
	public static final int LOG_TYPE_2_FILE = 0x02;
	public static final int LOG_TYPE_2_FILE_AND_LOGCAT = 0x03;
	public static final int LOG_TYPE_2_NETWORK = 0x04;

	public final static String LOG_CACHE_DIRECTORY_NAME = "log";
	public final static String CRASH_CACHE_DIRECTORY_NAME = "crash";
	private static String sLogFileName = null;
	private static String sCrashFileName = null;

	public static File getLogDir(Context context) {
		return Utils.getAppCacheDir(context, LOG_CACHE_DIRECTORY_NAME);
	}

	public static File getCrashDir(Context context) {
		return Utils.getAppCacheDir(context, CRASH_CACHE_DIRECTORY_NAME);
	}

	public static String getLogFileName(Context context) {
		if (TextUtils.isEmpty(sLogFileName)) {
			File sub = new File(getLogDir(context), "log_"
					+ System.currentTimeMillis() + ".txt");
			sLogFileName = sub.getAbsolutePath();
		}

		return sLogFileName;
	}

	public static String getCrashFileName(Context context) {
		if (TextUtils.isEmpty(sCrashFileName)) {
			File sub = new File(getCrashDir(context), "crash_"
					+ System.currentTimeMillis() + ".txt");
			sCrashFileName = sub.getAbsolutePath();
		}

		return sCrashFileName;
	}
}
