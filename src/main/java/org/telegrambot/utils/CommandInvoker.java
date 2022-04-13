package org.telegrambot.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.telegram.entities.CommandMethod;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegrambot.annotations.Type;
import org.telegrambot.exceptions.InexistentCommandException;
import org.telegrambot.exceptions.InsufficientPermission;
import org.telegrambot.exceptions.WrongParamsNumberException;

public class CommandInvoker {
	
	private Update updateReceived;
	private String textCommandExtrapolated;
	
	Command parsedCommand;
	CommandMethod methodToCall;
	Object[] paramsOfMethod;
	
	public CommandInvoker(Update updateReceived) {
		if(!CommandUtils.isACommand(updateReceived)) {
			throw new RuntimeException("The value passed in input is not a command. Update: " + updateReceived);
		}
		setUpdateReceived(updateReceived);
		
		fetchTextCommandExtrapolated();
		fetchParsedCommand();
		fetchMethodToCall();
		fetchParamsOfMethod();
	}
	
	public List<SendMessage> invokeCommand() throws Exception {
		UpdateUtils.assertRole(methodToCall.getRole(), updateReceived);		
		Object valueReturned = methodToCall.getMethod().invoke(methodToCall.getController(), paramsOfMethod);
		if(valueReturned instanceof List<?>) {
			List<SendMessage> messagesToSend = (List<SendMessage>)valueReturned;
			return messagesToSend;
		}
		return new ArrayList<>();
	}
	
	private void setUpdateReceived(Update updateReceived) {
		this.updateReceived = updateReceived;
	}
	
	private void fetchTextCommandExtrapolated() {
		textCommandExtrapolated = UpdateUtils.getText(updateReceived);
	}

	private void fetchParsedCommand() {
		parsedCommand = parseExtrapolatedTextToCommand();
	}
	
	private void fetchMethodToCall() {
		methodToCall = searchMethodToCall();
	}

	private void fetchParamsOfMethod() {
		paramsOfMethod = getParamsOfMethod();
	}

	
	private Command parseExtrapolatedTextToCommand() {
		return new Command(textCommandExtrapolated);
	}

	
	private CommandMethod searchMethodToCall() {
		ConcurrentHashMap<String, CommandMethod> methodsForCommands = Cache.getInstanceCommand();
		if(methodsForCommands == null) {
			throw new InexistentCommandException("Method not found!", parsedCommand);
		}
		CommandMethod methodRole = methodsForCommands.get(parsedCommand.getCommand());
		if(methodRole == null || methodRole.getMethod() == null) {
			throw new InexistentCommandException("Method not found!", parsedCommand);
		} else {
			return methodRole;
		}
	}
	
	private Object[] getParamsOfMethod() {
		Object[] paramsInMessageInput = parsedCommand.getParams();
		Method method = methodToCall.getMethod();
		
		java.lang.reflect.Type[] types = method.getGenericParameterTypes();
		Object[] newParams = new Object[paramsInMessageInput.length + 1];
		if(types.length >= 1 && types[0].equals(Update.class)) {
			if(types.length - 1 != paramsInMessageInput.length) {
				throw new WrongParamsNumberException(updateReceived, method.getName(), paramsInMessageInput);
			}
			System.arraycopy(paramsInMessageInput, 0, newParams, 1, paramsInMessageInput.length);
			newParams[0] = updateReceived;
		} else {
			if(types.length != paramsInMessageInput.length) {
				throw new WrongParamsNumberException(updateReceived, method.getName(), paramsInMessageInput);
			}
			newParams = paramsInMessageInput;		
		}
		return newParams;
	}

	
	
	

}
