package org.telegrambot.handlers;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.telegram.entities.CommandMethod;
import org.telegram.interfaces.Controller;
import org.telegram.interfaces.ErrorHandler;
import org.telegram.interfaces.UpdateHandler;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegrambot.annotations.BotHandler;
import org.telegrambot.annotations.Role;
import org.telegrambot.annotations.Type;
import org.telegrambot.exceptions.InexistentCommandException;
import org.telegrambot.exceptions.InsufficientPermission;
import org.telegrambot.exceptions.WrongParamsNumberException;
import org.telegrambot.utils.AnnotatedMethodCacher;
import org.telegrambot.utils.Cache;
import org.telegrambot.utils.KeyCache;
import org.telegrambot.utils.Command;
import org.telegrambot.utils.CommandInvoker;
import org.telegrambot.utils.CommandUtils;
import org.telegrambot.utils.GarbageCommunication;
import org.telegrambot.utils.PagesFactory;
import org.telegrambot.utils.UpdateUtils;

import com.vdurmont.emoji.EmojiParser;

public class EasyBotHandler implements UpdateHandler {
	
	ErrorHandler errorHandler = new DefaultErrorHandler();
	
	HashMap<KeyCache, Object> cache = new HashMap<KeyCache, Object>();
	
	List<Controller> controllers; //TODO
	
	GarbageCommunication garbageCommunication;
	
	public EasyBotHandler(List<Controller> controllers) {
		setControllers(controllers);
		init();
	}

	private void setControllers(List<Controller> controllers) {
		this.controllers = controllers;
	}

	private void init() {
		for(Controller controller : controllers) {
			AnnotatedMethodCacher cacher = AnnotatedMethodCacher.OfClass(controller);
			cacher.cacheAllAnnotatedMethod();	
		}
	}

	public List<? extends BotApiMethod> onUpdateReceived(Update update) {
		try {
			
			System.out.println("Messaggio ricevuto: " + update);
			
			if(CommandUtils.isACommand(update)) {
				CommandInvoker commandInvoker = new CommandInvoker(update);
				return commandInvoker.invokeCommand();
			}
			
		} catch(InsufficientPermission e) {
			errorHandler.handleError(e);
		} catch (WrongParamsNumberException e) {
			errorHandler.handleError(e);
		} catch (InexistentCommandException e) {
			errorHandler.handleError(e);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ArrayList<>();
		
	}
	
}
