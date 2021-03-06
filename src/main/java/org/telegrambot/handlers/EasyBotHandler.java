package org.telegrambot.handlers;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.telegram.entities.MethodRole;
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
import org.telegrambot.exceptions.InexistentCommand;
import org.telegrambot.exceptions.InsufficientPermission;
import org.telegrambot.exceptions.WrongParamsNumberException;
import org.telegrambot.utils.Cache;
import org.telegrambot.utils.KeyCache;
import org.telegrambot.utils.Command;
import org.telegrambot.utils.GarbageCommunication;
import org.telegrambot.utils.PagesFactory;

import com.vdurmont.emoji.EmojiParser;

public abstract class EasyBotHandler extends TelegramLongPollingBot {

	protected abstract void handleError(InsufficientPermission e);
	protected abstract void handleError(WrongParamsNumberException e);
	protected abstract void handleError(InexistentCommand e);
	protected abstract void handleTimeoutPageFactory(Long chatId, String idPage);
	protected abstract void handleOtherUpdates(Update update);
	protected abstract void customInit();
	HashMap<KeyCache, Object> cache = new HashMap<KeyCache, Object>();
	HashMap<Long, CommunicationHandler> communicationForIds = new HashMap<Long, CommunicationHandler>();	
	HashMap<String, PagesFactory> pagesFactory = new HashMap<String, PagesFactory>();
	HashMap<Long, List> listForPagesFactory = new HashMap<Long, List>();
	
	GarbageCommunication garbageCommunication;
	
	public EasyBotHandler() {
		super();
		init();
		customInit();
	}

	public static boolean isFromGroup(Update update) throws Exception {
		return getChatId(update) < 0;
	}
	
	public static boolean isFromUser(Update update) throws Exception {
		return !isFromGroup(update);
	}
	
	public static String getText(Update update) {
		String text = "";
		if(update != null && update.getMessage() != null) {
			text = update.getMessage().getText();
		}
		return text;
	}
	
	public static String getData(Update update) {
		String text = "";
		if(update.hasCallbackQuery() && update.getCallbackQuery() != null && update.getCallbackQuery().getData() != null) {
			text =  update.getCallbackQuery().getData();
		}
		return text;
	}
	
	public static PhotoSize getPhoto(Update update, int index) {
		if(hasPhoto(update)) {
			return update.getMessage().getPhoto().get(index);
		}
		return null;
	}
	
	public static boolean hasPhoto(Update update) {
		return update != null && update.getMessage() != null && update.getMessage().getPhoto() != null && !update.getMessage().getPhoto().isEmpty();
	}
	
	public static Long getLong(Update update) {
		try {
			return Long.valueOf(getText(update));	
		} catch (Exception e) {
			return 0L;
		}
	}
	
	public static BigDecimal getBigDecimal(Update update) {
		try {
			return new BigDecimal(getText(update).replace("�", "").replace(",", ".").trim());	
		} catch (Exception e) {
			return new BigDecimal("0");
		}
	}
	
	public static int getInt(Update update) {
		try {
			return Integer.parseInt(getText(update).trim());	
		} catch (Exception e) {
			return 0;
		}
	}
	
	private void init() {
		
		Thread thread = new Thread(new GarbageCommunication(communicationForIds));
		thread.start();
		
		ConcurrentHashMap<Type, ConcurrentHashMap<String, MethodRole>> methodsCache = Cache.getInstance();

		Class clazz = this.getClass();
		while(clazz != Object.class) {
			Method[] methods = clazz.getMethods();
			for(Method method : methods) {
				BotHandler command = method.getDeclaredAnnotation(BotHandler.class);
				if(command != null) {
					ConcurrentHashMap<String, MethodRole> methodsForString = methodsCache.get(command.type());

					if(methodsForString == null) {
						methodsCache.put(command.type(), new ConcurrentHashMap<String, MethodRole>());
						methodsForString = methodsCache.get(command.type());
					}

					methodsForString.put(command.value(), new MethodRole(method, command.role()));
				}
			}
			clazz = clazz.getSuperclass();
		}

	}

	public EasyBotHandler(DefaultBotOptions options) {
		super(options);
		init();
	}

	public static String emoji(String text) {
		return EmojiParser.parseToUnicode(text);
	}
	
