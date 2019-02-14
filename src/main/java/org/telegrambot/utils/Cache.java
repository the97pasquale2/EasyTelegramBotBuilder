package org.telegrambot.utils;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import org.telegram.entities.MethodRole;
import org.telegrambot.annotations.Type;

public class Cache {
	
	private static ConcurrentHashMap<Type, ConcurrentHashMap<String, MethodRole>> methods;
	
	public static ConcurrentHashMap<Type, ConcurrentHashMap<String, MethodRole>> getInstance() {
		if(methods == null) {
			methods = new ConcurrentHashMap<Type, ConcurrentHashMap<String, MethodRole>>();
		}
		return methods;
	}

}
