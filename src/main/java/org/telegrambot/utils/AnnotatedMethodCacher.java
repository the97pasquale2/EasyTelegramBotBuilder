package org.telegrambot.utils;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import org.telegram.entities.CommandMethod;
import org.telegram.interfaces.Controller;
import org.telegrambot.annotations.BotHandler;
import org.telegrambot.annotations.Type;

public class AnnotatedMethodCacher {
	
	private Controller controllerToScrape;
	
	private AnnotatedMethodCacher() {}
	
	public static AnnotatedMethodCacher OfClass(Controller controllerToScrape) {
		AnnotatedMethodCacher cacher = new AnnotatedMethodCacher();
		cacher.controllerToScrape = controllerToScrape;
		return cacher;
	}
	
	public void cacheAllAnnotatedMethod() {
		Class classToScrape = controllerToScrape.getClass();
		
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
		final ConcurrentHashMap<String,CommandMethod> methodsCacheCommand = Cache.getInstanceCommand();
		//TODO final ConcurrentHashMap<Type, ConcurrentHashMap<String, CommandMethod>> methodsCacheCallBackQuery = Cache.getInstanceCallBackQuery();
		
		CommandMethod commandMethod = new CommandMethod();
		commandMethod.setCommand(command.value());
		commandMethod.setController(controllerToScrape);
		commandMethod.setRole(command.role());
		commandMethod.setMethod(method);

		methodsCacheCommand.put(command.value(), commandMethod);
	}

}
