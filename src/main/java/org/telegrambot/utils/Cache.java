package org.telegrambot.utils;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import org.telegram.entities.CommandMethod;
import org.telegrambot.annotations.Type;

public class Cache {
	
	private static ConcurrentHashMap<String, CommandMethod> methodsCommand;
	private static ConcurrentHashMap<String, CommandMethod> methodsCallBackQuery;
	
	public static ConcurrentHashMap<String, CommandMethod> getInstanceCommand() {
		if(methodsCommand == null) {
			methodsCommand = new ConcurrentHashMap<String, CommandMethod>();
		}
		return methodsCommand;
	}
	
	public static ConcurrentHashMap<String, CommandMethod> getInstanceCallBackQuery() {
		if(methodsCallBackQuery == null) {
			methodsCallBackQuery = new ConcurrentHashMap<String, CommandMethod>();
		}
		return methodsCallBackQuery;
	}

}
