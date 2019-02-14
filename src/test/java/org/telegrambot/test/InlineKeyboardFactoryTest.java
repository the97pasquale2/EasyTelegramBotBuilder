package org.telegrambot.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegrambot.utils.InlineKeyboardFactory;

public class InlineKeyboardFactoryTest {
	
	@Test
	public void testInlineKeyboardFactory() {
		InlineKeyboardMarkup keyboard = new InlineKeyboardFactory().btn("BTN1").btn("BTN2").newLine()
			.btn("BTN3").btn("BTN4", "BLA").newLine()
			.btn("BTN5").build();
		
		//assertEquals(3, keyboard.getKeyboard().size());
		
		assertEquals("BTN1", keyboard.getKeyboard().get(0).get(0).getText());
		assertEquals("BTN1", keyboard.getKeyboard().get(0).get(0).getCallbackData());
		
		assertEquals("BTN2", keyboard.getKeyboard().get(0).get(1).getText());
		assertEquals("BTN2", keyboard.getKeyboard().get(0).get(1).getCallbackData());
		
		assertEquals("BTN3", keyboard.getKeyboard().get(1).get(0).getText());
		assertEquals("BTN3", keyboard.getKeyboard().get(1).get(0).getCallbackData());
		
		assertEquals("BTN4", keyboard.getKeyboard().get(1).get(1).getText());
		assertEquals("BLA",  keyboard.getKeyboard().get(1).get(1).getCallbackData());
	
		assertEquals("BTN5", keyboard.getKeyboard().get(2).get(0).getText());
		assertEquals("BTN5", keyboard.getKeyboard().get(2).get(0).getCallbackData());
	}
		
}
