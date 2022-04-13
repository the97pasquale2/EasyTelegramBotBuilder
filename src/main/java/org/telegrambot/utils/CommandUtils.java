package org.telegrambot.utils;

import java.math.BigDecimal;

import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegrambot.annotations.Role;
import org.telegrambot.exceptions.InsufficientPermission;

public class CommandUtils {

	public static boolean isACommand(Update update) {
		return update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().trim().length() > 1 && update.getMessage().getText().trim().charAt(0) == '/';
	}

	

	
}
