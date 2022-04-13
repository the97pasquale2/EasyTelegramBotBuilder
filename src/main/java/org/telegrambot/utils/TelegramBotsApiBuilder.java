package org.telegrambot.utils;

import org.telegram.telegrambots.TelegramBotsApi;

public class TelegramBotsApiBuilder {

	static TelegramBotsApi api;
	
	public static TelegramBotsApi getIstance() {
		if(api == null) {
			api = new TelegramBotsApi();
		}
		return api;
	}

	
}
