package org.telegrambot.utils;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import org.telegrambot.annotations.Type;

public class Cache {
	
	private static ConcurrentHashMap<Type, ConcurrentHashMap<String, Method>> methods;
	
	public static ConcurrentHashMap<Type, ConcurrentHashMap<String, Method>> getInstance() {
		if(methods == null) {
			methods = new ConcurrentHashMap<Type, ConcurrentHashMap<String, Method>>();
		}
		return methods;
	}

}
