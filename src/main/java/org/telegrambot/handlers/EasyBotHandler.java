package org.telegrambot.handlers;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegrambot.annotations.BotHandler;
import org.telegrambot.annotations.Type;
import org.telegrambot.exceptions.InexistentCommand;
import org.telegrambot.exceptions.WrongParamsNumberException;
import org.telegrambot.utils.Cache;
import org.telegrambot.utils.Command;

public abstract class EasyBotHandler extends TelegramLongPollingBot {

	ConcurrentHashMap<Type, ConcurrentHashMap<String, Method>> methods = new ConcurrentHashMap<Type, ConcurrentHashMap<String, Method>>();

	protected abstract void handleError(WrongParamsNumberException e);
	protected abstract void handleError(InexistentCommand e);
	
	public EasyBotHandler() {
		super();
		init();
	}

	private void init() {
		ConcurrentHashMap<Type, ConcurrentHashMap<String, Method>> methodsCache = Cache.getInstance();
		
		Class clazz = this.getClass();
		while(clazz != Object.class) {
			Method[] methods = clazz.getMethods();
			for(Method method : methods) {
				BotHandler command = method.getDeclaredAnnotation(BotHandler.class);
				if(command != null) {
					ConcurrentHashMap<String, Method> methodsForString = methodsCache.get(command.type());
					
					if(methodsForString == null) {
						methodsCache.put(command.type(), new ConcurrentHashMap<String, Method>());
						methodsForString = methodsCache.get(command.type());
					}
					
					methodsForString.put(command.value(), method);
				}
			}
			clazz = clazz.getSuperclass();
		}
		
	}

	public EasyBotHandler(DefaultBotOptions options) {
		super(options);
		init();
	}

	public void onUpdateReceived(Update update) {
		if(update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().trim().length() > 1 && update.getMessage().getText().trim().charAt(0) == '/') {
			try {
				invokeCommand(update, update.getMessage().getText(), Type.COMMAND);
			} catch (WrongParamsNumberException e) {
				handleError(e);
			} catch (InexistentCommand e) {
				handleError(e);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(update.hasCallbackQuery()) {
			try {
				invokeCommand(update, update.getCallbackQuery().getData(), Type.CALL_BACK_QUERY);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void invokeCommand(Update update, String textReceived, Type handlerType) throws Exception {
		if(textReceived != null) {
			Command command = new Command(textReceived);
			ConcurrentHashMap<String, Method> methodsForCommands = Cache.getInstance().get(handlerType);
			if(methodsForCommands == null) {
				throw new InexistentCommand("Method not found!", update, command);
			}
			Method method = methodsForCommands.get(command.getCommand());
			if(method == null) {
				throw new InexistentCommand("Method not found!", update, command);
			} else {
				Object[] paramsForMethod = getParams(update, method, command.getParams());
				method.invoke(this, paramsForMethod);
			}
		}
	}

	private Object[] getParams(Update update, Method method, Object[] params) {
		java.lang.reflect.Type[] types = method.getGenericParameterTypes();
		Object[] newParams = new Object[params.length + 1];
		if(types.length >= 1 && types[0].equals(Update.class)) {
			if(types.length - 1 != params.length) {
				throw new WrongParamsNumberException(update, method.getName(), params);
			}
			System.arraycopy(params, 0, newParams, 1, params.length);
			newParams[0] = update;
		} else {
			if(types.length != params.length) {
				throw new WrongParamsNumberException(update, method.getName(), params);
			}
			newParams = params;		
		}
		return newParams;
	}

}
