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
package com.forlong401.log.transaction.log.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.forlong401.log.transaction.log.task.LogTask;
import com.forlong401.log.transaction.utils.LogUtils;
import com.forlong401.log.transaction.utils.Utils;

/**
 * Log manager implement.
 * 
 * @author Li Cong
 * @date 2014-3-23
 */
public class LogManagerImpl implements ILogManager, UncaughtExceptionHandler {
	private static final String TAG = "LogManagerImpl";
	private final static int EXECUTOR_HANDLE_THREAD_PRIORITY = Thread.NORM_PRIORITY - 1;

	private Context mContext;
	private ExecutorService mExecutorService = null;
	// The available numbers of threads in executor service.
	private int mAvailableProcessors = 1;
	private static Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;
	private ArrayList<WeakReference<Activity>> mActivityList = new ArrayList<WeakReference<Activity>>();

	static {
		mDefaultUncaughtExceptionHandler = Thread
				.getDefaultUncaughtExceptionHandler();
	}

	LogManagerImpl(Context context) {
		mContext = context;
		mAvailableProcessors = Runtime.getRuntime().availableProcessors();
	}

	private void checkExecutor() {
		if (mExecutorService == null || mExecutorService.isShutdown()) {
			if (mAvailableProcessors < 0) {
				mAvailableProcessors = 1;
			}
			mExecutorService = Executors.newFixedThreadPool(
					mAvailableProcessors, new ThreadFactory() {
						@Override
						public Thread newThread(Runnable r) {
							Thread t = new Thread(r);
							t.setPriority(EXECUTOR_HANDLE_THREAD_PRIORITY);
							t.setName(TAG);
							return t;
						}
					});
		}
	}

	private void submitTask(final Runnable runnable) {
		checkExecutor();
		mExecutorService.submit(runnable);
	}

	@Override
	public boolean registerCrashHandler() {
		Thread.setDefaultUncaughtExceptionHandler(this);
		return true;
	}

	@Override
	public boolean unregisterCrashHandler() {
		Thread.setDefaultUncaughtExceptionHandler(mDefaultUncaughtExceptionHandler);
		return true;
	}

	@Override
	public boolean registerActivity(Activity activity) {
		if (activity == null) {
			return false;
		}

		WeakReference<Activity> ref = new WeakReference<Activity>(activity);
		if (!mActivityList.contains(ref)) {
			mActivityList.add(ref);
		}
		return true;
	}

	@Override
	public boolean unregisterActivity(Activity activity) {
		if (activity == null) {
			return false;
		}

		WeakReference<Activity> ref = new WeakReference<Activity>(activity);
		return mActivityList.remove(ref);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		try {
			final Writer result = new StringWriter();
			final PrintWriter printWriter = new PrintWriter(result);
			printWriter.append(ex.getMessage());
			ex.printStackTrace(printWriter);
			Log.getStackTraceString(ex);
			// If the exception was thrown in a background thread inside
			// AsyncTask, then the actual exception can be found with getCause.
			Throwable cause = ex.getCause();
			while (cause != null) {
				cause.printStackTrace(printWriter);
				cause = cause.getCause();
			}
			String msg = buildCrashLog(result.toString());
			printWriter.close();

			if (LogUtils.CRASH_SAVE_2_FILE) {
				// Save to file.
				saveCrashLog2File(msg);
			}

			if (LogUtils.CRASH_UPLOAD_2_NETWORK) {
				// Upload crash log to server.
				submitTask(new LogTask(mContext, TAG, msg,
						LogUtils.LOG_TYPE_2_NETWORK));
			}
		} catch (Exception e) {
		}

		mDefaultUncaughtExceptionHandler.uncaughtException(thread, ex);
	}

	private String saveCrashLog2File(String result) {
		if (Utils.sdAvailible()) {
			try {
				String fileName = LogUtils.getCrashFileName(mContext);
				File file = new File(fileName);
				FileOutputStream trace = new FileOutputStream(file, true);

				String lineSeparator = System.getProperty("line.separator");
				if (lineSeparator == null) {
					lineSeparator = "\n";
				}

				// Encode and encrypt the message.
				OutputStreamWriter writer = new OutputStreamWriter(trace,
						"utf-8");
				writer.write(Utils.encrypt(result));
				writer.flush();

				trace.flush();
				trace.close();
				return fileName;
			} catch (Exception e) {
			}
		}
		return null;
	}

	private String buildCrashLog(String msg) {
		StringBuilder sb = new StringBuilder();
		sb.append("#");
		sb.append(new Date().toString());
		sb.append("\n");
		// Add system and device info.
		sb.append(Utils.buildSystemInfo(mContext));
		sb.append("\n");
		sb.append("#-------AndroidRuntime-------");
		sb.append(msg);
		sb.append("\n");
		sb.append("#-------activity_stack-------");
		sb.append("\n");
		sb.append(buildActivityStack());
		sb.append("#end");

		return sb.toString();
	}

	private String buildActivityStack() {
		StringBuilder buffer = new StringBuilder();
		int size = mActivityList.size();
		for (int i = 0; i < size; i++) {
			buffer.append(i + ":");
			WeakReference<Activity> ref = mActivityList.get(i);
			if (ref != null && ref.get() != null) {
				buffer.append(ref.get().toString());
			}
			buffer.append("\n");
		}
		return buffer.toString();
	}

	@Override
	public boolean log(String tag, String msg, int logType) {
		if (LogUtils.DEBUG) {
			submitTask(new LogTask(mContext, tag, msg, logType));
			return true;
		} else {
			return false;
		}
	}

}
