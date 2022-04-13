package org.telegrambot.handlers;

import java.util.ArrayList;
import java.util.List;

import org.telegram.interfaces.Controller;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegrambot.bot.EasyBot;
import org.telegrambot.utils.TelegramBotsApiBuilder;

public class BotStarter {
	EasyBot easyBot;
	List<Controller> controllers = new ArrayList<>();

	public void start() throws TelegramApiRequestException {
		TelegramBotsApi botsApi = TelegramBotsApiBuilder.getIstance();
		TelegramLongPollingBotProxy longPolling = new TelegramLongPollingBotProxy();
		longPolling.setEasyBot(easyBot);
		longPolling.setUpdateHandler(new EasyBotHandler(controllers));
		
		botsApi.registerBot(longPolling);
		System.out.println("Running");
	}
		
	public EasyBot getEasyBot() {
		return easyBot;
	}

	public void setEasyBot(EasyBot easyBot) {
		this.easyBot = easyBot;
	}
	
	public void addController(Controller controller) {
		controllers.add(controller);
	}
	
}
