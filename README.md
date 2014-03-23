Log Collector
===

Collect the normal or crash log in Android, then save them into files or upload into server.

===

1. How using the libs?

1.1 step1:
    Add the below permission into your manifest xml.

    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />
    
1.2 step2:
    Add output\log.jar into your libs folder in your project.
    
1.3 step3:
    Register or unregister the crash handler in your application
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

1.4 step4(Collect carsh log done.):
    Register the activity in the onCreate() of Activity.Unregister the activity in the onDestroy() of Activity. You should register and unregister for all activities in your manifest xml.
    
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

1.5 step5(optional):
    If you need collect the normal log into files or server, you just should call the below method.
    
    LogManager.getManager(getApplicationContext()).log(TAG, "onCreate", LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);

===    

Enable or disable the log:

	public static boolean DEBUG = true;
	public static boolean CRASH_SAVE_2_FILE = true;
	public static boolean CRASH_UPLOAD_2_NETWORK = false;    
	
===    	

Find the log files:
    SD card dir + package name + log/crash + files. Such as:
    sd dir/com_forlong401_log/log/log_timestamp.txt       -->normal log
    sd dir/com_forlong401_log/crash/crash_timestamp.txt   -->crash log

===

Advance skills-1:
    Encrypt your data:
public final class Utils {
public static String encrypt(String str) {
		// TODO: encrypt data.
		return str;
}
}

Advance skills-2:
    Implement your upload method using your server log API:
/**
 * Handle log task.
 * 
 * @author Li Cong
 * @date 2014-3-23
 */
public class LogTask implements Runnable {
	private void log2Network(String tag, String msg) {
		// TODO: Server API for upload message.
		// TODO: Encode and encrypt the message.
	}    
}

===    

Copyright, license and contact:

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



===
===

日志收集器
===

记录普通或者crash的日志到文件或网络服务器。

===

1. 如何使用这个库？

1.1 步骤一:
    添加这些permission到你的manifest文件中。

    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />
    
1.2 步骤二:
    添加output目录下的log.jar到你工程的libs目录下，刷新。
    
1.3 步骤三:
    在Application中Register 或 unregister crash handler。
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

1.4 步骤四(如果只收集crash，这步骤搞完就ok了):
    在每个Activity的onCreate()中Register，onDestroy()中unregister.所有的Activity都要添加。
    
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

1.5 步骤五(打log):
    如果你要收集普通的日志到文件或者服务器，那么调用下面的方法即可。
    
    LogManager.getManager(getApplicationContext()).log(TAG, "onCreate", LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);

===    

打开或关闭log:

	public static boolean DEBUG = true; // 关闭或打开普通日志
	public static boolean CRASH_SAVE_2_FILE = true;// 关闭或打开crash日志写入文件。
	public static boolean CRASH_UPLOAD_2_NETWORK = false;// 关闭或打开crash日志上传服务器。
	
===    	

去哪里找到你的日志文件:
    SD card 目录下的包名中点替换为下划线的文件夹下的 + log/crash + 日志文件. 例如:
    sd dir/com_forlong401_log/log/log_timestamp.txt       -->普通日志
    sd dir/com_forlong401_log/crash/crash_timestamp.txt   -->crash日志

===

高级技能-1:
    加密你的数据:
public final class Utils {
public static String encrypt(String str) {
		// TODO: encrypt data.
		return str;
}
}

高级技能-2:
    实现你上传日志的代码:
/**
 * Handle log task.
 * 
 * @author Li Cong
 * @date 2014-3-23
 */
public class LogTask implements Runnable {
	private void log2Network(String tag, String msg) {
		// TODO: Server API for upload message.
		// TODO: Encode and encrypt the message.
	}    
}


===    

版权, 授权协议和联系方式:

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



