package org.telegrambot.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtils {
	
	private static PropertyUtils instance;
	
	private PropertyUtils() {
		
	}
	
	public static PropertyUtils getInstance() {
		if(instance == null) {
			instance = new PropertyUtils();
		}
		return instance;
	}

	public Properties loadPropertiesFromResource(String resource) {
		Properties properties = null;
		InputStream in = null;
		try {
			properties = new Properties();
			in = this.getClass().getClassLoader().getResourceAsStream(resource);
			properties.load(in);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if(in != null) {
				try {
					in.close();
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
				
		return properties;
	}

	public Properties loadPropertiesFromFile(String path) {
		Properties properties = null;
		InputStream in = null;
		try {
			properties = new Properties();
			in = new FileInputStream(path);
			properties.load(in);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if(in != null) {
				try {
					in.close();
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
				
		return properties;
	}
}
