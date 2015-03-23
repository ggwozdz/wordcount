package ggwozdz.nordea;

import org.slf4j.Logger;

import com.google.common.base.CaseFormat;
import com.google.common.base.Splitter;

public class TestUtils {
	public static void logTestName(Logger logger){
		String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		logger.debug("=================================");
		logger.debug(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, methodName).replaceAll("-", " "));
		logger.debug("=================================");
	}
}
