package com.huawei.lcloud.debugtools.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
public class ExceptionHandler {

	public static void handleEx(Throwable ex, Logger logger) {
		String stackErrorStr = getTrace(ex);
		logger.error(stackErrorStr);
	}

	/**
	 * 获取堆栈错误信息.
	 * 
	 * @param cause
	 *            Throwable
	 * @return 错误堆栈详情
	 */
	private static String getTrace(Throwable cause) {
		if (cause != null) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			cause.printStackTrace(pw);
			StringBuffer sb = sw.getBuffer();
			return sb.toString();
		}
		return "";
	}
}
