package org.gjgr.pig.chivalrous.core.stream;

import java.io.IOException;
import java.io.InputStream;

/**
 * File Name : pig-chivalrous - org.gjgr.pig.chivalrous.core.stream
 * CopyRright (c) 1949-xxxx:
 * File Number：
 * Author：gwd
 * Date：on 2018/12/7
 * Modify：gwd
 * Time ：
 * Comment：
 * Description：
 * Version：
 */
public final class StreamCommand {
	public static boolean isExist(InputStream inputStream) {
		if (inputStream == null) {
			return false;
		} else {
			try {
				int i = inputStream.available();
				if (i > 0) {
					return true;
				} else {
					return false;
				}
			} catch (IOException e) {
				return false;
			}
		}
	}
}
