package org.telegrambot.handlers;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.telegram.entities.CommandMethod;
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
import org.telegrambot.bot.EasyBot;
import org.telegrambot.exceptions.InexistentCommandException;
import org.telegrambot.exceptions.InsufficientPermission;
import org.telegrambot.exceptions.WrongParamsNumberException;
import org.telegrambot.utils.AnnotatedMethodCacher;
import org.telegrambot.utils.Cache;
import org.telegrambot.utils.KeyCache;
import org.telegrambot.utils.Command;
import org.telegrambot.utils.GarbageCommunication;
import org.telegrambot.utils.PagesFactory;
import org.telegrambot.utils.UpdateUtils;

import com.vdurmont.emoji.EmojiParser;

public class TelegramLongPollingBotProxy extends TelegramLongPollingBot {

	EasyBot easyBot;
	UpdateHandler updateHandler;
	
	@Override
	public void onUpdateReceived(Update update) {
		List<? extends BotApiMethod> methodList = updateHandler.onUpdateReceived(update);
		methodList.forEach(method -> {
			try {
				execute(method);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public String getBotUsername() {
		return easyBot.getAccessInfo().getUsername();
	}

	@Override
	public String getBotToken() {
		return easyBot.getAccessInfo().getToken();
	}

	public void setEasyBot(EasyBot easyBot) {
		this.easyBot = easyBot;
	}

	public void setUpdateHandler(UpdateHandler updateHandler) {
		this.updateHandler = updateHandler;
	}
	
}
