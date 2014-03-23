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
package com.forlong401.log.transaction.log.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.forlong401.log.transaction.utils.FileUtils;
import com.forlong401.log.transaction.utils.LogUtils;
import com.forlong401.log.transaction.utils.Utils;

/**
 * Handle log task.
 * 
 * @author Li Cong
 * @date 2014-3-23
 */
public class LogTask implements Runnable {
	private Context mContext;
	private String mTag;
	private String mMsg;
	private int mLogType;

	public LogTask(Context context, String tag, String msg, int logType) {
		mContext = context;
		mMsg = msg;
		mTag = tag;
		mLogType = logType;
	}

	@Override
	public void run() {
		if (mContext == null || TextUtils.isEmpty(mMsg)
				|| TextUtils.isEmpty(mTag)) {
			return;
		}

		if ((mLogType & LogUtils.LOG_TYPE_2_LOGCAT) > 0) {
			Log.d(mTag, mMsg);
		}

		if ((mLogType & LogUtils.LOG_TYPE_2_FILE) > 0) {
			log2File("ms:" + System.currentTimeMillis() + " [" + mTag + "]"
					+ mMsg);
		}

		if ((mLogType & LogUtils.LOG_TYPE_2_NETWORK) > 0) {
			log2Network(mTag, mMsg);
		}
	}

	private void log2File(String msg) {
		if (Utils.sdAvailible()) {
			try {
				String systemInfo = "\n";
				File file = new File(LogUtils.getLogFileName(mContext));
				if (!file.exists() || file.isDirectory()) {
					FileUtils.delete(file);
					file.createNewFile();
					systemInfo = Utils.buildSystemInfo(mContext);
				}

				String lineSeparator = System.getProperty("line.separator");
				if (lineSeparator == null) {
					lineSeparator = "\n";
				}

				// Encode and encrypt the message.
				FileOutputStream trace = new FileOutputStream(file, true);
				OutputStreamWriter writer = new OutputStreamWriter(trace,
						"utf-8");
				writer.write(Utils.encrypt(buildLog(msg, systemInfo)));
				writer.flush();

				trace.flush();
				trace.close();
			} catch (Exception e) {
			}
		}
	}

	private String buildLog(String msg, String systemInfo) {
		StringBuilder sb = new StringBuilder();
		sb.append(systemInfo);
		sb.append("\n");
		sb.append(new Date().toString());
		sb.append(msg);
		sb.append("\n");

		return sb.toString();
	}

	private void log2Network(String tag, String msg) {
		// TODO: Server API for upload message.
		// TODO: Encode and encrypt the message.
	}
}
