package org.telegrambot.utils;

import java.math.BigDecimal;

import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegrambot.annotations.Role;
import org.telegrambot.exceptions.InsufficientPermission;

public class UpdateUtils {

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
			return new BigDecimal(getText(update).replace("€", "").replace(",", ".").trim());	
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
	
	public static Long getChatId(Update update) throws Exception {
		if(update.getMessage() != null) {
			return update.getMessage().getChatId();
		} else if(update.getCallbackQuery().getMessage() != null) {
			return update.getCallbackQuery().getMessage().getChatId();
		}
		
		throw new Exception("Can't get ChatId");
		
	}
	
	public static void assertRole(Role role, Update updateReceived) throws Exception {
		switch(role) {
		case ALL:
			break;
		case GROUP:
			if(!UpdateUtils.isFromGroup(updateReceived)) {
				throw new InsufficientPermission();
			}
			break;
		case USER:
			if(!UpdateUtils.isFromUser(updateReceived)) {
				throw new InsufficientPermission();
			}
			break;
		default:
			break;
		}
	}
	
}
