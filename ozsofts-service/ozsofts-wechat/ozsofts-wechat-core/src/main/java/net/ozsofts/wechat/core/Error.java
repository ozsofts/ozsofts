package net.ozsofts.wechat.core;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Properties;

public class Error {
	private static Object lock = new Object();

	private static Properties properties = null;

	public static String getErrorMessage(String errorCode) {
		if (properties == null) {
			synchronized (lock) {
				if (properties == null) {
					try {
						String propertiesName = Error.class.getPackage().getName().replace('.', '/') + "/error.properties";
						URL errorFileUrl = Error.class.getClassLoader().getResource(propertiesName);
						File jasperFile = new File(errorFileUrl.toURI());

						properties = new Properties();
						properties.load(new FileInputStream(jasperFile));
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}

		return properties.getProperty(errorCode);
	}

	public static void main(String[] args) {
		System.out.println(Error.getErrorMessage("40033"));
	}
}
