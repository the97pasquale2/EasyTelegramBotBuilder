package org.telegrambot.utils;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegrambot.handlers.EasyBotHandler;

public class InlineKeyboardFactory {
	private int row = 0;
	List<List<InlineKeyboardButton>> list = new ArrayList<List<InlineKeyboardButton>>();
	
	public InlineKeyboardFactory() {
		list.add(0, new ArrayList<InlineKeyboardButton>());
	}
	
	public InlineKeyboardFactory btn(String text, String callbackData) {
		list.get(row).add(new InlineKeyboardButton(EasyBotHandler.emoji(text)).setCallbackData(callbackData));
		return this;
	}

	public InlineKeyboardFactory btn(String text) {
		return btn(text, text);
	}
	
	public InlineKeyboardFactory newLine() {
		list.add(++row, new ArrayList<InlineKeyboardButton>());
		return this;
	}
	
	public InlineKeyboardMarkup build() {
		InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
	    inlineKeyboardMarkup.setKeyboard(list);

	    return inlineKeyboardMarkup;
	}

	@Override
	public String toString() {
		return "InlineKeyboardFactory [row=" + row + ", list=" + list + "]";
	}
	
	
	
}
