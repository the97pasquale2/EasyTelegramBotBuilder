package org.telegrambot.utils;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import org.telegram.entities.MethodRole;
import org.telegrambot.annotations.BotHandler;
import org.telegrambot.annotations.Type;

public class AnnotatedMethodCacher {
	
	Class classToScrape;
	
	private AnnotatedMethodCacher() {}
	
	public static AnnotatedMethodCacher OfClass(Class classToScrape) {
		AnnotatedMethodCacher cacher = new AnnotatedMethodCacher();
		cacher.classToScrape = classToScrape;
		return cacher;
	}
	
	public void cacheAllAnnotatedMethod() {
		while(classToScrape != Object.class) {
			Method[] methods = classToScrape.getMethods();
			for(Method method : methods) {
				BotHandler command = method.getDeclaredAnnotation(BotHandler.class);
				if(command != null) {
					cacheAnnotatedMethod(method, command);
				}
			}
			classToScrape = classToScrape.getSuperclass();
		}
	}
	private void cacheAnnotatedMethod(Method method, BotHandler command) {
		final ConcurrentHashMap<Type, ConcurrentHashMap<String, MethodRole>> methodsCache = Cache.getInstance();
		
		ConcurrentHashMap<String, MethodRole> methodsForString = methodsCache.get(command.type());

		if(methodsForString == null) {
			methodsCache.put(command.type(), new ConcurrentHashMap<String, MethodRole>());
			methodsForString = methodsCache.get(command.type());
		}

		methodsForString.put(command.value(), new MethodRole(method, command.role()));
	}

}
