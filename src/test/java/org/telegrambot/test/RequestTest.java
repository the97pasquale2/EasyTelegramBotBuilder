package org.telegrambot.test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegrambot.classes.MyTestBot;
import org.telegrambot.exceptions.InexistentCommand;
import org.telegrambot.exceptions.WrongParamsNumberException;
import org.telegrambot.handlers.CommunicationHandler;
import org.telegrambot.utils.Cache;

public class RequestTest {

	MyTestBot myTestBot = new MyTestBot();	
	
	private void set(Object object, String attribute, Object value) throws Exception { //Used for private fields
		Field f1 = object.getClass().getDeclaredField(attribute);
		f1.setAccessible(true);
		f1.set(object, value);
	}
	
}