	protected void sendHTMLMessage(Update update, String text) {
		SendMessage sendMessage;
		try {
			sendMessage = getHTMLMessage(update, text);
			execute(sendMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void sendHTMLMessage(Long chatId, String text) {
		SendMessage sendMessage;
		try {
			sendMessage = getHTMLMessage(chatId, text);
			execute(sendMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected SendMessage getHTMLMessage(Long chatId, String text) {
		SendMessage sendMessage = null;
		try {
			sendMessage = new SendMessage(chatId, text);
			sendMessage.setParseMode("HTML");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sendMessage;
	}
	
	protected SendMessage getHTMLMessage(Update update, String text) {
		SendMessage sendMessage = null;
		try {
			sendMessage = new SendMessage(getChatId(update), text);
			sendMessage.setParseMode("HTML");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sendMessage;
	}
	
	protected void put(String cacheName, Object[] params, Object value) {
		KeyCache key = new KeyCache(cacheName, params);
		cache.put(key, value);
	}
	
	protected Object get(String cacheName, Object[] params) {
		return cache.get(new KeyCache(cacheName, params));
	}
	
	protected <T> void showFirst(PagesFactory<T> pageFactory, List<T> list, Update update, String parseMode) throws Exception {
		String id = pageFactory.getId();
		pagesFactory.put(id, pageFactory);
		Long chatId = getChatId(update);
		listForPagesFactory.put(chatId, list);
		SendMessage send;
		try {
			send = pageFactory.getFirst(update, list, parseMode);
			execute(send);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected <T> void showFirst(PagesFactory<T> pageFactory, List<T> list, Update update) throws Exception {
		showFirst(pageFactory, list, update, null);
	}
	
	public PagesFactory getPageFrom(Update update) {
		
		if(update.getCallbackQuery() == null || update.getCallbackQuery().getData() == null) {
			return null;
		}
		
		String text = update.getCallbackQuery().getData();
		String[] textSplitted = text.split("_");
		String id = textSplitted[0].replace("/", "");
		return pagesFactory.get(id);
	}
	
	public void onUpdateReceived(Update update) {
		try {
			
			System.out.println("Messaggio ricevuto: " + update);
			
			Long chatId = getChatId(update);
			CommunicationHandler actualCommunication = communicationForIds.get(chatId);
			
			//Page factory
			if(update.hasCallbackQuery()) {
				PagesFactory page = getPageFrom(update);
				if(page != null) {
					List list = listForPagesFactory.get(chatId);
					if(list == null) {
						handleTimeoutPageFactory(chatId, page.getId());
					} else {
						EditMessageText editedText = page.getPage(update, list);
						if(editedText != null) {
							editMessageText(editedText);
						}
					}
					return;
				}
			}
			//Actual communication
			if(actualCommunication != null) {
				
				if(actualCommunication.getEndCommunicationHandler() == null) {
					actualCommunication.setEndCommunicationHandler((Long chatIdEnded) -> communicationForIds.remove(chatIdEnded));
				}
				
				if(actualCommunication.isAllowCommandForQuit() && isACommand(update)) {
					actualCommunication.end(chatId, true);
					invokeCommand(update);
				} else {
					actualCommunication.handle(update);
				}

			} else if(isACommand(update)) {
				invokeCommand(update);	
			} else if(update.hasCallbackQuery()) {
				invokeCallBackQuery(update);
			} else {
				handleOtherUpdates(update);
			}
			
		} catch(InsufficientPermission e) {
			handleError(e);
		} catch (WrongParamsNumberException e) {
			handleError(e);
		} catch (InexistentCommand e) {
			handleError(e);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void invokeCallBackQuery(Update update) throws Exception {
		invokeCommand(update, update.getCallbackQuery().getData(), Type.CALL_BACK_QUERY);
	}
	private void invokeCommand(Update update) throws Exception {
		invokeCommand(update, update.getMessage().getText(), Type.COMMAND);
	}
	private boolean isACommand(Update update) {
		return update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().trim().length() > 1 && update.getMessage().getText().trim().charAt(0) == '/';
	}

	private void invokeCommand(Update update, String textReceived, Type handlerType) throws Exception {
		if(textReceived != null) {
			Command command = new Command(textReceived);
			ConcurrentHashMap<String, MethodRole> methodsForCommands = Cache.getInstance().get(handlerType);
			if(methodsForCommands == null) {
				throw new InexistentCommand("Method not found!", update, command);
			}
			MethodRole methodRole = methodsForCommands.get(command.getCommand());
			if(methodRole == null || methodRole.getMethod() == null) {
				throw new InexistentCommand("Method not found!", update, command);
			} else {
				assertRole(methodRole, update);
				Object[] paramsForMethod = getParams(update, methodRole.getMethod(), command.getParams());
				methodRole.getMethod().invoke(this, paramsForMethod);
			}
		}
	}
	
	public void assertRole(MethodRole methodRole, Update update) throws Exception {
		switch(methodRole.getRole()) {
		case ALL:
			break;
		case GROUP:
			if(!isFromGroup(update)) {
				throw new InsufficientPermission();
			}
			break;
		case USER:
			if(!isFromUser(update)) {
				throw new InsufficientPermission();
			}
			break;
		default:
			break;
			
		}
	}
	
	public static Long getChatId(Update update) throws Exception {
		if(update.getMessage() != null) {
			return update.getMessage().getChatId();
		} else if(update.getCallbackQuery().getMessage() != null) {
			return update.getCallbackQuery().getMessage().getChatId();
		}
		
		throw new Exception("Can't get ChatId");
		
	}

	protected void start(CommunicationHandler c, Update update) throws RuntimeException, TelegramApiException, Exception {
		c.start(update);
		communicationForIds.put(getChatId(update), c);
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
