package org.telegrambot.test;

import java.util.List;

import org.telegram.telegrambots.api.objects.Update;
import org.telegrambot.annotations.BotHandler;
import org.telegrambot.annotations.Role;
import org.telegrambot.exceptions.InexistentCommandException;
import org.telegrambot.exceptions.InsufficientPermission;
import org.telegrambot.exceptions.WrongParamsNumberException;
import org.telegrambot.handlers.EasyBotHandler;
import org.telegrambot.annotations.Type;

public class MyTestBot {

	@BotHandler("/command")
	public void command() {
		System.out.println("Command ok");
	}
	
	@BotHandler("/anotherCommand")
	public void anotherCommand(Update update) {
		System.out.println("anotherCommand ok; update " + update);
	}
	
	@BotHandler("/login")
	public void login(String username, String password) {
		System.out.println("Username: " + username);
		System.out.println("Password: " + password);	
	}
	
	@BotHandler("/anotherlogin")
	public void login(Update update, String username, String password) {
		System.out.println("Update: " + update);
		System.out.println("Username: " + username);
		System.out.println("Password: " + password);	
	}
	
	@BotHandler(type = Type.CALL_BACK_QUERY, value="find") 
	public void find() {
		System.out.println("Find");
	}
	
	@BotHandler(value="/role", role=Role.GROUP)
	public void role(Update update) {
		System.out.println("Update: " + update);
	}
	
}
