package org.telegrambot.test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegrambot.test.MyTestBot;
import org.telegrambot.exceptions.InexistentCommandException;
import org.telegrambot.exceptions.WrongParamsNumberException;

public class CommandTest {

	MyTestBot myTestBot = new MyTestBot();
	
	//I just want to test my method because I'm assuming Telegram services are already tested...
	//This is why I'm forcing setting private fields...
	/*
	@Test
	public void commandTestNoParams() throws Exception {
		
		MyTestBot spy = Mockito.spy(myTestBot);

		Update update = new Update();
		Message message = new Message();
		
		Chat chat = new Chat();
		set(chat, "id", 0L);
		set(message, "chat", chat);
		
		set(message, "text", "/command");
		set(update, "message", message);
		
		spy.onUpdateReceived(update);
		
		verify(spy, times(1)).command();
				
	}
	
	@Test
	public void inexistentParams() throws Exception {
		
		MyTestBot spy = Mockito.spy(myTestBot);

		Update update = new Update();
		Message message = new Message();

		Chat chat = new Chat();
		set(chat, "id", 0L);
		set(message, "chat", chat);
		
		set(message, "text", "/inexistent");
		set(update, "message", message);
		
		spy.onUpdateReceived(update);
		
		verify(spy, times(1)).handleError(any(InexistentCommandException.class));		
	}
	
	@Test
	public void wrongNumberParams() throws Exception {
		
		MyTestBot spy = Mockito.spy(myTestBot);

		Update update = new Update();
		Message message = new Message();
		
		Chat chat = new Chat();
		set(chat, "id", 0L);
		set(message, "chat", chat);
		
		set(message, "text", "/login USERNAME PASSWORD OTHER");
		set(update, "message", message);
		
		spy.onUpdateReceived(update);
		
		verify(spy, times(1)).handleError(any(WrongParamsNumberException.class));		
	}
	
	@Test
	public void wrongNumberParams_WithUpdate() throws Exception {
		
		MyTestBot spy = Mockito.spy(myTestBot);

		Update update = new Update();
		Message message = new Message();

		Chat chat = new Chat();
		set(chat, "id", 0L);
		set(message, "chat", chat);
		
		set(message, "text", "/anotherlogin USERNAME PASSWORD OTHER");
		set(update, "message", message);
		
		spy.onUpdateReceived(update);
		
		verify(spy, times(1)).handleError(any(WrongParamsNumberException.class));		
	}
	
	@Test
	public void commandTestParams() throws Exception {
		MyTestBot spy = Mockito.spy(myTestBot);

		Update update = new Update();
		Message message = new Message();

		Chat chat = new Chat();
		set(chat, "id", 0L);
		set(message, "chat", chat);
		
		set(message, "text", "/login USERNAME PASSWORD");
		set(update, "message", message);
		
		spy.onUpdateReceived(update);
		
		verify(spy, times(1)).login("USERNAME", "PASSWORD");
		
		set(message, "text", "/login USERNAME2 PASSWORD2");
		set(update, "message", message);
		
		spy.onUpdateReceived(update);
		
		verify(spy, times(1)).login("USERNAME2", "PASSWORD2");
	}
	
	@Test
	public void callBackQuery() throws Exception {
		MyTestBot spy = Mockito.spy(myTestBot);

		Update update = new Update();
		CallbackQuery callbackQuery = new CallbackQuery();
		Message message = new Message();
		
		Chat chat = new Chat();
		set(chat, "id", 0L);
		set(message, "chat", chat);
		
		set(callbackQuery, "data", "find");
		set(update, "callbackQuery", callbackQuery);
		set(update, "message", message);
		
		spy.onUpdateReceived(update);
		
		verify(spy, times(1)).find();
	}
	
	@Test
	public void commandTestNoParams_WithUpdate() throws Exception {
		
		MyTestBot spy = Mockito.spy(myTestBot);

		Update update = new Update();
		Message message = new Message();
		
		Chat chat = new Chat();
		set(chat, "id", 0L);
		set(message, "chat", chat);
		
		set(message, "text", "/anotherCommand");
		set(update, "message", message);
		
		spy.onUpdateReceived(update);
		
		verify(spy, times(1)).anotherCommand(update);
				
	}
	
	@Test
	public void commandTestGroup_OK() throws Exception {
		
		MyTestBot spy = Mockito.spy(myTestBot);

		Update update = new Update();
		Message message = new Message();
		
		Chat chat = new Chat();
		set(chat, "id", -50L);
		set(message, "chat", chat);
		
		set(message, "text", "/role");
		set(update, "message", message);
		
		spy.onUpdateReceived(update);
		
		verify(spy, times(1)).role(update);
				
	}
	
	@Test
	public void commandTestGroup_Fail() throws Exception {
		
		MyTestBot spy = Mockito.spy(myTestBot);

		Update update = new Update();
		Message message = new Message();
		
		Chat chat = new Chat();
		set(chat, "id", 50L);
		set(message, "chat", chat);
		
		set(message, "text", "/role");
		set(update, "message", message);
		
		spy.onUpdateReceived(update);
		
		verify(spy, times(0)).role(update);
				
	}
	
	@Test
	public void commandTestParams_WithUpdate() throws Exception {
		MyTestBot spy = Mockito.spy(myTestBot);

		Update update = new Update();
		Message message = new Message();
		
		Chat chat = new Chat();
		set(chat, "id", 0L);
		set(message, "chat", chat);
		
		set(message, "text", "/anotherlogin USERNAME PASSWORD");
		set(update, "message", message);
		
		spy.onUpdateReceived(update);
		
		verify(spy, times(1)).login(update, "USERNAME", "PASSWORD");
		
		set(message, "text", "/anotherlogin USERNAME2 PASSWORD2");
		set(update, "message", message);
		
		spy.onUpdateReceived(update);
		
		verify(spy, times(1)).login(update, "USERNAME2", "PASSWORD2");
	}
	*/
	private void set(Object object, String attribute, Object value) throws Exception { //Used for private fields
		Field f1 = object.getClass().getDeclaredField(attribute);
		f1.setAccessible(true);
		f1.set(object, value);
	}
	
}
