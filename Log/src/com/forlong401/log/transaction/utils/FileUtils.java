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

/**
 * File utility.
 * 
 * @author Li Cong
 * @date 2014-3-23
 */
public final class FileUtils {

	/**
	 * Delete file(file or folder).
	 * 
	 * @param file
	 */
	public static void delete(File file) {
		if (file == null) {
			return;
		}
		if (file.isFile()) {
			file.delete();
			return;
		}

		File[] files = file.listFiles();
		if (files == null) {
			return;
		}
		for (File f : files) {
			if (f.isDirectory()) {
				delete(f);
			} else {
				f.delete();
			}
		}
		file.delete();
	}

}
